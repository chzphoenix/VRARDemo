package com.huichongzi.vrardemo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.huichongzi.vrardemo.databinding.ActivityZoomVideoBinding
import kotlin.math.max
import kotlin.math.min

class ZoomVideo: AppCompatActivity() {

    private lateinit var binding: ActivityZoomVideoBinding

    private val gestureDetector = GestureDetector(object : GestureDetector.OnGestureListener{
        override fun onDown(e: MotionEvent?): Boolean {
            return false
        }

        override fun onShowPress(e: MotionEvent?) {
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return false
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return false
        }

        override fun onLongPress(e: MotionEvent?) {
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if(e2.x - e1.x > 10){
                binding.videoView.seekTo(min(binding.videoView.duration, binding.videoView.currentPosition + ((e2.x - e1.x) * 100).toInt()))
                Log.e("vrvideo seek", binding.videoView.currentPosition.toString())
            }
            else if(e2.x - e1.x < -10){
                binding.videoView.seekTo(max(0, binding.videoView.currentPosition + ((e2.x - e1.x) * 100).toInt()))
                Log.e("vrvideo seek", binding.videoView.currentPosition.toString())
            }
            return false
        }

    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = ActivityZoomVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.minusWidth.setOnClickListener {
            resizeVideoWidth(min(binding.root.width / 4, binding.videoView.marginStart + 30))
        }
        binding.plusWidth.setOnClickListener {
            resizeVideoWidth(max(0, binding.videoView.marginStart - 30))
        }

        binding.minusHeight.setOnClickListener {
            resizeVideoHeight(min(binding.root.height / 4, binding.videoView.marginTop + 20))
        }
        binding.plusHeight.setOnClickListener {
            resizeVideoHeight(max(0, binding.videoView.marginTop - 20))
        }

        val uri = intent.getParcelableExtra<Uri>("uri")
        binding.videoView.setVideoURI(uri)
        binding.videoView.setOnPreparedListener {
            binding.videoView.start()
        }
        binding.videoView.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            return@setOnTouchListener true
        }
    }

    private fun resizeVideoWidth(margin : Int){
        val layoutParams = binding.videoView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.marginEnd = margin
        layoutParams.marginStart = margin
        binding.videoView.layoutParams = layoutParams
    }

    private fun resizeVideoHeight(margin : Int){
        val layoutParams = binding.videoView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.topMargin = margin
        layoutParams.bottomMargin = margin
        binding.videoView.layoutParams = layoutParams
    }
}