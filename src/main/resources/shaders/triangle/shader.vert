#version 460 core

layout (location = 0) in vec3 in_pos;
layout (location = 1) in vec4 in_color;

layout (location = 0) out vec4 out_color;

layout (binding = 0) uniform block{
    mat4 model;
    mat4 view;
    mat4 projection;
} Block;

void main() {
    mat4 mvp = Block.projection * Block.view * Block.model;
    gl_Position = mvp * vec4(in_pos, 1.0);
    out_color = in_color;
}
