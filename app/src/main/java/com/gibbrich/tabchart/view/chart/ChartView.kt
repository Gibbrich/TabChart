package com.gibbrich.tabchart.view.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import kotlin.math.abs
import kotlin.math.max

class ChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
    var data: List<Int> = emptyList()
        set(value) {
            updateChartBarViews(field, value)
            field = value
            requestLayout()
            invalidate()
        }

    init {
        setWillNotDraw(false)
    }

    private val paint = Paint().apply {
        color = Color.CYAN
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    // todo - implement later, for now use default implementation
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
//        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
//
//        val minWidth =
//            data.count() * (ChartBarView.MIN_WIDTH + ChartBarView.MIN_MARGIN) + paddingLeft + paddingRight
//        val w = resolveSizeAndState(minWidth, widthMeasureSpec, 0)
//
//        val maxBarHeight = data.max()
//
//        setMeasuredDimension()
//    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val barCount = data.size
        val barWidth = max(width.toFloat() / barCount, ChartBarView.MIN_WIDTH.toFloat()).toInt()
        val maxBarHeight = height - paddingBottom - paddingTop
        val maxValue = data.maxOrNull() ?: 0
        val pixelsToValueFactor = (maxBarHeight / maxValue.toFloat()).toInt()

        var childLeft = paddingLeft
        val childBottom = height - paddingBottom
        for (i in 0 until barCount) {
            val childRight = childLeft + barWidth
            val childTop = (maxValue - data[i]) * pixelsToValueFactor

            getChildAt(i).layout(childLeft, childTop, childRight, childBottom)

            childLeft = childRight + ChartBarView.MIN_MARGIN
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        }
    }

    /**
     * Simple algorithm to create/remove new views. No caching views for now
     */
    private fun updateChartBarViews(oldData: List<Int>, newData: List<Int>) {
        val oldDataCount = oldData.count()
        val newDataCount = newData.count()
        val delta = abs(newDataCount - oldDataCount)
        when {
            oldDataCount < newDataCount -> {
                for (i in 0 until delta) {
                    addView(ChartBarView(context))
                }
            }
            oldDataCount > newDataCount -> {
                removeViews(childCount - delta, delta)
            }
            else -> Unit
        }

        for (i in 0 until childCount) {
            (getChildAt(i) as ChartBarView).value = newData[i].toString()
        }
    }
}