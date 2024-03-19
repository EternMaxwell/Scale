#version 460 core

layout (points) in;

layout (location = 0) in vec4 in_color[];
layout (location = 1) in vec4 in_clip[];

layout (triangle_strip, max_vertices = 6) out;

out gl_PerVertex {
    vec4 gl_Position;
    float gl_PointSize;
    float gl_ClipDistance[4];
};

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
    vec4 vertexPos;
    float halfSize = size / 2.0;

    out_color = in_color[0];
    vertexPos = vec4(pos.x - halfSize, pos.y - halfSize, pos.z, 1.0);
    gl_ClipDistance[0] = vertexPos.x - in_clip[0].x;
    gl_ClipDistance[1] = vertexPos.y - in_clip[0].y;
    gl_ClipDistance[2] = in_clip[0].z - vertexPos.x;
    gl_ClipDistance[3] = in_clip[0].w - vertexPos.y;
    gl_Position = mvp * vertexPos;
    EmitVertex();

    out_color = in_color[0];
    vertexPos = vec4(pos.x + halfSize, pos.y - halfSize, pos.z, 1.0);
    gl_ClipDistance[0] = vertexPos.x - in_clip[0].x;
    gl_ClipDistance[1] = vertexPos.y - in_clip[0].y;
    gl_ClipDistance[2] = in_clip[0].z - vertexPos.x;
    gl_ClipDistance[3] = in_clip[0].w - vertexPos.y;
    gl_Position = mvp * vertexPos;
    EmitVertex();

    out_color = in_color[0];
    vertexPos = vec4(pos.x + halfSize, pos.y + halfSize, pos.z, 1.0);
    gl_ClipDistance[0] = vertexPos.x - in_clip[0].x;
    gl_ClipDistance[1] = vertexPos.y - in_clip[0].y;
    gl_ClipDistance[2] = in_clip[0].z - vertexPos.x;
    gl_ClipDistance[3] = in_clip[0].w - vertexPos.y;
    gl_Position = mvp * vertexPos;
    EmitVertex();

    EndPrimitive();

    out_color = in_color[0];
    vertexPos = vec4(pos.x - halfSize, pos.y - halfSize, pos.z, 1.0);
    gl_ClipDistance[0] = vertexPos.x - in_clip[0].x;
    gl_ClipDistance[1] = vertexPos.y - in_clip[0].y;
    gl_ClipDistance[2] = in_clip[0].z - vertexPos.x;
    gl_ClipDistance[3] = in_clip[0].w - vertexPos.y;
    gl_Position = mvp * vertexPos;
    EmitVertex();

    out_color = in_color[0];
    vertexPos = vec4(pos.x + halfSize, pos.y + halfSize, pos.z, 1.0);
    gl_ClipDistance[0] = vertexPos.x - in_clip[0].x;
    gl_ClipDistance[1] = vertexPos.y - in_clip[0].y;
    gl_ClipDistance[2] = in_clip[0].z - vertexPos.x;
    gl_ClipDistance[3] = in_clip[0].w - vertexPos.y;
    gl_Position = mvp * vertexPos;
    EmitVertex();

    out_color = in_color[0];
    vertexPos = vec4(pos.x - halfSize, pos.y + halfSize, pos.z, 1.0);
    gl_ClipDistance[0] = vertexPos.x - in_clip[0].x;
    gl_ClipDistance[1] = vertexPos.y - in_clip[0].y;
    gl_ClipDistance[2] = in_clip[0].z - vertexPos.x;
    gl_ClipDistance[3] = in_clip[0].w - vertexPos.y;
    gl_Position = mvp * vertexPos;
    EmitVertex();
    EndPrimitive();
}
