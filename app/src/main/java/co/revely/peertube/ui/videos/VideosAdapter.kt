package co.revely.peertube.ui.videos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import co.revely.peertube.R
import co.revely.peertube.api.peertube.response.Video
import co.revely.peertube.databinding.ItemVideoBinding
import co.revely.peertube.ui.common.DataBoundPagedListAdapter
import co.revely.peertube.utils.AppExecutors


/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
class VideosAdapter(
	val host: String,
	appExecutors: AppExecutors,
	private val itemClickCallback: ((Video) -> Unit)?
) : DataBoundPagedListAdapter<Video, ItemVideoBinding>(
	appExecutors = appExecutors,
	diffCallback = object : DiffUtil.ItemCallback<Video>() {
		override fun areItemsTheSame(oldItem: Video, newItem: Video) =
			oldItem.id == newItem.id

		override fun areContentsTheSame(oldItem: Video, newItem: Video) =
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

	override fun bind(binding: ItemVideoBinding, item: Video) {
		binding.host = host
		binding.video = item
	}
}