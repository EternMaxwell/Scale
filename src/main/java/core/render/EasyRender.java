package core.render;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.glfw.GLFW.*;

public class EasyRender {

    public void begin() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public void end(Window window) {
        glfwSwapBuffers(window.id());
    }

    public void drawTriangleTest(){
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        int vertexShader = createShader("src/test/resources/shaders/triangle/shader.vert", GL_VERTEX_SHADER);
        int fragmentShader = createShader("src/test/resources/shaders/triangle/shader.frag", GL_FRAGMENT_SHADER);
        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        glUseProgram(shaderProgram);

        float[] vertices = {
                0.0f, 0.5f, 1, 0, 0, 1,
                -0.5f, -0.5f, 0, 1, 0, 1,
                0.5f, -0.5f, 0,  0, 1, 1
        };
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        glUseProgram(0);
        glDeleteProgram(shaderProgram);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
    }

    public int createShader(String shaderSourceFileLoc, int shaderType) {
        int shader = glCreateShader(shaderType);
        //get the shader source
        String shaderSource = "";
        try {
            shaderSource = new String(Files.readAllBytes(Paths.get(shaderSourceFileLoc)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        glShaderSource(shader, shaderSource);
//        int status = glGetShaderi(shader, GL_COMPILE_STATUS);
//        if (status != GL_TRUE) {
//            String error = glGetShaderInfoLog(shader);
//            throw new RuntimeException(error);
//        }
        glCompileShader(shader);
        return shader;
    }
}
