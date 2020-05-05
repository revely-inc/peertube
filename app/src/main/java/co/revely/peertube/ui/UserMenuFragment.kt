package co.revely.peertube.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import co.revely.peertube.InstanceNavGraphDirections
import co.revely.peertube.R
import co.revely.peertube.api.ApiSuccessResponse
import co.revely.peertube.utils.GlideApp
import co.revely.peertube.utils.observe
import co.revely.peertube.viewmodel.OAuthViewModel
import co.revely.peertube.viewmodel.UserViewModel
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

/**
 * Created at 2019-10-22
 *
 * @author rbenjami
 */
open class UserMenuFragment<DB : ViewDataBinding>(@LayoutRes layoutId: Int): LayoutFragment<DB>(layoutId)
{
	protected val oAuthViewModel: OAuthViewModel by sharedViewModel(parameters = { parametersOf(host) })
	protected val userViewModel: UserViewModel by sharedViewModel(parameters = { parametersOf(host, oAuthViewModel) })
	protected val host by lazy { arguments?.getString("host")!! }

	private lateinit var menu: Menu

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		setHasOptionsMenu(true)
		return super.onCreateView(inflater, container, savedInstanceState)
	}

	private fun initUser()
	{
		observe(userViewModel.me()) { response -> when (response) {
			is ApiSuccessResponse -> {
				val user = response.body
				(activity as? AppCompatActivity?)?.also { activity ->
					GlideApp.with(activity).load(user.account?.avatar?.path?.let { "https://$host$it" })
							.thumbnail(GlideApp.with(activity)
									.load("https://$host/client/assets/images/default-avatar.png")
									.apply(RequestOptions.circleCropTransform())
							)
							.apply(RequestOptions.circleCropTransform())
							.into(object : CustomTarget<Drawable>() {
								override fun onLoadCleared(placeholder: Drawable?) {}
								override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
									menu.findItem(R.id.account).icon = resource
								}
							})
				}
			}
		} }
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
	{
		this.menu = menu
		inflater.inflate(R.menu.menu_videos, menu)
		initUser()
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		when (item.itemId)
		{
			R.id.account -> {
				observe(userViewModel.me()) {
					val direction = if (it is ApiSuccessResponse)
						InstanceNavGraphDirections.actionGlobalNavigationAccount(host)
					else
						InstanceNavGraphDirections.actionGlobalNavigationLogin(host)
					findNavController().navigate(direction)
				}
			}
			else -> return false
		}
		return true
	}
}