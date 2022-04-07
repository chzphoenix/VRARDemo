package com.huichongzi.vrardemo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginStart
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
            if(e2.y - e1.y > 10){
                binding.videoView.seekTo(min(binding.videoView.duration, binding.videoView.currentPosition + ((e2.y - e1.y) * 100).toInt()))
                Log.e("vrvideo seek", binding.videoView.currentPosition.toString())
            }
            else if(e2.y - e1.y < -10){
                binding.videoView.seekTo(max(0, binding.videoView.currentPosition + ((e2.y - e1.y) * 100).toInt()))
                Log.e("vrvideo seek", binding.videoView.currentPosition.toString())
            }
            return false
        }

    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityZoomVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.minus.setOnClickListener {
            resizeVideo(min(binding.root.width / 4, binding.videoView.marginStart + 60))
        }
        binding.plus.setOnClickListener {
            resizeVideo(max(0, binding.videoView.marginStart - 60))
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

    private fun resizeVideo(margin : Int){
        val layoutParams = binding.videoView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.marginEnd = margin
        layoutParams.marginStart = margin
        binding.videoView.layoutParams = layoutParams
    }
}