#version 330

layout (location = 0) in vec2 inVertexPosition;
layout (location = 1) in vec2 inTextureCoord;

out vec2 outTextureCoord;

void main(){
    gl_Position = vec4(inVertexPosition, 0.0, 1.0);
    outTextureCoord = inTextureCoord;
}
