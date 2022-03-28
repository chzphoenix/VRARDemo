package com.huichongzi.vrardemo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.google.vr.sdk.widgets.video.VrVideoEventListener
import com.google.vr.sdk.widgets.video.VrVideoView
import com.huichongzi.vrardemo.databinding.FragmentVrVideoBinding

class VrVideoFragment : Fragment() {
    private var _binding: FragmentVrVideoBinding? = null

    private val binding get() = _binding!!

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
            _binding?.vrVideo?.apply {
                if(e2.y - e1.y > 10){
                    seekTo(Math.min(duration, currentPosition + ((e2.y - e1.y) * 100).toLong()))
                    Log.e("vrvideo seek", currentPosition.toString())
                }
                else if(e2.y - e1.y < -10){
                    seekTo(Math.max(0, currentPosition + ((e2.y - e1.y) * 100).toLong()))
                    Log.e("vrvideo seek", currentPosition.toString())
                }
            }
            return false
        }

    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentVrVideoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var options = VrVideoView.Options()
        options.inputFormat = VrVideoView.Options.FORMAT_DEFAULT

        val uri = arguments?.getParcelable<Uri>("uri")
        if(uri != null){
            options.inputType = arguments?.getInt("type") ?: VrVideoView.Options.TYPE_MONO
            _binding?.vrVideo?.loadVideo(uri, options)
        }
        else if(arguments?.getBoolean("isMono") == true){
            options.inputType = VrVideoView.Options.TYPE_MONO
            _binding?.vrVideo?.loadVideoFromAsset("pingpang.mp4", options)
        }
        else{
            options.inputType = VrVideoView.Options.TYPE_STEREO_OVER_UNDER
            _binding?.vrVideo?.loadVideoFromAsset("congo.mp4", options)
        }

        _binding?.vrVideo?.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            return@setOnTouchListener true
        }
        _binding?.vrVideo?.setEventListener(object : VrVideoEventListener() {
            override fun onLoadSuccess() {
                super.onLoadSuccess()
                _binding?.vrVideo?.playVideo()
            }

            override fun onLoadError(errorMessage: String?) {
                super.onLoadError(errorMessage)
            }

            override fun onClick() {
                super.onClick()
            }

            override fun onDisplayModeChanged(newDisplayMode: Int) {
                super.onDisplayModeChanged(newDisplayMode)
            }

            override fun onCompletion() {
                super.onCompletion()
            }

            override fun onNewFrame() {
                super.onNewFrame()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        _binding?.vrVideo?.resumeRendering()
    }

    override fun onPause() {
        super.onPause()
        _binding?.vrVideo?.pauseRendering()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _binding?.vrVideo?.shutdown()
    }
}