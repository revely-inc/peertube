package co.revely.peertube.ui.videos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import co.revely.peertube.R
import co.revely.peertube.api.dao.VideoDao
import co.revely.peertube.databinding.ItemVideoBinding
import co.revely.peertube.helper.PreferencesHelper
import co.revely.peertube.ui.common.DataBoundPagedListAdapter
import co.revely.peertube.utils.AppExecutors


/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
class VideosAdapter(
	appExecutors: AppExecutors,
	private val itemClickCallback: ((VideoDao) -> Unit)?
) : DataBoundPagedListAdapter<VideoDao, ItemVideoBinding>(
	appExecutors = appExecutors,
	diffCallback = object : DiffUtil.ItemCallback<VideoDao>() {
		override fun areItemsTheSame(oldItem: VideoDao, newItem: VideoDao) =
			oldItem.id == newItem.id

		override fun areContentsTheSame(oldItem: VideoDao, newItem: VideoDao) =
			oldItem.updatedAt == newItem.updatedAt
	}
) {
	override fun createBinding(parent: ViewGroup): ItemVideoBinding
	{
		val binding = DataBindingUtil.inflate<ItemVideoBinding>(
			LayoutInflater.from(parent.context),
			R.layout.item_video,
			parent,
			false
		)
		binding.root.setOnClickListener {
			binding.video?.let {
				itemClickCallback?.invoke(it)
			}
		}
		return binding
	}

	override fun bind(binding: ItemVideoBinding, item: VideoDao) {
		binding.host = PreferencesHelper.currentHost.get()
		binding.video = item
	}
}