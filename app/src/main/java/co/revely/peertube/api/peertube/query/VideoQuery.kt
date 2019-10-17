package co.revely.peertube.api.peertube.query

/**
 * Created at 2019-06-20
 *
 * @author rbenjami
 */
data class VideoQuery(
	//category id of the video
	val categoryOneOf: List<Int>? = null,
	//Number of items
	val count: Int? = null,
	//Special filters (local for instance) which might require special rights:
	val filter: String? = null,
	//language id of the video
	val languageOneOf: List<String>? = null,
	//licence id of the video
	val licenceOneOf: List<String>? = null,
	//whether to include nsfw videos, if any
	val nsfw: Boolean? = null,
	//Sort videos by criteria ("-name" "-duration" "-createdAt" "-publishedAt" "-views" "-likes" "-trending")
	val sort: String? = null,
	//Offset
	val start: Int? = null,
	//tag(s) of the video, where all should be present in the video
	val tagsAllOf: List<String>? = null,
	//tag(s) of the video
	val tagsOneOf: List<String>? = null
)
