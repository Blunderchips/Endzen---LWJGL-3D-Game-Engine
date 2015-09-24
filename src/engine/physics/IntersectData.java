package engine.physics;

/**
 * @author Matthew Van der Bijl
 */
public class IntersectData extends Object {

    private final float distance;
    private final boolean intersection;

    public IntersectData(boolean intersects, float distance) {
        super();

        this.distance = distance;
        this.intersection = intersects;
    }

    public float getDistance() {
        return this.distance;
    }

    public boolean isIntersection() {
        return this.intersection;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Float.floatToIntBits(distance);
        hash = 89 * hash + (intersection ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        } else if (super.getClass() != obj.getClass()) {
            return false;
        }

        final IntersectData that = (IntersectData) obj;

        if (Float.floatToIntBits(this.distance)
                != Float.floatToIntBits(that.distance)) {
            return false;
        } else if (this.intersection != that.intersection) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "IntersectData["
                + "distance:" + distance
                + ", intersection:" + intersection + "]";
    }

}
