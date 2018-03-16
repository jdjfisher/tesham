#version 330

in vec2 outTextureCoord;

out vec4 fragmentColour;

uniform sampler2D textureSampler;
uniform bool test;

void main(){
    if(test){
        fragmentColour = vec4(1,0,0,1);
    }else{
        fragmentColour = texture(textureSampler, outTextureCoord);
    }
}


