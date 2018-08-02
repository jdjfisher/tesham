#version 330

in vec2 outTextureCoord;

out vec4 fragmentColour;

uniform sampler2D primaryScene_Sampler;
uniform sampler2D bloomHighlights_Sampler;
uniform bool useHDR;
uniform bool correctGamma;
uniform float exposure;
uniform float gamma;

void main()
{
    vec3 result = texture(primaryScene_Sampler, outTextureCoord).rgb + texture(bloomHighlights_Sampler, outTextureCoord).rgb;

    if(useHDR)
    {
        result = vec3(1.0) - exp(-result * exposure);
    }

    if(correctGamma)
    {
        result = pow(result, vec3(1.0 / gamma));
    }

    fragmentColour = vec4(result, 1.0);
}