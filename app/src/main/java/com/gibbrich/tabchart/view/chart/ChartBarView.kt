package com.gibbrich.tabchart.view.chart

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.animation.BounceInterpolator
import java.lang.Math.random

class ChartBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    companion object {
        const val MIN_WIDTH = 30
        const val MIN_MARGIN = 5

        fun getRandomColor(): Int =
            Color.rgb((random() * 255).toInt(), (random() * 255).toInt(), (random() * 255).toInt())
    }

    var value: String? = null
    val animator: ValueAnimator

    private var factor = 0f
    private var shouldShowText = false;

    init {
        setOnClickListener {
            shouldShowText = !shouldShowText
            invalidate()
        }

        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            interpolator = BounceInterpolator(context, attrs)
            addUpdateListener {
                factor = animatedValue as Float
                invalidate()
            }
        }
    }

    private val paint = Paint().apply {
        color = getRandomColor()
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        typeface = Typeface.DEFAULT
        textSize = 18f * resources.displayMetrics.density
        textAlign = Paint.Align.CENTER
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.start()
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putInt("color", paint.color)
        bundle.putParcelable("superState", super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var viewState = state
        if (viewState is Bundle) {
            paint.color = viewState.getInt("color", getRandomColor())
            viewState = viewState.getParcelable("superState")
        }
        super.onRestoreInstanceState(viewState)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            drawRect(0f, height * (1 - factor), width.toFloat(), height.toFloat(), paint)

            value?.let {
                if (shouldShowText) {
                    val xPos = width / 2f
                    val yPos = height / 2 - (textPaint.descent() + textPaint.ascent()) / 2
                    drawText(it, xPos, yPos, textPaint)
                }
            }
        }
    }
}