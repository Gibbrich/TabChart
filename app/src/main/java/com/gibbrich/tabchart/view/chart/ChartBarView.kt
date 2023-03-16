package com.gibbrich.tabchart.view.chart

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.shapes.RoundRectShape
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
        color =
            Color.rgb((random() * 255).toInt(), (random() * 255).toInt(), (random() * 255).toInt())
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