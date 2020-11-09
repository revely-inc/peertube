package co.revely.peertube.composable.video

import android.text.format.DateUtils
import androidx.compose.animation.*
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.annotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import co.revely.peertube.R
import co.revely.peertube.api.dao.DescriptionDao
import co.revely.peertube.api.dao.RateDao
import co.revely.peertube.api.dao.VideoDao
import co.revely.peertube.composable.*
import co.revely.peertube.utils.humanReadableBigNumber
import dev.chrisbanes.accompanist.coil.CoilImage
import timber.log.Timber

/**
 * Created at 02/11/2020
 *
 * @author rbenjami
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Header(
	video: VideoDao,
	videoDescription: DescriptionDao,
	rate: RateDao?,
	modifier: Modifier = Modifier
)
{
	ConstraintLayout(
		modifier = modifier.fillMaxWidth().padding(16.dp).background(MaterialTheme.colors.background)
	) {
		val (
			title,
			dateViews,
			expand,
			description,
			thumbUp,
			thumbDown,
			share,
			download
		) = createRefs()

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
				modifier = modifier.constrainAs(dateViews) {
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

		video.likes?.also {
			TextIcon(
				modifier = Modifier
					.constrainAs(thumbUp) {
						top.linkTo(dateViews.bottom)
						start.linkTo(parent.start)
						end.linkTo(thumbDown.start)
						width = Dimension.fillToConstraints
					}
					.padding(top = 16.dp),
				asset = Icons.Rounded.ThumbUp,
				text = it.humanReadableBigNumber()
			)
		}
		video.dislikes?.also {
			TextIcon(
				modifier = Modifier
					.constrainAs(thumbDown) {
						top.linkTo(dateViews.bottom)
						start.linkTo(thumbUp.end)
						end.linkTo(share.start)
						width = Dimension.fillToConstraints
					}
					.padding(top = 16.dp),
				asset = Icons.Rounded.ThumbDown,
				text = it.humanReadableBigNumber()
			)
		}
		TextIcon(
			modifier = Modifier
				.constrainAs(share) {
					top.linkTo(dateViews.bottom)
					start.linkTo(thumbDown.end)
					end.linkTo(download.start)
					width = Dimension.fillToConstraints
				}
				.padding(top = 16.dp),
			asset = Icons.Rounded.Share,
			text = stringResource(R.string.share)
		)
		TextIcon(
			modifier = Modifier
				.constrainAs(download) {
					top.linkTo(dateViews.bottom)
					start.linkTo(share.end)
					end.linkTo(parent.end)
					width = Dimension.fillToConstraints
				}
				.padding(top = 16.dp),
			asset = Icons.Rounded.CloudDownload,
			text = stringResource(R.string.download)
		)

		val expanded = remember { mutableStateOf(false) }
		IconButton(
			modifier = Modifier
				.constrainAs(expand) {
					top.linkTo(parent.top)
					end.linkTo(parent.end)
				},
			onClick = {
				expanded.value = expanded.value.not()
			}
		) {
			Icon(if (expanded.value) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown)
		}
		AnimatedVisibility(
			enter = fadeIn() + slideIn({ IntOffset(0, -it.height) }),
			exit = fadeOut() + slideOut({ IntOffset(0, -it.height) }),
			modifier = Modifier
				.padding(top = 8.dp)
				.constrainAs(description) {
					top.linkTo(thumbUp.bottom)
					start.linkTo(parent.start)
					end.linkTo(parent.end)
				},
			visible = expanded.value
		) {
			Divider(modifier = Modifier.fillMaxWidth())
			videoDescription.description?.also {
				Markdown(text = it)
			}
		}
	}
	Divider(modifier = Modifier.fillMaxWidth())
}

@Preview
@Composable
fun HeaderPreview()
{
	PeertubeTheme {
		Header(videoPreview, DescriptionDao(MIXED_MD), RateDao())
	}
}

@Preview
@Composable
fun HeaderPreviewDark()
{
	PeertubeTheme(darkTheme = true) {
		Header(videoPreview, DescriptionDao(MIXED_MD), RateDao())
	}
}