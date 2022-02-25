package com.huichongzi.vrardemo

import android.os.Bundle
import com.google.vr.sdk.base.*
import com.huichongzi.vrardemo.databinding.ActivityGvrDemoBinding
import javax.microedition.khronos.egl.EGLConfig
import android.opengl.GLES30
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class GvrDemoActivity : GvrActivity(), GvrView.StereoRenderer {

    private var _binding : ActivityGvrDemoBinding? = null

    val camera = kotlin.FloatArray(16)
    val view = kotlin.FloatArray(16)
    val headView = kotlin.FloatArray(16)
    val headRotation = kotlin.FloatArray(4)

    val triangleCoords = floatArrayOf(0.5f, 0.5f, 0.0f, // top
        -0.5f, -0.5f, 0.0f, // bottom left
        0.5f, -0.5f, 0.0f // bottom right
    )
    lateinit var vertexBuffer : FloatBuffer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGvrDemoBinding.inflate(layoutInflater)
        setContentView(_binding?.root)

        //初始化
        gvrView = _binding?.gvrView
        gvrView.setEGLConfigChooser(8,8,8,8,16,8)
        gvrView.setRenderer(this)
        gvrView.setTransitionViewEnabled(true)
        gvrView.enableCardboardTriggerEmulation()
    }

    override fun onNewFrame(headTransform: HeadTransform?) {
//        // Build the camera matrix and apply it to the ModelView.
//        Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f)
//
//        headTransform!!.getHeadView(headView, 0)
//
//        // Update the 3d audio engine with the most recent head rotation.
//        headTransform.getQuaternion(headRotation, 0)
    }

    override fun onDrawEye(eye: Eye?) {

        GLES30.glEnable(GLES30.GL_DEPTH_TEST) //启用深度测试，自动隐藏被遮住的材料
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        //准备坐标数据
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer)
        //启用顶点位置句柄，注意这里是属性0，对应着顶点着色器中的layout (location = 0)
        GLES30.glEnableVertexAttribArray(0)
        //绘制三角形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3)
        //禁用顶点位置句柄
        GLES30.glDisableVertexAttribArray(0)

        //Matrix.multiplyMM(view, 0, eye!!.eyeView, 0, camera, 0)


    }

    override fun onFinishFrame(viewport: Viewport?) {
    }

    override fun onSurfaceChanged(width: Int, height: Int) {

    }

    override fun onSurfaceCreated(config: EGLConfig?) {
        GLES30.glClearColor(0f,0f,1f,1f)

        //初始化顶点坐标缓冲
        vertexBuffer = ByteBuffer.allocateDirect(triangleCoords.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
        vertexBuffer.put(triangleCoords)
        vertexBuffer.position(0)

        //加载着色器
        val vertexShaderId = ShaderUtils.compileVertexShader(R.raw.vertex_simple_shade, this)
        val fragmentShaderId = ShaderUtils.compileFragmentShader(R.raw.fragment_simple_shade, this)
        val program = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId)
        GLES30.glUseProgram(program)
    }

    override fun onRendererShutdown() {
    }

    override fun onBackPressed() {
        finish()
    }
}