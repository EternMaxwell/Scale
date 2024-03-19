#version 460 core

layout (location = 0) in vec3 in_pos;
layout (location = 1) in vec4 in_color;
layout (location = 2) in vec4 in_clip;

layout (location = 0) out vec4 out_color;

out gl_PerVertex {
    vec4 gl_Position;
    float gl_PointSize;
    float gl_ClipDistance[4];
};

layout (binding = 0) uniform block{
    mat4 model;
    mat4 view;
    mat4 projection;
} Block;

void main() {
    mat4 mvp = Block.projection * Block.view * Block.model;
    gl_Position = mvp * vec4(in_pos, 1.0);
    gl_ClipDistance[0] = in_pos.x - in_clip.x;
    gl_ClipDistance[1] = in_pos.y - in_clip.y;
    gl_ClipDistance[2] = in_clip.z - in_pos.x;
    gl_ClipDistance[3] = in_clip.w - in_pos.y;
    out_color = in_color;
}
