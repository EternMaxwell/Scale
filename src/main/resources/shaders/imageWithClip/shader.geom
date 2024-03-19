#version 460 core

layout (points) in;

layout (location = 0) in vec2 in_size[];
layout (location = 1) in vec4 in_color[];
layout (location = 2) in vec4 in_tex[];
layout (location = 3) in vec4 in_clip[];

layout (triangle_strip, max_vertices = 6) out;
out gl_PerVertex {
    vec4 gl_Position;
    float gl_ClipDistance[4];
};

layout (location = 0) out vec4 out_color;
layout (location = 1) out vec2 out_tex;

layout (binding = 0) uniform block{
    mat4 model;
    mat4 view;
    mat4 projection;
} Block;

struct block2 {
    float x1;
    float x2;
    float y1;
    float y2;
};

void main() {
    mat4 mvp = Block.projection * Block.view * Block.model;
    vec4 pos = gl_in[0].gl_Position;
    vec4 vertexPos;
    block2 Clip;
    Clip.x1 = in_clip[0].x;
    Clip.y1 = in_clip[0].y;
    Clip.x2 = in_clip[0].z;
    Clip.y2 = in_clip[0].w;

    float width = in_size[0].x;
    float height = in_size[0].y;

    out_color = in_color[0];
    vertexPos = vec4(pos.x, pos.y, pos.z, 1.0);
    gl_ClipDistance[0] = vertexPos.x - Clip.x1;
    gl_ClipDistance[1] = Clip.x2 - vertexPos.x;
    gl_ClipDistance[2] = vertexPos.y - Clip.y1;
    gl_ClipDistance[3] = Clip.y2 - vertexPos.y;
    gl_Position = mvp * vertexPos;
    out_tex = vec2(in_tex[0].x, in_tex[0].y);
    EmitVertex();

    out_color = in_color[0];
    vertexPos = vec4(pos.x + width, pos.y, pos.z, 1.0);
    gl_ClipDistance[0] = vertexPos.x - Clip.x1;
    gl_ClipDistance[1] = Clip.x2 - vertexPos.x;
    gl_ClipDistance[2] = vertexPos.y - Clip.y1;
    gl_ClipDistance[3] = Clip.y2 - vertexPos.y;
    gl_Position = mvp * vertexPos;
    out_tex = vec2(in_tex[0].z, in_tex[0].y);
    EmitVertex();

    out_color = in_color[0];
    vertexPos = vec4(pos.x + width, pos.y + height, pos.z, 1.0);
    gl_ClipDistance[0] = vertexPos.x - Clip.x1;
    gl_ClipDistance[1] = Clip.x2 - vertexPos.x;
    gl_ClipDistance[2] = vertexPos.y - Clip.y1;
    gl_ClipDistance[3] = Clip.y2 - vertexPos.y;
    gl_Position = mvp * vertexPos;
    out_tex = vec2(in_tex[0].z, in_tex[0].w);
    EmitVertex();

    EndPrimitive();

    out_color = in_color[0];
    vertexPos = vec4(pos.x, pos.y, pos.z, 1.0);
    gl_ClipDistance[0] = vertexPos.x - Clip.x1;
    gl_ClipDistance[1] = Clip.x2 - vertexPos.x;
    gl_ClipDistance[2] = vertexPos.y - Clip.y1;
    gl_ClipDistance[3] = Clip.y2 - vertexPos.y;
    gl_Position = mvp * vertexPos;
    out_tex = vec2(in_tex[0].x, in_tex[0].y);
    EmitVertex();

    out_color = in_color[0];
    vertexPos = vec4(pos.x + width, pos.y + height, pos.z, 1.0);
    gl_ClipDistance[0] = vertexPos.x - Clip.x1;
    gl_ClipDistance[1] = Clip.x2 - vertexPos.x;
    gl_ClipDistance[2] = vertexPos.y - Clip.y1;
    gl_ClipDistance[3] = Clip.y2 - vertexPos.y;
    gl_Position = mvp * vertexPos;
    out_tex = vec2(in_tex[0].z, in_tex[0].w);
    EmitVertex();

    out_color = in_color[0];
    vertexPos = vec4(pos.x, pos.y + height, pos.z, 1.0);
    gl_ClipDistance[0] = vertexPos.x - Clip.x1;
    gl_ClipDistance[1] = Clip.x2 - vertexPos.x;
    gl_ClipDistance[2] = vertexPos.y - Clip.y1;
    gl_ClipDistance[3] = Clip.y2 - vertexPos.y;
    gl_Position = mvp * vertexPos;
    out_tex = vec2(in_tex[0].x, in_tex[0].w);
    EmitVertex();

    EndPrimitive();
}
