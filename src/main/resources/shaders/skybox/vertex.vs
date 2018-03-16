#version 330

layout (location = 0) in vec3 inVertexPosition;
layout (location = 2) in vec2 inTextureCoord;

out vec2 outTextureCoord;

uniform mat4 globalMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(){
    gl_Position = projectionMatrix * viewMatrix * globalMatrix * vec4(inVertexPosition, 1.0);
    outTextureCoord = inTextureCoord;
}