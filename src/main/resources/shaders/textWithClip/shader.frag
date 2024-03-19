#version 460 core

layout (location = 0) in vec4 color;
layout (location = 1) in vec2 texCoord;

layout (location = 0) out vec4 FragColor;

layout (binding = 1) uniform sampler2D tex;

void main() {
    vec4 texColor = texture(tex, texCoord);
    FragColor = color * texColor;
}
