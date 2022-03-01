package com.huichongzi.vrardemo

import android.content.Context
import android.opengl.GLES20
import java.io.BufferedReader
import java.io.InputStreamReader

object ShaderUtils {
    //编译顶点着色器
    fun compileVertexShader(resId : Int, context: Context) : Int{
        return compileShader(GLES20.GL_VERTEX_SHADER, readCode(resId, context))
    }

    //编译片元着色器
    fun compileFragmentShader(resId : Int, context: Context) : Int{
        return compileShader(GLES20.GL_FRAGMENT_SHADER, readCode(resId, context))
    }

    //编译着色器
    fun compileShader(type: Int, code : String) : Int{
        //创建一个着色器
        val id = GLES20.glCreateShader(type)
        if(id != 0){
            //载入着色器源码
            GLES20.glShaderSource(id, code)
            //编译着色器
            GLES20.glCompileShader(id)
            //检查着色器是否编译成功
            val status = IntArray(1)
            GLES20.glGetShaderiv(id, GLES20.GL_COMPILE_STATUS, status, 0)
            if(status[0] == 0){
                //创建失败
                GLES20.glDeleteShader(id)
                return 0
            }
            return id
        }
        else {
            return 0
        }
    }

    //链接程序
    fun linkProgram(vertexShaderId : Int, fragmentShaderId : Int) : Int{
        //创建一个GLES程序
        val id = GLES20.glCreateProgram()
        if(id != 0){
            //将顶点和片元着色器加入程序
            GLES20.glAttachShader(id, vertexShaderId)
            GLES20.glAttachShader(id, fragmentShaderId)

            //链接着色器程序
            GLES20.glLinkProgram(id)

            //检查是否链接成功
            val status = IntArray(1)
            GLES20.glGetProgramiv(id, GLES20.GL_LINK_STATUS, status, 0)
            if(status[0] == 0){
                GLES20.glDeleteProgram(id)
                return 0
            }
            return id
        }
        else{
            return 0
        }
    }

    fun readCode(resId : Int, context: Context) : String{
        val input = BufferedReader(InputStreamReader(context.resources.openRawResource(resId)))
        return input.readText()
    }
}