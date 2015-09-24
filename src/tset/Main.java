package tset;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import engine.Game;
import engine.GameObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.vecmath.Vector3f;
import org.lwjgl.opengl.DisplayMode;

public class Main extends Game {

    private DynamicsWorld world;
    private Set<Entity> entitys;

    public Main(String title) {
        this(title, new DisplayMode(800, 600));
    }

    public Main(String title, DisplayMode displayMode) {
        super(title, displayMode, null, new ArrayList<GameObject>());

        this.entitys = new HashSet<>();

        BroadphaseInterface broadphase = new DbvtBroadphase();
        CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
        CollisionDispatcher collisionDispatcher = new CollisionDispatcher(collisionConfiguration);
        ConstraintSolver solver = new SequentialImpulseConstraintSolver();

        this.world = new DiscreteDynamicsWorld(collisionDispatcher, broadphase,
                solver, collisionConfiguration);
        this.world.setGravity(new Vector3f(0, -10, 0));
        
        this.world.addRigidBody(new RigidBody(0, new DefaultMotionState(), new BvhTriangleMeshShape()));
    }

    @Override
    public void load() {

    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render() {
    }
}
