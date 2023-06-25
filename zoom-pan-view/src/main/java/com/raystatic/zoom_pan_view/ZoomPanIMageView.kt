package com.raystatic.zoom_pan_view
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView

class ZoomPanIMageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr), View.OnTouchListener {

    private companion object {
        private const val NONE = 0
        private const val DRAG = 1
        private const val ZOOM = 2
    }

    private var mode = NONE
    private var oldDist = 1f
    private var d = 0f
    private var newRot = 0f
    private var scalediff = 0f

    private var parms: FrameLayout.LayoutParams? = null
    private var startwidth = 0
    private var startheight = 0
    private var dx = 0f
    private var dy = 0f
    private var x1 = 0f
    private var y1 = 0f
    private var angle = 0f

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun rotation(event: MotionEvent): Float {
        val delta_x = (event.getX(0) - event.getX(1))
        val delta_y = (event.getY(0) - event.getY(1))
        val radians = Math.atan2(delta_y.toDouble(), delta_x.toDouble())
        return Math.toDegrees(radians).toFloat()
    }

    init {
        setOnTouchListener(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val view = v as ImageView
        (view.drawable as BitmapDrawable).setAntiAlias(true)

        parms = view.layoutParams as FrameLayout.LayoutParams
        val x = event.rawX.toInt()
        val y = event.rawY.toInt()

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                startwidth = parms!!.width
                startheight = parms!!.height
                dx = x.toFloat() - parms!!.leftMargin
                dy = y.toFloat() - parms!!.topMargin
                mode = DRAG
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)
                if (oldDist > 10f) {
                    mode = ZOOM
                }
                d = rotation(event)
            }

            MotionEvent.ACTION_UP -> {
            }

            MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
            }

            MotionEvent.ACTION_MOVE -> {
                if (mode == DRAG) {
                    parms!!.leftMargin = (x - dx).toInt()
                    parms!!.topMargin = (y - dy).toInt()

                    parms!!.rightMargin = 0
                    parms!!.bottomMargin = 0
                    parms!!.rightMargin = parms!!.leftMargin + (5 * parms!!.width)
                    parms!!.bottomMargin = parms!!.topMargin + (10 * parms!!.height)

                    view.layoutParams = parms
                } else if (mode == ZOOM && event.pointerCount == 2) {
                    newRot = rotation(event)
                    val r = newRot - d
                    angle = r

                    x1 = event.rawX
                    y1 = event.rawY

                    val newDist = spacing(event)
                    if (newDist > 10f) {
                        val scale = newDist / oldDist * view.scaleX
                        if (scale > 0.6) {
                            scalediff = scale
                            view.scaleX = scale
                            view.scaleY = scale
                        }
                    }

                    view.animate().rotationBy(angle).setDuration(0)
                        .setInterpolator(LinearInterpolator()).start()

                    x1 = event.rawX
                    y1 = event.rawY

                    parms!!.leftMargin = ((x1 - dx) + scalediff).toInt()
                    parms!!.topMargin = ((y1 - dy) + scalediff).toInt()

                    parms!!.rightMargin = 0
                    parms!!.bottomMargin = 0
                    parms!!.rightMargin = parms!!.leftMargin + (5 * parms!!.width)
                    parms!!.bottomMargin = parms!!.topMargin + (10 * parms!!.height)

                    view.layoutParams = parms
                }
            }
        }
        return true
    }
}
