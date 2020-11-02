package co.revely.peertube.api.dao

import androidx.annotation.Keep
import java.util.*

/**
 * Created at 30/10/2020
 *
 * @author rbenjami
 */
@Keep
data class ScheduledUpdateDao(
    val privacy: Int? = null,
    val updateAt: Date? = null
): Dao()
