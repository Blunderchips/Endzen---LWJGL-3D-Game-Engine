package engine.physics;

/**
 * @author Matthew Van der Bijl
 */
public interface Collidable {

    public abstract AABB getAABB();

    public abstract IntersectData intersects(AABB coll);

    public abstract RadialCollider getRadialCollider();

    public abstract IntersectData intersects(RadialCollider coll);

    public abstract IntersectData intersects(Collidable coll);
}
