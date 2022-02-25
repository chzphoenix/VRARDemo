//版本
#version 300 es
//输入一个4分量向量vPosition，layout (location = 0)表示这个变量的位置是顶点属性0
layout (location = 0) in vec4 vPosition;
void main() {
    //将输入属性拷贝给gl_Position的特殊输出变量
    gl_Position = vPosition;
    //设置顶点直径是10
    gl_PointSize = 10.0;
}
