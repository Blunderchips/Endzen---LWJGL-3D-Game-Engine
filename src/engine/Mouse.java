package engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.ReadableVector2f;

/**
 * Provides an interface to the user's Mouse.
 *
 * @author Matthew 'siD' Van der Bijl
 *
 * @see enigne.engine.core.Input
 * @see org.lwjgl.input.Mouse
 */
public final class Mouse {

    /**
     * You can not instantiate this class.
     */
    @Deprecated
    public Mouse() throws UnsupportedOperationException {
        // Prevents instantiation of this class.
        throw new UnsupportedOperationException(
                "You can not instantiate this class.");
    }

    /**
     * The left Mouse button.
     */
    public static final byte MOUSE_LEFT = 0x0;
    /**
     * The right Mouse button.
     */
    public static final byte MOUSE_RIGHT = 0x1;
    /**
     * The middle Mouse button. (scroller click)
     */
    public static final byte MOUSE_MIDDLE = 0x2;
    public static final byte NUM_MOUSE_BUTTONS = (byte) org.lwjgl.input.Mouse.getButtonCount();
    // -- 
    private static boolean[] currentButtons;
    private static boolean[] downButtons;
    private static boolean[] upButtons;

    /**
     * initialization the Mouse. The display must first have been created.
     * Initially, the Mouse is not grabbed and the delta values are reported
     * with respect to the center of the display.
     *
     * @throws LWJGLException shit happens
     */
    public static void init() throws LWJGLException {
        org.lwjgl.input.Mouse.create();

        Mouse.currentButtons = new boolean[NUM_MOUSE_BUTTONS];
        Mouse.downButtons = new boolean[NUM_MOUSE_BUTTONS];
        Mouse.upButtons = new boolean[NUM_MOUSE_BUTTONS];
    }

    /**
     * Updates the Mouse.
     */
    public static void update() {
        for (short btn = 0; btn < NUM_MOUSE_BUTTONS; btn++) {
            boolean isDown = org.lwjgl.input.Mouse.isButtonDown(btn);
            if (isDown) {
                downButtons[btn] = !currentButtons[btn];
            } else {
                upButtons[btn] = currentButtons[btn];
            }
            currentButtons[btn] = isDown;
        }
    }

    /**
     * "destroys" the keyboard.
     */
    public static void destroy() {
        org.lwjgl.input.Mouse.destroy();
    }

    public static ReadableVector2f getPosition() {
        return new Vector2f(
                org.lwjgl.input.Mouse.getX(),
                org.lwjgl.input.Mouse.getY());
    }

    /**
     * @return the X position of the Mouse (int)
     * @see org.lwjgl.input.Mouse#getX()
     */
    public static int getPosX() {
        return org.lwjgl.input.Mouse.getX();
    }

    /**
     * @return the Y position of the Mouse (int)
     * @see org.lwjgl.input.Mouse#getY()
     */
    public static int getPosY() {
        return org.lwjgl.input.Mouse.getY();
    }

    /**
     * Sets the current position of the Mouse.
     *
     * @param pos The new position of the Mouse
     */
    public static void setPosition(ReadableVector2f pos) {
        org.lwjgl.input.Mouse.setCursorPosition((int) pos.getX(), (int) pos.getY());
    }

    public static void setPosition(int xPos, int yPos) {
        Mouse.setPosition(new Vector2f(xPos, yPos));
    }

    public static boolean isCreated() {
        return org.lwjgl.input.Mouse.isCreated();
    }

    public static void poll() {
        org.lwjgl.input.Mouse.poll();
    }

    /**
     * returns true if the Mouse has a wheel if not it will return false.
     *
     * @see org.lwjgl.input.Mouse#hasWheel()
     * @return whether or not this Mouse has wheel support
     */
    public static boolean hasWheel() {
        return org.lwjgl.input.Mouse.hasWheel();
    }

    public static boolean isGrabbed() {
        return org.lwjgl.input.Mouse.isGrabbed();
    }

