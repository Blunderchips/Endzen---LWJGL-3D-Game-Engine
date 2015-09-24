package game;

import java.awt.Canvas;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

import engine.Crashform;
import engine.Destroyable;
import engine.GameObject;
import engine.Keyboard;
import engine.Shader;

import game.assets.Window;
import game.assets.Player;

import org.lwjgl.input.Mouse;

import static engine.Keys.*;
import org.lwjgl.opengl.Display;

/**
 * The main Game file.
 */
public final class Game extends engine.Game {

    /**
     * the main <code>Game</code>.
     */
    public static Game main;

    private final static Set<Destroyable> Destroyable = new HashSet<>();

    public static void addDestroyable(Destroyable destroyable) {
        Game.Destroyable.add(destroyable);
    }

    /**
     * "starts" the game.
     */
    public static void Start() {
        Game.main = new Game("Endzen", null);
        Game.main.start();
    }

    private Player player;
    private World world;
    private Shader shader;
    private Window activeWindow;

    private double dtTotal;
    private long numFrames;

    public Game(String title, Canvas parent) {
        super(title, Display.getDesktopDisplayMode() /*new DisplayMode(800, 600)*/, parent, new ArrayList<GameObject>());

        this.dtTotal = 0.0d;
        this.numFrames = 0l;
    }

    @Override
    public void load() {
        try {
            System.out.println("loading...");

            { // loaded GL
//                GL11.glLoadIdentity();
                engine.Mouse.setGrabbed(true);
            }

//            
//            this.shader = new Shader(GLSL.loadShaderPair(
//                    "game/res/shaders/vertex_phong_lighting.vs",
//                    "game/res/shaders/vertex_phong_lighting.fs"
//            ));
//            
            // the main Game world:
            this.activeWindow = new World(this);
            super.addGameObject(activeWindow);
            // --

            Game.main = this;
        } catch (Throwable ex) {
            Mouse.setGrabbed(false);
            super.destroy();
            new Crashform(ex).setVisible(true);
            Thread.currentThread().stop();
            System.exit(1);
        } finally {
            System.out.println("done loading." + "\n");
        }
    }

    @Override
    public void update(float dt) {
        this.numFrames++;
        this.dtTotal += dt;

        dt *= 10;
        
        try {
            { // key events:
                if (Keyboard.isKeyPressed(KEY_F2)) {
                    // take a screenshot and save it in the screenshot file
                } else if (Keyboard.isKeyPressed(KEY_ESCAPE)) {
                    this.onCloseRequested();
                }
            }
            super.updateAllGameObjects(dt);
        } catch (Throwable ex) {
            Mouse.setGrabbed(false);
            super.destroy();
            new Crashform(ex).setVisible(true);
            Thread.currentThread().stop();
        }
    }

    @Override
    public void render() {
        try {
//            this.shader.bind();
            {
                super.renderAllGameObjects();
            }
//            this.shader.realese();
        } catch (Throwable ex) {
            Mouse.setGrabbed(false);
            super.destroy();
            new Crashform(ex).setVisible(true);
            Thread.currentThread().stop();
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public void onCloseRequested() {
        System.out.println("\n" + "Number of Frames: " + numFrames);
        System.out.println("Average: " + (dtTotal / numFrames));

        System.out.println("\n" + "Goodbye :)");

        if (!Destroyable.isEmpty()) {
            for (Destroyable destroyable : Destroyable) {
                destroyable.destroy();
            }
            Destroyable.clear();
        }

        super.onCloseRequested();
    }
}
