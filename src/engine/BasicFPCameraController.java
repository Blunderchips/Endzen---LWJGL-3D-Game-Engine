package engine;

import java.util.Objects;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.ReadableVector3f;

/**
 * First Person Camera Controller.
 * <br>
 * w - forward s - backward a - strafe left d - strafe right.
 * <br>
 * This is a modified version of the First Person Camera Controller class from
 * <a
 * href="http://www.lloydgoodall.com/tutorials/first-person-camera-control-with-lwjgl/">lloydgoodall.com</a>
 *
 * @see enigne.Camera
 * @see enigne.GameObject
 * @see java.lang.Object
 *
 * @author Matthew 'siD' Van der Bijl
 */
public class BasicFPCameraController extends Object implements Camera {

    /**
     * <code>Vector3f</code> to store the camera's position in the world.
     */
    private Vector3f position;
    /**
     * the rotation around the Y axis of the camera.
     */
    private float yaw;
    /**
     * the rotation around the X axis of the camera.
     */
    private float pitch;
    private float movementSpeed;
    private float mouseSensitivity;
    private byte[] controller;
    private boolean invertMouse;

    /**
     * Default constructor.
     */
    public BasicFPCameraController() {
        this(new Vector3f(0f, 0f, 0f));
    }

    /**
     * Constructs a new <code>BasicFPCameraController</code>.
     *
     * @param xPos the X position of the Camera
     * @param yPos the Y position of the Camera
     * @param zPos the Z position of the Camera
     */
    public BasicFPCameraController(float xPos, float yPos, float zPos) {
        this(new Vector3f(xPos, yPos, zPos));
    }

    /**
     * Constructs a new <code>BasicFPCameraController</code>.
     *
     * @param position the position of the camera in the world
     */
    public BasicFPCameraController(ReadableVector3f position) {
        super();

        this.yaw = 0;
        this.pitch = 180;

        this.movementSpeed = 1;
        this.mouseSensitivity = 1;

        this.position = new Vector3f(position);
        this.controller = DEFAULT_CAMERA_CONTROLLER;
    }

    @Override
    public void update(float dt) {

        this.yaw(Mouse.getDX() * mouseSensitivity);
        this.pitch(Mouse.getDY() * mouseSensitivity);

        if (invertMouse) {
            this.yaw(-Mouse.getDX() * mouseSensitivity);
            this.pitch(-Mouse.getDY() * mouseSensitivity);
        } else {
            this.yaw(-Mouse.getDX() * mouseSensitivity);
            this.pitch(-Mouse.getDY() * mouseSensitivity);
        }

        while (pitch > 360 || pitch < 0) {
            if (pitch > 360) {
                pitch -= 360;
            } else if (pitch < 0) {
                pitch += 360;
            }
        }

        while (yaw > 360 || yaw < 0) {
            if (yaw > 360) {
                yaw -= 360;
            } else if (yaw < 0) {
                yaw += 360;
            }
        }

        if (pitch < 90) {
            this.pitch = 90;
        } else if (pitch > 270) {
            this.pitch = 270;
        }

        if (Keyboard.isKeyDown(controller[FORWARDS])) {
            this.moveForward(movementSpeed * dt);
        }
        if (Keyboard.isKeyDown(controller[BACKWARDS])) {
            this.moveBackwards(movementSpeed * dt);
        }
        if (Keyboard.isKeyDown(controller[LEFT])) {
            this.strafeLeft(movementSpeed * dt);
        }
        if (Keyboard.isKeyDown(controller[RIGHT])) {
            this.strafeRight(movementSpeed * dt);
        }
    }

    @Override
    public void render() {
        // Roatate the pitch around the X axis.
        GL11.glRotated(pitch, 0x1, 0x0, 0x0);
        // Roatate the yaw around the Y axis.
        GL11.glRotated(yaw, 0x0, 0x1, 0x0);
        // Translate to the position vector's location.
        GL11.glTranslated(position.getX(), position.getY(), position.getZ());
        GL11.glRotated(180, 0x0, 0x0, 0x1); // invert the world?!?!?
    }

    /**
     * increment the camera's current yaw rotation.
     */
    public void yaw(double delta) {
        yaw += delta;
    }

    /**
     * increment the camera's current yaw rotation.
     */
    public void pitch(double delta) {
        pitch += delta;
    }

    /**
     * moves the camera forward relative to its current rotation (yaw).
     */
    public void moveForward(double distance) {
        position.x += distance * java.lang.Math.sin(java.lang.Math.toRadians(yaw));
        position.z -= distance * java.lang.Math.cos(java.lang.Math.toRadians(yaw));
    }

    /**
     * moves the camera backward relative to its current rotation (yaw).
     */
    public void moveBackwards(double distance) {
        position.x -= distance * java.lang.Math.sin(java.lang.Math.toRadians(yaw));
        position.z += distance * java.lang.Math.cos(java.lang.Math.toRadians(yaw));
    }

