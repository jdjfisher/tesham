#version 330

in vec2 outTextureCoord;

out vec4 fragmentColour;

uniform sampler2D textureSampler;

void main(){
    fragmentColour = vec4(texture(textureSampler, outTextureCoord).rgb, 1);
}