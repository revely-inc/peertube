package co.revely.peertube.composable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.ResourceFont
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.unit.sp
import co.revely.peertube.R

/**
 * Created at 30/10/2020
 *
 * @author rbenjami
 */
private val OpenSans = fontFamily(
		fonts = listOf(
				ResourceFont(
						resId = R.font.open_sans_light,
						weight = FontWeight.W300,
						style = FontStyle.Normal
				),
				ResourceFont(
						resId = R.font.open_sans,
						weight = FontWeight.W700,
						style = FontStyle.Normal
				)
		)
)

val PeertubeTypography = Typography(
		h1 = TextStyle(
				fontWeight = FontWeight.Light,
				fontFamily = OpenSans,
				fontSize = 96.sp,
				letterSpacing = (-1.5).sp
		),
		h2 = TextStyle(
				fontWeight = FontWeight.Light,
				fontFamily = OpenSans,
				fontSize = 60.sp,
				letterSpacing = (-0.5).sp
		),
		h3 = TextStyle(
				fontWeight = FontWeight.Normal,
				fontFamily = OpenSans,
				fontSize = 48.sp,
				letterSpacing = 0.sp
		),
		h4 = TextStyle(
				fontFamily = OpenSans,
				fontWeight = FontWeight.W600,
				fontSize = 30.sp
		),
		h5 = TextStyle(
				fontFamily = OpenSans,
				fontWeight = FontWeight.W600,
				fontSize = 24.sp
		),
		h6 = TextStyle(
				fontFamily = OpenSans,
				fontWeight = FontWeight.W600,
				fontSize = 20.sp
		),
		subtitle1 = TextStyle(
				fontFamily = OpenSans,
				fontWeight = FontWeight.W600,
				fontSize = 16.sp
		),
		subtitle2 = TextStyle(
				fontFamily = OpenSans,
				fontWeight = FontWeight.W500,
				fontSize = 14.sp
		),
		body1 = TextStyle(
				fontFamily = OpenSans,
				fontWeight = FontWeight.Normal,
				fontSize = 16.sp
		),
		body2 = TextStyle(
				fontFamily = OpenSans,
				fontSize = 14.sp
		),
		button = TextStyle(
				fontFamily = OpenSans,
				fontWeight = FontWeight.W500,
				fontSize = 14.sp
		),
		caption = TextStyle(
				fontFamily = OpenSans,
				fontWeight = FontWeight.Normal,
				fontSize = 12.sp
		),
		overline = TextStyle(
				fontFamily = OpenSans,
				fontWeight = FontWeight.W500,
				fontSize = 12.sp
		)
)

val Primary = Color(0xF1680D)
val PrimaryDark = Color(0xCD590C)
val Red800 = Color(0xffd00036)

private val LightColors = lightColors(
	primary = Primary,
	primaryVariant = PrimaryDark,
	onPrimary = Color.White,
	onSecondary = Color.White,
	error = Red800
)

private val DarkColors = darkColors(
	primary = Primary,
	primaryVariant = PrimaryDark,
	onPrimary = Color.Black,
	onSecondary = Color.White,
	error = Red800
)

@Composable
fun PeertubeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
	MaterialTheme(
		colors = if (darkTheme) DarkColors else LightColors,
		typography = PeertubeTypography,
		content = content
	)
}