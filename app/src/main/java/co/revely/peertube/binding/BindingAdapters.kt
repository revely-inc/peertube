package co.revely.peertube.binding

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import co.revely.peertube.utils.GlideApp
import co.revely.peertube.utils.duration
import co.revely.peertube.utils.humanReadableBigNumber
import com.bumptech.glide.request.RequestOptions
import io.noties.markwon.Markwon
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
@BindingAdapter("drawableTint")
fun drawableTint(view: TextView, @ColorInt color: Int)
{
	for (drawable in view.compoundDrawables)
	{
		if (drawable != null)
			drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
	}
}

@BindingAdapter("duration")
fun duration(view: TextView, duration: Long)
{
	view.text = duration.duration()
}

@BindingAdapter("markdown")
fun markdown(view: TextView, markdown: String?)
{
	val markwon = object: KoinComponent { val markwon: Markwon by inject() }.markwon

	if (markdown != null)
		markwon.setMarkdown(view, markdown)
}

@BindingAdapter("humanReadableDate")
fun humanReadableDate(view: TextView, date: Date)
{
	view.text = DateUtils.getRelativeTimeSpanString(date.time, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("humanReadableBigNumber", "humanReadableBigNumberPrefix", "humanReadableBigNumberSuffix", requireAll = false)
fun humanReadableBigNumber(view: TextView, n: Long, prefix: String?, suffix: String?)
{
	view.text = "${prefix ?: ""}${n.humanReadableBigNumber()}${suffix ?: ""}"
}

@BindingAdapter("joinToString", "joinToStringSeparator", "joinToStringPrefix", "joinToStringSuffix", requireAll = false)
fun joinToString(view: TextView, list: List<String>?, separator: String?, prefix: String?, suffix: String?)
{
	view.text = list?.joinToString(separator ?: " ") { "${prefix ?: ""}$it${suffix ?: ""}" } ?: ""
}

@BindingAdapter("glideUrl", "glideThumbnailUrl", "glideCircleCrop", requireAll = false)
fun glide(view: ImageView, url: String?, thumbnailUrl: String?, circleCrop: Boolean?)
{
	GlideApp.with(view)
			.load(url)
			.apply {
				if (thumbnailUrl != null)
					thumbnail(GlideApp.with(view)
							.load(thumbnailUrl).apply {
								if (circleCrop == true)
									apply(RequestOptions.circleCropTransform())
							}
					)
				if (circleCrop == true)
					apply(RequestOptions.circleCropTransform())
			}
			.into(view)
}

@BindingAdapter("visible")
fun visible(view: View, visible: Boolean) {
	view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}