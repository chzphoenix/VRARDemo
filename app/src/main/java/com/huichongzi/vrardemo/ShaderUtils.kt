package com.huichongzi.vrardemo

import android.content.Context
import android.opengl.GLES30
import java.io.BufferedReader
import java.io.InputStreamReader

object ShaderUtils {
    //编译顶点着色器
    fun compileVertexShader(resId : Int, context: Context) : Int{
        return compileShader(GLES30.GL_VERTEX_SHADER, readCode(resId, context))
    }

    //编译片元着色器
    fun compileFragmentShader(resId : Int, context: Context) : Int{
        return compileShader(GLES30.GL_FRAGMENT_SHADER, readCode(resId, context))
    }

    //编译着色器
    fun compileShader(type: Int, code : String) : Int{
        //创建一个着色器
        val id = GLES30.glCreateShader(type)
        if(id != 0){
            //载入着色器源码
            GLES30.glShaderSource(id, code)
            //编译着色器
            GLES30.glCompileShader(id)
            //检查着色器是否编译成功
            val status = IntArray(1)
            GLES30.glGetShaderiv(id, GLES30.GL_COMPILE_STATUS, status, 0)
            if(status[0] == 0){
                //创建失败
                GLES30.glDeleteShader(id)
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
        val id = GLES30.glCreateProgram()
        if(id != 0){
            //将顶点和片元着色器加入程序
            GLES30.glAttachShader(id, vertexShaderId)
            GLES30.glAttachShader(id, fragmentShaderId)

            //链接着色器程序
            GLES30.glLinkProgram(id)

            //检查是否链接成功
            val status = IntArray(1)
            GLES30.glGetProgramiv(id, GLES30.GL_LINK_STATUS, status, 0)
            if(status[0] == 0){
                GLES30.glDeleteProgram(id)
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