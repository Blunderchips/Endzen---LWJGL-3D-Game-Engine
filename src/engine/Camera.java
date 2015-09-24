package engine;

import org.lwjgl.util.vector.ReadableVector3f;

/**
 * Definition of all cameras that can be attached to the user.
 */
public interface Camera extends GameObject {

    public static final byte FORWARDS = 0x0;
    public static final byte LEFT = 0x1;
    public static final byte BACKWARDS = 0x2;
    public static final byte RIGHT = 0x3;

    /**
     * The default camera controls.
     */
    public static final byte[] DEFAULT_CAMERA_CONTROLLER = {
        Keys.KEY_W, Keys.KEY_A, Keys.KEY_S, Keys.KEY_D
    };

    /**
     * @return the position of the <code>Camera</code> in the world
     */
    public abstract ReadableVector3f getPosition();

    /**
     * @param deltaX the change along the X axis
     * @param deltaY the change along the Y axis
     * @param deltaZ the change along the Z axis
     */
    public abstract void translate(float deltaX, float deltaY, float deltaZ);

    public abstract void translate(ReadableVector3f delta);

    /**
     * @param position the new position of the <code>Camera</code>
     */
    public abstract void setPosition(ReadableVector3f position);
}
