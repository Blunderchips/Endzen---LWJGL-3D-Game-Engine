package game.assets;

import engine.Destroyable;
import engine.GameObject;
import engine.physics.AABB;
import engine.physics.AABBMesh;
import engine.physics.IntersectData;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public abstract class Entity extends java.lang.Object implements GameObject,
        Comparable<Entity>, Destroyable {

    /**
     * The name of the <code>Entity</code>.
     */
    private final String name;

    /**
     * @param name the name of the <code>Object</code>
     */
    public Entity(String name) {
        super();
        this.name = name;
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render() {
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(
                    this.getPosition().getX(),
                    this.getPosition().getY(),
                    this.getPosition().getZ()
            );
            GL11.glRotatef(getRotation(), 0, 1, 0);

            GL11.glCallList(getDisplayList());
        }
        GL11.glPopMatrix();
    }

    public abstract Vector3f getPosition();

    public abstract int getDisplayList();

    public int getRotation() {
        return 0;
    }

    /**
     * @return the name of the <code>Entity</code>
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.getName() + "[" + String.format("%.2f, %.2f, %.2f",
                this.getPosition().getX(),
                this.getPosition().getY(),
                this.getPosition().getZ()
        ) + "]";
    }

    @Override
    public int compareTo(Entity other) {
        if (this.getPosition().equals(other.getPosition())) {
            return 0;
        } else {
            float a = this.getPosition().length();
            float b = other.getPosition().length();

            if (a > b) {
                return 1;
            } else if (a < b) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public void destroy() {
        GL11.glDeleteLists(1, getDisplayList());
    }

    public abstract AABBMesh getMesh();

    public abstract AABB getAABB();

    public IntersectData inretsects(AABB coll) {
        IntersectData data = this.getAABB().intersects(coll);
//        if (data.isIntersection()) {
//            data = this.getMesh().intersects(coll);
//        }
        return data;
    }
}
