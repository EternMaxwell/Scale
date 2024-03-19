#version 460 core

layout (points) in;

layout (location = 0) in vec4 in_color[];

layout (triangle_strip, max_vertices = 6) out;

layout (location = 0) out vec4 out_color;

uniform float size;

layout (binding = 0) uniform block{
    mat4 model;
    mat4 view;
    mat4 projection;
} Block;

void main() {
    mat4 mvp = Block.projection * Block.view * Block.model;
    vec4 pos = gl_in[0].gl_Position;
    float halfSize = size / 2.0;

    out_color = in_color[0];
    gl_Position = mvp * vec4(pos.x - halfSize, pos.y - halfSize, pos.z, 1.0);
    EmitVertex();
    out_color = in_color[0];
    gl_Position = mvp * vec4(pos.x + halfSize, pos.y - halfSize, pos.z, 1.0);
    EmitVertex();
    out_color = in_color[0];
    gl_Position = mvp * vec4(pos.x + halfSize, pos.y + halfSize, pos.z, 1.0);
    EmitVertex();
    EndPrimitive();

    out_color = in_color[0];
    gl_Position = mvp * vec4(pos.x - halfSize, pos.y - halfSize, pos.z, 1.0);
    EmitVertex();
    out_color = in_color[0];
    gl_Position = mvp * vec4(pos.x + halfSize, pos.y + halfSize, pos.z, 1.0);
    EmitVertex();
    out_color = in_color[0];
    gl_Position = mvp * vec4(pos.x - halfSize, pos.y + halfSize, pos.z, 1.0);
    EmitVertex();
    EndPrimitive();
}
