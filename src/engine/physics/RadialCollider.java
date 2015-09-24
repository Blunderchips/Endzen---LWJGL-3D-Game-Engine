package engine.physics;

import java.util.Objects;

import engine.Math;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.ReadableVector3f;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;

public class RadialCollider extends Object {

    private float radiusSq;
    private ReadableVector3f position;

    /**
     * Default constructor.
     */
    public RadialCollider() {
        this(0, null);
    }

    public RadialCollider(float radiusSq, ReadableVector3f position) {
        super();

        this.radiusSq = radiusSq;
        this.position = position;
    }

    public IntersectData intersects(RadialCollider coll) {
        if (this == null || coll == null) {
            return new IntersectData(false, 0);
        } else if (this.equals(coll)) {
            return new IntersectData(true, 0);
        }

        float distSq = Math.distanceSq(this.getPosition(), coll.getPosition());
        return new IntersectData(
                distSq <= Math.min(this.getRadiusSq(), coll.getRadiusSq()),
                Math.sqrt(distSq)
        );
    }

    public ReadableVector3f getPosition() {
        return this.position;
    }

    public void setPosition(ReadableVector3f position) {
        this.position = position;
    }

    public float getRadiusSq() {
        return this.radiusSq;
    }

    public float getRadius() {
        return Math.sqr(radiusSq);
    }

    public void setRadiusSq(float radiusSq) {
        this.radiusSq = radiusSq;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Float.floatToIntBits(radiusSq);
        hash = 37 * hash + Objects.hashCode(position);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        } else if (super.getClass() != obj.getClass()) {
            return false;
        }

        final RadialCollider other = (RadialCollider) obj;

        if (Float.floatToIntBits(this.radiusSq)
                != Float.floatToIntBits(other.radiusSq)) {
            return false;
        } else if (!Objects.equals(this.position, other.position)) {
            return false;
        }

        return true;
    }

    @Deprecated
    public void render() {
        GL11.glPushMatrix();
        {
            GL11.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

            GL11.glTranslatef(
                    this.getPosition().getX(),
                    this.getPosition().getY(),
                    this.getPosition().getZ()
            );

            new Sphere().draw(radiusSq, 30, 30);

            GL11.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        }
        GL11.glPopMatrix();
    }
}
