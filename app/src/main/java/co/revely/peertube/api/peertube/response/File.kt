package co.revely.peertube.api.peertube.response

data class File(
		val resolution: Label?,
		val magnetUri: String?,
		val size: Int?,
		val fps: Int?,
		val torrentUrl: String?,
		val torrentDownloadUrl: String?,
		val fileUrl: String?,
		val fileDownloadUrl: String?
	)