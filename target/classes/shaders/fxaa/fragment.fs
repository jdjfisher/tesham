#version 330

#ifndef FXAA_REDUCE_MIN
    #define FXAA_REDUCE_MIN   (1.0/ 128.0)
#endif
#ifndef FXAA_REDUCE_MUL
    #define FXAA_REDUCE_MUL   (1.0 / 8.0)
#endif
#ifndef FXAA_SPAN_MAX
    #define FXAA_SPAN_MAX     8.0
#endif

in vec2 outTextureCoord;

out vec4 fragmentColour;

precision mediump float;

uniform vec2 screenResolution;
uniform sampler2D texure;
uniform bool enabled;

vec4 fxaa(sampler2D texure, vec2 textureCoord, vec2 screenResolution);

void main() {
  if (enabled) {
      fragmentColour = fxaa(texure, outTextureCoord, screenResolution);
  } else {
      fragmentColour = texture2D(texure, outTextureCoord);
  }
}

vec4 fxaa(sampler2D texure, vec2 textureCoord, vec2 screenResolution) {
    mediump vec2 inverseVP = 1.0 / screenResolution;
    vec2 fragCoord = textureCoord * screenResolution;

    vec3 rgbNW = texture2D(texure, (fragCoord + vec2(-1.0, -1.0)) * inverseVP).xyz;
    vec3 rgbNE = texture2D(texure, (fragCoord + vec2(1.0, -1.0)) * inverseVP).xyz;
    vec3 rgbSW = texture2D(texure, (fragCoord + vec2(-1.0, 1.0)) * inverseVP).xyz;
    vec3 rgbSE = texture2D(texure, (fragCoord + vec2(1.0, 1.0)) * inverseVP).xyz;
    vec4 rgbaM = texture2D(texure, vec2(fragCoord * inverseVP));
    vec3 rgbM  = rgbaM.xyz;
    float aM = rgbaM.a;

    vec3 luma = vec3(0.299, 0.587, 0.114);
    float lumaNW = dot(rgbNW, luma);
    float lumaNE = dot(rgbNE, luma);
    float lumaSW = dot(rgbSW, luma);
    float lumaSE = dot(rgbSE, luma);
    float lumaM  = dot(rgbM,  luma);
    float lumaMin = min(lumaM, min(min(lumaNW, lumaNE), min(lumaSW, lumaSE)));
    float lumaMax = max(lumaM, max(max(lumaNW, lumaNE), max(lumaSW, lumaSE)));

    mediump vec2 dir;
    dir.x = -((lumaNW + lumaNE) - (lumaSW + lumaSE));
    dir.y =  ((lumaNW + lumaSW) - (lumaNE + lumaSE));

    float dirReduce = max((lumaNW + lumaNE + lumaSW + lumaSE) *
                          (0.25 * FXAA_REDUCE_MUL), FXAA_REDUCE_MIN);

    float rcpDirMin = 1.0 / (min(abs(dir.x), abs(dir.y)) + dirReduce);
    dir = min(vec2(FXAA_SPAN_MAX, FXAA_SPAN_MAX),
          max(vec2(-FXAA_SPAN_MAX, -FXAA_SPAN_MAX), dir * rcpDirMin)) * inverseVP;

    vec3 rgbA = 0.5 * (
        texture2D(texure, fragCoord * inverseVP + dir * (1.0 / 3.0 - 0.5)).xyz +
        texture2D(texure, fragCoord * inverseVP + dir * (2.0 / 3.0 - 0.5)).xyz);
    vec3 rgbB = rgbA * 0.5 + 0.25 * (
        texture2D(texure, fragCoord * inverseVP + dir * -0.5).xyz +
        texture2D(texure, fragCoord * inverseVP + dir * 0.5).xyz);

    float lumaB = dot(rgbB, luma);

    if ((lumaB < lumaMin) || (lumaB > lumaMax)){
        return vec4(rgbA, aM);
    }else{
        return vec4(rgbB, aM);
    }
}