package co.revely.peertube.di

import androidx.room.Room
import co.revely.peertube.db.instances.InstancesDatabase
import co.revely.peertube.db.peertube.PeerTubeDatabase
import co.revely.peertube.repository.instances.InstancesRepository
import co.revely.peertube.repository.peertube.oauth.OAuthRepository
import co.revely.peertube.repository.peertube.user.UserRepository
import co.revely.peertube.repository.peertube.video.VideoRepository
import org.koin.dsl.module

/**
 * Created at 2019-10-20
 *
 * @author rbenjami
 */
val databaseModule = module {

	single {
		Room.databaseBuilder(get(), InstancesDatabase::class.java, "instances.db")
				.fallbackToDestructiveMigration()
				.build()
	}

	single { (host: String) -> VideoRepository(getWithParams(host), get()) }

	single { get<InstancesDatabase>().instanceDao() }
	single { InstancesRepository(get(), get(), get()) }

	single { (host: String) -> PeerTubeDatabase.instance(get(), host) }

	single { (host: String) -> UserRepository(getWithParams(host)) }

	single { (host: String) -> getWithParams<PeerTubeDatabase>(host).oAuthTokenDao() }
	single { (host: String) -> getWithParams<PeerTubeDatabase>(host).oAuthClientDao() }
	single { (host: String) -> OAuthRepository(getWithParams(host), get(), getWithParams(host), getWithParams(host)) }
}