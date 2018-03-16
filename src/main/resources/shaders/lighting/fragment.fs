#version 330 core

layout (location = 0) out vec4 fragmentColour;
layout (location = 1) out vec4 bloomHighlightsColor;

in vec2 outTextureCoord;

uniform sampler2D gFragmentGlobalViewPosition;
uniform sampler2D gFragmentGlobalViewNormal;
uniform sampler2D gDiffuseComponent;
uniform sampler2D ssaoSampler;

uniform float ambientLightBrightness;
uniform mat4 viewMatrix;
uniform bool useSSAO;

struct Attenuation{
    float constant;
    float linear;
    float exponent;
};

struct Light{
    vec3 colour;
    float intensity;
    bool active;
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
uniform sampler2D shadowMapSampler;
uniform mat4 lightClipMatrix;

const int MAX_POINT_LIGHTS = 100;
uniform int activePointLights;
uniform PointLight[MAX_POINT_LIGHTS] pointLights;

const int MAX_SPOT_LIGHTS = 4;
uniform int activeSpotLights;
uniform SpotLight[MAX_SPOT_LIGHTS] spotLights;

float calcShadowFactor(mat4 lightClipMatrix, vec3 fragmentGlobalViewPosition, vec3 fragmentGlobalViewNormal);
vec3 calcDirectionalLight(DirectionalLight directionalLight, vec3 fragmentGlobalViewPosition, vec3 fragmentGlobalViewNormal, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction);
vec3 calcPointLight(PointLight pointLight, vec3 fragmentGlobalViewPosition, vec3 fragmentGlobalViewNormal, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction);
vec3 calcSpotLight(SpotLight spotLight, vec3 fragmentGlobalViewPosition, vec3 fragmentGlobalViewNormal, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction);

void main(){
    vec3 fragmentGlobalViewPosition = texture(gFragmentGlobalViewPosition, outTextureCoord).rgb;

    vec4 normal = texture(gFragmentGlobalViewNormal, outTextureCoord).rgba;
    vec3 fragmentGlobalViewNormal = normal.xyz;
    float specularExponent = normal.w;

    vec4 albedo = texture(gDiffuseComponent, outTextureCoord).rgba;
    vec3 diffuseComponent = albedo.xyz;
    float specularIntensity = albedo.w;

    float ambientOcclusionFactor;

    if(useSSAO){
        ambientOcclusionFactor = texture(ssaoSampler, outTextureCoord).r;
    }else{
        ambientOcclusionFactor = 1f;
    }

    vec3 fragment_to_camera_direction = normalize(-fragmentGlobalViewPosition);
    vec3 combinedLightingColour = vec3(ambientLightBrightness * ambientOcclusionFactor);

    if ( directionalLight.light.intensity > 0 && directionalLight.light.active){
        combinedLightingColour += calcDirectionalLight(directionalLight, fragmentGlobalViewPosition, fragmentGlobalViewNormal, diffuseComponent, specularIntensity, specularExponent, fragment_to_camera_direction) * calcShadowFactor(lightClipMatrix, fragmentGlobalViewPosition, fragmentGlobalViewNormal);
    }

    for (int i = 0; i < activePointLights; i++){
        PointLight pointLight = pointLights[i];
        if ( pointLight.light.intensity > 0 && pointLight.light.active){
            combinedLightingColour += calcPointLight(pointLight, fragmentGlobalViewPosition, fragmentGlobalViewNormal, diffuseComponent, specularIntensity, specularExponent, fragment_to_camera_direction);
        }
    }

    for (int i = 0; i < activeSpotLights; i++){
        SpotLight spotLight = spotLights[i];
        if ( spotLight.light.intensity > 0 && spotLight.light.active){
            combinedLightingColour += calcSpotLight(spotLight, fragmentGlobalViewPosition, fragmentGlobalViewNormal, diffuseComponent, specularIntensity, specularExponent, fragment_to_camera_direction);
        }
    }

    fragmentColour = vec4(diffuseComponent * combinedLightingColour, 1.0);

    float brightness = dot(fragmentColour.rgb, vec3(0.2126, 0.7152, 0.0722));

    if(brightness > 10.0){
        bloomHighlightsColor = vec4(fragmentColour.rgb, 1.0);
    }else{
        bloomHighlightsColor = vec4(vec3(0), 1.0);
    }
}

vec3 calcSpecularDiffuseColour(vec3 fragmentGlobalViewNormal, vec3 fragment_to_light_source_direction, vec3 fragment_to_camera_direction, float specularIntensity, float specularExponent, Light light){
    vec3 diffuseColour = vec3(0);
    vec3 specularColour = vec3(0);

    float diffuseFactor = max(dot(fragmentGlobalViewNormal, fragment_to_light_source_direction), 0.0);

    if(diffuseFactor > 0){
        diffuseColour = light.intensity * light.colour * diffuseFactor;

        if(specularIntensity > 0){
            vec3 halfwayDirection = normalize(fragment_to_light_source_direction + fragment_to_camera_direction);
            float specularFactor = pow(clamp(dot(fragmentGlobalViewNormal, halfwayDirection), 0.0, 1.0), specularExponent);

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

float calcShadowFactor(mat4 lightClipMatrix, vec3 fragmentGlobalViewPosition, vec3 fragmentGlobalViewNormal){
    vec4 fragmentGlobalViewLightViewPosition = lightClipMatrix * inverse(viewMatrix) * vec4(fragmentGlobalViewPosition, 1.0);

    // perform perspective divide
    vec3 projCoords = fragmentGlobalViewLightViewPosition.xyz / fragmentGlobalViewLightViewPosition.w;
    if(projCoords.z > 1.0){
        return 1;
    }
    // transform to [0,1] range
    projCoords = projCoords * 0.5 + 0.5;
    // get closest depth value from light's perspective (using [0,1] range fragPosLight as coords)
    float closestDepth = texture(shadowMapSampler, projCoords.xy).r;
    // get depth of current fragment from light's perspective
    float currentDepth = projCoords.z;
    // remove shadow acne
    float bias = max(0.05 * (1.0 - dot(fragmentGlobalViewNormal, directionalLight.direction)), 0.005);
    // pcf
    float shadowFactor = 0.0;
    vec2 texelSize = 1.0 / textureSize(shadowMapSampler, 0);
    for(int x = -1; x <= 1; ++x){
        for(int y = -1; y <= 1; ++y){
            float pcfDepth = texture(shadowMapSampler, projCoords.xy + vec2(x, y) * texelSize).r;
             // check whether current frag pos is in shadow
            shadowFactor += currentDepth - bias > pcfDepth ? 1.0 : 0.0;
        }
    }

    return (1 - shadowFactor/ 9.0);
}

vec3 calcDirectionalLight(DirectionalLight directionalLight, vec3 fragmentGlobalViewPosition, vec3 fragmentGlobalViewNormal, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction){
    vec3 fragment_to_light_source_direction = normalize(-directionalLight.direction);
    Light light = directionalLight.light;

    vec3 specularDiffuseColour = calcSpecularDiffuseColour(fragmentGlobalViewNormal, fragment_to_light_source_direction, fragment_to_camera_direction, specularIntensity, specularExponent, light);

    return specularDiffuseColour;
}

vec3 calcPointLight(PointLight pointLight, vec3 fragmentGlobalViewPosition, vec3 fragmentGlobalViewNormal, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction){
    vec3 fragment_to_light_source = (viewMatrix * vec4(pointLight.position, 1)).xyz - fragmentGlobalViewPosition;
    vec3 fragment_to_light_source_direction = normalize(fragment_to_light_source);
    float distanceToLight = length(fragment_to_light_source);
    Light light = pointLight.light;

    if(distanceToLight > pointLight.range){
        return vec3(0);
    }

    vec3 specularDiffuseColour = calcSpecularDiffuseColour(fragmentGlobalViewNormal, fragment_to_light_source_direction, fragment_to_camera_direction, specularIntensity, specularExponent, light);

    float attenuationFactor = calcAttenuationFactor(pointLight.attenuation, distanceToLight);

    return specularDiffuseColour / attenuationFactor;
}

vec3 calcSpotLight(SpotLight spotLight, vec3 fragmentGlobalViewPosition, vec3 fragmentGlobalViewNormal, vec3 diffuseComponent, float specularIntensity, float specularExponent, vec3 fragment_to_camera_direction){
    vec3 fragment_to_light_source = (viewMatrix * vec4(spotLight.position, 1)).xyz - fragmentGlobalViewPosition;
    vec3 fragment_to_light_source_direction = normalize(fragment_to_light_source);
    float distanceToLight = length(fragment_to_light_source);
    Light light = spotLight.light;

    vec3 specularDiffuseColour = calcSpecularDiffuseColour(fragmentGlobalViewNormal, fragment_to_light_source_direction, fragment_to_camera_direction, specularIntensity, specularExponent, light);

    float attenuationFactor = calcAttenuationFactor(spotLight.attenuation, distanceToLight);

    float theta = dot(fragment_to_light_source_direction, normalize((viewMatrix * vec4(-spotLight.coneDirection, 0)).xyz));
    float epsilon = spotLight.cutOff - spotLight.outerCutOff;
    float spotLightConeIntensity = clamp((theta - spotLight.outerCutOff) / epsilon, 0.0, 1.0);

    return (specularDiffuseColour * spotLightConeIntensity) / attenuationFactor;
}
