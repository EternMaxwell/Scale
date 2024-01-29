#version 460 core

layout (location = 0) in vec4 color;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec2 pos;
layout (location = 3) in vec2 size;
layout (location = 4) in vec2 texSize;

layout (location = 0) out vec4 fragColor;
layout (location = 1) out vec2 fragTexCoord;
layout (location = 2) out vec2 fragPos;
layout (location = 3) out vec2 fragSize;
layout (location = 4) out vec2 fragTexSize;

layout (binding = 0) uniform block{
    mat4 model;
    mat4 view;
    mat4 projection;
} Block;

void main() {
    gl_Position = vec4(pos, 0.0, 1.0);
    fragColor = color;
    fragTexCoord = texCoord;
    fragPos = pos;
    fragSize = size;
    fragTexSize = texSize;
}
