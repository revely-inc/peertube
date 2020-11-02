package co.revely.peertube.repository.peertube.user

import co.revely.peertube.api.PeerTubeService

/**
 * Created at 2019-10-21
 *
 * @author rbenjami
 */
class UserRepository(
		private val peerTubeService: PeerTubeService
)
{
	fun me() = peerTubeService.me()
}