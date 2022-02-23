package com.huichongzi.vrardemo

import android.os.Bundle
import com.google.vr.sdk.base.*
import com.huichongzi.vrardemo.databinding.ActivityGvrDemoBinding
import javax.microedition.khronos.egl.EGLConfig

class GvrDemoActivity : GvrActivity(), GvrView.StereoRenderer {

    private var _binding : ActivityGvrDemoBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGvrDemoBinding.inflate(layoutInflater)
        setContentView(_binding?.root)
    }

    override fun onNewFrame(headTransform: HeadTransform?) {
    }

    override fun onDrawEye(eye: Eye?) {
    }

    override fun onFinishFrame(viewport: Viewport?) {
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
    }

    override fun onSurfaceCreated(config: EGLConfig?) {
    }

    override fun onRendererShutdown() {
    }

    override fun onBackPressed() {
        finish()
    }
}