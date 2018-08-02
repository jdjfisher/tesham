#version 450 core
layout (location = 0) out vec3 fragmentPosition_W_Texture;
layout (location = 1) out vec4 fragmentNormal_W_Texture;
layout (location = 2) out vec4 diffuseComponent_Texture;

in vec3 outFragmentPosition_W;
in vec2 outTextureCoord;
in mat3 outTBN;
in vec3 outFragmentNormal_W;

uniform float reflectance;
uniform vec3 diffuseColour;

uniform sampler2D normalMap;
uniform bool hasNormalMap;
uniform sampler2D diffuseTexture;
uniform bool hasDiffuseTexture;
uniform sampler2D specularMap;
uniform bool hasSpecularMap;

void main(){
    fragmentPosition_W_Texture = outFragmentPosition_W;

    if(hasNormalMap){
        fragmentNormal_W_Texture.rgb = normalize(outTBN * normalize((texture(normalMap, outTextureCoord).rgb) * 2.0 - 1.0));
    }else{
        fragmentNormal_W_Texture.rgb = outFragmentNormal_W;
    }

    fragmentNormal_W_Texture.a = reflectance;

    if(hasDiffuseTexture){
        diffuseComponent_Texture.rgb = texture(diffuseTexture, outTextureCoord).rgb;
    }else{
        diffuseComponent_Texture.rgb = diffuseColour;
    }

    if(hasSpecularMap){
        diffuseComponent_Texture.a = texture(specularMap, outTextureCoord).r;
    }else{
        diffuseComponent_Texture.a = 1f;
    }
}
