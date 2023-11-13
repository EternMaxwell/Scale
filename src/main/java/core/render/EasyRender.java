package core.render;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.glfw.GLFW.*;

public class EasyRender {

    public final Line line;
    public class Line{
        public int vao;
        public int vbo;
        public int vertexShader;
        public int fragmentShader;
        public int shaderProgram;
        public ByteBuffer vertices;

        public int vertexCount;

        public Line() {
            vao = glGenVertexArrays();
            glBindVertexArray(vao);
            vbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            vertexShader = createShader("src/main/resources/shaders/line/shader.vert", GL_VERTEX_SHADER);
            fragmentShader = createShader("src/main/resources/shaders/line/shader.frag", GL_FRAGMENT_SHADER);
            shaderProgram = glCreateProgram();
            glAttachShader(shaderProgram, vertexShader);
            glAttachShader(shaderProgram, fragmentShader);
            glLinkProgram(shaderProgram);
            glUseProgram(shaderProgram);
            glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "model"), false, new Matrix4f().identity().get(new float[16]));
            glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "view"), false, new Matrix4f().identity().get(new float[16]));
            glUniformMatrix4fv(glGetUniformLocation(shaderProgram, "projection"), false, new Matrix4f().identity().get(new float[16]));
            vertices = MemoryUtil.memAlloc(1024 * 6 * 4);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 6 * Float.BYTES, 0);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 6 * Float.BYTES, 2 * Float.BYTES);
            glEnableVertexAttribArray(1);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
            glUseProgram(0);
        }

        public void setModelMatrix(Matrix4f modelMatrix){
            glProgramUniformMatrix4fv(shaderProgram, glGetUniformLocation(shaderProgram, "model"), false, modelMatrix.get(new float[16]));
        }

        public void setViewMatrix(Matrix4f viewMatrix){
            glProgramUniformMatrix4fv(shaderProgram, glGetUniformLocation(shaderProgram, "view"), false, viewMatrix.get(new float[16]));
        }

        public void setProjectionMatrix(Matrix4f projectionMatrix){
            glProgramUniformMatrix4fv(shaderProgram, glGetUniformLocation(shaderProgram, "projection"), false, projectionMatrix.get(new float[16]));
        }

        /**
         * begin the line renderer
         */
        public void begin(){
            vertices.clear();
            vertexCount = 0;
        }

        public void drawLine(float x1, float y1, float x2, float y2, float r, float g, float b, float a){
            if(vertices.remaining() < 6 * 4 * 2){
                flush();
            }
            vertices.putFloat(x1);
            vertices.putFloat(y1);
            vertices.putFloat(r);
            vertices.putFloat(g);
            vertices.putFloat(b);
            vertices.putFloat(a);
            vertices.putFloat(x2);
            vertices.putFloat(y2);
            vertices.putFloat(r);
            vertices.putFloat(g);
            vertices.putFloat(b);
            vertices.putFloat(a);
            vertexCount += 2;
        }

        /**
         * flush the data to the gpu and render the lines
         */
        public void flush(){
            vertices.flip();
            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
            glUseProgram(shaderProgram);
            glDrawArrays(GL_LINES, 0, vertexCount);
            glBindVertexArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glUseProgram(0);
            vertices.clear();
        }

        /**
         * end the line renderer
         */
        public void end(){
            flush();
        }

        /**
         * dispose the line renderer and free the memory
         */
        public void dispose() {
            glDeleteProgram(shaderProgram);
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);
            glDeleteBuffers(vbo);
            glDeleteVertexArrays(vao);
        }
    }

    public EasyRender() {
        line = new Line();
    }

    public void begin() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        line.begin();
    }

    public void end(Window window) {
        line.end();
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
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 6 * Float.BYTES, 2 * Float.BYTES);
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
        glCompileShader(shader);
        int status = glGetShaderi(shader, GL_COMPILE_STATUS);
        if (status == GL_FALSE) {
            String error = glGetShaderInfoLog(shader);
            throw new RuntimeException(error);
        }
        return shader;
    }
}
