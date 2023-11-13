#version 460 core

layout (points) in;

layout (location = 0) in vec2 in_size[];
layout (location = 1) in vec4 in_color[];
layout (location = 2) in vec4 in_tex[];

layout (triangle_strip, max_vertices = 6) out;

layout (location = 0) out vec4 out_color;
layout (location = 1) out vec2 out_tex;

layout (binding = 0) uniform block{
    mat4 model;
    mat4 view;
    mat4 projection;
} Block;

void main() {
    mat4 mvp = Block.projection * Block.view * Block.model;
    vec4 pos = gl_in[0].gl_Position;

    float width = in_size[0].x;
    float height = in_size[0].y;

    out_color = in_color[0];
    gl_Position = mvp * vec4(pos.x, pos.y, pos.z, 1.0);
    out_tex = vec2(in_tex[0].x, in_tex[0].y);
    EmitVertex();
    out_color = in_color[0];
    gl_Position = mvp * vec4(pos.x + width, pos.y, pos.z, 1.0);
    out_tex = vec2(in_tex[0].z, in_tex[0].y);
    EmitVertex();
    out_color = in_color[0];
    gl_Position = mvp * vec4(pos.x + width, pos.y + height, pos.z, 1.0);
    out_tex = vec2(in_tex[0].z, in_tex[0].w);
    EmitVertex();
    EndPrimitive();

    out_color = in_color[0];
    gl_Position = mvp * vec4(pos.x, pos.y, pos.z, 1.0);
    out_tex = vec2(in_tex[0].x, in_tex[0].y);
    EmitVertex();
    out_color = in_color[0];
    gl_Position = mvp * vec4(pos.x + width, pos.y + height, pos.z, 1.0);
    out_tex = vec2(in_tex[0].z, in_tex[0].w);
    EmitVertex();
    out_color = in_color[0];
    gl_Position = mvp * vec4(pos.x, pos.y + height, pos.z, 1.0);
    out_tex = vec2(in_tex[0].x, in_tex[0].w);
    EmitVertex();
    EndPrimitive();
}
