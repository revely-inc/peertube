package co.revely.peertube.viewmodel

import androidx.lifecycle.ViewModel
import co.revely.peertube.api.PeerTubeService
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created at 11/05/2020
 *
 * @author rbenjami
 */
class InstanceViewModel: ViewModel(), KoinComponent
{
	private val peerTubeService: PeerTubeService by inject()

	val configAbout = peerTubeService.configAbout()
	val serverStats = peerTubeService.serverStats()
}