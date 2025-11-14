#type vertex
#version 330 core

layout(location=0) in vec3 aPos;
layout(location=1) in vec4 aColor;
layout(location=2) in vec2 aUV;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fUV;

void main() {
    fColor = aColor;
    fUV = aUV;
    gl_Position = uProjection * uView * vec4(aPos, 1);
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fUV;

uniform float uTime;
uniform sampler2D uTextureSampler;

out vec4 color;

void main() {
    vec4 textureColor = texture(uTextureSampler, fUV);
    float brightness = (textureColor.r + textureColor.g + textureColor.b) / 3;
    color = vec4(brightness, brightness, brightness, 1);
}