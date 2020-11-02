package co.revely.peertube.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import co.revely.peertube.R
import timber.log.Timber

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
open class LayoutFragment<DB : ViewDataBinding>(@LayoutRes val layoutId: Int) : Fragment()
{
	protected lateinit var binding: DB

	open fun title(): String? = null

	@Suppress("UNCHECKED_CAST")
	fun <DB> binding() = binding as? DB

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
		binding.lifecycleOwner = viewLifecycleOwner
		(activity as? AppCompatActivity)?.apply {
			supportActionBar?.apply {
				title()?.also { title = it }
				show()
			}
		}
		return binding.root
	}
}

fun <DB: ViewDataBinding> Fragment.binding(): DB? =
	if (this is LayoutFragment<*>) binding() else null
