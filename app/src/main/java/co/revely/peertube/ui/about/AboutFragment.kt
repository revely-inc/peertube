package co.revely.peertube.ui.about

import android.os.Bundle
import android.view.View
import co.revely.peertube.R
import co.revely.peertube.databinding.FragmentAboutBinding
import co.revely.peertube.ui.LayoutFragment
import co.revely.peertube.ui.MainActivity
import co.revely.peertube.utils.autoCleared
import co.revely.peertube.utils.visible
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
class AboutFragment : LayoutFragment<FragmentAboutBinding>(R.layout.fragment_about)
{
	var adapter: AboutAdapter by autoCleared()

	override fun title(): String = getString(R.string.title_about)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		(activity as MainActivity).binding.navigation.visible()
		adapter = AboutAdapter(this)
		binding.viewPager.adapter = adapter
		TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
			binding.viewPager.setCurrentItem(tab.position, true)
			if (position == 0) tab.setText(R.string.instance)
			if (position == 1) tab.setText(R.string.app_name)
		}.attach()
	}
}