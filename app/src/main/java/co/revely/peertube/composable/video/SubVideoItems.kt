package co.revely.peertube.composable.video

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.ui.tooling.preview.Preview
import co.revely.peertube.api.ApiResponse
import co.revely.peertube.api.dao.CommentDao
import co.revely.peertube.api.dao.DescriptionDao
import co.revely.peertube.api.dao.RateDao
import co.revely.peertube.api.dao.VideoDao
import co.revely.peertube.composable.PeertubeTheme
import co.revely.peertube.composable.Video
import co.revely.peertube.ui.video.VideoViewModel
import co.revely.peertube.viewmodel.OAuthViewModel

/**
 * Created at 29/10/2020
 *
 * @author rbenjami
 */
@Composable
fun SubVideoItems(videoViewModel: VideoViewModel)
{
	val video: VideoDao by videoViewModel.video.data.observeAsState(VideoDao("unknown"))
	val videoDescription by  videoViewModel.videoDescription.data.observeAsState(DescriptionDao())
	val comments by videoViewModel.comments.data.observeAsState(emptyList())
	val rating by videoViewModel.rating.data.observeAsState()

	LazyColumnForIndexed(items = listOf(CommentDao(isDeleted = true), *comments.toTypedArray())) { index, comment ->
		if (index == 0)
			Header(video, videoDescription, rating)

		if (comment.isDeleted == false)
		{
			Comment(comment)
			Divider(modifier = Modifier.fillMaxWidth())
		}
	}
}

@Preview
@Composable
fun SubVideoItemsPreview()
{
	PeertubeTheme {
		SubVideoItems(VideoViewModel("", OAuthViewModel()))
	}
}