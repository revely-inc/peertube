package co.revely.peertube.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
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

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, onChanged: (T) -> Unit) =
	liveData.observe(this, onChanged)

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

fun View.showKeyboard()
{
	val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun View.hideKeyboard()
{
	val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
	imm.hideSoftInputFromWindow(windowToken, 0)
}