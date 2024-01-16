package com.adiandrodev.filmadda.presentation.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.adiandrodev.filmadda.R
import kotlin.math.min

class RatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {

    private var progress = 0
    private val maxProgress = 100

    private val progressPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 12f
        strokeCap = Paint.Cap.ROUND
    }

    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 12f
    }

    private val rectF = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (min(width, height) - progressPaint.strokeWidth) / 2f

        val startAngle = -90f
        val sweepAngle = (360 * progress) / maxProgress.toFloat()

        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        // Draw progress background
        backgroundPaint.color = getProgressBackgroundColor()
        canvas.drawArc(rectF, startAngle, 360f, false, backgroundPaint)

        // Draw progress
        progressPaint.color = getProgressColor()
        canvas.drawArc(rectF, startAngle, sweepAngle, false, progressPaint)
    }

    fun setProgress(value: Double?) {
        if (value != null) {
            progress = (value * 10).toInt()
            invalidate()
        }
    }

    private fun getProgressColor(): Int {
        return when((progress/10)) {
            0 -> ContextCompat.getColor(context, R.color.progress_1)
            1 -> ContextCompat.getColor(context, R.color.progress_2)
            2 -> ContextCompat.getColor(context, R.color.progress_3)
            3 -> ContextCompat.getColor(context, R.color.progress_4)
            4 -> ContextCompat.getColor(context, R.color.progress_5)
            5 -> ContextCompat.getColor(context, R.color.progress_6)
            6 -> ContextCompat.getColor(context, R.color.progress_7)
            7 -> ContextCompat.getColor(context, R.color.progress_8)
            8 -> ContextCompat.getColor(context, R.color.progress_9)
            9 -> ContextCompat.getColor(context, R.color.progress_10)
            else -> ContextCompat.getColor(context, R.color.progress_10)
        }
    }

    private fun getProgressBackgroundColor(): Int {
        return when((progress/10)) {
            0 -> ContextCompat.getColor(context, R.color.progress_bg_1)
            1 -> ContextCompat.getColor(context, R.color.progress_bg_2)
            2 -> ContextCompat.getColor(context, R.color.progress_bg_3)
            3 -> ContextCompat.getColor(context, R.color.progress_bg_4)
            4 -> ContextCompat.getColor(context, R.color.progress_bg_5)
            5 -> ContextCompat.getColor(context, R.color.progress_bg_6)
            6 -> ContextCompat.getColor(context, R.color.progress_bg_7)
            7 -> ContextCompat.getColor(context, R.color.progress_bg_8)
            8 -> ContextCompat.getColor(context, R.color.progress_bg_9)
            9 -> ContextCompat.getColor(context, R.color.progress_bg_10)
            else -> ContextCompat.getColor(context, R.color.progress_bg_10)
        }
    }
}