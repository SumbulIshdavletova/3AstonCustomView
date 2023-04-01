package ru.sumbul.a3astoncustomview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class ClockView(
    context: Context,
    attributeSet: AttributeSet,
) : View(context, attributeSet) {

    private var clockHeight = 0
    private var clockWidth = 0
    private var radius = 0
    private var timeAngle = 0.0
    private var centerX = 0
    private var centerY = 0
    private var padding = 0
    private var isInit = false
    private var paint: Paint? = null
    private var path: Path? = null
    private var time: IntArray = intArrayOf()
    private var mMinimum = 0
    private var mHour = 0f
    private var mMinute = 0f
    private var mSecond = 0f
    private var hourHandSize = 0
    private var minutesHandSize = 0
    private var secondsHandSize = 0
    private var timePoint = 0
    private var tailSize = 0
    private var secondsTailSize = 0
    private var rect = Rect()
    private var mDensity = 0f


    private fun init() {
        mDensity = resources.displayMetrics.density
        clockHeight = height
        clockWidth = width
        padding = 50
        centerX = clockWidth / 2
        centerY = clockHeight / 2
        mMinimum = Math.min(clockHeight, clockWidth)
        radius = mMinimum / 2 - padding
        timeAngle = (Math.PI / 30 - Math.PI / 2).toFloat().toDouble() //!!
        paint = Paint()
        path = Path() //??
        rect = Rect()
        hourHandSize = (radius - radius / 1.7).toInt()
        minutesHandSize = radius - radius / 2
        secondsHandSize = radius - radius / 4
        time = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        timePoint = (15 * mDensity).toInt()
        tailSize = (35 * mDensity).toInt()
        secondsTailSize = (45 * mDensity).toInt()
        isInit = true

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isInit) {
            init()
        }
        drawCircle(canvas)
        drawHands(canvas)
        drawNumerals(canvas)
        postInvalidateDelayed(500)
    }

    private fun setPaintAttributes(colour: Int, stroke: Paint.Style, strokeWidth: Int) {
        paint?.reset()
        paint?.color = colour
        paint?.style = stroke
        paint?.strokeWidth = strokeWidth * mDensity.toFloat()
        paint?.isAntiAlias = true
    }

    private fun drawCircle(canvas: Canvas) {
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 7)
        paint?.let {
            canvas.drawCircle(
                centerX.toFloat(), centerY.toFloat(), radius.toFloat(),
                it
            )
        }
    }

    private fun drawHands(canvas: Canvas) {
        val calendar: Calendar = Calendar.getInstance()
        mHour =
            calendar.get(Calendar.HOUR_OF_DAY)
                .toFloat() //convert to 12hour format from 24 hour format
        mHour = if (mHour > 12) mHour - 12 else mHour
        mMinute = calendar.get(Calendar.MINUTE).toFloat()
        mSecond = calendar.get(Calendar.SECOND).toFloat()
        drawHourHand(canvas, (mHour + mMinute / 60.0) * 5f)
        drawMinuteHand(canvas, mMinute)
        drawSecondsHand(canvas, mSecond)
    }

    private fun drawMinuteHand(canvas: Canvas, location: Float) {
        paint?.reset()
        setPaintAttributes(Color.RED, Paint.Style.STROKE, 5)
        timeAngle = Math.PI * location / 30 - Math.PI / 2
        val startX = centerX - cos(timeAngle) * (minutesHandSize - tailSize)
        val startY = centerX - sin(timeAngle) * (minutesHandSize - tailSize)
        var endX = centerX + cos(timeAngle) * minutesHandSize
        var endY = centerY + sin(timeAngle) * minutesHandSize
        paint?.let {
            canvas.drawLine(
                startX.toFloat(),
                startY.toFloat(),
                endX.toFloat(),
                endY.toFloat(),
                it
            )
        }
    }

    private fun drawHourHand(canvas: Canvas, location: Double) {
        paint?.reset()
        setPaintAttributes(Color.BLUE, Paint.Style.STROKE, 4)
        timeAngle = Math.PI * location / 30 - Math.PI / 2
        canvas.drawLine(
            (centerX - cos(timeAngle) * (hourHandSize - tailSize)).toFloat(),
            (centerX - sin(timeAngle) * (hourHandSize - tailSize)).toFloat(),
            (centerX + cos(timeAngle) * hourHandSize).toFloat(),
            (centerY + sin(timeAngle) * hourHandSize).toFloat(),
            paint!!
        )
    }

    private fun drawSecondsHand(canvas: Canvas, location: Float) {
        paint?.reset()
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 6)
        timeAngle = Math.PI * location / 30 - Math.PI / 2
        canvas.drawLine(
            (centerX - cos(timeAngle) * (secondsHandSize - secondsTailSize)).toFloat(),
            (centerX - sin(timeAngle) * (secondsHandSize - secondsTailSize)).toFloat(),
            (centerX + cos(timeAngle) * secondsHandSize).toFloat(),
            (centerY + sin(timeAngle) * secondsHandSize).toFloat(),
            paint!!
        )
    }


    private fun drawNumerals(canvas: Canvas) {
        paint?.reset()
        setPaintAttributes(Color.BLACK, Paint.Style.STROKE, 6)
        for (line in time) {
            val angle = Math.PI / 6 * (line - 3)
            val x = (centerX + cos(angle) * radius - rect.width() / 2)
            val y = (centerY + sin(angle) * radius + rect.height() / 2)
            val endX = (centerX + cos(angle) * (radius - timePoint) - rect.width() / 2)
            val endY = (centerY + sin(angle) * (radius - timePoint) + rect.height() / 2)
            canvas.drawLine(
                endX.toFloat(),
                endY.toFloat(),
                x.toFloat(), y.toFloat(),
                paint!!
            )
        }
    }


}
