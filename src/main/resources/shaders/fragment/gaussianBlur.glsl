#version 450 core

const float weight[5] = float[] (0.2270270270, 0.1945945946, 0.1216216216, 0.0540540541, 0.0162162162);

in vec2 outTextureCoord;

out vec4 fragmentColour;

uniform sampler2D texture_Sampler;
uniform bool horizontal;

void main()
{
     vec2 tex_offset = 1.0 / textureSize(texture_Sampler, 0); // gets size of single texel
     vec3 result = texture(texture_Sampler, outTextureCoord).rgb * weight[0];

     if(horizontal)
     {
         for(int i = 1; i < 5; ++i)
         {
            result += texture(texture_Sampler, outTextureCoord + vec2(tex_offset.x * i, 0.0)).rgb * weight[i];
            result += texture(texture_Sampler, outTextureCoord - vec2(tex_offset.x * i, 0.0)).rgb * weight[i];
         }
     }
     else
     {
         for(int i = 1; i < 5; ++i)
         {
             result += texture(texture_Sampler, outTextureCoord + vec2(0.0, tex_offset.y * i)).rgb * weight[i];
             result += texture(texture_Sampler, outTextureCoord - vec2(0.0, tex_offset.y * i)).rgb * weight[i];
         }
     }

     fragmentColour = vec4(result, 1.0);
}