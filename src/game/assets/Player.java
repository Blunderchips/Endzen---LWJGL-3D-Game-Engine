package game.assets;

import java.util.List;
import java.util.ArrayList;

import engine.BasicFPCameraController;
import engine.Camera;
import engine.GameObject;
import game.assets.entitys.Buildable;
import engine.Keyboard;
import game.World;
import engine.Math;
import engine.Mouse;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.ReadableVector3f;

import static engine.Keys.KEY_1;
import static engine.Keys.KEY_SPACE;
import engine.physics.AABB;
import static engine.physics.Constants.GRAVITY;

/**
 * Player to be attached to the <code>World</code>.
 */
public class Player extends java.lang.Object implements Camera {

    private final List<GameObject> children;

    /**
     * the <code>World</code> that the <code>Player</code> is attached to.
     */
    private World world;

    private float yvel;
    private float jumpHeight;
    private float rotationSpeed;
    private int buildRotation;

    private boolean jumping;
    private boolean buildMode;

    private final BasicFPCameraController camera;

    public Player(World world, ReadableVector3f position) {
        super();

        this.world = world;
        this.camera = new BasicFPCameraController(position);

        this.yvel = 0;
        this.jumpHeight = 1;
        this.rotationSpeed = 20;
        this.jumping = false;
        this.buildMode = false;
        this.children = new ArrayList<>();
    }

    public Player(World world) {
        this(world, new Vector3f(0f, 0f, 0f));
    }

    @Override
    public void update(float dt) {
        this.camera.update(dt);

        { // movement:
            if (this.getPosition().getY() >= world.getTerrain().interpolateHeight(
                    getPosition().getX(), -getPosition().getZ()) + 5) {
                this.yvel = (yvel + (GRAVITY * (float) dt));
            }
            if (Keyboard.isKeyDown(KEY_SPACE)) {
                this.jump();
            }

            if (yvel != 0) {
                this.translate(0, yvel, 0);
            }
            if (this.getPosition().getY() < world.getTerrain().interpolateHeight(
                    getPosition().getX(), -getPosition().getZ()) + 5) {
                if (isJumping()) {
                    this.jumping = false;
                }
                this.getPosition().setY(world.getTerrain().interpolateHeight(
                        getPosition().getX(), -getPosition().getZ()) + 5
                );
            }
        }

        if (Mouse.hasWheel()) {
            int dWheel = Mouse.getDWheel();
            if (dWheel < 0) {
                this.buildRotation += rotationSpeed * dt;
            } else if (dWheel > 0) {
                this.buildRotation -= rotationSpeed * dt;
            }

            while (buildRotation > 360 || buildRotation < 0) {
                if (buildRotation > 360) {
                    buildRotation -= 360;
                } else if (buildRotation < 0) {
                    buildRotation += 360;
                }
            }
        }
    }

    @Override
    public void render() {
        this.camera.render();

        if (isInBuildMode()) {
            GL11.glTranslatef(0, -15, 5);

            Buildable obj = new Buildable(1,
                    new Vector3f(
                            this.getPosition().getX(),
                            this.world.getTerrain().interpolateHeight(
                                    this.getPosition().getX(),
                                    -this.getPosition().getZ()
                            ), -this.getPosition().getZ()
                    ), buildRotation, this);
            obj.render();

            if (Mouse.isMouseClicked()) {
                obj.place();
                this.children.add(obj);
                this.buildMode = false;
                this.move(-10);
            }
        }
        if (Keyboard.isKeyPressed(KEY_1)) {
            this.buildMode = !buildMode;
        }
    }

    public void updateChildren(float dt) {
        for (GameObject child : this.children) {
            child.update(dt);
        }
    }

    public void renderChildren() {
        for (GameObject child : this.children) {
            child.render();
        }
    }

    public void jump() {
        if (!isJumping() && !isInBuildMode()) {
            this.jumping = true;
            this.yvel = jumpHeight;
        }
    }

    public World getWorld() {
        return this.world;
    }

    public void attachWorld(World world) {
        this.world = world;
    }

    public void detachWorld() {
        this.world = null;
    }

    @Override
    public void translate(float deltaX, float deltaY, float deltaZ) {
        this.camera.translate(deltaX, deltaY, deltaZ);
    }

    public void setPosition(float deltaX, float deltaY, float deltaZ) {
        this.camera.setPosition(new Vector3f(deltaX, deltaY, deltaZ));
    }

    @Override
    public Vector3f getPosition() {
        return this.camera.getPosition();
    }

    @Override
    public void translate(ReadableVector3f delta) {
        this.camera.translate(delta);
    }

    @Override
    public void setPosition(ReadableVector3f position) {
        this.camera.setPosition(position);
    }

    public BasicFPCameraController getCamera() {
        return this.camera;
    }

    public float getJumpHeight() {
        return this.jumpHeight;
    }

    public void setJumpHeight(float jumpHeight) {
        this.jumpHeight = jumpHeight;
    }

    public boolean isJumping() {
        return this.jumping;
    }

    public List<GameObject> getChildren() {
        return this.children;
    }

    public boolean isInBuildMode() {
        return this.buildMode;
    }

    public void setBuildMode(boolean isInBuildMode) {
        this.buildMode = isInBuildMode;
    }

    public float getBuildRotation() {
        return this.buildRotation;
    }

    public void setBuildRotation(int buildRotation) {
        this.buildRotation = buildRotation;
    }

    public float getRotationSpeed() {
        return this.rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public void move(float delta) {
        this.camera.move(delta);
    }

    public AABB getAABB() {
        return new AABB(getPosition(), (Vector3f) (Math.add(getPosition(), new Vector3f(0, 25, 0))));
    }
}
