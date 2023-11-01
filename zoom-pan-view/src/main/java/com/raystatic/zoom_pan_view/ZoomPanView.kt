package com.raystatic.zoom_pan_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import android.widget.MediaController
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.palette.graphics.Palette
import com.raystatic.zoom_pan_view.databinding.LayoutZoomPanViewBinding
import kotlin.random.Random

class ZoomPanView: FrameLayout {

    private var binding: LayoutZoomPanViewBinding? = null
    private var showGradientBackground: Boolean? = false
    private var image: Drawable? = null
    private var videoUri: Uri? = null
    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

        binding = LayoutZoomPanViewBinding.bind(inflate(context, R.layout.layout_zoom_pan_view, this))

        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.ZoomPanView,
            defStyle,
            0
        )

        showGradientBackground = a.getBoolean(R.styleable.ZoomPanView_showGradientBackground, false)
        image = a.getDrawable(R.styleable.ZoomPanView_image)

        a.recycle()

        initViews()
    }

    private fun initViews() {
        binding?.image?.isVisible = image != null
        binding?.video?.isVisible = videoUri != null
        if (image != null) {
            binding?.image?.setImageDrawable(image)

            if (showGradientBackground == true) {
                val bitmap: Bitmap = when (val drawable = binding?.image?.drawable) {
                    is BitmapDrawable -> drawable.bitmap
                    else -> {
                        val width = drawable?.intrinsicWidth ?: 0
                        val height = drawable?.intrinsicHeight ?: 0
                        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                        val canvas = Canvas(bitmap)
                        drawable?.setBounds(0, 0, canvas.width, canvas.height)
                        drawable?.draw(canvas)
                        bitmap
                    }
                }

                setGradientBackground(bitmap)
            }

            binding?.image?.setOnTouchListener { v, event ->
                binding?.image?.onTouch(v, event) == true
            }
        } else if (videoUri != null && videoUri.toString().isNotEmpty()) {

            binding?.video?.setMediaController(MediaController(context))
            binding?.video?.setVideoURI(videoUri)
            binding?.video?.setOnPreparedListener {
                it?.start()
                it?.isLooping = true
            }
            binding?.video?.setOnErrorListener { mp, what, extra ->
                Log.d("ERROR PLABACK:", "$what | $extra")
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                false
            }

            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(context, videoUri)
            val time =
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

            val duration = time?.toLong() ?: 0

            val frameStart: Long = if (duration > 0 && duration > 3000) {
               3000L
            } else {
                0L
            }

            val frameBitmap = mediaMetadataRetriever.getFrameAtTime(frameStart)

            if (showGradientBackground == true) {
                frameBitmap?.let {
                    setGradientBackground(it)
                }
            }

            binding?.video?.setOnTouchListener { v, event ->
                binding?.video?.setOnTouchListener(v, event, frameBitmap)
                true
            }
        }

    }

    private fun setGradientBackground(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val colors = (palette?.swatches?.map { swatch -> swatch.rgb } ?: emptyList()).asReversed()
            val gradientColors = if (colors.size > 3) {
                colors.subList(0,3).toIntArray()
            } else {
                colors.toIntArray()
            }

            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                gradientColors
            )

            binding?.root?.background = gradientDrawable
        }
    }

    fun setImage(image: Drawable?, shouldShowGradientBackground: Boolean? = false) {
        this.image = image
        this.showGradientBackground = shouldShowGradientBackground
        initViews()
    }

    fun setVideo(videoPath: Uri, shouldShowGradientBackground: Boolean? = false) {
        this.videoUri = videoPath
        this.showGradientBackground = shouldShowGradientBackground
        initViews()
    }

}