package co.revely.peertube.composable

import android.annotation.SuppressLint
import co.revely.peertube.api.dao.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created at 02/11/2020
 *
 * @author rbenjami
 */
@SuppressLint("SimpleDateFormat")
private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

val avatarPreview = AvatarDao(
	path = "/lazy-static/avatars/4f01c4fd-44b7-4766-852b-5b878447cabe.jpg",
	createdAt = sdf.parse("2020-06-29T09:51:00.861Z"),
	updatedAt = sdf.parse("2020-07-01T22:17:30.949Z")
)

val accountPreview = AccountDao(
	url = "https://peertube.parleur.net/accounts/parleur",
	name = "parleur",
	host = "peertube.parleur.net",
	avatar = avatarPreview,
	id = "2343",
	followingCount = 2,
	followersCount = 133,
	createdAt = sdf.parse("2018-07-26T17:00:47.751Z"),
	updatedAt = sdf.parse("2019-05-29T15:12:41.358Z"),
	displayName = "Parleur",
	description = "Parce-que le disque dur de mon portable n'a pas assez de place pour toutes ces vidéos.\n--\nUne mise en ligne sur cette instance ne signifie pas forcément que je suis en accord avec son contenu, mais que je souhaite pouvoir retrouver ce contenu aisément plus tard."
)

val channelPreview = ChannelDao(
	url = "https://peertube.parleur.net/video-channels/films",
	name = "films",
	host = "peertube.parleur.net",
	avatar = avatarPreview,
	id = "2277",
	followingCount = 0,
	followersCount = 28,
	createdAt = sdf.parse("2018-09-12T11:46:47.311Z"),
	updatedAt = sdf.parse("2018-09-12T11:46:47.311Z"),
	displayName = "Films",
	description = null,
	support = null,
	isLocal = false,
	ownerAccount = null //TODO userPreview
)

val videoPreview = VideoDao(
	id = "41085",
	uuid = "76011c74-2a6c-4da0-b277-3568c4d0f2ad",
	name = "La belle verte (1996)",
	category = LabelDao("2", "Films"),
	licence = LabelDao(null, "Unknown"),
	language = LabelDao("fr", "French"),
	privacy = LabelDao("1", "Public"),
	nsfw = false,
	description = null,
	isLocal = false,
	duration = 5396,
	views = 62728,
	likes = 16234,
	dislikes = 1214,
	thumbnailPath = "/static/thumbnails/76011c74-2a6c-4da0-b277-3568c4d0f2ad.jpg",
	previewPath = "/lazy-static/previews/76011c74-2a6c-4da0-b277-3568c4d0f2ad.jpg",
	embedPath = "/videos/embed/76011c74-2a6c-4da0-b277-3568c4d0f2ad",
	createdAt = sdf.parse("2019-06-01T11:14:19.522Z"),
	updatedAt = sdf.parse("2020-11-02T16:01:10.692Z"),
	publishedAt = sdf.parse("2019-06-01T11:14:19.522Z"),
	originallyPublishedAt = null,
	account = accountPreview,
	channel = channelPreview,
	blacklisted = false,
	blacklistedReason = null,
	support = null,
	descriptionPath = "/api/v1/videos/76011c74-2a6c-4da0-b277-3568c4d0f2ad/description",
	tags = listOf(
		"Coline Serreau",
		"La belle verte",
		"Vincent Lindon"
	),
	commentsEnabled = true,
	downloadEnabled = true,
	waitTranscoding = true,
	state = LabelDao("1", "Published"),
	trackerUrls = null,
	files = null,
	streamingPlaylists = null
)

val commentPreview = CommentDao(
	id = "214",
	url = "https://video.obermui.de/videos/watch/f8a4b32d-0bf1-48ed-af9f-d8c5d87efedb/comments/2306",
	text = "doesnt work with   LibreELEC 9.0.1 (Leia) = Kodi v18.1  at the moment!!",
	threadId = "214",
	inReplyToCommentId = null,
	videoId = "45",
	createdAt = Date(),
	updatedAt = Date(),
	deletedAt = null,
	isDeleted = false,
	totalRepliesFromVideoAuthor = 0,
	totalReplies = 0,
	account = accountPreview
)
