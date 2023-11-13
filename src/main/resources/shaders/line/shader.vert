#version 460 core

layout (location = 0) in vec2 in_pos;
layout (location = 1) in vec4 in_color;

layout (location = 0) out vec4 out_color;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;


void main() {
    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(in_pos,0.0, 1.0);
    out_color = in_color;
}
