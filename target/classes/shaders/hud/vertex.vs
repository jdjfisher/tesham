#version 330

layout (location = 0) in vec2 inVertexPosition;
layout (location = 1) in vec2 inTextureCoord;

out vec2 outTextureCoord;

uniform mat4 globalMatrix;
uniform mat4 projectionMatrix;

void main(){
    gl_Position = projectionMatrix * globalMatrix * vec4(inVertexPosition, 0.0, 1.0);
    outTextureCoord = inTextureCoord;
} 