#version 460 core

layout (location = 0) in vec2 in_pos;
layout (location = 1) in vec2 in_size;
layout (location = 2) in vec4 in_color;
layout (location = 3) in vec4 in_tex;

layout (location = 0) out vec2 out_size;
layout (location = 1) out vec4 out_color;
layout (location = 2) out vec4 out_tex;

layout (binding = 0) uniform block{
    mat4 model;
    mat4 view;
    mat4 projection;
} Block;

void main() {
    mat4 mvp = Block.projection * Block.view * Block.model;
    gl_Position = mvp * vec4(in_pos,0.0, 1.0);
    out_color = in_color;
    out_size = in_size;
    out_tex = in_tex;
}
