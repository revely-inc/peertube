package co.revely.peertube.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import co.revely.peertube.R

class SingleViewTouchableMotionLayout(context: Context, attributeSet: AttributeSet? = null)
	: MotionLayout(context, attributeSet)
{
	private var ids: List<Int> = emptyList()
	private val viewsToDetectTouch: List<View> by lazy {
		ids.map { findViewById<View>(it) }
	}
	private val viewRect = Rect()
	private var touchStarted = false

	init
	{
		val a: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SingleViewTouchableMotionLayout)
		ids = a.getString(R.styleable.SingleViewTouchableMotionLayout_touchable_views_ids)?.split(",")?.map {
			resources.getIdentifier(it, "id", context.packageName)
		} ?: emptyList()
		a.recycle()
		setTransitionListener(object : TransitionAdapter()
		{
			override fun onTransitionCompleted(p0: MotionLayout, p1: Int)
			{
				touchStarted = false
			}
		})
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun onTouchEvent(event: MotionEvent): Boolean
	{
		when (event.actionMasked)
		{
			MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
			{
				touchStarted = false
				return super.onTouchEvent(event)
			}
		}
		if (!touchStarted)
		{
			for (viewToDetectTouch in viewsToDetectTouch)
			{
				viewToDetectTouch.getHitRect(viewRect)
				touchStarted = viewRect.contains(event.x.toInt(), event.y.toInt())
				if (touchStarted)
					break
			}
		}
		return touchStarted && super.onTouchEvent(event)
	}
}