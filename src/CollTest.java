
import engine.FreeCameraController;
import engine.Game;
import engine.Keyboard;
import engine.gfx.Model;
import engine.lwjgl.Texture;
import engine.lwjgl.TextureLoader;
import engine.obj.OBJLoader;
import engine.obj.Obj;
import engine.physics.IntersectData;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public class CollTest implements Runnable {

    public static void main(String[] args) {
        new CollTest().run();
    }

    @Override
    public void run() {
        new Game("Coll Test") {

            private int cnt;

            private Model a;
            private Model b;

            @Override
            public void load() {
                super.addGameObject(new FreeCameraController());

                OBJLoader loader = new OBJLoader();
                Texture tex = new TextureLoader().getTexture("game/res/gfx/wood_01.jpg");

                Obj block = loader.loadModel(ClassLoader.getSystemResourceAsStream("game/res/obj/shelter.obj"));

                this.a = new Model(block, loader, new Vector3f(0, 9, 0), new Quaternion(), tex);
                this.b = new Model(block, loader, new Vector3f(0, 0, 0), new Quaternion(), tex);

                super.addGameObject(a);
                super.addGameObject(b);
            }

            @Override
            public void update(float dt) {
                super.updateAllGameObjects(dt);

                if (Keyboard.isKeyDown("down")) {
                    a.translate(0, -1 * dt, 0);
                }
                if (Keyboard.isKeyDown("up")) {
                    a.translate(0, 1 * dt, 0);
                }
                if (Keyboard.isKeyDown("left")) {
                    a.translate(-1 * dt, 0, 0);
                }
                if (Keyboard.isKeyDown("right")) {
                    a.translate(1 * dt, 0, 0);
                }

                IntersectData data = a.intersects(b);
                if (data.isIntersection()) {
                    System.out.println(cnt++);
                }
            }

            @Override
            public void render() {
                super.renderAllGameObjects();
            }
        }.start();
    }
}
