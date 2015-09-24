package engine;

import java.util.Scanner;

import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

/**
 * Provides a interface that offers GLSL (shader) support to the user.
 *
 * <p>
 * OpenGL Shading Language (abbreviated: GLSL or GLslang), is a high-level
 * shading language based on the syntax of the C programming language. It was
 * created by the OpenGL ARB (OpenGL Architecture Review Board) to give
 * developers more direct control of the graphics pipeline without having to use
 * ARB assembly language or hardware-specific languages.</p>
 *
 * @author Matthew 'siD' Van der Bijl
 *
 * @see
 * <a href='http://en.wikipedia.org/wiki/OpenGL_Shading_Language'>wikipedia</a>
 * @see engine.Shader
 */
public abstract class GLSL {

    /**
     * You can not instantiate this class.
     */
    @Deprecated
    public GLSL() throws UnsupportedOperationException {
        // Prevents instantiation of this class.
        throw new UnsupportedOperationException(
                "You can not instantiate this class.");
    }

    /**
     * A Vertex Shader is the programmable Shader stage in the rendering
     * pipeline that handles the processing of individual vertices.
     */
    public final static int VERTEX_SHADER = GL_VERTEX_SHADER;
    /**
     * A Fragment Shader is a user-supplied program that, when executed, will
     * process a Fragment from the rasterization process into a set of colours
     * and a single depth value.
     */
    public final static int FRAGMENT_SHADER = GL_FRAGMENT_SHADER;

    /**
     * Send floats to the shader.
     *
     * @param name
     * @param program
     * @param values
     */
    public static final void sendFloats(String name, int program, float... values) {
        int location = GL20.glGetUniformLocation(program, name);
        switch (values.length) {
            case 1:
                GL20.glUniform1f(location, values[0]);
                break;
            case 2:
                GL20.glUniform2f(location, values[0], values[1]);
                break;
            case 3:
                GL20.glUniform3f(location, values[0], values[1], values[2]);
                break;
            case 4:
                GL20.glUniform4f(location, values[0], values[1], values[2], values[3]);
                break;
        }
    }

    /**
     * Send integers to the shader.
     *
     * @param name
     * @param program
     * @param values
     */
    public static final void sendIntegers(String name, int program, int... values) {
        int location = GL20.glGetUniformLocation(program, name);
        switch (values.length) {
            case 1:
                GL20.glUniform1i(location, values[0]);
                break;
            case 2:
                GL20.glUniform2i(location, values[0], values[1]);
                break;
            case 3:
                GL20.glUniform3i(location, values[0], values[1], values[2]);
                break;
            case 4:
                GL20.glUniform4i(location, values[0], values[1], values[2], values[3]);
                break;
        }
    }

    /**
     * Run a given shader program.
     *
     * @param program to shader program to be run
     */
    public static final void useProgram(int program) {
        GL20.glUseProgram(program);
    }

    /**
     * Reset the shader program being used.
     */
    public static final void resetShaders() {
        GL20.glUseProgram(0);
    }

    /**
     * destroys a given shader program.
     *
     * @param program the shader program to be destroyed
     */
    public static final void destroy(int program) {
        GL20.glDeleteProgram(program);
    }

    /**
     * Create a shader program.
     *
     * @param shaders the shaders to be attached
     */
    public static final int createShaderProgram(int... shaders) {
        int program = GL20.glCreateProgram();
        for (int shader : shaders) {
            GL20.glAttachShader(program, shader);
        }
        GL20.glLinkProgram(program);
        for (int shader : shaders) {
            GL20.glDetachShader(program, shader);
            GL20.glDeleteShader(shader);
        }
        GL20.glValidateProgram(program);
        if (GL20.glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            throw new DwarfException(
                    "Shader program wasn't linked correctly."
                    + GL20.glGetProgramInfoLog(program, 1024));
        } else {
            return program;
        }
    }

    /**
     * Creates a new shader program.
     *
     * @param path the path to the shader file
     * @param type the type of program to be created
     * @return the shader program
     */
    public static final int createShader(String path, int type) {
        if (ClassLoader.getSystemResourceAsStream(path) != null) {
            int shader = GL20.glCreateShader(type);
            Scanner shaderFileReader = new Scanner(ClassLoader.getSystemResourceAsStream(path));
            StringBuilder shaderSource = new StringBuilder();
            while (shaderFileReader.hasNextLine()) {
                shaderSource.append(shaderFileReader.nextLine()).append('\n');
            }
            GL20.glShaderSource(shader, shaderSource);
            GL20.glCompileShader(shader);
            if (GL20.glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
                throw new DwarfException(
                        "Shader wasn't able to be compiled correctly. Error log: "
                        + GL20.glGetShaderInfoLog(shader, 1024));
            } else {
                return shader;
            }
        } else {
            throw new DwarfException(path + " not found.");
        }
    }

    /**
     * Loads a shader program from two source files.
     *
     * @param vertexShaderPath the location of the file containing the vertex
     * shader source
     * @param fragmentShaderPath the location of the file containing the
     * fragment shader source
     *
     * @return the shader program
     */
    public static final int loadShaderPair(String vertexShaderPath,
            String fragmentShaderPath) throws DwarfException {
        return GLSL.createShaderProgram(
                createShader(vertexShaderPath, VERTEX_SHADER),
                createShader(fragmentShaderPath, FRAGMENT_SHADER)
        );
    }
}