    public static void setGrabbed(boolean grabbed) {
        org.lwjgl.input.Mouse.setGrabbed(grabbed);
    }

    //========================================================================
    // Mouse events
    //========================================================================
    
    public static int getDWheel() {
        return org.lwjgl.input.Mouse.getDWheel();
    }

    /**
     * Checks whether a certain Mouse button is down.
     *
     * @see org.lwjgl.input.Mouse#isButtonDown(int)
     *
     * @param mouseButton the key code of the button to be tested
     * @return true if the Mouse button is down, otherwise it will return false.
     */
    public static boolean isMouseDown(int mouseButton) {
        return Mouse.currentButtons[mouseButton];
    }

    /**
     * checks if a Mouse key is downs.
     *
     * @see enigne.Mouse#isMouseDown(int)
     * @see org.lwjgl.input.Mouse#isButtonDown(int)
     *
     * @param buttonName the name of the button to be tested
     * @return true if the Mouse button is down, otherwise it will return false.
     */
    public static boolean isMouseDown(String buttonName) {
        return Mouse.isMouseDown(Mouse.getKeyCode(buttonName));
    }

    /**
     * checks if a Mouse key is clicked.
     *
     * @param mouseButton the key code of the button to be tested
     * @return return upMouse.contains(upKeys)
     */
    public static boolean isMouseClicked(int mouseButton) {
        return Mouse.downButtons[mouseButton];
    }

    /**
     * checks if a Mouse key is clicked, callback function triggered when a
     * Mouse button is pressed.
     *
     * @see enigne.Mouse#isMouseClicked(int)
     *
     * @param buttonName the name of the button to be tested
     * @return true if the Mouse button is clicked, otherwise it will return
     * false.
     */
    public static boolean isMouseClicked(String buttonName) {
        return Mouse.isMouseClicked(getKeyCode(buttonName));
    }

    /**
     * checks if the left button Mouse key is clicked, callback function
     * triggered when a Mouse button is pressed.
     *
     * @see enigne.Mouse#isMouseClicked(int)
     *
     * @return true is the left Mouse button is clicked otherwise false
     */
    public static boolean isMouseClicked() {
        return Mouse.isMouseClicked(MOUSE_LEFT);
    }

    /**
     * checks if a Mouse key is released, callback function triggered when a
     * Mouse button is released.
     *
     * @param mouseButton the key code of the button to be tested
     * @return true if the Mouse button is realesed, otherwise it will return
     * false.
     */
    public static boolean isMouseRealesed(int mouseButton) {
        return Mouse.upButtons[mouseButton];
    }

    /**
     * checks if a Mouse key is released, callback function triggered when a
     * Mouse button is released.
     *
     * @see enigne.Mouse#isMouseRealesed(int)
     *
     * @param buttonName the name of the button to be tested
     * @return true if the Mouse button is realesed, otherwise it will return
     * false.
     */
    public static boolean isMouseRealesed(String buttonName) {
        return Mouse.isMouseRealesed(getKeyCode(buttonName));
    }

    /**
     * Gets a button's name.
     *
     * @param code The button code to be tested
     * @throws DwarfException will throw if no suitable result is found
     * @return a String with the button's human readable name in it or will
     * throw a IllegalArgumentException if the button is unnamed
     */
    private static byte getKeyCode(String code) throws DwarfException {
        switch (code) {
            case "left":
                return MOUSE_LEFT;
            case "right":
                return MOUSE_RIGHT;
            case "scroler":
                return MOUSE_MIDDLE;
            default:
                throw new DwarfException("illegal argument. (keycode '" + code + "' is unknown)");
        }
    }

    /**
     * Retrieves whether or not the Mouse cursor is within the bounds of the
     * window. If the Mouse cursor was moved outside the display during a drag,
     * then the result of calling this method will be true until the button is
     * released.
     *
     * @see org.lwjgl.input.Mouse#isInsideWindow()
     *
     * @return true if Mouse is inside display, false otherwise.
     */
    public static boolean isInsideWindow() {
        return org.lwjgl.input.Mouse.isInsideWindow();
    }
}
