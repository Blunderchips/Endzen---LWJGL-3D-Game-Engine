package engine.core;

import java.awt.Canvas;
import java.io.Serializable;

import engine.Crashform;
import engine.Destroyable;
import engine.GameObject;

import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.ARBDepthClamp.GL_DEPTH_CLAMP;
import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_AMBIENT_AND_DIFFUSE;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_COLOR_MATERIAL;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FOG_HINT;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LINE_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_POINT_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_COMPRESSION_HINT;
import static org.lwjgl.opengl.GL14.GL_GENERATE_MIPMAP_HINT;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER_DERIVATIVE_HINT;

public abstract strictfp class Engine extends Object implements Runnable,
        Destroyable, GameObject, Serializable {

    /**
     * The serialVersionUID is a universal version identifier for a Serializable
     * class.
     */
    static final long serialVersionUID = 42L;

    /**
     * The main game Window.
     */
    private final Window display;

    /**
     * Constructs a new <code>Engine</code>.
     *
     * @param title the title of the window
     * @param displayMode the displayMode of the window
     * @param parent the parent of the window
     */
    public Engine(String title, DisplayMode displayMode, Canvas parent) {
        super();

        this.display = new Window(title, displayMode, parent);
    }

    /**
     * "Starts" the <code>Engine</code>.
     */
    public void start() {
        if (display.getTitle() == null) {
            new Thread(this).start();
        } else {
            new Thread(this, display.getTitle()).start();
        }
    }

    @Override
    public void run() {
        try {
            this.display.create();
            this.intitGL();
            AL.create();
            Input.init();
        } catch (LWJGLException ex) {
            System.err.println(ex);
            new Crashform(ex).setVisible(true);
        }

        this.load();

        long currentTime;
        long lastUpdateTime = System.nanoTime();

        while (true) {
            currentTime = System.nanoTime();

            if (!display.isCloseRequested()) {
                this.update((float) (currentTime - lastUpdateTime) / 1000000000);
                display.update();

                display.clear();
                this.render();
                display.render();

                Input.update();
            } else {
                this.onCloseRequested();
            }

            lastUpdateTime = currentTime;
        }
    }

    /**
     * This function is called exactly once at the beginning of the game.
     */
    public abstract void load();

    /**
     * Runs on close requested.
     */
    public abstract void onCloseRequested();

    /**
     * Destroys the <code>Engine</code>.
     */
    @Override
    public void destroy() {
        Input.destroy();
        AL.destroy();
        this.display.destroy();
    }

    public final Window Display() {
        return this.display;
    }

    /**
     * Initializes <a href='http://www.opengl.org/'>OpenGL</a>.
     */
    private void intitGL() {
        GL11.glClearColor(0x0f, 0x0f, 0x0f, 0x0f);
        GL11.glClearDepth(0x1d);
        GL11.glEnable(GL_DEPTH_TEST);
        GL11.glEnable(GL_CULL_FACE);
        GL11.glDepthFunc(GL_LEQUAL);
        GL11.glDepthMask(true);

        GL11.glMatrixMode(GL_PROJECTION);
        GL11.glLoadIdentity();

        // --
        GL11.glShadeModel(GL_SMOOTH);
//        GL11.glEnable(GL_LIGHTING);
        GL11.glFrontFace(GL_CCW);
        GL11.glEnable(GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
        // --

        if (GLContext.getCapabilities().GL_ARB_depth_clamp) {
            GL11.glEnable(GL_DEPTH_CLAMP);
        }

        { // render distance:
            final float VIEW_DISTANCE = 256;

            GLU.gluPerspective(0x2f,
                    (float) (display.getWidth()
                    / display.getHeight()),
                    (float) 0.1f, VIEW_DISTANCE
            );
        }

        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glLoadIdentity();

        this.setGLHint(GL_NICEST);

        GL11.glCullFace(GL_BACK);

        GL11.glEnable(GL_BLEND);
        GL11.glEnable(GL_TEXTURE_2D);

        GL11.glEnable(GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL_GREATER, 0.5f);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    }

    public final void setGLHint(int hint) {
        GL11.glHint(GL_FOG_HINT, hint);
        GL11.glHint(GL_LINE_SMOOTH_HINT, hint);
        GL11.glHint(GL_POINT_SMOOTH_HINT, hint);
        GL11.glHint(GL_POLYGON_SMOOTH_HINT, hint);
        GL11.glHint(GL_TEXTURE_COMPRESSION_HINT, hint);
        GL11.glHint(GL_FRAGMENT_SHADER_DERIVATIVE_HINT, hint);
        GL11.glHint(GL_GENERATE_MIPMAP_HINT, hint);
        GL11.glHint(GL_PERSPECTIVE_CORRECTION_HINT, hint);
    }
}
