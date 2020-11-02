package co.revely.peertube.api.dao

import androidx.annotation.Keep

@Keep
data class LabelDao(
		val id: String? = null,
		val label: String? = null
): Dao()