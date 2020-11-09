package co.revely.peertube.api.dao

import androidx.annotation.Keep

/**
 * Created at 03/11/2020
 *
 * @author rbenjami
 */
@Keep
data class DescriptionDao(
	val description: String? = null
): Dao()
