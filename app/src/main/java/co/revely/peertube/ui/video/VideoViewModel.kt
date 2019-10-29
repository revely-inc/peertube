package co.revely.peertube.ui.video

import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import co.revely.peertube.api.ApiSuccessResponse
import co.revely.peertube.api.peertube.response.Video
import co.revely.peertube.repository.peertube.video.VideoRepository
import co.revely.peertube.utils.Rate
import co.revely.peertube.utils.enqueue
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import org.jetbrains.anko.toast
import timber.log.Timber

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class VideoViewModel(private val id: String, private val videoRepository: VideoRepository) : ViewModel()
{
	var exoPlayer: SimpleExoPlayer? = null
	var mediaSource: MediaSource? = null

	private val _video = videoRepository.getVideoById(id)
	val video = MediatorLiveData<Video>()

	private val _rating = videoRepository.getMyRating(id)
	val rating = MediatorLiveData<@Rate String>()

	init
	{
		rating.addSource(_rating) {
			if (it is ApiSuccessResponse)
				rating.value = it.body.rating
		}
		video.addSource(_video) {
			if (it is ApiSuccessResponse)
				video.value = it.body
		}
	}

	override fun onCleared()
	{
		super.onCleared()
		exoPlayer?.release()
		exoPlayer = null
	}

	fun onLikeVideoClicked() =
			likeOrDislikeClicked(Rate.LIKE)

	fun onDislikeVideoClicked() =
			likeOrDislikeClicked(Rate.DISLIKE)

	private fun likeOrDislikeClicked(@Rate clickedRating: String)
	{
		val r = if (rating.value == clickedRating) Rate.NONE else clickedRating
		val likesToAdd: Long = if (rating.value == Rate.LIKE) -1L else if (clickedRating == Rate.LIKE) 1L else 0L
		val dislikesToAdd: Long = if (rating.value == Rate.DISLIKE) -1L else if (clickedRating == Rate.DISLIKE) 1L else 0L
		videoRepository.rateVideo(id, r)
				.enqueue { _, response, t ->
					if (response?.isSuccessful == true)
					{
						rating.value = r
						video.value = video.value?.copy(
								likes = (video.value?.likes ?: 0) + likesToAdd,
								dislikes = (video.value?.dislikes ?: 0) + dislikesToAdd
						)
					}
					else
						Timber.e(t)
				}
	}

	fun onShareVideoClicked(view: View)
	{
		view.context.toast("No yet implemented")
	}

	fun onDownloadVideoClicked(view: View)
	{
		view.context.toast("No yet implemented")
	}
}