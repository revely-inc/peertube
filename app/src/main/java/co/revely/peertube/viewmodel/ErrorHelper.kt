package co.revely.peertube.viewmodel

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.revely.peertube.R
import org.jetbrains.anko.intentFor
import co.revely.peertube.ui.account.AccountActivity

/**
 * Created at 07/05/2020
 *
 * @author rbenjami
 */
class ErrorHelper
{
	companion object
	{
		private val _error = MutableLiveData<Error>()
		val error: LiveData<Error> = _error

		fun setError(error: Error) = this._error.postValue(error)
	}

	open class Error(@StringRes val title: Int, @StringRes val actionText: Int, val action: (Context) -> Unit)
	{
		var displayed = 0
	}

	class Retry(action: (Context) -> Unit): Error(R.string.error, R.string.retry, action)

	class NotLogged: Error(R.string.error, R.string.retry, {
		it.startActivity(it.intentFor<AccountActivity>())
	})
}