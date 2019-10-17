package co.revely.peertube.ui.videos

import androidx.lifecycle.ViewModel
import co.revely.peertube.api.peertube.query.VideoQuery
import co.revely.peertube.repository.peertube.video.VideoRepository
import co.revely.peertube.utils.AppExecutors

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class VideosViewModel constructor(host: String, appExecutors: AppExecutors) : ViewModel()
{
	private val videoRepository = VideoRepository.instance(host, appExecutors)

	val videos = videoRepository.getVideos(null)
	val videosTrending = videoRepository.getVideos(VideoQuery(sort = "-trending"))
	val videosRecent = videoRepository.getVideos(VideoQuery(sort = "-publishedAt"))
	val videosLocal = videoRepository.getVideos(VideoQuery(filter = "local"))
}