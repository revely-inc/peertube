package co.revely.peertube.helper

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * Created at 30/10/2018
 *
 * @author rbenjami
 */
class PreferencesHelper
{
	class Pref<T>(val name: String, private val defaultValue: T): MutableLiveData<T>(), KoinComponent
	{
		private val sharedPreferences: SharedPreferences by inject()

		init
		{
			postValue(get())
		}

		fun set(value: T): T
		{
			Timber.d("Change pref $name to $value")
			when (value) {
				is String -> sharedPreferences.edit { putString(name, value) }
				is Int -> sharedPreferences.edit { putInt(name, value) }
				is Boolean -> sharedPreferences.edit { putBoolean(name, value) }
				is Float -> sharedPreferences.edit { putFloat(name, value) }
				is Long -> sharedPreferences.edit { putLong(name, value) }
				is Enum<*> -> sharedPreferences.edit { putString(name, value.name) }
				is Set<*> -> sharedPreferences.edit { putStringSet(name, value as Set<String>) }
				else -> throw UnsupportedOperationException("Not yet implemented")
			}
			postValue(value)
			return value
		}

		@Suppress("unchecked_cast")
		fun get(): T {
			val r = when (defaultValue) {
				is String -> sharedPreferences.getString(name, defaultValue) as T
				is Int -> sharedPreferences.getInt(name, defaultValue as? Int ?: 0) as T
				is Boolean -> sharedPreferences.getBoolean(name, defaultValue as? Boolean ?: false) as T
				is Float -> sharedPreferences.getFloat(name, defaultValue as? Float ?: 0f) as T
				is Long -> sharedPreferences.getLong(name, defaultValue as? Long ?: 0) as T
				is Set<*> -> sharedPreferences.getStringSet(name, mutableSetOf()) as T
				else -> throw UnsupportedOperationException("Not yet implemented")
			}
			Timber.d("Get pref $name: $r")
			return r
		}

		fun reset(): T = set(defaultValue)
	}

	companion object
	{
		val currentHost by lazy { Pref("CURRENT_HOST", "") }
		val hostsLogged by lazy { Pref("HOSTS_LOGGED", mutableSetOf<String>()) }
	}
}