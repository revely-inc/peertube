package co.revely.peertube.di

import co.revely.peertube.ui.instances.InstancesViewModel
import co.revely.peertube.ui.video.VideoViewModel
import co.revely.peertube.ui.videos.VideosViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created at 2019-10-11
 *
 * @author rbenjami
 */

val viewModelModule = module {
	viewModel { InstancesViewModel(get()) }
	viewModel { (host: String) -> VideosViewModel(host, get()) }
	viewModel { (host: String, videoId: String) -> VideoViewModel(host, videoId, get()) }
}