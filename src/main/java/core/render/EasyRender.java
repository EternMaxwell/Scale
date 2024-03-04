package core.render;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.glfw.GLFW.*;

public class EasyRender {

    public final Window window;
    public final Line line;
    public final Triangle triangle;
    public final Pixel pixel;
    public final Image image;
    public final Point point;
    public final Text text;

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
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 7 * Float.BYTES, 0);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 7 * Float.BYTES, 3 * Float.BYTES);
            glEnableVertexAttribArray(1);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
            glUseProgram(0);
        }

        /**
         * set the model matrix
         * @param modelMatrix the model matrix
         */
        public void setModelMatrix(Matrix4f modelMatrix){
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(modelMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 0, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        /**
         * set the view matrix
         * @param viewMatrix the view matrix
         */
        public void setViewMatrix(Matrix4f viewMatrix){
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(viewMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 16 * Float.BYTES, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        /**
         * set the projection matrix
         * @param projectionMatrix the projection matrix
         */
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

        /**
         * draw a line with 2 2d points and 1 color
         * @param x1 the x position of the first point
         * @param y1 the y position of the first point
         * @param x2 the x position of the second point
         * @param y2 the y position of the second point
         * @param r the red value of the line
         * @param g the green value of the line
         * @param b the blue value of the line
         * @param a the alpha value of the line
         */
        public void drawLine2D(float x1, float y1, float x2, float y2, float r, float g, float b, float a){
            if(vertices.remaining() < 7 * 4 * 2){
                flush();
            }
            vertices.putFloat(x1);
            vertices.putFloat(y1);
            vertices.putFloat(0);
            vertices.putFloat(r);
            vertices.putFloat(g);
            vertices.putFloat(b);
            vertices.putFloat(a);
            vertices.putFloat(x2);
            vertices.putFloat(y2);
            vertices.putFloat(0);
            vertices.putFloat(r);
            vertices.putFloat(g);
            vertices.putFloat(b);
            vertices.putFloat(a);
            vertexCount += 2;
        }

        /**
         * draw a line with 2 3d points and 1 color
         * @param x1 the x position of the first point
         * @param y1 the y position of the first point
         * @param z1 the z position of the first point
         * @param x2 the x position of the second point
         * @param y2 the y position of the second point
         * @param z2 the z position of the second point
         * @param r the red value of the line
         * @param g the green value of the line
         * @param b the blue value of the line
         * @param a the alpha value of the line
         */
        public void drawLine3D(float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a){
            if(vertices.remaining() < 7 * 4 * 2){
                flush();
            }
            vertices.putFloat(x1);
            vertices.putFloat(y1);
            vertices.putFloat(z1);
            vertices.putFloat(r);
            vertices.putFloat(g);
            vertices.putFloat(b);
            vertices.putFloat(a);
            vertices.putFloat(x2);
            vertices.putFloat(y2);
            vertices.putFloat(z2);
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
            vertexCount = 0;
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

    public class Triangle{
        public int vao;
        public int vbo;
        public int vertexShader;
        public int fragmentShader;
        public int shaderProgram;
        public ByteBuffer vertices;

        public int uniformBuffer;
        public FloatBuffer uniformData;

        public int vertexCount;

        public Triangle() {
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
            vertexShader = createShader("src/main/resources/shaders/triangle/shader.vert", GL_VERTEX_SHADER);
            fragmentShader = createShader("src/main/resources/shaders/triangle/shader.frag", GL_FRAGMENT_SHADER);
            shaderProgram = glCreateProgram();
            glAttachShader(shaderProgram, vertexShader);
            glAttachShader(shaderProgram, fragmentShader);
            glLinkProgram(shaderProgram);
            glUseProgram(shaderProgram);

            //====SPECIFY THE VERTEX DATA====//
            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, vertices.capacity(), GL_DYNAMIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 7 * Float.BYTES, 0);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 7 * Float.BYTES, 3 * Float.BYTES);
            glEnableVertexAttribArray(1);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
            glUseProgram(0);
        }

        /**
         * set the model matrix
         * @param modelMatrix the model matrix
         */
        public void setModelMatrix(Matrix4f modelMatrix){
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(modelMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 0, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        /**
         * set the view matrix
         * @param viewMatrix the view matrix
         */
        public void setViewMatrix(Matrix4f viewMatrix){
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(viewMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 16 * Float.BYTES, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        /**
         * set the projection matrix
         * @param projectionMatrix the projection matrix
         */
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

        /**
         * draw a triangle with 3 points and 1 color
         * @param x1 the x position of the first point
         * @param y1 the y position of the first point
         * @param x2 the x position of the second point
         * @param y2 the y position of the second point
         * @param x3 the x position of the third point
         * @param y3 the y position of the third point
         * @param r the red value of the triangle
         * @param g the green value of the triangle
         * @param b the blue value of the triangle
         * @param a the alpha value of the triangle
         */
        public void drawTriangle2D(float x1, float y1, float x2, float y2, float x3, float y3, float r, float g, float b, float a){
            if(vertices.remaining() < 7 * 4 * 3){
                flush();
            }
            vertices.putFloat(x1);
            vertices.putFloat(y1);
            vertices.putFloat(0);
            vertices.putFloat(r);
            vertices.putFloat(g);
            vertices.putFloat(b);
            vertices.putFloat(a);
            vertices.putFloat(x2);
            vertices.putFloat(y2);
            vertices.putFloat(0);
            vertices.putFloat(r);
            vertices.putFloat(g);
            vertices.putFloat(b);
            vertices.putFloat(a);
            vertices.putFloat(x3);
            vertices.putFloat(y3);
            vertices.putFloat(0);
            vertices.putFloat(r);
            vertices.putFloat(g);
            vertices.putFloat(b);
            vertices.putFloat(a);
            vertexCount += 3;
        }

        /**
         * draw a triangle with 3 points and 3 colors
         * @param x1 the x position of the first point
         * @param y1 the y position of the first point
         * @param x2 the x position of the second point
         * @param y2 the y position of the second point
         * @param x3 the x position of the third point
         * @param y3 the y position of the third point
         * @param r1 the red value of the first point
         * @param g1 the green value of the first point
         * @param b1 the blue value of the first point
         * @param a1 the alpha value of the first point
         * @param r2 the red value of the second point
         * @param g2 the green value of the second point
         * @param b2 the blue value of the second point
         * @param a2 the alpha value of the second point
         * @param r3 the red value of the third point
         * @param g3 the green value of the third point
         * @param b3 the blue value of the third point
         * @param a3 the alpha value of the third point
         */
        public void drawTriangle2D(float x1, float y1, float x2, float y2, float x3, float y3, float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2, float r3, float g3, float b3, float a3){
            if(vertices.remaining() < 7 * 4 * 3){
                flush();
            }
            vertices.putFloat(x1);
            vertices.putFloat(y1);
            vertices.putFloat(0);
            vertices.putFloat(r1);
            vertices.putFloat(g1);
            vertices.putFloat(b1);
            vertices.putFloat(a1);
            vertices.putFloat(x2);
            vertices.putFloat(y2);
            vertices.putFloat(0);
            vertices.putFloat(r2);
            vertices.putFloat(g2);
            vertices.putFloat(b2);
            vertices.putFloat(a2);
            vertices.putFloat(x3);
            vertices.putFloat(y3);
            vertices.putFloat(0);
            vertices.putFloat(r3);
            vertices.putFloat(g3);
            vertices.putFloat(b3);
            vertices.putFloat(a3);
            vertexCount += 3;
        }

        /**
         * draw a triangle with 3 points and 1 color
         * @param x1 the x position of the first point
         * @param y1 the y position of the first point
         * @param z1 the z position of the first point
         * @param x2 the x position of the second point
         * @param y2 the y position of the second point
         * @param z2 the z position of the second point
         * @param x3 the x position of the third point
         * @param y3 the y position of the third point
         * @param z3 the z position of the third point
         * @param r the red value of the triangle
         * @param g the green value of the triangle
         * @param b the blue value of the triangle
         * @param a the alpha value of the triangle
         */
        public void drawTriangle3D(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float r, float g, float b, float a) {
            if (vertices.remaining() < 7 * 4 * 3) {
                flush();
            }
            vertices.putFloat(x1);
            vertices.putFloat(y1);
            vertices.putFloat(z1);
            vertices.putFloat(r);
            vertices.putFloat(g);
            vertices.putFloat(b);
            vertices.putFloat(a);
            vertices.putFloat(x2);
            vertices.putFloat(y2);
            vertices.putFloat(z2);
            vertices.putFloat(r);
            vertices.putFloat(g);
            vertices.putFloat(b);
            vertices.putFloat(a);
            vertices.putFloat(x3);
            vertices.putFloat(y3);
            vertices.putFloat(z3);
            vertices.putFloat(r);
            vertices.putFloat(g);
            vertices.putFloat(b);
            vertices.putFloat(a);
            vertexCount += 3;
        }

        /**
         * draw a triangle with 3 points and 3 colors
         * @param x1 the x position of the first point
         * @param y1 the y position of the first point
         * @param z1 the z position of the first point
         * @param x2 the x position of the second point
         * @param y2 the y position of the second point
         * @param z2 the z position of the second point
         * @param x3 the x position of the third point
         * @param y3 the y position of the third point
         * @param z3 the z position of the third point
         * @param r1 the red value of the first point
         * @param g1 the green value of the first point
         * @param b1 the blue value of the first point
         * @param a1 the alpha value of the first point
         * @param r2 the red value of the second point
         * @param g2 the green value of the second point
         * @param b2 the blue value of the second point
         * @param a2 the alpha value of the second point
         * @param r3 the red value of the third point
         * @param g3 the green value of the third point
         * @param b3 the blue value of the third point
         * @param a3 the alpha value of the third point
         */
        public void drawTriangle3D(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2, float r3, float g3, float b3, float a3) {
            if (vertices.remaining() < 7 * 4 * 3) {
                flush();
            }
            vertices.putFloat(x1);
            vertices.putFloat(y1);
            vertices.putFloat(z1);
            vertices.putFloat(r1);
            vertices.putFloat(g1);
            vertices.putFloat(b1);
            vertices.putFloat(a1);
            vertices.putFloat(x2);
            vertices.putFloat(y2);
            vertices.putFloat(z2);
            vertices.putFloat(r2);
            vertices.putFloat(g2);
            vertices.putFloat(b2);
            vertices.putFloat(a2);
            vertices.putFloat(x3);
            vertices.putFloat(y3);
            vertices.putFloat(z3);
            vertices.putFloat(r3);
            vertices.putFloat(g3);
            vertices.putFloat(b3);
            vertices.putFloat(a3);
            vertexCount += 3;
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
            glDrawArrays(GL_TRIANGLES, 0, vertexCount);
            glBindVertexArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glUseProgram(0);

            //====CLEAR THE BUFFER====//
            vertices.clear();
            vertexCount = 0;
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

    public class Pixel{
        public int vao;
        public int vbo;
        public int vertexShader;
        public int geometryShader;
        public int fragmentShader;
        public int shaderProgram;
        public ByteBuffer vertices;

        public int uniformBuffer;
        public FloatBuffer uniformData;

        public int vertexCount;

        public Pixel() {
            //====CREATE THE VAO AND VBO====//
            vao = glGenVertexArrays();
            vbo = glGenBuffers();
            //vertices = MemoryUtil.memAlloc(256 * 1024 * 6 * 4);

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
            vertexShader = createShader("src/main/resources/shaders/pixel/shader.vert", GL_VERTEX_SHADER);
            geometryShader = createShader("src/main/resources/shaders/pixel/shader.geom", GL_GEOMETRY_SHADER);
            fragmentShader = createShader("src/main/resources/shaders/pixel/shader.frag", GL_FRAGMENT_SHADER);
            shaderProgram = glCreateProgram();
            glAttachShader(shaderProgram, vertexShader);
            glAttachShader(shaderProgram, geometryShader);
            glAttachShader(shaderProgram, fragmentShader);
            glLinkProgram(shaderProgram);
            glUseProgram(shaderProgram);

            //====SPECIFY THE VERTEX DATA====//
            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, 1024 * 1024 * 6 * 4, GL_DYNAMIC_DRAW);
            vertices = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, 256 * 1024 * 6 * 4, vertices);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 6 * Float.BYTES, 0);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 6 * Float.BYTES, 2 * Float.BYTES);
            glEnableVertexAttribArray(1);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
            glUseProgram(0);
        }

        /**
         * set the size of the pixels
         * @param size the size of the pixels
         */
        public void setPixelSize(float size){
            glUseProgram(shaderProgram);
            glUniform1f(glGetUniformLocation(shaderProgram, "size"), size);
            glUseProgram(0);
        }

        /**
         * set the model matrix
         * @param modelMatrix the model matrix
         */
        public void setModelMatrix(Matrix4f modelMatrix){
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(modelMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 0, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        /**
         * set the view matrix
         * @param viewMatrix the view matrix
         */
        public void setViewMatrix(Matrix4f viewMatrix){
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(viewMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 16 * Float.BYTES, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        /**
         * set the projection matrix
         * @param projectionMatrix the projection matrix
         */
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

        public void drawPixel(float x, float y, float r, float g, float b, float a){
            if(vertices.remaining() < 6 * 4){
                flush();
            }
            vertices.putFloat(x);
            vertices.putFloat(y);
            vertices.putFloat(r);
            vertices.putFloat(g);
            vertices.putFloat(b);
            vertices.putFloat(a);
            vertexCount += 1;
        }

        /**
         * flush the data to the gpu and render the lines
         */
        public void flush(){
            //====FLIP THE BUFFER====//
            vertices.flip();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glUnmapBuffer(GL_ARRAY_BUFFER);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            //====BIND THE VAO AND VBO====//
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBindVertexArray(vao);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
            glBindBufferBase(GL_UNIFORM_BUFFER, 0, uniformBuffer);

            //====RENDER THE LINES====//
            glUseProgram(shaderProgram);
            glDrawArrays(GL_POINTS, 0, vertexCount);
            vertices = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, 1024 * 1024 * 6 * 4, vertices);

            //====UNBIND====//
            glBindVertexArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBufferBase(GL_UNIFORM_BUFFER, 0, 0);
            glUseProgram(0);

            //====CLEAR THE BUFFER====//
            vertices.clear();
            vertexCount = 0;
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
            glDeleteShader(geometryShader);
            glDeleteShader(fragmentShader);
            glDeleteBuffers(vbo);
            glDeleteVertexArrays(vao);
            glDeleteBuffers(uniformBuffer);
            //MemoryUtil.memFree(vertices);
            MemoryUtil.memFree(uniformData);
        }
    }

    public class Image{
        public int vao;
        public int vbo;
        public int vertexShader;
        public int geometryShader;
        public int fragmentShader;
        public int shaderProgram;
        public int texture;
        public int sampler;
        public ByteBuffer vertices;

        public int uniformBuffer;
        public FloatBuffer uniformData;

        public int vertexCount;

        public Image() {
            //====CREATE THE VAO AND VBO====//
            vao = glGenVertexArrays();
            vbo = glGenBuffers();
            vertices = MemoryUtil.memAlloc(512 * 12 * 4);

            //====CREATE THE UNIFORM BUFFER====//
            uniformBuffer = glGenBuffers();
            uniformData = MemoryUtil.memAllocFloat(16);
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            glBufferData(GL_UNIFORM_BUFFER, 48 * Float.BYTES, GL_DYNAMIC_DRAW);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);

            //====CREATE THE TEXTURE====//
            texture = glGenTextures();
            sampler = glGenSamplers();
            glSamplerParameteri(sampler, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
            glSamplerParameteri(sampler, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
            glSamplerParameteri(sampler, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glSamplerParameteri(sampler, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            //====INITIALIZE THE MATRICES====//
            setModelMatrix(new Matrix4f().identity());
            setViewMatrix(new Matrix4f().identity());
            setProjectionMatrix(new Matrix4f().identity());

            //====CREATE THE SHADER PROGRAM====//
            vertexShader = createShader("src/main/resources/shaders/image/shader.vert", GL_VERTEX_SHADER);
            geometryShader = createShader("src/main/resources/shaders/image/shader.geom", GL_GEOMETRY_SHADER);
            fragmentShader = createShader("src/main/resources/shaders/image/shader.frag", GL_FRAGMENT_SHADER);
            shaderProgram = glCreateProgram();
            glAttachShader(shaderProgram, vertexShader);
            glAttachShader(shaderProgram, geometryShader);
            glAttachShader(shaderProgram, fragmentShader);
            glLinkProgram(shaderProgram);
            glUseProgram(shaderProgram);

            //====SPECIFY THE VERTEX DATA====//
            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, vertices.capacity(), GL_DYNAMIC_DRAW);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 12 * Float.BYTES, 0);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 12 * Float.BYTES, 2 * Float.BYTES);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(2, 4, GL_FLOAT, false, 12 * Float.BYTES, 4 * Float.BYTES);
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(3, 4, GL_FLOAT, false, 12 * Float.BYTES, 8 * Float.BYTES);
            glEnableVertexAttribArray(3);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
            glUseProgram(0);
        }

        /**
         * set the model matrix
         * @param modelMatrix the model matrix
         */
        public void setModelMatrix(Matrix4f modelMatrix){
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(modelMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 0, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        /**
         * set the view matrix
         * @param viewMatrix the view matrix
         */
        public void setViewMatrix(Matrix4f viewMatrix){
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(viewMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 16 * Float.BYTES, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        /**
         * set the projection matrix
         * @param projectionMatrix the projection matrix
         */
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
        public void begin() {
            vertices.clear();
            vertexCount = 0;
        }

        /**
         * draw a image with the given sampler
         * @param x the x position of the image
         * @param y the y position of the image
         * @param width the width of the image
         * @param height the height of the image
         * @param r the red value of the image
         * @param g the green value of the image
         * @param b the blue value of the image
         * @param a the alpha value of the image
         * @param s1 the s1 value of the image
         * @param t1 the t1 value of the image
         * @param s2 the s2 value of the image
         * @param t2 the t2 value of the image
         * @param image the image to draw
         * @param sampler the sampler to use
         */
        public void drawImage(float x, float y, float width, float height, float r, float g, float b, float a, float s1, float t1, float s2, float t2, BufferedImage image, int sampler){
            flush();
            ByteBuffer buffer = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
            for (int j = image.getHeight()-1; j >=0; j--) {
                for (int i = 0; i < image.getWidth(); i++) {
                    buffer.put((byte) (image.getRGB(i, j) >> 16 & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) >> 8 & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) >> 24 & 0xFF));
                }
            }
            buffer.flip();
            glBindTexture(GL_TEXTURE_2D, texture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glBindTexture(GL_TEXTURE_2D, 0);
            drawTexture(x, y, width, height, r, g, b, a, s1, t1, s2, t2, texture, sampler);
        }

        /**
         * draw a image
         * @param x the x position of the image
         * @param y the y position of the image
         * @param width the width of the image
         * @param height the height of the image
         * @param r the red value of the image
         * @param g the green value of the image
         * @param b the blue value of the image
         * @param a the alpha value of the image
         * @param s1 the s1 value of the image
         * @param t1 the t1 value of the image
         * @param s2 the s2 value of the image
         * @param t2 the t2 value of the image
         * @param image the image to draw
         */
        public void drawImage(float x, float y, float width, float height, float r, float g, float b, float a, float s1, float t1, float s2, float t2, BufferedImage image){
            flush();
            ByteBuffer buffer = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
            for (int j = image.getHeight()-1; j >=0; j--) {
                for (int i = 0; i < image.getWidth(); i++) {
                    buffer.put((byte) (image.getRGB(i, j) >> 16 & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) >> 8 & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) >> 24 & 0xFF));
                }
            }
            buffer.flip();
            glBindTexture(GL_TEXTURE_2D, texture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glBindTexture(GL_TEXTURE_2D, 0);
            drawTexture(x, y, width, height, r, g, b, a, s1, t1, s2, t2, texture, sampler);
        }

        /**
         * draw texture with the given sampler
         * @param x the x position of the image
         * @param y the y position of the image
         * @param width the width of the image
         * @param height the height of the image
         * @param r the red value of the image
         * @param g the green value of the image
         * @param b the blue value of the image
         * @param a the alpha value of the image
         * @param s1 the s1 value of the image
         * @param t1 the t1 value of the image
         * @param s2 the s2 value of the image
         * @param t2 the t2 value of the image
         * @param texture the texture to draw
         * @param sampler the sampler to use
         */
        public void drawTexture(float x, float y, float width, float height, float r, float g, float b, float a, float s1, float t1, float s2, float t2, int texture, int sampler){
            flush();
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, texture);
            glBindSampler(1, sampler);
            draw(x, y, width, height, r, g, b, a, s1, t1, s2, t2);
            glBindTexture(GL_TEXTURE_2D, 0);
            glBindSampler(1, 0);
            glActiveTexture(0);
        }

        /**
         * draw a texture
         * @param x the x position of the image
         * @param y the y position of the image
         * @param width the width of the image
         * @param height the height of the image
         * @param r the red value of the image
         * @param g the green value of the image
         * @param b the blue value of the image
         * @param a the alpha value of the image
         * @param s1 the s1 value of the image
         * @param t1 the t1 value of the image
         * @param s2 the s2 value of the image
         * @param t2 the t2 value of the image
         * @param texture the texture to draw
         */
        public void drawTexture(float x, float y, float width, float height, float r, float g, float b, float a, float s1, float t1, float s2, float t2, int texture){
            drawTexture(x, y, width, height, r, g, b, a, s1, t1, s2, t2, texture, sampler);
        }

        /**
         * draw a image with the current texture
         * @param x the x position of the image
         * @param y the y position of the image
         * @param width the width of the image
         * @param height the height of the image
         * @param r the red value of the image
         * @param g the green value of the image
         * @param b the blue value of the image
         * @param a the alpha value of the image
         * @param s1 the s1 value of the image
         * @param t1 the t1 value of the image
         * @param s2 the s2 value of the image
         * @param t2 the t2 value of the image
         */
        private void draw(float x, float y, float width, float height, float r, float g, float b, float a, float s1, float t1, float s2, float t2){
            vertices.putFloat(x).putFloat(y).putFloat(width).putFloat(height).putFloat(r).putFloat(g).putFloat(b).putFloat(a).putFloat(s1).putFloat(t1).putFloat(s2).putFloat(t2);
            vertexCount += 1;
            flush();
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
            glDrawArrays(GL_POINTS, 0, vertexCount);

            //====UNBIND====//
            glBindVertexArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBufferBase(GL_UNIFORM_BUFFER, 0, 0);
            glUseProgram(0);

            //====CLEAR THE BUFFER====//
            vertices.clear();
            vertexCount = 0;
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
            glDeleteTextures(texture);
            glDeleteSamplers(sampler);
            MemoryUtil.memFree(vertices);
            MemoryUtil.memFree(uniformData);
        }
    }

    public class Point{
        public int vao;
        public int vbo;
        public int vertexShader;
        public int fragmentShader;
        public int shaderProgram;
        public ByteBuffer vertices;

        public int uniformBuffer;
        public FloatBuffer uniformData;

        public int vertexCount;

        public Point() {
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
            vertexShader = createShader("src/main/resources/shaders/point/shader.vert", GL_VERTEX_SHADER);
            fragmentShader = createShader("src/main/resources/shaders/point/shader.frag", GL_FRAGMENT_SHADER);
            shaderProgram = glCreateProgram();
            glAttachShader(shaderProgram, vertexShader);
            glAttachShader(shaderProgram, fragmentShader);
            glLinkProgram(shaderProgram);
            glUseProgram(shaderProgram);

            //====SPECIFY THE VERTEX DATA====//
            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, vertices.capacity(), GL_DYNAMIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 7 * Float.BYTES, 0);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(1, 4, GL_FLOAT, false, 7 * Float.BYTES, 3 * Float.BYTES);
            glEnableVertexAttribArray(1);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
            glUseProgram(0);
        }

        /**
         * set the model matrix
         * @param modelMatrix the model matrix
         */
        public void setModelMatrix(Matrix4f modelMatrix){
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(modelMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 0, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        /**
         * set the view matrix
         * @param viewMatrix the view matrix
         */
        public void setViewMatrix(Matrix4f viewMatrix){
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(viewMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 16 * Float.BYTES, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        /**
         * set the projection matrix
         * @param projectionMatrix the projection matrix
         */
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

        /**
         * draw a point with 1 color
         * @param x the x position of the point
         * @param y the y position of the point
         * @param z the z position of the point
         * @param r the red value of the point
         * @param g the green value of the point
         * @param b the blue value of the point
         * @param a the alpha value of the point
         */
        public void drawPoint(float x, float y, float z, float r, float g, float b, float a) {
            if (vertices.remaining() < 7 * 4) {
                flush();
            }
            vertices.putFloat(x);
            vertices.putFloat(y);
            vertices.putFloat(z);
            vertices.putFloat(r);
            vertices.putFloat(g);
            vertices.putFloat(b);
            vertices.putFloat(a);
            vertexCount += 1;
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
            glDrawArrays(GL_POINTS, 0, vertexCount);

            //====UNBIND====//
            glBindVertexArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glUseProgram(0);

            //====CLEAR THE BUFFER====//
            vertices.clear();
            vertexCount = 0;
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

    public class Text {
        public class Glyph {
            public float s1;
            public float t1;
            public float s2;
            public float t2;
            public float width;
            public float height;
        }

        public int vao;
        public int vbo;
        public int vertexShader;
        public int geometryShader;
        public int fragmentShader;
        public int shaderProgram;
        public int texture;
        public int sampler;
        public ByteBuffer vertices;

        public int uniformBuffer;
        public FloatBuffer uniformData;

        public int vertexCount;

        public Text() {
            //====CREATE THE VAO AND VBO====//
            vao = glGenVertexArrays();
            vbo = glGenBuffers();
            vertices = MemoryUtil.memAlloc(512 * 12 * 4);

            //====CREATE THE UNIFORM BUFFER====//
            uniformBuffer = glGenBuffers();
            uniformData = MemoryUtil.memAllocFloat(16);
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            glBufferData(GL_UNIFORM_BUFFER, 48 * Float.BYTES, GL_DYNAMIC_DRAW);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);

            //====CREATE THE TEXTURE====//
            texture = glGenTextures();
            sampler = glGenSamplers();
            glSamplerParameteri(sampler, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glSamplerParameteri(sampler, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glSamplerParameteri(sampler, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glSamplerParameteri(sampler, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            //====INITIALIZE THE MATRICES====//
            setModelMatrix(new Matrix4f().identity());
            setViewMatrix(new Matrix4f().identity());
            setProjectionMatrix(new Matrix4f().identity());

            //====CREATE THE SHADER PROGRAM====//
            vertexShader = createShader("src/main/resources/shaders/text/shader.vert", GL_VERTEX_SHADER);
            geometryShader = createShader("src/main/resources/shaders/text/shader.geom", GL_GEOMETRY_SHADER);
            fragmentShader = createShader("src/main/resources/shaders/text/shader.frag", GL_FRAGMENT_SHADER);
            shaderProgram = glCreateProgram();
            glAttachShader(shaderProgram, vertexShader);
            glAttachShader(shaderProgram, geometryShader);
            glAttachShader(shaderProgram, fragmentShader);
            glLinkProgram(shaderProgram);
            glUseProgram(shaderProgram);

            //====SPECIFY THE VERTEX DATA====//
            glBindVertexArray(vao);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, vertices.capacity(), GL_DYNAMIC_DRAW);
            glVertexAttribPointer(0, 4, GL_FLOAT, false, 12 * Float.BYTES, 0);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 12 * Float.BYTES, 4 * Float.BYTES);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(2, 2, GL_FLOAT, false, 12 * Float.BYTES, 6 * Float.BYTES);
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(3, 2, GL_FLOAT, false, 12 * Float.BYTES, 8 * Float.BYTES);
            glEnableVertexAttribArray(3);
            glVertexAttribPointer(4, 2, GL_FLOAT, false, 12 * Float.BYTES, 10 * Float.BYTES);
            glEnableVertexAttribArray(4);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }

        /**
         * set the model matrix
         *
         * @param modelMatrix the model matrix
         */
        public void setModelMatrix(Matrix4f modelMatrix) {
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(modelMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 0, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        /**
         * set the view matrix
         *
         * @param viewMatrix the view matrix
         */
        public void setViewMatrix(Matrix4f viewMatrix) {
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(viewMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 16 * Float.BYTES, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        /**
         * set the projection matrix
         *
         * @param projectionMatrix the projection matrix
         */
        public void setProjectionMatrix(Matrix4f projectionMatrix) {
            glBindBuffer(GL_UNIFORM_BUFFER, uniformBuffer);
            uniformData.clear();
            uniformData.put(projectionMatrix.get(new float[16]));
            uniformData.flip();
            glBufferSubData(GL_UNIFORM_BUFFER, 32 * Float.BYTES, uniformData);
            glBindBuffer(GL_UNIFORM_BUFFER, 0);
        }

        public void begin() {
            vertices.clear();
            vertexCount = 0;
        }

        public int[] drawTextRelative(float x, float y, float h, float r, float g, float b, float a, String text, Font font, float xCenter, float yCenter){
            Graphics2D g2d = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
            g2d.setFont(font);
            FontMetrics metrics = g2d.getFontMetrics(font);
            int width = metrics.stringWidth(text);
            int height = metrics.getHeight();
            g2d.dispose();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            g2d = image.createGraphics();
            //g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setBackground(new Color(0,0,0,0));
            g2d.setFont(font);
            g2d.setPaint(new Color(255,255,255,255));
            int h1 = metrics.getAscent();
            g2d.drawString(text, 0, h1);
            g2d.dispose();

            ByteBuffer buffer = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
            for (int j = image.getHeight()-1; j >=0; j--) {
                for (int i = 0; i < image.getWidth(); i++) {
                    buffer.put((byte) (image.getRGB(i, j) >> 16 & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) >> 8 & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) >> 24 & 0xFF));
                }
            }
            buffer.flip();

            glBindTexture(GL_TEXTURE_2D, texture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glGenerateMipmap(GL_TEXTURE_2D);
            MemoryUtil.memFree(buffer);
            glBindTexture(GL_TEXTURE_2D, 0);
            if(h <= 0){
                h = height;
            }
            draw(x - xCenter * h * width / height, y - yCenter * h, h * width / height, h, r, g, b, a, 0, 0, 1, 1);

            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, texture);
            glBindSampler(1, sampler);
            flush();
            glBindTexture(GL_TEXTURE_2D, 0);
            glBindSampler(1, 0);
            glActiveTexture(0);

            return new int[]{width, height};
        }

        /**
         * draw a text with the given font and position with the bottom left as the origin
         * @param x the x position of the text
         * @param y the y position of the text
         * @param h the height of the text
         * @param r the red value of the text
         * @param g the green value of the text
         * @param b the blue value of the text
         * @param a the alpha value of the text
         * @param text the text to draw
         * @param font the font to use
         * @return the width and height of the text
         */
        public int[] drawTextLB(float x, float y, float h, float r, float g, float b, float a, String text, Font font) {
            Graphics2D g2d = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
            g2d.setFont(font);
            FontMetrics metrics = g2d.getFontMetrics(font);
            int width = metrics.stringWidth(text);
            int height = metrics.getHeight();
            g2d.dispose();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            g2d = image.createGraphics();
            //g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setBackground(new Color(0,0,0,0));
            g2d.setFont(font);
            g2d.setPaint(new Color(255,255,255,255));
            int h1 = metrics.getAscent();
            g2d.drawString(text, 0, h1);
            g2d.dispose();

            ByteBuffer buffer = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
            for (int j = image.getHeight()-1; j >=0; j--) {
                for (int i = 0; i < image.getWidth(); i++) {
                    buffer.put((byte) (image.getRGB(i, j) >> 16 & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) >> 8 & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) >> 24 & 0xFF));
                }
            }
            buffer.flip();

            glBindTexture(GL_TEXTURE_2D, texture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glGenerateMipmap(GL_TEXTURE_2D);
            MemoryUtil.memFree(buffer);
            glBindTexture(GL_TEXTURE_2D, 0);
            if(h <= 0){
                h = height;
            }
            draw(x, y, h * width / height, h, r, g, b, a, 0, 0, 1, 1);

            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, texture);
            glBindSampler(1, sampler);
            flush();
            glBindTexture(GL_TEXTURE_2D, 0);
            glBindSampler(1, 0);
            glActiveTexture(0);

            return new int[]{width, height};
        }

        /**
         * draw a text with the given font and position with the top left as the origin
         * @param x the x position of the text
         * @param y the y position of the text
         * @param h the height of the text
         * @param r the red value of the text
         * @param g the green value of the text
         * @param b the blue value of the text
         * @param a the alpha value of the text
         * @param text the text to draw
         * @param font the font to use
         * @return the width and height of the text
         */
        public int[] drawTextLT(float x, float y, float h, float r, float g, float b, float a, String text, Font font) {
            Graphics2D g2d = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
            g2d.setFont(font);
            FontMetrics metrics = g2d.getFontMetrics(font);
            int width = metrics.stringWidth(text);
            int height = metrics.getHeight();
            g2d.dispose();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            g2d = image.createGraphics();
            //g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setBackground(new Color(0,0,0,0));
            g2d.setFont(font);
            g2d.setPaint(new Color(255,255,255,255));
            int h1 = metrics.getAscent();
            g2d.drawString(text, 0, h1);
            g2d.dispose();

            ByteBuffer buffer = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
            for (int j = image.getHeight()-1; j >=0; j--) {
                for (int i = 0; i < image.getWidth(); i++) {
                    buffer.put((byte) (image.getRGB(i, j) >> 16 & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) >> 8 & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) >> 24 & 0xFF));
                }
            }
            buffer.flip();

            glBindTexture(GL_TEXTURE_2D, texture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glGenerateMipmap(GL_TEXTURE_2D);
            MemoryUtil.memFree(buffer);
            glBindTexture(GL_TEXTURE_2D, 0);
            if(h <= 0){
                h = height;
            }
            draw(x, y - height, h * width / height, h, r, g, b, a, 0, 0, 1, 1);

            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, texture);
            glBindSampler(1, sampler);
            flush();
            glBindTexture(GL_TEXTURE_2D, 0);
            glBindSampler(1, 0);
            glActiveTexture(0);

            return new int[]{width, height};
        }

        /**
         * draw a text with the given font and position with the bottom right as the origin
         * @param x the x position of the text
         * @param y the y position of the text
         * @param h the height of the text
         * @param r the red value of the text
         * @param g the green value of the text
         * @param b the blue value of the text
         * @param a the alpha value of the text
         * @param text the text to draw
         * @param font the font to use
         * @return the width and height of the text
         */
        public int[] drawTextRB(float x, float y, float h, float r, float g, float b, float a, String text, Font font) {

            Graphics2D g2d = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
            g2d.setFont(font);
            FontMetrics metrics = g2d.getFontMetrics(font);
            int width = metrics.stringWidth(text);
            int height = metrics.getHeight();
            g2d.dispose();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            g2d = image.createGraphics();
            //g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setBackground(new Color(0,0,0,0));
            g2d.setFont(font);
            g2d.setPaint(new Color(255,255,255,255));
            int h1 = metrics.getAscent();
            g2d.drawString(text, 0, h1);
            g2d.dispose();

            ByteBuffer buffer = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
            for (int j = image.getHeight()-1; j >=0; j--) {
                for (int i = 0; i < image.getWidth(); i++) {
                    buffer.put((byte) (image.getRGB(i, j) >> 16 & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) >> 8 & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) >> 24 & 0xFF));
                }
            }
            buffer.flip();

            glBindTexture(GL_TEXTURE_2D, texture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glGenerateMipmap(GL_TEXTURE_2D);
            MemoryUtil.memFree(buffer);
            glBindTexture(GL_TEXTURE_2D, 0);
            if(h <= 0){
                h = height;
            }
            draw(x - h * width / height, y, h * width / height, h, r, g, b, a, 0, 0, 1, 1);

            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, texture);
            glBindSampler(1, sampler);
            flush();
            glBindTexture(GL_TEXTURE_2D, 0);
            glBindSampler(1, 0);
            glActiveTexture(0);

            return new int[]{width, height};
        }

        /**
         * draw a text with the given font and position with the top right as the origin
         * @param x the x position of the text
         * @param y the y position of the text
         * @param h the height of the text
         * @param r the red value of the text
         * @param g the green value of the text
         * @param b the blue value of the text
         * @param a the alpha value of the text
         * @param text the text to draw
         * @param font the font to use
         * @return the width and height of the text
         */
        public int[] drawTextRT(float x, float y, float h, float r, float g, float b, float a, String text, Font font) {

            Graphics2D g2d = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
            g2d.setFont(font);
            FontMetrics metrics = g2d.getFontMetrics(font);
            int width = metrics.stringWidth(text);
            int height = metrics.getHeight();
            g2d.dispose();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            g2d = image.createGraphics();
            //g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setBackground(new Color(0,0,0,0));
            g2d.setFont(font);
            g2d.setPaint(new Color(255,255,255,255));
            int h1 = metrics.getAscent();
            g2d.drawString(text, 0, h1);
            g2d.dispose();

            ByteBuffer buffer = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
            for (int j = image.getHeight()-1; j >=0; j--) {
                for (int i = 0; i < image.getWidth(); i++) {
                    buffer.put((byte) (image.getRGB(i, j) >> 16 & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) >> 8 & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) & 0xFF));
                    buffer.put((byte) (image.getRGB(i, j) >> 24 & 0xFF));
                }
            }
            buffer.flip();

            glBindTexture(GL_TEXTURE_2D, texture);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glGenerateMipmap(GL_TEXTURE_2D);
            MemoryUtil.memFree(buffer);
            glBindTexture(GL_TEXTURE_2D, 0);
            if(h <= 0){
                h = height;
            }
            draw(x - h * width / height, y - h, h * width / height, h, r, g, b, a, 0, 0, 1, 1);

            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, texture);
            glBindSampler(1, sampler);
            flush();
            glBindTexture(GL_TEXTURE_2D, 0);
            glBindSampler(1, 0);
            glActiveTexture(0);

            return new int[]{width, height};
        }

        /**
         * draw a text with the given position with the center as the origin
         * @param x the x position of the text
         * @param y the y position of the text
         * @param h the height of the text
         * @param r the red value of the text
         * @param g the green value of the text
         * @param b the blue value of the text
         * @param a the alpha value of the text
         * @param text the text to draw
         * @return the width and height of the text
         */
        public int[] drawTextLB(float x, float y, float h, float r, float g, float b, float a, String text){
            return drawTextLB(x, y, h, r, g, b, a, text, new Font(Font.MONOSPACED, Font.PLAIN, 16));
        }

        /**
         * draw a text with the given position with the center as the origin
         * @param x the x position of the text
         * @param y the y position of the text
         * @param h the height of the text
         * @param r the red value of the text
         * @param g the green value of the text
         * @param b the blue value of the text
         * @param a the alpha value of the text
         * @param text the text to draw
         * @return the width and height of the text
         */
        public int[] drawTextLT(float x, float y, float h, float r, float g, float b, float a, String text){
            return drawTextLT(x, y, h, r, g, b, a, text, new Font(Font.MONOSPACED, Font.PLAIN, 16));
        }

        /**
         * draw a text with the given position with the center as the origin
         * @param x the x position of the text
         * @param y the y position of the text
         * @param h the height of the text
         * @param r the red value of the text
         * @param g the green value of the text
         * @param b the blue value of the text
         * @param a the alpha value of the text
         * @param text the text to draw
         * @return the width and height of the text
         */
        public int[] drawTextRB(float x, float y, float h, float r, float g, float b, float a, String text){
            return drawTextRB(x, y, h, r, g, b, a, text, new Font(Font.MONOSPACED, Font.PLAIN, 16));
        }

        /**
         * draw a text with the given position with the center as the origin
         * @param x the x position of the text
         * @param y the y position of the text
         * @param h the height of the text
         * @param r the red value of the text
         * @param g the green value of the text
         * @param b the blue value of the text
         * @param a the alpha value of the text
         * @param text the text to draw
         * @return the width and height of the text
         */
        public int[] drawTextRT(float x, float y, float h, float r, float g, float b, float a, String text){
            return drawTextRT(x, y, h, r, g, b, a, text, new Font(Font.MONOSPACED, Font.PLAIN, 16));
        }

        private void draw(float x1, float y1, float w, float h, float r, float g, float b, float a, float s1, float t1, float sl, float tl) {
            if (vertices.remaining() < 12 * 4) {
                flush();
            }
            vertices.putFloat(r).putFloat(g).putFloat(b).putFloat(a).putFloat(s1).putFloat(t1).putFloat(x1).putFloat(y1).putFloat(w).putFloat(h).putFloat(sl).putFloat(tl);
            vertexCount += 1;
        }

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
            glDrawArrays(GL_POINTS, 0, vertexCount);

            //====UNBIND====//
            glBindVertexArray(0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBufferBase(GL_UNIFORM_BUFFER, 0, 0);
            glUseProgram(0);

            //====CLEAR THE BUFFER====//
            vertices.clear();
            vertexCount = 0;
        }

        public void dispose() {
            glDeleteProgram(shaderProgram);
            glDeleteShader(vertexShader);
            glDeleteShader(geometryShader);
            glDeleteShader(fragmentShader);
            glDeleteBuffers(vbo);
            glDeleteVertexArrays(vao);
            glDeleteBuffers(uniformBuffer);
            glDeleteTextures(texture);
            glDeleteSamplers(sampler);
            MemoryUtil.memFree(vertices);
            MemoryUtil.memFree(uniformData);
        }
    }

    public EasyRender(Window window) {
        this.window = window;
        line = new Line();
        triangle = new Triangle();
        pixel = new Pixel();
        image = new Image();
        point = new Point();
        text = new Text();
        glEnable(GL_BLEND);
        glBlendEquation(GL_FUNC_ADD);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public Window window(){
        return window;
    }

    public void begin() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        line.begin();
        triangle.begin();
        pixel.begin();
        image.begin();
        point.begin();
        text.begin();
    }

    public void end(Window window) {
        line.end();
        triangle.end();
        pixel.end();
        image.end();
        point.end();
        text.flush();
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
        triangle.dispose();
        pixel.dispose();
        image.dispose();
        point.dispose();
        text.dispose();
    }
}
