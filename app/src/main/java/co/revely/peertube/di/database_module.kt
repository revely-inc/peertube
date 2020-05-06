package co.revely.peertube.di

import androidx.room.Room
import co.revely.peertube.db.instances.InstancesDatabase
import co.revely.peertube.db.peertube.PeerTubeDatabase
import co.revely.peertube.helper.PreferencesHelper
import co.revely.peertube.repository.instances.InstancesRepository
import co.revely.peertube.repository.peertube.comment.CommentRepository
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

	single { VideoRepository(get(), get()) }
	single { CommentRepository(get(), get()) }

	single { get<InstancesDatabase>().instanceDao() }
	single { InstancesRepository(get(), get(), get()) }

	single { PeerTubeDatabase.instance(get(), PreferencesHelper.defaultHost.get()) }

	single { UserRepository(get()) }

	single { get<PeerTubeDatabase>().oAuthTokenDao() }
	single { get<PeerTubeDatabase>().oAuthClientDao() }
	single { OAuthRepository(get(), get(), get(), get()) }
}