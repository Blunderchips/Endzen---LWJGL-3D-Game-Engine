package tset;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.MotionState;
import engine.Destroyable;
import engine.GameObject;

public class Entity extends RigidBody implements GameObject, Destroyable {

    public Entity(RigidBodyConstructionInfo rbci) {
        super(rbci);
    }

    public Entity(float f, MotionState ms, CollisionShape cs) {
        super(f, ms, cs);
    }

    public Entity(float f, MotionState ms, CollisionShape cs, javax.vecmath.Vector3f vctrf) {
        super(f, ms, cs, vctrf);
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render() {
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
