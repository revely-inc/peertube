package co.revely.peertube.api.peertube.query

/**
 * Created at 30/10/2019
 *
 * @author rbenjami
 */
data class CommentQuery(
		//The video id or uuid
		val id: String,
		//Number of items
		//val count: Int? = null,
		//Sort videos by criteria ("-createdAt" "-updatedAt")
		val sort: String? = null
		//Offset
		//val start: Int? = null,
)