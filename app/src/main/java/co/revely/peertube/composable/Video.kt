package co.revely.peertube.composable

import androidx.compose.foundation.Text
import androidx.compose.material.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.ui.tooling.preview.Preview
import co.revely.peertube.api.dao.VideoDao
import dev.chrisbanes.accompanist.coil.CoilImage

/**
 * Created at 02/11/2020
 *
 * @author rbenjami
 */
@Composable
fun Video(
		video: VideoDao,
		modifier: Modifier = Modifier
)
{
	ListItem(
			modifier = modifier,
			icon = {
				CoilImage(
						data = "https://${video.channel?.host}${video.previewPath}",
						fadeIn = true
				)
			},
			text = {
				video.name?.also { Text(text = it) }
			}
	)
}

@Preview
@Composable
fun PreviewVideo()
{
	PeertubeTheme {
		Video(VideoDao(
				"123",
				name = "Test",
		))
	}
}

@Preview
@Composable
fun PreviewVideoDark()
{
	PeertubeTheme(true) {
		Video(VideoDao(
				"123",
				name = "Test",
		))
	}
}