#version 330

layout (location = 0) in vec3 inVertexPosition;
layout (location = 1) in vec3 inVertexNormal;
layout (location = 2) in vec2 inTextureCoord;
layout (location = 3) in vec3 inTangent;
layout (location = 4) in vec3 inBitangent;

out vec3 outFragmentPosition_W;
out vec2 outTextureCoord;
out mat3 outTBN;
out vec3 outFragmentNormal_W;

uniform mat4 PV_Matrix;
uniform mat4 W_Matrix;
uniform bool hasNormalMap;

void main(){
    vec4 vertexPosition_W = W_Matrix * vec4(inVertexPosition, 1.0);
    outFragmentPosition_W = vertexPosition_W.xyz;
    gl_Position = PV_Matrix * vertexPosition_W;

    mat3 N_Matrix = mat3(transpose(inverse(W_Matrix)));

    outFragmentNormal_W = normalize(N_Matrix * inVertexNormal);

    outTextureCoord = inTextureCoord;

    if(hasNormalMap){
        vec3 T = normalize(N_Matrix * inTangent);
        vec3 B = normalize(N_Matrix * inBitangent);
        outTBN = mat3(T, B, outFragmentNormal_W);
    }
}