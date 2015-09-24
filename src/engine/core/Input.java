package engine.core;

import engine.Mouse;
import engine.Keyboard;
import org.lwjgl.LWJGLException;

public final class Input extends Object {

    /**
     * You can not instantiate this class.
     */
    @Deprecated
    public Input() throws UnsupportedOperationException {
        // Prevents instantiation of this class.
        throw new UnsupportedOperationException(
                "You can not instantiate this class.");
    }

    /**
     * initialises the keyboard and mouse. The display must first have been
     * created.
     *
     * @see engine.Keyboard#init()
     * @see engine.Mouse#init()
     *
     * @throws LWJGLException will throw a <code>DwarfError</code> if a error
     * occurs.
     */
    public static void init() throws LWJGLException {
        Keyboard.init();
        Mouse.init();
    }

    /**
     * updates the keyboard and mouse.
     *
     * @see engine.Keyboard#update()
     * @see engine.Mouse#update()
     */
    public static void update() {
        Mouse.update();
        Keyboard.update();
    }

    /**
     * "Destroys" the keyboard and mouse.
     *
     * @see engine.Keyboard#destroy()
     * @see engine.Mouse#destroy()
     */
    public static void destroy() {
        Keyboard.destroy();
        Mouse.destroy();
    }

    public static void poll() {
        Keyboard.poll();
        Mouse.poll();
    }

    /**
     * @return if true both the keyboard and the mouse is created
     */
    public static boolean isCreated() {
        return Keyboard.isCreated() && Mouse.isCreated();
    }
}
