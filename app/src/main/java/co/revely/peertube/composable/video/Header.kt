package co.revely.peertube.composable.video

import android.text.format.DateUtils
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.annotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import co.revely.peertube.R
import co.revely.peertube.api.dao.VideoDao
import co.revely.peertube.composable.PeertubeTheme
import co.revely.peertube.composable.PeertubeTypography
import co.revely.peertube.utils.humanReadableBigNumber
import dev.chrisbanes.accompanist.coil.CoilImage

/**
 * Created at 02/11/2020
 *
 * @author rbenjami
 */
@Composable
fun Header(
		video: VideoDao,
		modifier: Modifier = Modifier
)
{
	ConstraintLayout(
		modifier = modifier.fillMaxWidth().padding(16.dp)
	) {
		val (title, date_views) = createRefs()
		video.name?.also {
			Text(
					modifier = modifier.constrainAs(title) {
						top.linkTo(parent.top)
					},
					text = it,
					style = PeertubeTypography.h5.copy(fontWeight = FontWeight.Bold)
			)
		}
		video.createdAt?.also {
			Text(
					modifier = modifier.constrainAs(date_views) {
						top.linkTo(title.bottom)
					},
					text = annotatedString {
						pushStyle(SpanStyle(fontSize = 12.sp))
						append(DateUtils.getRelativeTimeSpanString(it.time).toString())
						video.views?.also {
							append(" • ${it.humanReadableBigNumber()} ${stringResource(id = R.string.views)}")
						}
					}
			)
		}
		CoilImage(
				modifier = Modifier.clickable {
					
				},
				data = R . drawable . ic_arrow_down,
		)
	}
}

val videoPreview = VideoDao(
		"123",
		name = "Test video"
)

@Preview
@Composable
fun HeaderPreview()
{
	PeertubeTheme {
		Header(videoPreview)
	}
}

@Preview
@Composable
fun HeaderPreviewDark()
{
	PeertubeTheme(darkTheme = true) {
		Header(videoPreview)
	}
}