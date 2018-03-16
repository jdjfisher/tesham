#version 330

layout (location = 0) in vec3 inVertexPosition;

uniform mat4 globalMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(){
    gl_Position = projectionMatrix * viewMatrix * globalMatrix * vec4(inVertexPosition, 1.0);
}