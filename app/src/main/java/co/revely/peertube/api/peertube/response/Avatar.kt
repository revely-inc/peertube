package co.revely.peertube.api.peertube.response

import androidx.annotation.Keep
import java.util.*

@Keep
data class Avatar(
		val path: String?,
		val createdAt: Date?,
		val updatedAt: Date?
)