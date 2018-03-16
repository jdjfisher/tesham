#version 330

layout (location = 0) in vec3 inVertexPosition;

uniform mat4 globalMatrix;
uniform mat4 clipMatrix;

void main(){
    gl_Position = clipMatrix * globalMatrix * vec4(inVertexPosition, 1.0);
}