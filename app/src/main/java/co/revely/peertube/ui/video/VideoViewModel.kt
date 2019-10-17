package co.revely.peertube.ui.video

import androidx.lifecycle.ViewModel
import co.revely.peertube.repository.peertube.video.VideoRepository
import co.revely.peertube.utils.AppExecutors

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class VideoViewModel(host: String, id: String, appExecutors: AppExecutors) : ViewModel()
{
	private val videoRepository = VideoRepository.instance(host, appExecutors)

	val video = videoRepository.getVideoById(id)
	val rating = videoRepository.getMyRating(id)

	var startAutoPlay: Boolean = true
	var startWindow: Int = 0
	var startPosition: Long = 0

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