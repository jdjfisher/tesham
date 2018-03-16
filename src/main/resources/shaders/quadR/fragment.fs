#version 330

in vec2 outTextureCoord;

out vec4 fragmentColour;

uniform sampler2D textureSampler;

void main(){
    fragmentColour = vec4(vec3(texture(textureSampler, outTextureCoord).r), 1.0);
}