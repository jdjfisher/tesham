#version 330

layout (location = 0) out vec4 fragmentColour;
layout (location = 1) out vec4 bloomHighlightsColour;

uniform float lampIntensity;
uniform vec3 lampColour;

void main()
{
    vec3 result = lampColour * lampIntensity;

    fragmentColour = vec4(result, 1.0);

    float brightness = dot(result, vec3(0.2126, 0.7152, 0.0722));

    if(brightness > 1.0)
    {
        bloomHighlightsColour = vec4(result, 1.0);
    }
    else
    {
        bloomHighlightsColour = vec4(0.0, 0.0, 0.0, 1.0);
    }
}