#version 450 core
layout (location = 0) out vec3 gFragmentWorldViewPosition;
layout (location = 1) out vec4 gFragmentWorldViewNormal;
layout (location = 2) out vec4 gDiffuseComponent;

in vec3 outFragmentWorldViewPosition;
in vec2 outTextureCoord;
in mat3 outTBN;
in vec3 outFragmentWorldViewNormal;

uniform float reflectance;
uniform vec3 diffuseColour;

uniform sampler2D normalMap;
uniform bool hasNormalMap;
uniform sampler2D diffuseTexture;
uniform bool hasDiffuseTexture;
uniform sampler2D specularMap;
uniform bool hasSpecularMap;

void main(){
    gFragmentWorldViewPosition = outFragmentWorldViewPosition;

    if(hasNormalMap){
        gFragmentWorldViewNormal.rgb = normalize(outTBN * normalize((texture(normalMap, outTextureCoord).rgb) * 2.0 - 1.0));
    }else{
        gFragmentWorldViewNormal.rgb = outFragmentWorldViewNormal;
    }

    gFragmentWorldViewNormal.a = reflectance;

    if(hasDiffuseTexture){
        gDiffuseComponent.rgb = texture(diffuseTexture, outTextureCoord).rgb;
    }else{
        gDiffuseComponent.rgb = diffuseColour;
    }

    if(hasSpecularMap){
        gDiffuseComponent.a = texture(specularMap, outTextureCoord).r;
    }else{
        gDiffuseComponent.a = 1f;
    }
}
