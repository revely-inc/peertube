package co.revely.peertube.repository.peertube.comment

import androidx.annotation.MainThread
import androidx.lifecycle.Transformations
import androidx.paging.toLiveData
import co.revely.peertube.api.PeerTubeService
import co.revely.peertube.api.dto.CommentDto
import co.revely.peertube.api.dao.CommentDao
import co.revely.peertube.utils.AppExecutors
import co.revely.peertube.api.ApiDataList
import co.revely.peertube.api.DataList
import co.revely.peertube.api.apiDataList
import co.revely.peertube.api.dao.VideoDao
import retrofit2.Call

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
class CommentRepository(
		private val peerTubeService: PeerTubeService
)
{
	@MainThread
	fun commentsDataList(
		request: ((peerTubeService: PeerTubeService, size: Int, index: Int) -> Call<DataList<CommentDao>>)
	) =
		apiDataList(peerTubeService, request)
}