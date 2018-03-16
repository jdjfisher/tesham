#version 330

layout (location = 0) in vec3 inVertexPosition;
layout (location = 1) in vec3 inVertexNormal;
layout (location = 2) in vec2 inTextureCoord;
layout (location = 3) in vec3 inTangent;
layout (location = 4) in vec3 inBitangent;

out vec3 outFragmentGlobalViewPosition;
out vec2 outTextureCoord;
out mat3 outTBN;
out vec3 outFragmentGlobalViewNormal;

uniform mat4 globalViewMatrix;
uniform mat4 projectionMatrix;
uniform bool hasNormalMap;

void main(){
    vec4 globalViewVertexPosition = globalViewMatrix * vec4(inVertexPosition, 1.0);
    outFragmentGlobalViewPosition = globalViewVertexPosition.xyz;
    gl_Position = projectionMatrix * globalViewVertexPosition;

    mat3 normalMatrix = mat3(transpose(inverse(globalViewMatrix)));

    outFragmentGlobalViewNormal = normalize(normalMatrix * inVertexNormal);

    outTextureCoord = inTextureCoord;

    if(hasNormalMap){
        vec3 T = normalize(normalMatrix * inTangent);
        vec3 B = normalize(normalMatrix * inBitangent);
        outTBN = mat3(T, B, outFragmentGlobalViewNormal);
    }
}