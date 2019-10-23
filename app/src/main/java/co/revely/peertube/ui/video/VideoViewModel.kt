package co.revely.peertube.ui.video

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import co.revely.peertube.api.ApiSuccessResponse
import co.revely.peertube.repository.peertube.video.VideoRepository
import co.revely.peertube.utils.Rate
import co.revely.peertube.utils.enqueue
import com.google.android.exoplayer2.SimpleExoPlayer
import timber.log.Timber

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class VideoViewModel(private val id: String, private val videoRepository: VideoRepository) : ViewModel()
{
	var exoPlayer: SimpleExoPlayer? = null

	val video = videoRepository.getVideoById(id)

	private val _rating = videoRepository.getMyRating(id)
	val rating = MediatorLiveData<@Rate String>()

	init
	{
		rating.addSource(_rating) {
			if (it is ApiSuccessResponse)
				rating.value = it.body.rating
		}
	}

	override fun onCleared()
	{
		super.onCleared()
		exoPlayer?.release()
		exoPlayer = null
	}

	fun onLikeVideoClicked()
	{
		val r = if (rating.value == Rate.LIKE) Rate.NONE else Rate.LIKE
		videoRepository.rateVideo(id, r)
				.enqueue { _, response, t ->
					if (response?.isSuccessful == true)
						rating.value = r
					else
						Timber.e(t)
				}
	}

	fun onDislikeVideoClicked()
	{
		val r = if (rating.value == Rate.DISLIKE) Rate.NONE else Rate.DISLIKE
		videoRepository.rateVideo(id, r)
				.enqueue { _, response, t ->
					if (response?.isSuccessful == true)
						rating.value = r
					else
						Timber.e(t)
				}
	}

	fun onShareVideoClicked()
	{

	}

	fun onDownloadVideoClicked()
	{

	}
}