    /**
     * strafes the camera left relative to its current rotation (yaw).
     */
    public void strafeLeft(double distance) {
        position.x -= distance * java.lang.Math.sin(java.lang.Math.toRadians(yaw - 90));
        position.z += distance * java.lang.Math.cos(java.lang.Math.toRadians(yaw - 90));
    }

    /**
     * strafes the camera right relative to its current rotation (yaw).
     */
    public void strafeRight(double distance) {
        position.x -= distance * java.lang.Math.sin(java.lang.Math.toRadians(yaw + 90));
        position.z += distance * java.lang.Math.cos(java.lang.Math.toRadians(yaw + 90));
    }

    /**
     * @return the position of the <code>Camera</code> in the world.
     */
    @Override
    public Vector3f getPosition() {
        return this.position;
    }

    /**
     * @param position the new position of the <code>Camera</code>
     */
    @Override
    public void setPosition(ReadableVector3f position) {
        this.position = new Vector3f(position);
    }

    /**
     * @return the yaw of the camera
     */
    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    /**
     * @return the pitch of the camera
     */
    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * @return the sensitivity of the mouse
     */
    public float getMouseSensitivity() {
        return this.mouseSensitivity;
    }

    /**
     * @param sensitivity the new sensitivity of the mouse
     */
    public void setMouseSensitivity(float sensitivity) {
        this.mouseSensitivity = sensitivity;
    }

    public float getMovementSpeed() {
        return this.movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    /**
     * Class Object is the root of the class hierarchy.
     * <p>
     * Every class has Object as a superclass. All objects, including arrays,
     * implement the methods of this class.</p>
     *
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(position);
        hash = 31 * hash + Float.floatToIntBits(yaw);
        hash = 31 * hash + Float.floatToIntBits(pitch);
        hash = 31 * hash + (int) (Double.doubleToLongBits(mouseSensitivity)
                ^ (Double.doubleToLongBits(mouseSensitivity) >>> 32));
        hash = 31 * hash + (int) (Double.doubleToLongBits(movementSpeed)
                ^ (Double.doubleToLongBits(movementSpeed) >>> 32));
        return hash;
    }

    /**
     * Returns true if the <code>this</code> is equal to the argument and false
     * otherwise.
     * <p>
     * Consequently, if both argument are null, true is returned, false is
     * returned. Otherwise, equality is determined by using the equals method of
     * the first argument.</p>
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @param obj the <code>Object</code> to be tested
     *
     * @return true if the argument is equal to <code>this</code> other and
     * false otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        final BasicFPCameraController fpcc = (BasicFPCameraController) obj;

        if (!Objects.equals(this.position, fpcc.position)) {
            return false;
        } else if (Float.floatToIntBits(this.yaw)
                != Float.floatToIntBits(fpcc.yaw)) {
            return false;
        } else if (Float.floatToIntBits(this.pitch)
                != Float.floatToIntBits(fpcc.pitch)) {
            return false;
        } else if (Double.doubleToLongBits(this.mouseSensitivity)
                != Double.doubleToLongBits(fpcc.mouseSensitivity)) {
            return false;
        } else if (Double.doubleToLongBits(this.movementSpeed)
                != Double.doubleToLongBits(fpcc.movementSpeed)) {
            return false;
        }

        return true;
    }

    /**
     * Returns a string representation of the object.
     * <p>
     * In general, the toString method returns a string that "textually
     * represents" this object. The result should be a concise but informative
     * representation that is easy for a person to read. It is recommended that
     * all subclasses override this method.</p>
     *
     * @return a textually representation of this object
     */
    @Override
    public String toString() {
        return "FPCameraController["
                + "position: " + position
                + ", yaw: " + yaw
                + ", pitch: " + pitch
                + ", mouseSensitivity: " + mouseSensitivity
                + ", movementSpeed: " + movementSpeed + "]";
    }

    /**
     * @param deltaX the change along the X axis
     * @param deltaY the change along the Y axis
     * @param deltaZ the change along the Z axis
     */
    @Override
    public void translate(float deltaX, float deltaY, float deltaZ) {
        this.position.set(
                this.position.getX() + deltaX,
                this.position.getY() + deltaY,
                this.position.getZ() + deltaZ
        );
    }

    @Override
    public void translate(ReadableVector3f delta) {
        this.position.set(
                this.position.getX() + delta.getX(),
                this.position.getY() + delta.getY(),
                this.position.getZ() + delta.getZ()
        );
    }

    /**
     * @return the camera controller
     */
    public byte[] getController() {
        return this.controller;
    }

    /**
     * @param controller the news camera controller
     */
    public void setController(byte[] controller) {
        this.controller = controller;
    }

    public void move(float delta) {
        if (delta > 0) {
            this.moveForward(delta);
        } else {
            this.moveBackwards(-delta);
        }
    }
}
