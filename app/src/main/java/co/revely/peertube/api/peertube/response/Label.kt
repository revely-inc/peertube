package co.revely.peertube.api.peertube.response

import androidx.annotation.Keep

@Keep
data class Label(
		val id: String?,
		val label: String?
	)