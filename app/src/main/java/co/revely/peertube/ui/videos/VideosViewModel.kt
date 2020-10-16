package co.revely.peertube.ui.videos

import androidx.lifecycle.ViewModel
import co.revely.peertube.repository.peertube.video.VideoRepository

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
class VideosViewModel(private val videoRepository: VideoRepository) : ViewModel()
{
	val videos = videoRepository.videosListing { peerTubeService, size, index ->
		peerTubeService.getVideos(start = index, count = size)
	}
	val videosTrending = videoRepository.videosListing { peerTubeService, size, index ->
		peerTubeService.getVideos(sort = "-trending", start = index, count = size)
	}
	val videosRecent = videoRepository.videosListing { peerTubeService, size, index ->
		peerTubeService.getVideos(sort = "-publishedAt", start = index, count = size)
	}
	val videosLocal = videoRepository.videosListing{ peerTubeService, size, index ->
		peerTubeService.getVideos(filter = "local", start = index, count = size)
	}

	fun videosByQuery(query: String) = videoRepository.videosListing { peerTubeService, size, index ->
		peerTubeService.searchVideos(query, start = index, count = size)
	}
}