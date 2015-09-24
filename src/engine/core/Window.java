package engine.core;

import java.awt.Canvas;
import java.io.Serializable;
import java.util.Objects;

import engine.Destroyable;

import org.lwjgl.opengl.GL11;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

/**
 *
 */
public final class Window implements Destroyable, Serializable {

    /**
     * The serialVersionUID is a universal version identifier for a Serializable
     * class.
     */
    static final long serialVersionUID = 42L;

    /**
     * The frame cap of the <code>window</code>.
     */
    private int frameCap;
    /**
     * The title of the <code>window</code>.
     */
    private String title;
    /**
     * The parent of the <code>window</code>.
     */
    private Canvas parent;
    private DisplayMode displayMode;

    /**
     * Constructs a new <code>Window</code>.
     *
     * @see org.lwjgl.opengl.DisplayMode
     * @param displayMode the DisplayMode of the <code>Window</code>
     */
    public Window(DisplayMode displayMode) {
        this(null, displayMode, null);
    }

    /**
     * Constructs a new <code>Window</code>.
     *
     * @param title the title of the <code>Window</code>
     * @param displayMode the DisplayMode of the <code>Window</code>
     *
     * @see org.lwjgl.opengl.DisplayMode
     */
    public Window(String title, DisplayMode displayMode) {
        this(title, displayMode, null);
    }

    /**
     * Constructs a new <code>Window</code>.
     *
     * @param title the title of the <code>Window</code>
     * @param displayMode the DisplayMode of the <code>Window</code>
     * @param parent the parent of the <code>Window</code>
     *
     * @see java.awt.Canvas
     * @see org.lwjgl.opengl.DisplayMode
     */
    public Window(String title, DisplayMode displayMode, Canvas parent) {
        super();

        this.title = title;
        this.parent = parent;
        this.displayMode = displayMode;

        this.frameCap = 128; // default frame cap
    }

    /**
     * Creates the <code>Window</code>.
     *
     * @see org.lwjgl.LWJGLException
     * @throws org.lwjgl.LWJGLException shit happens
     */
    public void create() throws LWJGLException {
        try { //create the Display. 

            Display.setFullscreen(false);
            Display.setVSyncEnabled(true);
            Display.setResizable(false);
            Display.setTitle(title);
            Display.setParent(parent);
            Display.setDisplayMode(displayMode);
            Display.create();

            {
                this.title = null;
                this.parent = null;
                this.displayMode = null;
            }
        } catch (LWJGLException ex) {
            System.err.println("The display wasn't initialized correctly. :(");
            Display.destroy();
            throw ex;
        }
    }

    /**
     * Updates the window.
     */
    public void update() {
        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glLoadIdentity();
    }

    /**
     * Renders the window.
     */
    public void render() {
        Display.update();
        Display.sync(frameCap);
    }

    /**
     * Clears the window.
     */
    public void clear() {
        GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public DisplayMode getDisplayMode() {
        return Display.getDisplayMode();
    }

    /**
     * Destroys the display.
     */
    @Override
    public void destroy() {
        Display.destroy();
    }

    /**
     * @return true if the user or operating system has asked the
     * <code>Window</code> to close
     */
    public boolean isCloseRequested() {
        return Display.isCloseRequested();
    }

    /**
     * Returns a string representation of the object.
     * <p>
     * In general, the toString method returns a string that "textually
     * represents" this object. The result should be a concise but informative
     * representation that is easy for a person to read. It is recommended that
     * all subclasses override this method.</p>
     *
     * @return a textually representation of this object
     */
    @Override
    public String toString() {
        return "Window[" + displayMode + "]";
    }

    /**
     * Class Object is the root of the class hierarchy.
     * <p>
     * Every class has Object as a superclass. All objects, including arrays,
     * implement the methods of this class.</p>
     *
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(title);
        hash = 47 * hash + Objects.hashCode(displayMode);
        return hash;
    }

    /**
     * Returns true if the <code>this</code> is equal to the argument and false
     * otherwise.
     * <p>
     * Consequently, if both argument are null, true is returned, false is
     * returned. Otherwise, equality is determined by using the equals method of
     * the first argument.</p>
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @param obj the <code>Object</code> to be tested
     *
     * @return true if the argument is equal to <code>this</code> other and
     * false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (super.getClass() != obj.getClass()) {
            return false;
        }

        final Window other = (Window) obj;

        if (!Objects.equals(this.displayMode, other.displayMode)) {
            return false;
        }

        return true;
    }

    /**
     * @return the parent of the window
     */
    public Canvas getParent() {
        return Display.getParent();
    }

    /**
     * @param frameCap the new frame cap of the window
     */
    public void setFrameCap(int frameCap) {
        this.frameCap = frameCap;
    }

    /**
     * @return the frame cap of the window
     */
    public int getFrameCap() {
        return this.frameCap;
    }

    /**
     * @return the height of the window
     */
    public int getHeight() {
        return Display.getHeight();
    }

    /**
     * @return the width of the window
     */
    public int getWidth() {
        return Display.getWidth();
    }

    /**
     * @return the title of the window
     */
    public String getTitle() {
        return Display.getTitle();
    }

    public boolean isFullScreen() {
        return Display.isFullscreen();
    }

    public void setVSyncEnabled(boolean vSyncEnabled) {
        Display.setVSyncEnabled(vSyncEnabled);
    }
}
