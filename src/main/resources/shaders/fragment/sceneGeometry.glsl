#version 450 core
layout (location = 0) out vec3 fragmentPosition_W_Texture;
layout (location = 1) out vec4 fragmentNormal_W_Texture;
layout (location = 2) out vec4 diffuseComponent_Texture;

in vec3 outFragmentPosition_W;
in vec3 outFragmentNormal_W;
in vec2 outTextureCoord;
in mat3 outTBN;

uniform float reflectance;
uniform vec3 diffuseColour;

uniform bool hasDiffuseTexture;
uniform bool hasNormalMap;
uniform bool hasSpecularMap;

uniform sampler2D diffuseTexture_Sampler;
uniform sampler2D normalMap_Sampler;
uniform sampler2D specularMap_Sampler;

void main(){
    fragmentPosition_W_Texture = outFragmentPosition_W;

    if(hasNormalMap){
        fragmentNormal_W_Texture.rgb = normalize(outTBN * normalize((texture(normalMap_Sampler, outTextureCoord).rgb) * 2.0 - 1.0));
    }else{
        fragmentNormal_W_Texture.rgb = outFragmentNormal_W;
    }

    fragmentNormal_W_Texture.a = reflectance;

    if(hasDiffuseTexture){
        diffuseComponent_Texture.rgb = texture(diffuseTexture_Sampler, outTextureCoord).rgb;
    }else{
        diffuseComponent_Texture.rgb = diffuseColour;
    }

    if(hasSpecularMap){
        diffuseComponent_Texture.a = texture(specularMap_Sampler, outTextureCoord).r;
    }else{
        diffuseComponent_Texture.a = 1.0;
    }
}
