package co.revely.peertube.db.peertube

import androidx.room.TypeConverter
import co.revely.peertube.db.peertube.entity.Video
import com.google.gson.Gson
import java.util.*
import java.util.Collections.emptyList


class Converters
{
	var gson = Gson()

	@TypeConverter
	fun stringToStringList(value: String?) =
		value?.takeIf { it != "null" }?.let { gson.fromJson(it, Array<String>::class.java).toList() } ?: emptyList()

	@TypeConverter
	fun stringListToString(list: List<String>?): String? = gson.toJson(list)

	@TypeConverter
	fun stringToFileList(value: String?) =
		value?.takeIf { it != "null" }?.let { gson.fromJson(it, Array<Video.File>::class.java).toList() } ?: emptyList()

	@TypeConverter
	fun fileListToString(list: List<Video.File>?): String? = gson.toJson(list)

	@TypeConverter
	fun toDate(dateLong: Long?) = dateLong?.let { Date(it) }

	@TypeConverter
	fun fromDate(date: Date?) = date?.time
}