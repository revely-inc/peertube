package co.revely.peertube.api.peertube.response

import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created at 2019-10-20
 *
 * @author rbenjami
 */
@Entity(tableName = "oAuthToken")
@Keep
data class OAuthToken(
		@SerializedName("access_token")
		val accessToken: String,
		@SerializedName("token_type")
		val tokenType: String,
		@SerializedName("expires_in")
		val expiresIn: Long,
		@SerializedName("refresh_token")
		val refreshToken: String,
		val expiresAt: Long = Date().time + expiresIn * 1000,
		@PrimaryKey
		val unique: String = "unique"
)
{
	@androidx.room.Dao
	interface Dao
	{
		@Insert(onConflict = OnConflictStrategy.REPLACE)
		fun insert(vararg oAuthTokens: OAuthToken)

		@Query("SELECT * FROM oAuthToken")
		fun load(): LiveData<OAuthToken>
	}
}