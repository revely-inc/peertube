package co.revely.peertube.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.format.Formatter
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import co.revely.peertube.databinding.ViewStatBinding
import java.text.StringCharacterIterator

/**
 * Created at 26/03/2020
 *
 * @author rbenjami
 */
class StatView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
	ConstraintLayout(context, attrs, defStyleAttr)
{
	private var binding: ViewStatBinding =
		ViewStatBinding.inflate(LayoutInflater.from(context), this, true)

	var statValue: String = ""
		set(v) {
			field = v
			binding.value.text = statValue
		}

	fun setStatValue(value: Long)
	{
		statValue = value.toString()
	}

	var statTitle: String = ""
		set(v) {
			field = v
			binding.title.text = statTitle
		}

	var statIcon: Drawable? = null
		set(v) {
			field = v
			binding.icon.setImageDrawable(statIcon)
		}
}
