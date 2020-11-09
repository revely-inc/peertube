package co.revely.peertube.composable

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.AlignmentLine
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.ui.tooling.preview.Preview

/**
 * Created at 09/11/2020
 *
 * @author rbenjami
 */
@Composable
fun TextIcon(
	text: String,
	asset: VectorAsset,
	modifier: Modifier = Modifier
)
{
	Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
		Icon(asset = asset)
		Text(text = text, style = PeertubeTypography.caption)
	}
}

@Preview
@Composable
fun TextIconPreview()
{
	PeertubeTheme {
		TextIcon(text = "Text to test", asset = Icons.Rounded.AccountCircle)
	}
}

@Preview
@Composable
fun TextIconPreviewDark()
{
	PeertubeTheme(true) {
		TextIcon(text = "Text to test", asset = Icons.Rounded.AccountCircle)
	}
}