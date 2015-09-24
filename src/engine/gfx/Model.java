package engine.gfx;

import engine.Destroyable;
import engine.GameObject;
import engine.lwjgl.Texture;
import engine.obj.OBJLoader;
import engine.obj.Obj;
import engine.physics.AABB;
import engine.physics.AABBMesh;
import engine.physics.Collidable;
import engine.physics.IntersectData;
import engine.physics.RadialCollider;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.ReadableVector4f;

public class Model extends AABBMesh implements GameObject, Destroyable, Collidable {

    private int displayList;
    private Texture texture;
    private ReadableVector4f rotation;

    private final ReadableVector3f minExtents;
    private final ReadableVector3f maxExtents;

    public Model(Obj model, OBJLoader loader, Vector3f position,
            ReadableVector4f rotation, Texture texture) {
        super(AABBMesh.OBJtoMeshAABB(model), position);

        this.rotation = rotation;
        this.texture = texture;
        this.displayList = loader.createDisplayList(model);

        this.minExtents = Obj.Math.min(model);
        this.maxExtents = Obj.Math.max(model);
    }

    public Model(Obj model, Vector3f position, ReadableVector4f rotation,
            Texture texture) {
        this(model, new OBJLoader(), position, rotation, texture);
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
            GL11.glRotatef(
                    this.getRotation().getX(),
                    this.getRotation().getY(),
                    this.getRotation().getZ(),
                    this.getRotation().getW()
            );

            if (this.hasTexture()) {
                this.texture.bind();
            }
            GL11.glCallList(displayList);
            if (this.hasTexture()) {
                this.texture.release();
            }
        }
        GL11.glPopMatrix();
    }

    @Override
    public void destroy() {
        GL11.glDeleteLists(displayList, 1);
        this.displayList = -1;
        if (this.hasTexture()) {
            this.texture.destroy();
        }
    }

    public ReadableVector4f getRotation() {
        return this.rotation;
    }

    public void setRotation(ReadableVector4f rotation) {
        this.rotation = rotation;
    }

    public int getDisplayList() {
        return this.displayList;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean hasTexture() {
        return this.texture != null;
    }

    @Override
    public IntersectData intersects(AABB coll) {
        return this.getAABB().intersects(coll);
    }

    @Override
    public IntersectData intersects(RadialCollider coll) {
        return this.getRadialCollider().intersects(coll);
    }

    @Override
    public AABB getAABB() {
        return new AABB(new Vector3f(
                this.getPosition().getX() + this.minExtents.getX(),
                this.getPosition().getY() + this.minExtents.getY(),
                this.getPosition().getZ() + this.minExtents.getZ()
        ), new Vector3f(
                this.getPosition().getX() + this.maxExtents.getX(),
                this.getPosition().getY() + this.maxExtents.getY(),
                this.getPosition().getZ() + this.maxExtents.getZ()
        ));
    }

    @Override
    public IntersectData intersects(Collidable coll) {
        return this.intersects(coll.getAABB());
    }
}
