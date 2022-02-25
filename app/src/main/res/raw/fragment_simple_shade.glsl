//版本
#version 300 es
//声明着色器中浮点变量的默认精度
precision mediump float;
//输出，vec4表示四维浮点数向量
out vec4 fragColor;
void main() {
    //将颜色值(1.0,1.0,1.0,1.0)，即白色输出到颜色缓冲区。
    fragColor = vec4(1.0,1.0,1.0,1.0);
}
