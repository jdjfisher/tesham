#version 330

in vec2 outTextureCoord;

out vec4 fragmentColour;

uniform sampler2D texture_Sampler;

void main()
{
    fragmentColour = vec4(texture(texture_Sampler, outTextureCoord).rgb, 1.0);
}