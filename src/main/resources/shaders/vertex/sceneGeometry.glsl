#version 330

layout (location = 0) in vec3 inVertexPosition;
layout (location = 1) in vec3 inVertexNormal;
layout (location = 2) in vec2 inTextureCoord;
layout (location = 3) in vec3 inTangent;
layout (location = 4) in vec3 inBitangent;

out vec3 outFragmentWorldViewPosition;
out vec2 outTextureCoord;
out mat3 outTBN;
out vec3 outFragmentWorldViewNormal;

uniform mat4 worldViewMatrix;
uniform mat4 projectionMatrix;
uniform bool hasNormalMap;

void main(){
    vec4 worldViewVertexPosition = worldViewMatrix * vec4(inVertexPosition, 1.0);
    outFragmentWorldViewPosition = worldViewVertexPosition.xyz;
    gl_Position = projectionMatrix * worldViewVertexPosition;

    mat3 normalMatrix = mat3(transpose(inverse(worldViewMatrix)));

    outFragmentWorldViewNormal = normalize(normalMatrix * inVertexNormal);

    outTextureCoord = inTextureCoord;

    if(hasNormalMap){
        vec3 T = normalize(normalMatrix * inTangent);
        vec3 B = normalize(normalMatrix * inBitangent);
        outTBN = mat3(T, B, outFragmentWorldViewNormal);
    }
}