package co.revely.peertube.api.dao

import androidx.annotation.Keep

@Keep
data class FileDao(
		val resolution: LabelDao? = null,
		val magnetUri: String? = null,
		val size: Int? = null,
		val fps: Int? = null,
		val torrentUrl: String? = null,
		val torrentDownloadUrl: String? = null,
		val fileUrl: String? = null,
		val fileDownloadUrl: String? = null
): Dao()