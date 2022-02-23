package com.huichongzi.vrardemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.vr.sdk.widgets.video.VrVideoEventListener
import com.google.vr.sdk.widgets.video.VrVideoView
import com.huichongzi.vrardemo.databinding.FragmentVrVideoBinding

class VrVideoFragment : Fragment() {
    private var _binding: FragmentVrVideoBinding? = null

    private val binding get() = _binding!!

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
        if(arguments?.getBoolean("isMono") == true){
            options.inputType = VrVideoView.Options.TYPE_MONO
            _binding?.vrVideo?.loadVideoFromAsset("pingpang.mp4", options)
        }
        else{
            options.inputType = VrVideoView.Options.TYPE_STEREO_OVER_UNDER
            _binding?.vrVideo?.loadVideoFromAsset("congo.mp4", options)
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
                _binding?.vrVideo?.seekTo(0)
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