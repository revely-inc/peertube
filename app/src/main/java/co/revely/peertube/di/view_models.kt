package co.revely.peertube.di

import co.revely.peertube.ui.account.login.LoginViewModel
import co.revely.peertube.ui.instances.InstancesViewModel
import co.revely.peertube.ui.video.VideoViewModel
import co.revely.peertube.ui.videos.VideosViewModel
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
	viewModel { (host: String) -> VideosViewModel(getWithParams(host)) }
	viewModel { (host: String, videoId: String) -> VideoViewModel(videoId, getWithParams(host), getWithParams(host)) }
	viewModel { (host: String) -> LoginViewModel(host) }
	viewModel { (host: String) -> OAuthViewModel(getWithParams(host)) }
	viewModel { (host: String, oAuthViewModel: OAuthViewModel) -> UserViewModel(getWithParams(host), oAuthViewModel) }
}