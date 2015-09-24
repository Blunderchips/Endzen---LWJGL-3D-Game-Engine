package engine.physics;

import engine.Math;

import org.lwjgl.util.vector.ReadableVector3f;

/**
 * @author Matthew Van der Bijl
 */
public class PlaneCollider {

    private final float distance;
    private final ReadableVector3f normal;

    public PlaneCollider(ReadableVector3f normal, float distance) {
        super();

        this.normal = normal;
        this.distance = distance;
    }

    public PlaneCollider normalized() {
        return new PlaneCollider(
                Math.normalized(normal),
                distance / normal.length()
        );
    }

    public IntersectData intersect(RadialCollider coll) {
        final float distFromColl = Math.abs(
                Math.dot(getNormal(), coll.getPosition()) + this.distance
        ) - coll.getRadius();
        return new IntersectData(distFromColl < 0, distFromColl);
    }

    public float getDistance() {
        return this.distance;
    }

    public ReadableVector3f getNormal() {
        return this.normal;
    }
}
