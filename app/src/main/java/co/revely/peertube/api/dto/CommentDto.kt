package co.revely.peertube.api.dto

import androidx.annotation.Keep

/**
 * Created at 30/10/2019
 *
 * @author rbenjami
 */
@Keep
data class CommentDto(
		//The video id or uuid
		var id: String,
		//Number of items
		//val count: Int? = null,
		//Sort videos by criteria ("-createdAt" "-updatedAt")
		var sort: String? = null
		//Offset
		//val start: Int? = null,
): Dto()