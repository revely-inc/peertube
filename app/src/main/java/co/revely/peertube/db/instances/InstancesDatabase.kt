package co.revely.peertube.db.instances

import androidx.room.Database
import androidx.room.RoomDatabase
import co.revely.peertube.db.instances.entity.Instance

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
@Database(entities = [Instance::class], version = 1, exportSchema = false)
abstract class InstancesDatabase : RoomDatabase()
{
	abstract fun instanceDao(): Instance.Dao
}