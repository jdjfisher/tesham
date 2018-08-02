#version 330 core

layout (location = 0) out vec4 fragmentColour;
layout (location = 1) out vec4 bloomHighlightsColour;

in vec2 outTextureCoord;

uniform sampler2D fragmentPosition_W_Sampler;
uniform sampler2D fragmentNormal_W_Sampler;
uniform sampler2D diffuseComponent_Sampler;

uniform vec3 cameraPosition;
uniform float ambientLightBrightness;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct Light
{
    vec3 colour;
    float intensity;
    bool enabled;
};

struct DirectionalLight
{
    Light light;
    vec3 direction;
};

struct PointLight
{
    Light light;
    vec3 position;
    Attenuation attenuation;
    float range;
};

struct SpotLight
{
    Light light;
    vec3 position;
    Attenuation attenuation;
    vec3 coneDirection;
    float cutOff;
    float outerCutOff;
};

uniform DirectionalLight directionalLight;

const int MAX_POINT_LIGHTS = 5;
uniform int activePointLights;
uniform PointLight[MAX_POINT_LIGHTS] pointLights;

const int MAX_SPOT_LIGHTS = 5;
uniform int activeSpotLights;
uniform SpotLight[MAX_SPOT_LIGHTS] spotLights;

vec3 calcDirectionalLight(DirectionalLight directionalLight, vec3 fragmentPosition_W, vec3 fragmentNormal_W, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction);
vec3 calcPointLight(PointLight pointLight, vec3 fragmentPosition_W, vec3 fragmentNormal_W, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction);
vec3 calcSpotLight(SpotLight spotLight, vec3 fragmentPosition_W, vec3 fragmentNormal_W, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction);

void main()
{
    vec3 fragmentPosition_W = texture(fragmentPosition_W_Sampler, outTextureCoord).rgb;

    vec4 normal = texture(fragmentNormal_W_Sampler, outTextureCoord).rgba;
    vec3 fragmentNormal_W = normal.xyz;
    float specularExponent = normal.w;

    vec4 albedo = texture(diffuseComponent_Sampler, outTextureCoord).rgba;
    vec3 diffuseComponent = albedo.xyz;
    float specularIntensity = albedo.w;

    vec3 fragment_to_camera_direction = normalize(cameraPosition - fragmentPosition_W);
    vec3 combinedLightingColour = vec3(ambientLightBrightness);

    if ( directionalLight.light.intensity > 0 && directionalLight.light.enabled)
    {
        combinedLightingColour += calcDirectionalLight(directionalLight, fragmentPosition_W, fragmentNormal_W, diffuseComponent, specularIntensity, specularExponent, fragment_to_camera_direction);
    }

    for (int i = 0; i < activePointLights; i++)
    {
        PointLight pointLight = pointLights[i];
        if ( pointLight.light.intensity > 0 && pointLight.light.enabled)
        {
            combinedLightingColour += calcPointLight(pointLight, fragmentPosition_W, fragmentNormal_W, diffuseComponent, specularIntensity, specularExponent, fragment_to_camera_direction);
        }
    }

    for (int i = 0; i < activeSpotLights; i++)
    {
        SpotLight spotLight = spotLights[i];
        if ( spotLight.light.intensity > 0 && spotLight.light.enabled)
        {
            combinedLightingColour += calcSpotLight(spotLight, fragmentPosition_W, fragmentNormal_W, diffuseComponent, specularIntensity, specularExponent, fragment_to_camera_direction);
        }
    }

    fragmentColour = vec4(diffuseComponent * combinedLightingColour, 1.0);

    float brightness = dot(fragmentColour.rgb, vec3(0.2126, 0.7152, 0.0722));

    if(brightness > 10.0)
    {
        bloomHighlightsColour = vec4(fragmentColour.rgb, 1.0);
    }
    else
    {
        bloomHighlightsColour = vec4(vec3(0), 1.0);
    }
}

vec3 calcSpecularDiffuseColour(vec3 fragmentNormal_W, vec3 fragment_to_light_direction, vec3 fragment_to_camera_direction, float specularIntensity, float specularExponent, Light light)
{
    vec3 diffuseColour = vec3(0);
    vec3 specularColour = vec3(0);

    float diffuseFactor = max(dot(fragmentNormal_W, fragment_to_light_direction), 0.0);

    if(diffuseFactor > 0)
    {
        diffuseColour = light.intensity * light.colour * diffuseFactor;

        if(specularIntensity > 0)
        {
            vec3 halfwayDirection = normalize(fragment_to_light_direction + fragment_to_camera_direction);
            float specularFactor = pow(clamp(dot(fragmentNormal_W, halfwayDirection), 0.0, 1.0), specularExponent);

            if(specularFactor > 0)
            {
                specularColour = light.intensity * light.colour * specularFactor * specularIntensity;
            }
        }
    }

    return diffuseColour + specularColour;
}

float calcAttenuationFactor(Attenuation attenuation, float distance_to_light)
{
    return attenuation.constant + (attenuation.linear * distance_to_light) + (attenuation.exponent * distance_to_light * distance_to_light);
}

vec3 calcDirectionalLight(DirectionalLight directionalLight, vec3 fragmentPosition_W, vec3 fragmentNormal_W, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction)
{
    vec3 fragment_to_light_direction = - normalize(directionalLight.direction);
    Light light = directionalLight.light;

    vec3 specularDiffuseColour = calcSpecularDiffuseColour(fragmentNormal_W, fragment_to_light_direction, fragment_to_camera_direction, specularIntensity, specularExponent, light);

    return specularDiffuseColour;
}

vec3 calcPointLight(PointLight pointLight, vec3 fragmentPosition_W, vec3 fragmentNormal_W, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction)
{
    vec3 fragment_to_light = pointLight.position - fragmentPosition_W;
    vec3 fragment_to_light_direction = normalize(fragment_to_light);
    float distance_to_light = length(fragment_to_light);
    Light light = pointLight.light;

    if(distance_to_light > pointLight.range)
    {
        return vec3(0);
    }

    vec3 specularDiffuseColour = calcSpecularDiffuseColour(fragmentNormal_W, fragment_to_light_direction, fragment_to_camera_direction, specularIntensity, specularExponent, light);

    float attenuationFactor = calcAttenuationFactor(pointLight.attenuation, distance_to_light);

    return specularDiffuseColour / attenuationFactor;
}

vec3 calcSpotLight(SpotLight spotLight, vec3 fragmentPosition_W, vec3 fragmentNormal_W, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction)
{
    vec3 fragment_to_light = spotLight.position - fragmentPosition_W;
    vec3 fragment_to_light_direction = normalize(fragment_to_light);
    float distance_to_light = length(fragment_to_light);
    Light light = spotLight.light;

    vec3 specularDiffuseColour = calcSpecularDiffuseColour(fragmentNormal_W, fragment_to_light_direction, fragment_to_camera_direction, specularIntensity, specularExponent, light);

    float attenuationFactor = calcAttenuationFactor(spotLight.attenuation, distance_to_light);

    float theta = dot(fragment_to_light_direction, - normalize(spotLight.coneDirection));
    float epsilon = spotLight.cutOff - spotLight.outerCutOff;
    float spotLightConeIntensity = clamp((theta - spotLight.outerCutOff) / epsilon, 0.0, 1.0);

    return (specularDiffuseColour * spotLightConeIntensity) / attenuationFactor;
}
