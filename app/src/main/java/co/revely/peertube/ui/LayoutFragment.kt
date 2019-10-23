package co.revely.peertube.ui

import android.content.Context
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
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
open class LayoutFragment<DB : ViewDataBinding>(@LayoutRes val layoutId: Int) : Fragment()
{
	protected lateinit var binding: DB

	open fun title(context: Context) = ""

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
		(activity as? AppCompatActivity)?.apply {
			supportActionBar?.apply {
				title = title(binding.root.context)
				show()
			}
			swipe_refresh?.apply {
				isEnabled = false
				setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark)
			}
		}
		return binding.root
	}
}