package co.revely.peertube.composable.video

import android.text.format.DateUtils
import androidx.compose.foundation.AmbientContentColor
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.annotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import co.revely.peertube.api.dao.AccountDao
import co.revely.peertube.api.dao.AvatarDao
import co.revely.peertube.api.dao.CommentDao
import co.revely.peertube.composable.Markdown
import co.revely.peertube.composable.PeertubeTheme
import co.revely.peertube.composable.appendMarkdownChildren
import co.revely.peertube.composable.commentPreview
import co.revely.peertube.helper.PreferencesHelper
import dev.chrisbanes.accompanist.coil.CoilImage
import timber.log.Timber
import java.util.*


/**
 * Created at 29/10/2020
 *
 * @author rbenjami
 */
@Composable
fun Comment(
		comment: CommentDao,
		modifier: Modifier = Modifier
)
{
	Column(
			modifier = modifier.background(PeertubeTheme.colors.surface).padding(16.dp)
	) {
		comment.account?.also { account ->
			Row(verticalAlignment = Alignment.CenterVertically) {
				Card(
						modifier = Modifier
						.preferredSize(32.dp),
						shape = CircleShape
				) {
					CoilImage(
							data = "https://${account.host}${account.avatar?.path}",
							fadeIn = true,
							error = {
								CoilImage(data = "https://${account.host}/client/assets/images/default-avatar.png")
							}
					)
				}
				Spacer(modifier = Modifier.width(16.dp))
				Text(
						text = annotatedString {
							pushStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp))
							append("${account.displayName}")
							pop()
							comment.createdAt?.also {
								pushStyle(SpanStyle(color = Color.DarkGray, fontSize = 12.sp))
								append(" • ${DateUtils.getRelativeTimeSpanString(it.time)}")
								pop()
							}
						}
				)
			}
		}
		comment.text?.also {
			Markdown(it)
		}
	}
}

@Preview
@Composable
fun CommentPreview()
{
	PeertubeTheme {
		Comment(commentPreview)
	}
}

@Preview
@Composable
fun CommentPreviewDark()
{
	PeertubeTheme(darkTheme = true) {
		Comment(commentPreview)
	}
}