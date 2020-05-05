package co.revely.peertube.api.peertube.response

import androidx.annotation.Keep
import java.util.*

/**
 * Created at 30/10/2019
 *
 * @author rbenjami
 */
@Keep
data class Comment(
		val id: String?,
		val url: String?,
		val text: String?,
		val threadId: String?,
		val inReplyToCommentId: String?,
		val videoId: String?,
		val createdAt: Date?,
		val updatedAt: Date?,
		val totalReplies: Int?,
		val account: Account?
)
