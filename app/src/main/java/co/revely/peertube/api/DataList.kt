package co.revely.peertube.api

import androidx.annotation.Keep
import co.revely.peertube.api.dao.Dao
import retrofit2.Call

/**
 * Created at 30/10/2019
 *
 * @author rbenjami
 */
@Keep
data class DataList<T>(
		val total: Int,
		val data: List<T>
): Dao()
