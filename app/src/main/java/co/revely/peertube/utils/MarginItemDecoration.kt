package co.revely.peertube.utils

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.State

class MarginItemDecoration(private val divider: Drawable) : RecyclerView.ItemDecoration()
{
	override fun onDrawOver(c: Canvas, parent: RecyclerView, state: State)
	{
		val left = parent.paddingLeft
		val right = parent.width - parent.paddingRight
		val childCount = parent.childCount
		for (i in 0 until childCount)
		{
			val child = parent.getChildAt(i)
			val params = child.layoutParams as LayoutParams
			val top = child.bottom + params.bottomMargin
			val bottom: Int = top + divider.intrinsicHeight
			divider.setBounds(left, top, right, bottom)
			divider.draw(c)
		}
	}
}