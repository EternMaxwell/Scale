#version 460 core

layout (points) in;

layout (location = 0) in vec4 color[];
layout (location = 1) in vec2 texCoord[];
layout (location = 2) in vec2 pos[];
layout (location = 3) in vec2 size[];
layout (location = 4) in vec2 texSize[];

layout (triangle_strip, max_vertices = 6) out;

layout (location = 0) out vec4 fColor;
layout (location = 1) out vec2 fTexCoord;

layout (binding = 0) uniform block{
    mat4 model;
    mat4 view;
    mat4 projection;
} Block;

void main() {
    mat4 mvp = Block.projection * Block.view * Block.model;

    gl_Position = mvp * vec4(pos[0], 0.0, 1.0);
    fColor = color[0];
    fTexCoord = texCoord[0];
    EmitVertex();

    gl_Position = mvp * vec4(pos[0].x + size[0].x, pos[0].y, 0.0, 1.0);
    fColor = color[0];
    fTexCoord = vec2(texCoord[0].x + texSize[0].x, texCoord[0].y);
    EmitVertex();

    gl_Position = mvp * vec4(pos[0].x, pos[0].y + size[0].y, 0.0, 1.0);
    fColor = color[0];
    fTexCoord = vec2(texCoord[0].x, texCoord[0].y + texSize[0].y);
    EmitVertex();
    EndPrimitive();

    gl_Position = mvp * vec4(pos[0].x + size[0].x, pos[0].y, 0.0, 1.0);
    fColor = color[0];
    fTexCoord = vec2(texCoord[0].x + texSize[0].x, texCoord[0].y);
    EmitVertex();

    gl_Position = mvp * vec4(pos[0].x + size[0].x, pos[0].y + size[0].y, 0.0, 1.0);
    fColor = color[0];
    fTexCoord = vec2(texCoord[0].x + texSize[0].x, texCoord[0].y + texSize[0].y);
    EmitVertex();

    gl_Position = mvp * vec4(pos[0].x, pos[0].y + size[0].y, 0.0, 1.0);
    fColor = color[0];
    fTexCoord = vec2(texCoord[0].x, texCoord[0].y + texSize[0].y);
    EmitVertex();
    EndPrimitive();
}
