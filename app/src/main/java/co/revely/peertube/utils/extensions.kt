package co.revely.peertube.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import android.widget.ImageView
import androidx.core.content.res.getTextOrThrow
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import kotlin.math.ln
import kotlin.math.pow



/**
 *
 * @author rbenjami
 */
fun <T> Call<T>.enqueue(blockResponse: (Response<T>) -> Unit, blockFailure: ((Throwable) -> Unit) = { Timber.e(it) }) = enqueue(object : Callback<T> {
	override fun onResponse(call: Call<T>, response: Response<T>) = blockResponse.invoke(response)
	override fun onFailure(call: Call<T>, t: Throwable) = blockFailure.invoke(t)
})

fun Context.hasNetwork(): Boolean
{
	val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
	return activeNetwork?.isConnected ?: false
}

fun ImageView.progress(visible: Boolean)
{
	if (visible)
	{
		AnimatedVectorDrawableCompat.create(context, co.revely.peertube.R.drawable.progress_anim)?.apply {
			setImageDrawable(this)
			registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
				override fun onAnimationEnd(drawable: Drawable?) {
					start()
				}
			})
			start()
		}
	}
	else
		setImageDrawable(null)
}

fun <T> Call<T>.enqueue(function: (call: Call<T>, response: Response<T>?, t: Throwable?) -> Unit) =
		enqueue(object : Callback<T> {
			override fun onFailure(call: Call<T>, t: Throwable) = function(call, null, t)
			override fun onResponse(call: Call<T>, response: Response<T>) = function(call, response, null)
		})

fun View.visible() { visibility = View.VISIBLE }
fun View.invisible() { visibility = View.INVISIBLE }
fun View.gone() { visibility = View.GONE }

fun <T> Fragment.observe(liveData: LiveData<T>, onChanged: (T) -> Unit) =
	liveData.observe(viewLifecycleOwner, onChanged)

fun Long.duration(): String
{
	val seconds = this % 60
	val minutes = this / 1000 % 60
	val hours = this / (1000 * 60) % 24
	return if (hours != 0L) "%d:%02d:%02d".format(hours, minutes, seconds) else "%d:%02d".format(minutes, seconds)
}

fun Long.humanReadableBigNumber(): String
{
	val unit = 1000.0
	if (this < unit)
		return "$this"
	val exp = (ln(this.toDouble()) / ln(unit)).toInt()
	val pre = arrayListOf("k", "M", "Md")[exp - 1]
	return String.format("%.1f %s", this / unit.pow(exp.toDouble()), pre)
}
