package com.huichongzi.vrardemo

import android.os.Bundle
import com.google.vr.sdk.base.*
import com.huichongzi.vrardemo.databinding.ActivityGvrDemoBinding
import javax.microedition.khronos.egl.EGLConfig
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*


class GvrDemoActivity : GvrActivity(), GvrView.StereoRenderer {

    companion object{
        const val Z_NEAR = 0.01f
        const val Z_FAR = 10.0f
    }

    private var _binding : ActivityGvrDemoBinding? = null

    val camera = kotlin.FloatArray(16)
    val view = kotlin.FloatArray(16)

    val modelViewProjection = kotlin.FloatArray(16)
    val modelView = kotlin.FloatArray(16)
    val modelTarget = kotlin.FloatArray(16)

    val triangleCoords = floatArrayOf(0.3f, 0.3f, 0.0f, // top
        -0.3f, -0.3f, 0.0f, // bottom left
        0.3f, -0.3f, 0.0f // bottom right
    )
    lateinit var vertexBuffer : FloatBuffer

    var objProgram = 0
    var objectPositionParam = 0
    var objectModelViewProjectionParam = 0;

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

        if (gvrView.setAsyncReprojectionEnabled(true)) {
            AndroidCompat.setSustainedPerformanceMode(this, true)
        }
    }

    override fun onNewFrame(headTransform: HeadTransform?) {
        //设置相机位置
        Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f)
    }

    override fun onDrawEye(eye: Eye) {

        GLES20.glEnable(GLES20.GL_DEPTH_TEST) //启用深度测试，自动隐藏被遮住的材料
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        //将眼睛的位置变化应用到相机，获取世界坐标系到眼坐标系的转换矩阵
        Matrix.multiplyMM(view, 0, eye.eyeView, 0, camera, 0)

        //将物体的世界坐标转换成眼坐标（视图坐标系）
        Matrix.multiplyMM(modelView, 0, view, 0, modelTarget, 0)

        //将物体的眼坐标转换成剪裁坐标（投影坐标系）
        //以上都是针对三维的顶点坐标进行转换，但是最终呈现在屏幕上还是一个二维坐标，所以需要一个投影过程
        //这里的计算将物体在视图坐标系中的三维坐标映射到二维坐标中，这样才能正常绘制出来
        val perspective = eye.getPerspective(Z_NEAR, Z_FAR)
        Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0)

        //将modelViewProjection输入顶点着色器（u_MVP）
        //这样通过着色器处理，物体的顶点通过以上4个坐标系（局部坐标系、世界坐标系、视图坐标系和投影坐标系）和
        //三个变换矩阵（modelTarget、view、perspective），再通过着色器处理得到了在屏幕上的坐标（屏幕坐标系）
        //最终物体的顶点经过一系列变换最终被映射到屏幕上进行绘制
        GLES20.glUseProgram(objProgram)
        GLES20.glUniformMatrix4fv(objectModelViewProjectionParam, 1, false, modelViewProjection, 0)

        //启用顶点位置句柄
        GLES20.glEnableVertexAttribArray(objectPositionParam)
        //准备坐标数据
        GLES20.glVertexAttribPointer(objectPositionParam, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        //绘制物体
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3)

        //禁用顶点位置句柄
        GLES20.glDisableVertexAttribArray(objectPositionParam)
    }

    override fun onFinishFrame(viewport: Viewport?) {
    }

    override fun onSurfaceChanged(width: Int, height: Int) {

    }

    override fun onSurfaceCreated(config: EGLConfig?) {
        GLES20.glClearColor(0f,0f,1f,1f)

        //初始化顶点坐标缓冲
        vertexBuffer = ByteBuffer.allocateDirect(triangleCoords.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
        vertexBuffer.put(triangleCoords)
        vertexBuffer.position(0)

        //加载着色器
        val vertexShaderId = ShaderUtils.compileVertexShader(R.raw.gvr_vertex_shade, this)
        val fragmentShaderId = ShaderUtils.compileFragmentShader(R.raw.fragment_simple_shade, this)
        objProgram = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId)

        //获取u_MVP这个输入量
        objectModelViewProjectionParam = GLES20.glGetUniformLocation(objProgram, "u_MVP")
        objectPositionParam = GLES20.glGetAttribLocation(objProgram, "vPosition")

        //初始化modelTarget
        //物体的世界坐标转换矩阵，将物体的局部坐标转成世界坐标
        //物体的顶点与它相乘就会得到世界坐标系中的位置，这样就确定了每个物体在三维空间的位置
        Matrix.setIdentityM(modelTarget, 0)
        Matrix.translateM(modelTarget, 0,0.0f, 0.0f, -3.0f)
    }

    override fun onRendererShutdown() {
    }

    override fun onBackPressed() {
        finish()
    }
}