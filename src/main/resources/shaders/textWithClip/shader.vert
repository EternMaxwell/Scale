#version 460 core

layout (location = 0) in vec4 color;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec2 pos;
layout (location = 3) in vec2 size;
layout (location = 4) in vec2 texSize;
layout (location = 5) in vec4 clip;

layout (location = 0) out vec2 out_size;
layout (location = 1) out vec4 out_color;
layout (location = 2) out vec4 out_tex;
layout (location = 3) out vec4 out_clip;

layout (binding = 0) uniform block{
    mat4 model;
    mat4 view;
    mat4 projection;
} Block;

void main() {
    gl_Position = vec4(pos, 0.0, 1.0);
    out_size = size;
    out_color = color;
    out_tex = vec4(texCoord, texSize);
    out_clip = clip;
}
