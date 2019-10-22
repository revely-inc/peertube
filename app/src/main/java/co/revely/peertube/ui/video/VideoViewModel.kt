package co.revely.peertube.ui.video

import androidx.lifecycle.ViewModel
import co.revely.peertube.repository.peertube.video.VideoRepository
import com.google.android.exoplayer2.SimpleExoPlayer

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class VideoViewModel(id: String, videoRepository: VideoRepository) : ViewModel()
{
	var exoPlayer: SimpleExoPlayer? = null
//	private var mediaSource: MediaSource? = null

	val video = videoRepository.getVideoById(id)
	val rating = videoRepository.getMyRating(id)

	override fun onCleared()
	{
		super.onCleared()
		exoPlayer?.release()
		exoPlayer = null
	}

	fun onLikeVideoClicked()
	{
//		videoRepository.likeVideo(id)
	}

	fun onDislikeVideoClicked()
	{
//		videoRepository.dislikeVideo(id)
	}

	fun onShareVideoClicked()
	{

	}

	fun onDownloadVideoClicked()
	{

	}
}