package co.revely.peertube.db.peertube.entity

import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.gson.annotations.SerializedName

/**
 * Created at 2019-10-20
 *
 * @author rbenjami
 */
@Entity(tableName = "oAuthClient")
@Keep
data class OAuthClient(
		@SerializedName("client_id")
		val id: String,
		@SerializedName("client_secret")
		val secret: String,
		@PrimaryKey
		val host: String
)
{
	@androidx.room.Dao
	interface Dao
	{
		@Insert(onConflict = OnConflictStrategy.REPLACE)
		fun insert(vararg oAuthTokens: OAuthClient)

		@Query("SELECT * FROM oAuthClient WHERE host=:host")
		fun load(host: String): LiveData<OAuthClient>
	}
}