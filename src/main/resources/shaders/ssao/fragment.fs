#version 330 core

out float fragmentColour;

in vec2 outTextureCoord;

uniform sampler2D gFragmentGlobalViewPosition;
uniform sampler2D gFragmentGlobalViewNormal;
uniform sampler2D noiseTexture;

const int kernelSize = 48;

uniform mat4 projectionMatrix;
uniform vec3[kernelSize] samples;
uniform vec2 noiseScale;

const float radius = 0.5;
const float bias = 0.025;

void main(){
       // get input for SSAO algorithm
       vec3 fragPos = texture(gFragmentGlobalViewPosition, outTextureCoord).xyz;
       vec3 normal = normalize(texture(gFragmentGlobalViewNormal, outTextureCoord).xyz);
       vec3 randomVec = normalize(texture(noiseTexture, outTextureCoord * noiseScale).xyz);
       // create TBN change-of-basis matrix: from tangent-space to view-space
       vec3 tangent = normalize(randomVec - normal * dot(randomVec, normal));
       vec3 bitangent = cross(normal, tangent);
       mat3 TBN = mat3(tangent, bitangent, normal);
       // iterate over the sample kernel and calculate occlusion factor
       float occlusion = 0.0;
       for(int i = 0; i < kernelSize; i++){
           // get sample position
           vec3 sample = TBN * samples[i]; // from tangent to view-space
           sample = fragPos + sample * radius;

           // project sample position (to sample texture) (to get position on screen/texture)
           vec4 offset = vec4(sample, 1.0);
           offset = projectionMatrix * offset; // from view to clip-space
           offset.xyz /= offset.w; // perspective divide
           offset.xyz = offset.xyz * 0.5 + 0.5; // transform to range 0.0 - 1.0

           // get sample depth
           float sampleDepth = texture(gFragmentGlobalViewPosition, offset.xy).z; // get depth value of kernel sample

           // range check & accumulate
           float rangeCheck = smoothstep(0.0, 1.0, radius / abs(fragPos.z - sampleDepth));
           occlusion += (sampleDepth >= sample.z + bias ? 1.0 : 0.0) * rangeCheck;
       }
       occlusion = 1.0 - (occlusion / kernelSize);

       fragmentColour = occlusion;
}
