package co.revely.peertube.composable.video

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.ui.tooling.preview.Preview
import co.revely.peertube.api.dao.VideoDao
import co.revely.peertube.composable.PeertubeTheme
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
	val video: VideoDao? by videoViewModel.video.observeAsState()

	val list by videoViewModel.comments.pagedList.observeAsState(initial = emptyList())

	LazyColumnForIndexed(items = list) { index, comment ->
		if (index == 0)
			video?.also { Header(it) }

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