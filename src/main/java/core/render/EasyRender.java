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

        public int uniformBuffer;
        public FloatBuffer uniformData;

        public int vertexCount;

        public Line() {
            //====CREATE THE VAO AND VBO====//
            vao = glGenVertexArrays();
            vbo = glGenBuffers();
            vertices = MemoryUtil.memAlloc(1024 * 6 * 4);

            //====CREATE THE UNIFORM BUFFER====//
            uniformBuffer = glGenBuffers();
            uniformData = MemoryUtil.memAllocFloat(16);
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            glBufferData(GL_UNIFORM_BUFFER, 48 * Float.BYTES, GL_DYNAMIC_DRAW);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);

            //====INITIALIZE THE MATRICES====//
            setModelMatrix(new Matrix4f().identity());
            setViewMatrix(new Matrix4f().identity());
            setProjectionMatrix(new Matrix4f().identity());

            //====CREATE THE SHADER PROGRAM====//
            vertexShader = createShader("src/main/resources/shaders/line/shader.vert", GL_VERTEX_SHADER);
            fragmentShader = createShader("src/main/resources/shaders/line/shader.frag", GL_FRAGMENT_SHADER);
            shaderProgram = glCreateProgram();
            glAttachShader(shaderProgram, vertexShader);
            glAttachShader(shaderProgram, fragmentShader);
            glLinkProgram(shaderProgram);
            glUseProgram(shaderProgram);

            //====SPECIFY THE VERTEX DATA====//
            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, vertices.capacity(), GL_DYNAMIC_DRAW);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 6 * Float.BYTES, 0);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 6 * Float.BYTES, 2 * Float.BYTES);
            glEnableVertexAttribArray(1);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
            glUseProgram(0);
        }

        public void setModelMatrix(Matrix4f modelMatrix){
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(modelMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 0, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        public void setViewMatrix(Matrix4f viewMatrix){
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(viewMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 16 * Float.BYTES, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        public void setProjectionMatrix(Matrix4f projectionMatrix){
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(projectionMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 32 * Float.BYTES, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
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
            //====FLIP THE BUFFER====//
            vertices.flip();

            //====BIND THE VAO AND VBO====//
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBindVertexArray(vao);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
            glBindBufferBase(GL_UNIFORM_BUFFER, 0, uniformBuffer);

            //====RENDER THE LINES====//
            glUseProgram(shaderProgram);
            glDrawArrays(GL_LINES, 0, vertexCount);
            glBindVertexArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glUseProgram(0);

            //====CLEAR THE BUFFER====//
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
            glDeleteBuffers(uniformBuffer);
            MemoryUtil.memFree(vertices);
            MemoryUtil.memFree(uniformData);
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

    public void dispose(){
        line.dispose();
    }
}
