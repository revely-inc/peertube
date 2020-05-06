package co.revely.peertube.ui.video

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import co.revely.peertube.R
import co.revely.peertube.api.peertube.response.Video
import co.revely.peertube.databinding.FragmentVideoBinding
import co.revely.peertube.helper.PreferencesHelper
import co.revely.peertube.repository.NetworkState
import co.revely.peertube.ui.LayoutFragment
import co.revely.peertube.ui.MainActivity
import co.revely.peertube.utils.*
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
import kotlin.math.abs

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
class VideoFragment : LayoutFragment<FragmentVideoBinding>(R.layout.fragment_video), EventListener
{
	private val appExecutors: AppExecutors by inject()
	private val videoViewModel: VideoViewModel by viewModel(parameters = { parametersOf(args.videoId) })
	private val args: VideoFragmentArgs by navArgs()
	private val downloadTracker: DownloadTracker by inject()
	private val dataSourceFactory: DefaultDataSourceFactory by inject()
	private var adapter: SubVideoListAdapter by autoCleared()

	companion object
	{
		fun newInstance(videoId: String): VideoFragment
		{
			val args = Bundle()
			args.putString("video_id", videoId)
			val fragment = VideoFragment()
			fragment.arguments = args
			return fragment
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		binding.viewModel = videoViewModel
		if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
			activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
		}

		video_motion_layout.setTransitionListener(object : TransitionAdapter() {
			override fun onTransitionChange(motionLayout: MotionLayout, startId: Int, endId: Int, progress: Float) {
				(activity as MainActivity).main_motion_layout.progress = 1 - abs(progress)
			}
		})
		close_video.setOnClickListener { activity?.also {
			it.supportFragmentManager.beginTransaction()
					.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down)
					.remove(this).commit()
		} }

		adapter = SubVideoListAdapter(videoViewModel, appExecutors) {

		}
		sub_video_list.adapter = adapter
		sub_video_list.layoutManager = LinearLayoutManager(context)
		ContextCompat.getDrawable(view.context, R.drawable.line_divider)?.also {
			sub_video_list.addItemDecoration(MarginItemDecoration(it))
		}

		initPlayer()
		initComments()
	}

	private fun initPlayer()
	{
		player.requestFocus()
		if (videoViewModel.exoPlayer == null)
		{
			videoViewModel.exoPlayer = SimpleExoPlayer.Builder(player.context).build()
			videoViewModel.exoPlayer?.playWhenReady = true
			observe(videoViewModel.video) { onVideo(it) }
		}
		videoViewModel.exoPlayer?.addListener(this)
		player.player = videoViewModel.exoPlayer
		player.setPlaybackPreparer { videoViewModel.exoPlayer?.retry() }
	}

	private fun initComments()
	{
		progress_bar.progress(true)
		videoViewModel.comments.apply {
			observe(pagedList) {
				adapter.submitList(it) { sub_video_list.scrollToPosition(0) }
				progress_bar.progress(false)
			}
			observe(refreshState) {
				activity?.swipe_refresh?.isRefreshing = it == NetworkState.LOADING
			}
//			observe(networkState) {
//			}

			activity?.swipe_refresh?.apply {
//				isEnabled = true
//				setOnRefreshListener { refresh() }
			}
		}
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
		adapter.setVideo(video)
		binding.host = PreferencesHelper.defaultHost.get()
		binding.video = video
		if (videoViewModel.mediaSource != null)
			return
		with(buildMediaSource(Uri.parse(video.files?.first()?.fileUrl))) {
			videoViewModel.mediaSource = this
			videoViewModel.exoPlayer?.prepare(this)
		}
	}

	override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int)
	{
		if (isVisible.not()) return
		when(playbackState) {
			STATE_IDLE -> {}
			STATE_BUFFERING -> play_pause.progress(true)
			STATE_READY -> {
				play_pause.progress(false)
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

	override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
	override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {}
	override fun onSeekProcessed() {}
	override fun onPlayerError(error: ExoPlaybackException)
	{
		if (isBehindLiveWindow(error))
			initPlayer()
	}
	override fun onLoadingChanged(isLoading: Boolean) {}
	override fun onPositionDiscontinuity(reason: Int) {}
	override fun onRepeatModeChanged(repeatMode: Int) {}
	override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
	override fun onTimelineChanged(timeline: Timeline, reason: Int) {}

	override fun onStart()
	{
		super.onStart()
		if (Util.SDK_INT > 23)
		{
			player?.onResume()
			videoViewModel.exoPlayer?.playWhenReady = true
		}
	}

	override fun onResume()
	{
		super.onResume()
		if (Util.SDK_INT <= 23 || videoViewModel.exoPlayer == null)
			player?.onResume()
	}

	override fun onPause()
	{
		super.onPause()
		player.onPause()
		videoViewModel.exoPlayer?.playWhenReady = false
	}
}