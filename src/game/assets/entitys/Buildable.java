package game.assets.entitys;

import engine.Destroyable;
import engine.Keyboard;
import engine.physics.AABB;
import game.assets.Entity;
import game.assets.Player;
import engine.physics.AABBMesh;

import org.lwjgl.opengl.GL11;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.ReadableVector3f;

public class Buildable extends Entity {

    private final byte index;
    private final int rotation;
    private final Vector3f position;

    private Player player;
    private boolean built;

    public Buildable(int index, ReadableVector3f position, int rotation,
            Player player) {
        super(null);

        this.index = (byte) index;
        this.rotation = rotation;
        this.player = player;
        this.position = new Vector3f(position);

        this.built = true;
    }

    @Override
    public void update(float dt) {
        if (!built && Keyboard.isKeyDown("up")) {
            this.build();
        }
    }

    @Override
    public void render() {
        if (built) {
            super.render();
        } else {
            GL11.glPushMatrix();
            {
                GL11.glTranslatef(
                        this.getPosition().getX(),
                        this.getPosition().getY(),
                        this.getPosition().getZ()
                );
                GL11.glRotatef(getRotation(), 0, 1, 0);

                Entitys.BUILD_SITE.render();
            }
            GL11.glPopMatrix();
        }
    }

    @Override
    public Vector3f getPosition() {
        return this.position;
    }

    @Override
    public int getRotation() {
        return this.rotation;
    }

    public byte getIndex() {
        return this.index;
    }

    public boolean isBuilt() {
        return this.built;
    }

    public void place() {
        this.built = false;
    }

    public void build() {
        this.built = true;
        this.player = null;
    }

    @Override
    public int getDisplayList() {
        return Entitys.values()[index].getDisplayList();
    }

    @Override
    public String getName() {
        return Entitys.values()[index].name();
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public AABBMesh getMesh() {
        return new AABBMesh(Entitys.values()[index].getMesh(), getPosition());
    }

    @Override
    public AABB getAABB() {
        return Entitys.values()[index].getAABB();
    }
}
