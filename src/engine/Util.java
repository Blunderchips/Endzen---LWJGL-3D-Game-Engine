package engine;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

/**
 * Provides an interface that offers general utilities to the user.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public final class Util {

    /**
     * You can not instantiate this class.
     */
    @Deprecated
    public Util() throws UnsupportedOperationException {
        // Prevents instantiation of this class
        throw new UnsupportedOperationException(
                "You cannot instantiate this class.");
    }

    /**
     * @param values the float values that are to be turned into a FloatBuffer
     * @return a FloatBuffer readable to OpenGL (not to you!) containing values
     */
    public static FloatBuffer asFlippedFloatBuffer(float... values) {
        FloatBuffer output = BufferUtils.createFloatBuffer(values.length);
        output.put(values);
        output.flip();

        return output;
    }
}
