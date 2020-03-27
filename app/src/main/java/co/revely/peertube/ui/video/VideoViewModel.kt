package co.revely.peertube.ui.video

import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import co.revely.peertube.api.ApiSuccessResponse
import co.revely.peertube.api.peertube.query.CommentQuery
import co.revely.peertube.api.peertube.response.Video
import co.revely.peertube.repository.peertube.comment.CommentRepository
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
class VideoViewModel(private val id: String, private val videoRepository: VideoRepository, private val commentRepository: CommentRepository) : ViewModel()
{
	var exoPlayer: SimpleExoPlayer? = null
	var mediaSource: MediaSource? = null

	private val _video = videoRepository.getVideoById(id)
	val video = MediatorLiveData<Video>()

	private val _rating = videoRepository.getMyRating(id)

	val comments = commentRepository.getComments(CommentQuery(id, sort = "-createdAt"))

	init
	{
		video.addSource(_rating) {
			if (it is ApiSuccessResponse)
				video.value?.copy(
						rating = it.body.rating
				)?.also { v -> video.value = v }
		}
		video.addSource(_video) {
			if (it is ApiSuccessResponse)
				video.value = it.body.copy(
						rating = (_rating.value as? ApiSuccessResponse)?.body?.rating
				)
		}
	}

	override fun onCleared()
	{
		super.onCleared()
		exoPlayer?.release()
		exoPlayer = null
	}

	fun onLikeVideoClicked(view: View) =
			likeOrDislikeClicked(Rate.LIKE)

	fun onDislikeVideoClicked(view: View) =
			likeOrDislikeClicked(Rate.DISLIKE)

	private fun likeOrDislikeClicked(@Rate clickedRating: String)
	{
		val r = if (video.value?.rating == clickedRating) Rate.NONE else clickedRating
		val likesToAdd = if (video.value?.rating == Rate.LIKE) -1 else if (clickedRating == Rate.LIKE) 1 else 0
		val dislikesToAdd = if (video.value?.rating == Rate.DISLIKE) -1 else if (clickedRating == Rate.DISLIKE) 1 else 0
		videoRepository.rateVideo(id, r)
				.enqueue { _, response, t ->
					if (response?.isSuccessful == true)
					{
						video.value = video.value?.copy(
								likes = (video.value?.likes ?: 0) + likesToAdd,
								dislikes = (video.value?.dislikes ?: 0) + dislikesToAdd,
								rating = r
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