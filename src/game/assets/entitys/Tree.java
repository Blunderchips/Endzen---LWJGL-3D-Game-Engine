package game.assets.entitys;

import engine.Random;
import engine.physics.AABB;
import engine.physics.AABBMesh;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.ReadableVector3f;

public final class Tree extends game.assets.Entity {

    private final byte index;
    /**
     * The rotation of the tree.
     */
    private final int rotation;
    /**
     * The position of the tree in the world.
     */
    private final Vector3f position;

    public Tree(ReadableVector3f position) {
        super("Tree");

        this.rotation = Random.interger(360);
        this.position = new Vector3f(position);
        this.index = (byte) Random.interger(0, 5);
    }

    /**
     * @return rotation of the Tree
     */
    @Override
    public int getRotation() {
        return this.rotation;
    }

    public Entitys getTree() {
        return Entitys.values()[index];
    }

    @Override
    public int getDisplayList() {
        return Entitys.values()[index].getDisplayList();
    }

    @Override
    public AABBMesh getMesh() {
        return new AABBMesh(Entitys.values()[index].getMesh(), getPosition());
    }

    @Override
    public AABB getAABB() {
        return Entitys.values()[index].getAABB();
    }

    @Override
    public Vector3f getPosition() {
        return this.position;
    }
}
