package co.revely.peertube.api.peertube.response

import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.gson.annotations.SerializedName

/**
 * Created at 2019-10-20
 *
 * @author rbenjami
 */
@Entity(tableName = "oAuthClient")
data class OAuthClient(
		@SerializedName("client_id")
		val id: String,
		@SerializedName("client_secret")
		val secret: String,
		@PrimaryKey
		val unique: String = "unique"
)
{
	@androidx.room.Dao
	interface Dao
	{
		@Insert(onConflict = OnConflictStrategy.REPLACE)
		fun insert(vararg oAuthTokens: OAuthClient)

		@Query("SELECT * FROM oAuthClient")
		fun load(): LiveData<OAuthClient>
	}
}