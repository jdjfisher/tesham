#version 330

layout (location = 0) in vec3 inVertexPosition;
layout (location = 1) in vec3 inVertexNormal;
layout (location = 2) in vec2 inTextureCoord;
layout (location = 3) in vec3 inTangent;
layout (location = 4) in vec3 inBitangent;

out vec2 outTextureCoord;
out vec3 vertexColour;

uniform mat4 PV_Matrix;
uniform mat4 W_Matrix;

void main(){
    gl_Position = PV_Matrix * W_Matrix * vec4(inVertexPosition, 1.0);
    outTextureCoord = inTextureCoord;
    vertexColour = inVertexNormal;
 }