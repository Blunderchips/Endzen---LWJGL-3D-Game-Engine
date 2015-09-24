package engine.physics;

import engine.Math;
import engine.GameObject;
import engine.obj.Obj;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author Matthew 'siD' Van der Bijl
 */
public class AABBMesh extends Object implements MeshCollider, GameObject {

    private AABB[] mesh;

    private Vector3f pastPos;
    private Vector3f currPos;

    public AABBMesh(AABB[] mesh, Vector3f position) {
        super();

        this.mesh = mesh;
        this.currPos = position;
        this.pastPos = new Vector3f();

        for (AABB aabb : mesh) {
            aabb.setMaxExtents(new Vector3f(Math.add(aabb.getMaxExtents(), position)));
            aabb.setMinExtents(new Vector3f(Math.add(aabb.getMinExtents(), position)));
        }
    }

    public AABBMesh(AABBMesh mesh) {
        this(mesh.getMesh(), mesh.getPosition());
    }

    @Override
    public void update(float dt) {
        if (!this.currPos.equals(pastPos)) {
            Vector3f delta = (Vector3f) Math.sub(currPos, pastPos);

            for (AABB aabb : mesh) {
                aabb.setMaxExtents((Vector3f) Math.add(aabb.getMaxExtents(), delta));
                aabb.setMinExtents((Vector3f) Math.add(aabb.getMinExtents(), delta));
            }

            this.pastPos = currPos;
        }
    }

    @Override
    public void render() {
    }

    public IntersectData intersects(AABBMesh coll) {
        return this.intersects(coll.getMesh());
    }

    public IntersectData intersects(AABB[] coll) {
        if (this == null || coll == null) {
            return new IntersectData(false, 0);
        }

        boolean intersection = false;
        float minDist = Float.POSITIVE_INFINITY;

        for (AABB aabb : coll) {
            IntersectData data = this.intersects(aabb);

            if (data.getDistance() != Float.NEGATIVE_INFINITY) {
                if (!intersection) {
                    intersection = data.isIntersection();
                }

                minDist = Math.min(minDist, data.getDistance());
            }
        }

        return new IntersectData(intersection, minDist);
    }

    @Override
    public IntersectData intersects(AABB coll) {
        if (this == null || coll == null) {
            return new IntersectData(false, 0);
        }

        boolean intersection = false;
        float minDist = Float.POSITIVE_INFINITY;

        for (AABB aabb : mesh) {
            IntersectData data = aabb.intersects(coll);

            if (data.getDistance() != Float.NEGATIVE_INFINITY) {
                if (!intersection) {
                    intersection = data.isIntersection();
                }

                minDist = Math.min(minDist, data.getDistance());
            }
        }

        return new IntersectData(intersection, minDist);
    }

    public AABB[] getMesh() {
        return this.mesh;
    }

    public void setMesh(AABB[] AABBMesh) {
        this.mesh = AABBMesh;
    }

    public Vector3f getPosition() {
        return this.currPos;
    }

    public void setPosition(ReadableVector3f position) {
        this.currPos = new Vector3f(position);
    }

    public void translate(ReadableVector3f delta) {
        this.currPos = (Vector3f) Math.add(currPos, delta);
    }

    public void translate(float deltaX, float deltaY, float deltaZ) {
        this.translate(new Vector3f(deltaX, deltaY, deltaZ));
    }

    @Override
    public AABB getAABB() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public RadialCollider getRadialCollider() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public IntersectData intersects(RadialCollider coll) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public IntersectData intersects(Collidable coll) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public static AABB[] OBJtoMeshAABB(Obj model) {
        AABB[] mesh;
        {
            List<AABB> temp = new ArrayList<>();

            for (Obj.Face face : model.getFaces()) {
                Vector3f[] vertices = {
                    model.getVertices().get(face.getVertices()[0] - 1),
                    model.getVertices().get(face.getVertices()[1] - 1),
                    model.getVertices().get(face.getVertices()[2] - 1)
                };
                Vector3f minExtents = (Vector3f) Math.min(vertices[0], Math.min(vertices[1], vertices[2]));
                Vector3f maxExtents = (Vector3f) Math.max(vertices[0], Math.max(vertices[1], vertices[2]));

                temp.add(new AABB(minExtents, maxExtents));
            }

            mesh = new AABB[temp.size()];
            for (int i = 0; i < mesh.length; i++) {
                mesh[i] = temp.get(i);
            }
        }

        return mesh;
    }
}
