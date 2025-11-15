#type vertex
#version 330 core

layout(location=0) in vec3 aPos;
layout(location=1) in vec4 aColor;
layout(location=2) in vec2 aUV;
layout(location=3) in float aTextureSlot;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fUV;
out float fTextureSlot;

void main() {
    fColor = aColor;
    fUV = aUV;
    fTextureSlot = aTextureSlot;
    gl_Position = uProjection * uView * vec4(aPos, 1f);
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fUV;
in float fTextureSlot;

uniform float uTime;
uniform sampler2D uTextureSampler0;
uniform sampler2D uTextureSampler1;

out vec4 color;

void main() {
    color = fColor;
    if(fTextureSlot == 0) {
        color = texture(uTextureSampler0, fUV);
    } else if(fTextureSlot == 1) {
        color = texture(uTextureSampler1, fUV);
    }
}