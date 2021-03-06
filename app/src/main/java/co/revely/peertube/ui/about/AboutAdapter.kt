package co.revely.peertube.ui.about

import androidx.viewpager2.adapter.FragmentStateAdapter


/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
class AboutAdapter(aboutFragment: AboutFragment) : FragmentStateAdapter(aboutFragment)
{
	override fun getItemCount() = 2

	override fun createFragment(position: Int) = when(position) {
		0 -> AboutInstanceFragment.newInstance()
		1 -> AboutPeertubeFragment.newInstance()
		else -> throw IndexOutOfBoundsException("AboutAdapter size is $itemCount, position: $position")
	}
}