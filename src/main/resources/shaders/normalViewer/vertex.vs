#version 330 core

layout (location = 0) in vec3 inVertexPosition;
layout (location = 1) in vec3 inVertexNormal;

out VS_OUT {
    vec4 normal;
} vs_out;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 globalMatrix;

void main(){
    mat3 normalMatrix = mat3(transpose(inverse(viewMatrix * globalMatrix)));
    vs_out.normal = normalize(projectionMatrix * vec4(normalMatrix * inVertexNormal, 0.0)); //TODO: fix
    gl_Position = projectionMatrix * viewMatrix * globalMatrix * vec4(inVertexPosition, 1.0);
}