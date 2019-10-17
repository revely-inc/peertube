package co.revely.peertube.ui.videos

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import co.revely.peertube.R
import co.revely.peertube.databinding.ItemVideoBinding
import co.revely.peertube.db.peertube.entity.Video
import co.revely.peertube.ui.common.DataBoundPagedListAdapter
import co.revely.peertube.utils.AppExecutors
import co.revely.peertube.utils.humanReadableBigNumber
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


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
		binding.video = item

		var info = "${item.account?.name}"
		item.views?.also {
			info += " • ${it.humanReadableBigNumber()} ${binding.root.context.getString(R.string.views)}"
		}
		item.publishedAt?.time?.also {
			info += " • ${DateUtils.getRelativeTimeSpanString(it, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)}"
		}
		binding.info.text = info

		item.previewPath?.also {
			Glide.with(binding.root.context).load("https://$host$it").into(binding.thumbnails)
		}

		Glide.with(binding.root.context)
			.load(item.account?.avatar?.path?.let { "https://$host$it" })
			.thumbnail(Glide.with(binding.root.context)
				.load("https://$host/client/assets/images/default-avatar.png")
				.apply(RequestOptions.circleCropTransform())
			)
			.apply(RequestOptions.circleCropTransform())
			.into(binding.accountAvatar)
	}
}