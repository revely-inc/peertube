package co.revely.peertube.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import co.revely.peertube.repository.Status
import co.revely.peertube.repository.peertube.user.UserRepository
import co.revely.peertube.utils.AbsentLiveData

/**
 * Created at 2019-10-21
 *
 * @author rbenjami
 */
class UserViewModel(userRepository: UserRepository, oAuthViewModel: OAuthViewModel) : ViewModel()
{
	private val me = Transformations.switchMap(oAuthViewModel.token) {
		if (it?.status == Status.SUCCESS && it.data != null)
			userRepository.me()
		else
			AbsentLiveData.create()
	}

	fun me() = me
}
