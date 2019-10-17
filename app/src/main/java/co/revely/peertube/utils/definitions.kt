package co.revely.peertube.utils

import androidx.annotation.StringDef

/**
 * Created at 2019-10-17
 *
 * @author rbenjami
 */
@StringDef(Rate.NONE, Rate.LIKE, Rate.DISLIKE)
@Retention(AnnotationRetention.SOURCE)
annotation class Rate {
	companion object {
		const val NONE = "none"
		const val LIKE = "like"
		const val DISLIKE = "dislike"
	}
}