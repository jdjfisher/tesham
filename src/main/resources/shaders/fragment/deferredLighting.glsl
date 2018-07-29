#version 330 core

layout (location = 0) out vec4 fragmentColour;

in vec2 outTextureCoord;

uniform sampler2D gFragmentWorldViewPosition;
uniform sampler2D gFragmentWorldViewNormal;
uniform sampler2D gDiffuseComponent;

uniform float ambientLightBrightness;
uniform mat4 viewMatrix;

struct Attenuation{
    float constant;
    float linear;
    float exponent;
};

struct Light{
    vec3 colour;
    float intensity;
    bool enabled;
};

struct DirectionalLight{
    Light light;
    vec3 direction;
};

struct PointLight{
    Light light;
    vec3 position;
    Attenuation attenuation;
    float range;
};

struct SpotLight{
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

vec3 calcDirectionalLight(DirectionalLight directionalLight, vec3 fragmentWorldViewPosition, vec3 fragmentWorldViewNormal, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction);
vec3 calcPointLight(PointLight pointLight, vec3 fragmentWorldViewPosition, vec3 fragmentWorldViewNormal, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction);
vec3 calcSpotLight(SpotLight spotLight, vec3 fragmentWorldViewPosition, vec3 fragmentWorldViewNormal, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction);

void main(){
    vec3 fragmentWorldViewPosition = texture(gFragmentWorldViewPosition, outTextureCoord).rgb;

    vec4 normal = texture(gFragmentWorldViewNormal, outTextureCoord).rgba;
    vec3 fragmentWorldViewNormal = normal.xyz;
    float specularExponent = normal.w;

    vec4 albedo = texture(gDiffuseComponent, outTextureCoord).rgba;
    vec3 diffuseComponent = albedo.xyz;
    float specularIntensity = albedo.w;

    vec3 fragment_to_camera_direction = normalize(-fragmentWorldViewPosition);
    vec3 combinedLightingColour = vec3(ambientLightBrightness);

    if ( directionalLight.light.intensity > 0 && directionalLight.light.enabled){
        combinedLightingColour += calcDirectionalLight(directionalLight, fragmentWorldViewPosition, fragmentWorldViewNormal, diffuseComponent, specularIntensity, specularExponent, fragment_to_camera_direction);
    }

    for (int i = 0; i < activePointLights; i++){
        PointLight pointLight = pointLights[i];
        if ( pointLight.light.intensity > 0 && pointLight.light.enabled){
            combinedLightingColour += calcPointLight(pointLight, fragmentWorldViewPosition, fragmentWorldViewNormal, diffuseComponent, specularIntensity, specularExponent, fragment_to_camera_direction);
        }
    }

    for (int i = 0; i < activeSpotLights; i++){
        SpotLight spotLight = spotLights[i];
        if ( spotLight.light.intensity > 0 && spotLight.light.enabled){
            combinedLightingColour += calcSpotLight(spotLight, fragmentWorldViewPosition, fragmentWorldViewNormal, diffuseComponent, specularIntensity, specularExponent, fragment_to_camera_direction);
        }
    }

    fragmentColour = vec4(diffuseComponent * combinedLightingColour, 1.0);
}

vec3 calcSpecularDiffuseColour(vec3 fragmentWorldViewNormal, vec3 fragment_to_light_source_direction, vec3 fragment_to_camera_direction, float specularIntensity, float specularExponent, Light light){
    vec3 diffuseColour = vec3(0);
    vec3 specularColour = vec3(0);

    float diffuseFactor = max(dot(fragmentWorldViewNormal, fragment_to_light_source_direction), 0.0);

    if(diffuseFactor > 0){
        diffuseColour = light.intensity * light.colour * diffuseFactor;

        if(specularIntensity > 0){
            vec3 halfwayDirection = normalize(fragment_to_light_source_direction + fragment_to_camera_direction);
            float specularFactor = pow(clamp(dot(fragmentWorldViewNormal, halfwayDirection), 0.0, 1.0), specularExponent);

            if(specularFactor > 0){
                specularColour = light.intensity * light.colour * specularFactor * specularIntensity;
            }
        }
    }

    return diffuseColour + specularColour;
}

float calcAttenuationFactor(Attenuation attenuation, float distanceToLight){
    return attenuation.constant + (attenuation.linear * distanceToLight) + (attenuation.exponent * distanceToLight * distanceToLight);
}

vec3 calcDirectionalLight(DirectionalLight directionalLight, vec3 fragmentWorldViewPosition, vec3 fragmentWorldViewNormal, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction){
    vec3 fragment_to_light_source_direction = -normalize((viewMatrix * vec4(directionalLight.direction, 0)).xyz);
    Light light = directionalLight.light;

    vec3 specularDiffuseColour = calcSpecularDiffuseColour(fragmentWorldViewNormal, fragment_to_light_source_direction, fragment_to_camera_direction, specularIntensity, specularExponent, light);

    return specularDiffuseColour;
}

vec3 calcPointLight(PointLight pointLight, vec3 fragmentWorldViewPosition, vec3 fragmentWorldViewNormal, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction){
    vec3 fragment_to_light_source = (viewMatrix * vec4(pointLight.position, 1)).xyz - fragmentWorldViewPosition;
    vec3 fragment_to_light_source_direction = normalize(fragment_to_light_source);
    float distanceToLight = length(fragment_to_light_source);
    Light light = pointLight.light;

    if(distanceToLight > pointLight.range){
        return vec3(0);
    }

    vec3 specularDiffuseColour = calcSpecularDiffuseColour(fragmentWorldViewNormal, fragment_to_light_source_direction, fragment_to_camera_direction, specularIntensity, specularExponent, light);

    float attenuationFactor = calcAttenuationFactor(pointLight.attenuation, distanceToLight);

    return specularDiffuseColour / attenuationFactor;
}

vec3 calcSpotLight(SpotLight spotLight, vec3 fragmentWorldViewPosition, vec3 fragmentWorldViewNormal, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction){
    vec3 fragment_to_light_source = (viewMatrix * vec4(spotLight.position, 1)).xyz - fragmentWorldViewPosition;
    vec3 fragment_to_light_source_direction = normalize(fragment_to_light_source);
    float distanceToLight = length(fragment_to_light_source);
    Light light = spotLight.light;

    vec3 specularDiffuseColour = calcSpecularDiffuseColour(fragmentWorldViewNormal, fragment_to_light_source_direction, fragment_to_camera_direction, specularIntensity, specularExponent, light);

    float attenuationFactor = calcAttenuationFactor(spotLight.attenuation, distanceToLight);

    float theta = dot(fragment_to_light_source_direction, normalize((viewMatrix * vec4(-spotLight.coneDirection, 0)).xyz));
    float epsilon = spotLight.cutOff - spotLight.outerCutOff;
    float spotLightConeIntensity = clamp((theta - spotLight.outerCutOff) / epsilon, 0.0, 1.0);

    return (specularDiffuseColour * spotLightConeIntensity) / attenuationFactor;
}
