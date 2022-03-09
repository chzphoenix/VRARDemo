uniform mat4 u_MVP; //外部输入，4x4矩阵
attribute vec4 vPosition;
void main() {
    gl_Position = u_MVP * vPosition;
}
