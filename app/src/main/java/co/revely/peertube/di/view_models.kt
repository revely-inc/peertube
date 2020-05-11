package co.revely.peertube.di

import co.revely.peertube.helper.PreferencesHelper
import co.revely.peertube.ui.account.login.LoginViewModel
import co.revely.peertube.ui.instances.InstancesViewModel
import co.revely.peertube.ui.video.VideoViewModel
import co.revely.peertube.ui.videos.VideosViewModel
import co.revely.peertube.viewmodel.InstanceViewModel
import co.revely.peertube.viewmodel.OAuthViewModel
import co.revely.peertube.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created at 2019-10-11
 *
 * @author rbenjami
 */
val viewModelModule = module {
	viewModel { InstancesViewModel(get()) }
	viewModel { VideosViewModel(get()) }
	viewModel { (videoId: String, oAuthViewModel: OAuthViewModel) -> VideoViewModel(videoId, oAuthViewModel) }
	viewModel { LoginViewModel(PreferencesHelper.defaultHost.get()) }
	viewModel { OAuthViewModel() }
	viewModel { (oAuthViewModel: OAuthViewModel) -> UserViewModel(get(), oAuthViewModel) }
	viewModel { InstanceViewModel() }
}