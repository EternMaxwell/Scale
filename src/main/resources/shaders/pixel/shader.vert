#version 460 core

layout (location = 0) in vec2 in_pos;
layout (location = 1) in vec4 in_color;

layout (location = 0) out vec4 out_color;

uniform float size;

layout (binding = 0) uniform block{
    mat4 model;
    mat4 view;
    mat4 projection;
} Block;

void main() {
    gl_Position = vec4(in_pos,0.0, 1.0);
    out_color = in_color;
}
