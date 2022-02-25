package com.huichongzi.vrardemo

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.huichongzi.vrardemo.databinding.FragmentGlesDemoBinding
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLESDemoFragment : Fragment(){

    private var _binding : FragmentGlesDemoBinding? = null
    val triangleCoords = floatArrayOf(0.5f, 0.5f, 0.0f, // top
        -0.5f, -0.5f, 0.0f, // bottom left
        0.5f, -0.5f, 0.0f // bottom right
    )
    lateinit var vertexBuffer : FloatBuffer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGlesDemoBinding.inflate(inflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.glSurface?.apply {
            setEGLContextClientVersion(3)
            setRenderer(object : GLSurfaceView.Renderer{
                override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
                    //设置背景颜色
                    GLES30.glClearColor(0f,0f,1f,1f)

                    //初始化顶点坐标缓冲
                    vertexBuffer = ByteBuffer.allocateDirect(triangleCoords.size * 4).order(
                        ByteOrder.nativeOrder()).asFloatBuffer()
                    vertexBuffer.put(triangleCoords)
                    vertexBuffer.position(0)

                    activity?.apply {
                        //加载着色器
                        val vertexShaderId = ShaderUtils.compileVertexShader(R.raw.vertex_simple_shade, this)
                        val fragmentShaderId = ShaderUtils.compileFragmentShader(R.raw.fragment_simple_shade, this)
                        val program = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId)
                        GLES30.glUseProgram(program)
                    }
                }

                override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
                    //设置视图窗口
                    GLES30.glViewport(0,0,width,height)
                }

                override fun onDrawFrame(gl: GL10?) {
                    //绘制背景
                    GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

                    //准备坐标数据
                    GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer)
                    //启用顶点位置句柄，注意这里是属性0，对应着顶点着色器中的layout (location = 0)
                    GLES30.glEnableVertexAttribArray(0)
                    //绘制三角形
                    GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3)
                    //禁用顶点位置句柄
                    GLES30.glDisableVertexAttribArray(0)
                }

            })
        }
    }
}