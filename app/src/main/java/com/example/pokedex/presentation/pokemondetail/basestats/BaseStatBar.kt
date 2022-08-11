package com.example.pokedex.presentation.pokemondetail.basestats

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

/**
 * TODO: document your custom view class.
 */
class BaseStatBar(
    context: Context,
    attrs: AttributeSet
) : View(context, attrs) {

    init {
        setWillNotDraw(false)
    }

    private var smallRectWidth by Delegates.notNull<Float>()
    private lateinit var valueAnimator: ValueAnimator

    private var startPosition: Float = 0F
    private var endPosition by Delegates.notNull<Float>()
    private var listIndex by Delegates.notNull<Int>()

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
        strokeWidth = 10F
        alpha = 100
    }
    private val statPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        strokeWidth = 10F
        alpha = 100
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            drawRoundRect(0F, 0F, width.toFloat(), height.toFloat(), 50F, 50F, backgroundPaint)
            drawRoundRect(0F, 0F, smallRectWidth, height.toFloat(), 50F, 50F, statPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightDp = BAR_HEIGHT_DP * resources.displayMetrics.density
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val widthToSet = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> widthSize
            else -> MAX_WIDTH_TO_SET
        }
        setMeasuredDimension(widthToSet, heightDp.toInt())

        // onMeasure called twice, want bottom part called with correct width
        if (widthToSet == MAX_WIDTH_TO_SET) {
            return
        }

        adjustPositions(widthToSet.toFloat())
        setupAnimator()
    }

    fun setIndexAndBarPositions(
        start: Float,
        end: Float,
        index: Int,
    ) {
        startPosition = start
        endPosition = end
        listIndex = index
    }

    private fun adjustPositions(widthToSet: Float) {
        var positionConstant = when (listIndex) {
            0 -> MAX_STAT_VALUE_1
            1 -> MAX_STAT_VALUE_2
            2 -> MAX_STAT_VALUE_3
            3 -> MAX_STAT_VALUE_4
            4 -> MAX_STAT_VALUE_5
            5 -> MAX_STAT_VALUE_6
            else -> MAX_STAT_VALUE_TOTAL
        }
        startPosition *= widthToSet / positionConstant
        endPosition *= widthToSet / positionConstant

        // sets smallRectWidth before onDraw, required for initial draw
        smallRectWidth = startPosition
    }

    private fun setupAnimator() {
        valueAnimator = ValueAnimator
            .ofFloat(startPosition, endPosition)
            .setDuration((100 * listIndex).toLong())
        valueAnimator.startDelay = 200
        valueAnimator
            .addUpdateListener {
                smallRectWidth = it.animatedValue as Float
                invalidate()
            }
        valueAnimator.start()
    }

    companion object {
        private const val BAR_HEIGHT_DP = 3F

        // need number greater than size of width
        private const val MAX_WIDTH_TO_SET = 1000

        // max values for each stat in api database
        private const val MAX_STAT_VALUE_1 = 255
        private const val MAX_STAT_VALUE_2 = 190
        private const val MAX_STAT_VALUE_3 = 250
        private const val MAX_STAT_VALUE_4 = 194
        private const val MAX_STAT_VALUE_5 = 250
        private const val MAX_STAT_VALUE_6 = 200
        private const val MAX_STAT_VALUE_TOTAL = 1339
    }
}