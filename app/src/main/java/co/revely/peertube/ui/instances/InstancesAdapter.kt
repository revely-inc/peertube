package co.revely.peertube.ui.instances

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import co.revely.peertube.R
import co.revely.peertube.databinding.ItemInstanceBinding
import co.revely.peertube.db.instances.entity.Instance
import co.revely.peertube.ui.common.DataBoundListAdapter
import co.revely.peertube.utils.AppExecutors
import com.google.android.material.animation.ArgbEvaluatorCompat
import timber.log.Timber

/**
 * Created at 17/04/2019
 *
 * @author rbenjami
 */
class InstancesAdapter(
	appExecutors: AppExecutors,
	private val itemClickCallback: ((Instance) -> Unit)?
) : DataBoundListAdapter<Instance, ItemInstanceBinding>(
	appExecutors = appExecutors,
	diffCallback = object : DiffUtil.ItemCallback<Instance>() {
		override fun areItemsTheSame(oldItem: Instance, newItem: Instance)= oldItem.id == newItem.id

		override fun areContentsTheSame(oldItem: Instance, newItem: Instance) = oldItem.name == newItem.name && oldItem.shortDescription == newItem.shortDescription
	}
) {

	override fun createBinding(parent: ViewGroup): ItemInstanceBinding
	{
		val binding = DataBindingUtil.inflate<ItemInstanceBinding>(
			LayoutInflater.from(parent.context),
			R.layout.item_instance,
			parent,
			false
		)
		binding.root.setOnClickListener {
			binding.instance?.let {
				itemClickCallback?.invoke(it)
			}
		}
		return binding
	}

	override fun bind(binding: ItemInstanceBinding, item: Instance) {
		binding.instance = item
		val startColor = ContextCompat.getColor(binding.root.context, R.color.colorRed)
		val endColor = ContextCompat.getColor(binding.root.context, R.color.colorGreen)
		val tint = ArgbEvaluatorCompat.getInstance().evaluate((binding.instance?.health ?: 0) / 100f, startColor, endColor)
		binding.health.background.setTint(tint)
	}
}