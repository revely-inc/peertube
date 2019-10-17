package co.revely.peertube.db.peertube

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.revely.peertube.db.peertube.entity.Video

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
@Database(entities = [
	Video::class
], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
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

	abstract fun videoDao(): Video.Dao
}