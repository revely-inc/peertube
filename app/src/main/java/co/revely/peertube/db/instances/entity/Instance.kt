package co.revely.peertube.db.instances.entity

import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
@Entity
@Keep
data class Instance(
	@PrimaryKey(autoGenerate = true)
	val id: Long,
	val name:  String,
	val host: String,
	val shortDescription: String,
	val version: String,
	val signupAllowed: Boolean,
	val userVideoQuota: Long,
	val totalUsers: Long,
	val totalVideos: Long,
	val totalLocalVideos: Long,
	val totalInstanceFollowers: Long,
	val totalInstanceFollowing: Long,
	val supportsIPv6: Boolean,
	val health: Long
)
{
	@androidx.room.Dao
	interface Dao
	{
		@Insert(onConflict = OnConflictStrategy.REPLACE)
		fun insert(vararg instances: Instance)

		@Query("SELECT * FROM instance")
		fun load(): LiveData<List<Instance>>

		@Query("SELECT * FROM instance WHERE host=:host")
		fun loadByHost(host: String): LiveData<Instance>
	}
}