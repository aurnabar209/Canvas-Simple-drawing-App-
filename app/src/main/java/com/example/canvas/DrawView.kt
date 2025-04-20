package com.example.canvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // Data class to store each stroke with its paint
    data class Stroke(val path: Path, val paint: Paint)

    private val strokes = mutableListOf<Stroke>()
    private var currentPath = Path()
    private var currentPaint = createPaint(Color.BLACK)

    // Helper to create a fresh Paint object
    private fun createPaint(color: Int): Paint {
        return Paint().apply {
            isAntiAlias = true
            this.color = color
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeWidth = 8f
        }
    }

    fun setColor(color: Int) {
        currentPaint = createPaint(color)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw all stored strokes
        for (stroke in strokes) {
            canvas.drawPath(stroke.path, stroke.paint)
        }
        // Draw current stroke in progress
        canvas.drawPath(currentPath, currentPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath = Path()
                currentPath.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                // Save the finished stroke
                strokes.add(Stroke(currentPath, currentPaint))
                currentPath = Path()
            }
        }

        invalidate()
        return true
    }

    fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

    fun clearCanvas() {
        strokes.clear()
        currentPath.reset()
        invalidate()
    }
}
