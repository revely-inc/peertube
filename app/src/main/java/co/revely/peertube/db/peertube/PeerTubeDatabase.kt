package co.revely.peertube.db.peertube

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.revely.peertube.api.peertube.response.OAuthClient
import co.revely.peertube.api.peertube.response.OAuthToken

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
@Database(entities = [
	OAuthToken::class,
	OAuthClient::class
], version = 3, exportSchema = false)
abstract class PeerTubeDatabase : RoomDatabase()
{
	companion object
	{
		private val databases = HashMap<String, PeerTubeDatabase>()

		fun instance(context: Context, host: String): PeerTubeDatabase
		{
			if (databases.containsKey(host))
				return databases[host]!!
			val database = Room
				.databaseBuilder(context, PeerTubeDatabase::class.java, "$host.db")
				.fallbackToDestructiveMigration()
				.build()
			databases[host] = database
			return database
		}
	}

	abstract fun oAuthTokenDao(): OAuthToken.Dao
	abstract fun oAuthClientDao(): OAuthClient.Dao
}