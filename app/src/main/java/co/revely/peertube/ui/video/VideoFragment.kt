package co.revely.peertube.ui.video

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.navigation.fragment.navArgs
import co.revely.peertube.R
import co.revely.peertube.api.dao.VideoDao
import co.revely.peertube.composable.PeertubeTheme
import co.revely.peertube.composable.video.SubVideoItems
import co.revely.peertube.databinding.FragmentVideoBinding
import co.revely.peertube.helper.PreferencesHelper
import co.revely.peertube.repository.NetworkState
import co.revely.peertube.ui.LayoutFragment
import co.revely.peertube.ui.MainActivity
import co.revely.peertube.utils.observe
import co.revely.peertube.utils.progress
import co.revely.peertube.viewmodel.OAuthViewModel
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.EventListener
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.util.Util
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import kotlin.math.abs

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
class VideoFragment : LayoutFragment<FragmentVideoBinding>(R.layout.fragment_video), EventListener
{
	private val oAuthViewModel: OAuthViewModel by sharedViewModel()
	private val videoViewModel: VideoViewModel by viewModel(parameters = { parametersOf(args.videoId, oAuthViewModel) })
	private val args: VideoFragmentArgs by navArgs()

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

		binding.videoMotionLayout.transitionToEnd()
		binding.videoMotionLayout.setTransitionListener(object : TransitionAdapter()
		{
			override fun onTransitionChange(motionLayout: MotionLayout, startId: Int, endId: Int, progress: Float) {
				(activity as MainActivity).binding.mainMotionLayout.progress = abs(progress)
			}

			override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
				(activity as MainActivity).binding.mainMotionLayout.progress = motionLayout.progress
			}
		})
		binding.closeVideo.setOnClickListener { activity?.also {
			it.supportFragmentManager.beginTransaction()
					.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down)
					.remove(this).commit()
		} }

		binding.subVideoItems.setContent {
			PeertubeTheme {
				SubVideoItems(videoViewModel)
			}
		}

		initPlayer()
		initComments()
	}

	private val gestureDetector = GestureDetector(
		context,
		object : GestureDetector.SimpleOnGestureListener() {
			override fun onDown(e: MotionEvent?): Boolean {
				if (binding.exoController.isVisible) binding.exoController.hide() else binding.exoController.show()
				return super.onDown(e)
			}
		})

	@SuppressLint("ClickableViewAccessibility")
	private fun initPlayer()
	{
		binding.player.setOnTouchListener { _, event ->
			return@setOnTouchListener gestureDetector.onTouchEvent(event)
		}
		if (videoViewModel.exoPlayer == null)
		{
			videoViewModel.exoPlayer = SimpleExoPlayer.Builder(binding.player.context).build()
			videoViewModel.exoPlayer?.playWhenReady = true
			observe(videoViewModel.video) { onVideo(it) }
		}
		binding.exoController.player = videoViewModel.exoPlayer
		videoViewModel.exoPlayer?.addListener(this)
		binding.player.player = videoViewModel.exoPlayer
		binding.player.setPlaybackPreparer { videoViewModel.exoPlayer?.prepare() }
	}

	private fun initComments()
	{
		binding.progressBar.progress(true)
		videoViewModel.comments.apply {
			observe(pagedList) {
//				adapter.submitList(it) { binding.subVideoList.scrollToPosition(0) }
				binding.progressBar.progress(false)
			}
			observe(refreshState) {
				(activity as MainActivity).binding.swipeRefresh.isRefreshing = it == NetworkState.LOADING
			}
//			observe(networkState) {
//			}

			(activity as MainActivity).binding.swipeRefresh.apply {
//				isEnabled = true
//				setOnRefreshListener { refresh() }
			}
		}
	}

	private fun onVideo(video: VideoDao)
	{
		binding.host = PreferencesHelper.currentHost.get()
		binding.video = video
		if (videoViewModel.mediaItem != null)
			return

		val uri = video.streamingPlaylists?.firstOrNull { it.type == 1/*HLS*/ }?.playlistUrl ?: video.files?.first()?.fileUrl
		if (uri == null) {
			Timber.d("Error: no uri found")
			return
		}

		videoViewModel.mediaItem = MediaItem.fromUri(Uri.parse(uri))
		videoViewModel.exoPlayer?.apply {
			setMediaItem(videoViewModel.mediaItem!!)
			prepare()
			play()
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

	override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {}
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
			binding.player.onResume()
	}

	override fun onResume()
	{
		super.onResume()
		if (Util.SDK_INT <= 23 || videoViewModel.exoPlayer == null)
			binding.player.onResume()
	}

	override fun onPause()
	{
		super.onPause()
		binding.player.onPause()
	}
}