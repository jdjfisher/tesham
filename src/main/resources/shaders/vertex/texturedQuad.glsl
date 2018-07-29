#version 450 core
layout (location = 0) in vec2 inScreenSpacePosition;
layout (location = 1) in vec2 inTextureCoord;

out vec2 outTextureCoord;

void main(){
    outTextureCoord = inTextureCoord;
    gl_Position = vec4(inScreenSpacePosition, 0.0, 1.0);
}