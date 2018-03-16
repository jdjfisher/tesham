#version 330

layout (location = 0) out vec4 fragmentColour;
layout (location = 1) out vec4 bloomHighlightsColor;

uniform float lampIntensity;
uniform float ambientLightBrightness;
uniform bool active;
uniform vec3 lampColour;

void main(){
    vec3 result = lampColour;

    if(active){
        result *= lampIntensity;
    }else{
        result *= ambientLightBrightness;
    }

    fragmentColour = vec4(result, 1.0);

    float brightness = dot(result, vec3(0.2126, 0.7152, 0.0722));

    if(brightness > 1.0){
        bloomHighlightsColor = vec4(result, 1.0);
    }else{
        bloomHighlightsColor = vec4(0.0, 0.0, 0.0, 1.0);
    }
}