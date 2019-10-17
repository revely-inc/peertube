package co.revely.peertube.api.peertube.response

import com.google.gson.annotations.SerializedName

/**
 * Created at 16/04/2019
 *
 * @author rbenjami
 */
data class ClientResponse(
	@SerializedName("client_id")
	val id: String,
	@SerializedName("client_secret")
	val secret: String
)
