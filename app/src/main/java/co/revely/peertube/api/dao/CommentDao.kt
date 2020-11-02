package co.revely.peertube.api.dao

import androidx.annotation.Keep
import java.util.*

/**
 * Created at 30/10/2019
 *
 * @author rbenjami
 */
@Keep
data class CommentDao(
		val id: String? = null,
		val url: String? = null,
		val text: String? = null,
		val threadId: String? = null,
		val inReplyToCommentId: String? = null,
		val videoId: String? = null,
		val createdAt: Date? = null,
		val updatedAt: Date? = null,
		val deletedAt: Date? = null,
		val isDeleted: Boolean? = null,
		val totalRepliesFromVideoAuthor: Int? = null,
		val totalReplies: Int? = null,
		val account: AccountDao? = null
): Dao()
