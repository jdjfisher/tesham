#version 330

in vec2 outTextureCoord;

out vec4 fragmentColour;

uniform sampler2D hBuffer_SceneColour_Texture;
uniform sampler2D hBuffer_BloomColour_Texture;
uniform bool useHDR;
uniform bool changeGamma;
uniform float exposure;
uniform float gamma;

void main(){
    vec3 postLightingColour = texture(hBuffer_SceneColour_Texture, outTextureCoord).rgb;
    vec3 postBloomBlurColour = texture(hBuffer_BloomColour_Texture, outTextureCoord).rgb;

    vec3 result = postLightingColour + postBloomBlurColour;

    if(useHDR){
        result = vec3(1.0) - exp(-result * exposure);
    }

    if(changeGamma){
        result = pow(result, vec3(1.0 / gamma));
    }

    fragmentColour = vec4(result, 1.0);
}