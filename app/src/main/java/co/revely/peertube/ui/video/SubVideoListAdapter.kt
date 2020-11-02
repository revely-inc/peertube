package co.revely.peertube.ui.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import co.revely.peertube.R
import co.revely.peertube.api.dao.CommentDao
import co.revely.peertube.api.dao.VideoDao
import co.revely.peertube.databinding.ItemCommentBinding
import co.revely.peertube.databinding.ItemVideoHeaderBinding
import co.revely.peertube.ui.common.DataBoundViewHolder
import co.revely.peertube.utils.AppExecutors
import java.security.InvalidParameterException

/**
 * Created at 30/10/2019
 *
 * @author rbenjami
 */
class SubVideoListAdapter(
		val videoViewModel: VideoViewModel,
		appExecutors: AppExecutors,
		private val itemClickCallback: ((CommentDao) -> Unit)?
) : PagedListAdapter<CommentDao, DataBoundViewHolder<*>>(
		AsyncDifferConfig.Builder(object : DiffUtil.ItemCallback<CommentDao>() {
			override fun areItemsTheSame(oldItem: CommentDao, newItem: CommentDao) =
					oldItem.id == newItem.id

			override fun areContentsTheSame(oldItem: CommentDao, newItem: CommentDao) =
					oldItem.updatedAt == newItem.updatedAt
		}).setBackgroundThreadExecutor(appExecutors.diskIO()).build()
) {
	companion object
	{
		const val HEADER_TYPE = 1
		const val COMMENT_TYPE = 2

		const val OTHER_ITEM = 1
	}

	private var video: VideoDao? = null

	fun setVideo(video: VideoDao)
	{
		this.video = video
		notifyItemChanged(0)
	}

	override fun getItemViewType(position: Int) = if (position == 0) HEADER_TYPE else COMMENT_TYPE

	override fun getItemCount() = super.getItemCount() + OTHER_ITEM

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
		HEADER_TYPE -> createItemBinding<ItemVideoHeaderBinding>(parent, R.layout.item_video_header)
		COMMENT_TYPE -> createItemBinding<ItemCommentBinding>(parent, R.layout.item_comment)
		else -> throw InvalidParameterException("Invalid view type: $viewType")
	}

	override fun onBindViewHolder(holder: DataBoundViewHolder<*>, position: Int)
	{
		when (val binding = holder.binding) {
			is ItemVideoHeaderBinding -> {
				binding.video = video
				binding.viewModel = videoViewModel
			}
			is ItemCommentBinding -> {
				binding.comment = getItem(position - OTHER_ITEM)
				binding.root.setOnClickListener {
					binding.comment?.let {
						itemClickCallback?.invoke(it)
					}
				}
			}
		}
		holder.binding.executePendingBindings()
	}

	private fun <T: ViewDataBinding>createItemBinding(parent: ViewGroup, @LayoutRes layoutId: Int): DataBoundViewHolder<T>
	{
		val binding = DataBindingUtil.inflate<T>(
				LayoutInflater.from(parent.context),
				layoutId,
				parent,
				false
		)
		return DataBoundViewHolder(binding)
	}
}