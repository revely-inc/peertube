package co.revely.peertube.repository.peertube.video

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import co.revely.peertube.api.peertube.PeerTubeService
import co.revely.peertube.api.peertube.query.VideoQuery
import co.revely.peertube.db.peertube.entity.Video
import java.util.concurrent.Executor

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
class VideoDataSourceFactory(
		private val peerTubeService: PeerTubeService,
		private val retryExecutor: Executor,
		private val videoQuery: VideoQuery?
) : DataSource.Factory<Int, Video>()
{
	val sourceLiveData = MutableLiveData<PageKeyedVideoDataSource>()

	override fun create(): DataSource<Int, Video>
	{
		val source = PageKeyedVideoDataSource(peerTubeService, retryExecutor, videoQuery)
		sourceLiveData.postValue(source)
		return source
	}
}