package co.revely.peertube.api.peertube.response

import com.google.gson.annotations.SerializedName

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
data class TokenResponse(
	@SerializedName("access_token")
	val accessToken: String,
	@SerializedName("token_type")
	val tokenType: String,
	@SerializedName("expires_in")
	val expiresIn: Long,
	@SerializedName("refresh_token")
	val refreshToken: String
)