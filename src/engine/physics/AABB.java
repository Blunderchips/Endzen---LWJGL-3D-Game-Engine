package engine.physics;

import java.util.Objects;

import engine.Math;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.ReadableVector3f;

/**
 * @author Matthew
 */
public strictfp class AABB {

    private ReadableVector3f minExtents;
    private ReadableVector3f maxExtents;

    public AABB() {
        this(null, null);
    }

    public AABB(ReadableVector3f minExtents, ReadableVector3f maxExtents) {
        super();

        this.minExtents = minExtents;
        this.maxExtents = maxExtents;
    }

    public IntersectData intersects(AABB coll) throws IllegalArgumentException {
        if (this == null || coll == null) {
            throw new IllegalArgumentException("null collider.");
        } else if (this.equals(coll)) {
            return new IntersectData(true, 0);
        }

        final float maxDist = Math.max(Math.max(new Vector3f(
                this.getMinExtents().getX() - coll.getMaxExtents().getX(),
                this.getMinExtents().getY() - coll.getMaxExtents().getY(),
                this.getMinExtents().getZ() - coll.getMaxExtents().getZ()
        ), new Vector3f(
                coll.getMinExtents().getX() - this.getMaxExtents().getX(),
                coll.getMinExtents().getY() - this.getMaxExtents().getY(),
                coll.getMinExtents().getZ() - this.getMaxExtents().getZ()
        )));

        return new IntersectData(maxDist < 0, maxDist);
    }

    public ReadableVector3f getMinExtents() {
        return this.minExtents;
    }

    public ReadableVector3f getMaxExtents() {
        return this.maxExtents;
    }

    public void setMinExtents(ReadableVector3f minExtents) {
        this.minExtents = minExtents;
    }

    public void setMaxExtents(ReadableVector3f maxExtents) {
        this.maxExtents = maxExtents;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(minExtents);
        hash = 79 * hash + Objects.hashCode(maxExtents);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        } else if (super.getClass() != obj.getClass()) {
            return false;
        }

        final AABB that = (AABB) obj;

        if (!Objects.equals(this.minExtents, that.minExtents)) {
            return false;
        } else if (!Objects.equals(this.maxExtents, that.maxExtents)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AABB{" + "minExtents=" + minExtents + ", maxExtents=" + maxExtents + '}';
    }

    public boolean contains(ReadableVector3f point) {
        if (point.getX() > maxExtents.getX()) {
            return false;
        } else if (point.getX() < minExtents.getX()) {
            return false;
        } else if (point.getY() > maxExtents.getY()) {
            return false;
        } else if (point.getY() < minExtents.getY()) {
            return false;
        } else if (point.getZ() > maxExtents.getZ()) {
            return false;
        } else if (point.getZ() < minExtents.getZ()) {
            return false;
        }
        return true;
    }
}
