package co.revely.peertube.binding

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import co.revely.peertube.utils.duration
import co.revely.peertube.utils.humanReadableBigNumber
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
@BindingAdapter("url", "circleCrop", requireAll = false)
fun loadImage(view: ImageView, circleCrop: Boolean, imageUrl: String)
{
	val builder = Glide.with(view.context).load(imageUrl)
	if (circleCrop) builder.apply(RequestOptions().circleCrop())
	builder.into(view)
}

@BindingAdapter("duration")
fun duration(view: TextView, duration: Long)
{
	view.text = duration.duration()
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
