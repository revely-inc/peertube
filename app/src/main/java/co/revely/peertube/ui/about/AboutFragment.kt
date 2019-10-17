package co.revely.peertube.ui.about

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentAboutBinding
import co.revely.peertube.ui.LayoutFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
class AboutFragment : LayoutFragment<FragmentAboutBinding>(R.layout.fragment_about)
{
	private val args: AboutFragmentArgs by navArgs()

	val adapter: AboutAdapter by lazy {
		AboutAdapter(this, args.host)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		view_pager.adapter = adapter
		TabLayoutMediator(tab_layout, view_pager) { tab, position ->
			view_pager.setCurrentItem(tab.position, true)
			if (position == 1) tab.setText(R.string.instance)
			if (position == 0) tab.setText(R.string.app_name)
		}.attach()
	}
}