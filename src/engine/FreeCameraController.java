package engine;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.ReadableVector3f;

/**
 * Free flying Camera Controller.
 *
 * @see engine.Camera
 * @see enigne.BasicFPCameraController
 *
 * @author Matthew 'siD' Van der Bijl
 */
public class FreeCameraController extends BasicFPCameraController {

    private byte up;
    private byte down;

    public FreeCameraController() {
        super(new Vector3f());
        
        this.up = Keys.KEY_Q;
        this.down = Keys.KEY_E;
    }

    public FreeCameraController(float xPos, float yPos, float zPos) {
        super(xPos, yPos, zPos);
        
        this.up = Keys.KEY_Q;
        this.down = Keys.KEY_E;
    }

    public FreeCameraController(ReadableVector3f position) {
        super(position);
        
        this.up = Keys.KEY_Q;
        this.down = Keys.KEY_E;
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (Keyboard.isKeyDown(up)) {
            super.translate(0, (float) super.getMovementSpeed(), 0);
        }
        if (Keyboard.isKeyDown(down)) {
            super.translate(0, (float) -super.getMovementSpeed(), 0);
        }
    }

    public byte getUp() {
        return this.up;
    }

    public void setUp(byte upKey) {
        this.up = upKey;
    }

    public byte getDown() {
        return this.down;
    }

    public void setDown(byte downKey) {
        this.down = downKey;
    }
}
