package co.revely.peertube.ui.video

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import co.revely.peertube.R
import co.revely.peertube.api.ApiSuccessResponse
import co.revely.peertube.databinding.FragmentVideoBinding
import co.revely.peertube.db.peertube.entity.Video
import co.revely.peertube.ui.LayoutFragment
import co.revely.peertube.utils.DownloadTracker
import co.revely.peertube.utils.invisible
import co.revely.peertube.utils.progress
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.*
import com.google.android.exoplayer2.offline.DownloadHelper
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_video.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
class VideoFragment : LayoutFragment<FragmentVideoBinding>(R.layout.fragment_video), EventListener
{
	private val videoViewModel: VideoViewModel by viewModel(parameters = { parametersOf(args.host, args.videoId) })
	private val args: VideoFragmentArgs by navArgs()
	private val downloadTracker: DownloadTracker by inject()
	private val dataSourceFactory: DefaultDataSourceFactory by inject()

	private var exoPlayer: SimpleExoPlayer? = null
	private var mediaSource: MediaSource? = null

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
			activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
			(activity as? AppCompatActivity)?.supportActionBar?.hide()
		}

		player.requestFocus()
	}

	private fun initializePlayer()
	{
		if (exoPlayer != null)
			return
		exoPlayer = ExoPlayerFactory.newSimpleInstance(context,
				DefaultRenderersFactory(context),
				DefaultTrackSelector(),
				DefaultLoadControl())
		player.player = exoPlayer
		player.setPlaybackPreparer { exoPlayer?.retry() }
		exoPlayer?.addListener(this)
		exoPlayer?.playWhenReady = videoViewModel.startAutoPlay
		videoViewModel.video.observe(this, Observer { when (it) {
			is ApiSuccessResponse -> onVideo(it.body)
		} })
		videoViewModel.rating.observe(this, Observer { when (it) {
			is ApiSuccessResponse -> it.body.rating
		} })
	}

	private fun releasePlayer()
	{
		if (exoPlayer != null)
		{
			updateStartPosition()
			exoPlayer?.release()
			exoPlayer = null
			mediaSource = null
		}
	}

	private fun updateStartPosition()
	{
		if (exoPlayer != null)
		{
			videoViewModel.startAutoPlay = exoPlayer!!.playWhenReady
			videoViewModel.startWindow = exoPlayer!!.currentWindowIndex
			videoViewModel.startPosition = exoPlayer!!.contentPosition.coerceAtLeast(0)
		}
	}

	private fun clearStartPosition()
	{
		videoViewModel.startAutoPlay = true
		videoViewModel.startWindow = C.INDEX_UNSET
		videoViewModel.startPosition = C.TIME_UNSET
	}

	private fun buildMediaSource(uri: Uri, overrideExtension: String? = null): MediaSource
	{
		val downloadRequest = downloadTracker.getDownloadRequest(uri)
		if (downloadRequest != null)
			return DownloadHelper.createMediaSource(downloadRequest, dataSourceFactory)
		return when (val type = Util.inferContentType(uri, overrideExtension)) {
			C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
			C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
			C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
			C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
			else -> throw IllegalStateException("Unsupported type: $type")
		}
	}

	private fun onVideo(video: Video)
	{
//		if (isVisible.not()) return
		binding.video = video
		video.previewPath?.also {
			Glide.with(preview).load("https://${args.host}$it").into(binding.preview)
		}
		mediaSource = buildMediaSource(Uri.parse(video.files?.first()?.fileUrl))

		val haveStartPosition = videoViewModel.startWindow != C.INDEX_UNSET
		if (haveStartPosition)
			exoPlayer?.seekTo(videoViewModel.startWindow, videoViewModel.startPosition)
		exoPlayer?.prepare(mediaSource, !haveStartPosition, false)
	}

	override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int)
	{
		if (isVisible.not()) return
		when(playbackState) {
			STATE_IDLE -> {}
			STATE_BUFFERING -> play_pause.progress(true)
			STATE_READY -> {
				play_pause.progress(false)
				preview.invisible()
			}
			STATE_ENDED -> {}
			else -> throw IllegalArgumentException("Wrong playbackState: $playbackState")
		}
	}

	private fun isBehindLiveWindow(e: ExoPlaybackException): Boolean
	{
		if (e.type != ExoPlaybackException.TYPE_SOURCE)
			return false
		var cause: Throwable? = e.sourceException
		while (cause != null)
		{
			if (cause is BehindLiveWindowException)
				return true
			cause = cause.cause
		}
		return false
	}

	override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {}
	override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {}
	override fun onSeekProcessed() {}
	override fun onPlayerError(error: ExoPlaybackException)
	{
		if (isBehindLiveWindow(error))
		{
			clearStartPosition()
			initializePlayer()
		}
	}
	override fun onLoadingChanged(isLoading: Boolean) {}
	override fun onPositionDiscontinuity(reason: Int) {}
	override fun onRepeatModeChanged(repeatMode: Int) {}
	override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
	override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {}

	override fun onSaveInstanceState(outState: Bundle)
	{
		super.onSaveInstanceState(outState)
		updateStartPosition()
	}

	override fun onStart()
	{
		super.onStart()
		if (Util.SDK_INT > 23) {
			initializePlayer()
			player?.onResume()
		}
	}

	override fun onResume()
	{
		super.onResume()
		if (Util.SDK_INT <= 23 || player == null) {
			initializePlayer()
			player?.onResume()
		}
	}

	override fun onPause()
	{
		super.onPause()
		if (Util.SDK_INT <= 23) {
			player?.onPause()
			releasePlayer()
		}
	}

	override fun onStop()
	{
		super.onStop()
		if (Util.SDK_INT > 23) {
			player?.onPause()
			releasePlayer()
		}
	}
}