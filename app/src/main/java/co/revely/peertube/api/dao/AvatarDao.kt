package co.revely.peertube.api.dao

import androidx.annotation.Keep
import java.util.*

@Keep
data class AvatarDao(
		val path: String? = null,
		val createdAt: Date? = null,
		val updatedAt: Date? = null
): Dao()