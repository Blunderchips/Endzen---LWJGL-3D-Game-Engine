package engine;

import java.util.List;
import java.awt.Canvas;
import java.util.Objects;
import java.util.ArrayList;

import engine.core.Engine;

import org.lwjgl.opengl.DisplayMode;

/**
 * the main <code>Game</code> file and is the entry point to the
 * <code>Dwarf Engine</code>. Create and runs the main game window and acts as a
 * buffer between the user and the main <code>Engine</code> file.
 *
 * @author Matthew 'siD' Van der Bijl
 *
 * @see enigne.engine.core.Engine
 * @see enigne.engine.core.Window
 */
public abstract class Game extends Engine {

    /**
     * The main <code>GameObject</code> <code>ArrayList</code>.
     */
    private List<GameObject> gameObjects;

    /**
     * Default constructor.
     */
    public Game() {
        this(null, new DisplayMode(800, 600), null, new ArrayList<GameObject>());
    }

    /**
     * Constructs a new <code>Game</code>.
     *
     * @param title the title of the window
     */
    public Game(String title) {
        this(title, new DisplayMode(800, 600), null, new ArrayList<GameObject>());
    }

    /**
     * Constructs a new <code>Game</code>.
     *
     * @param title the title of the window
     * @param displayMode the displayMdoe of th window
     * @param parent the parent of the window
     * @param gameObjects the GameObjects List
     */
    public Game(String title, DisplayMode displayMode, Canvas parent,
            List<GameObject> gameObjects) {
        super(title, displayMode, parent);
        this.gameObjects = gameObjects;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Game() {
            @Override
            public void load() {
                // This function is called exactly once at the beginning of the game.
            }

            @Override
            public void update(float dt) {
                // Callback function used to update the state of the game every frame.
            }

            @Override
            public void render() {
                // Callback function used to render on the screen every frame.
            }
        }.start();
    }

    @Override
    public void onCloseRequested() {
        this.close(false);
    }

    /**
     * @return the main <code>GameObject</code>
     */
    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    public void setGameObjects(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    /**
     * @param obj the <code>GameObject</code> to be added
     */
    public void addGameObject(GameObject obj) {
        this.gameObjects.add(obj);
    }

    public void updateAllGameObjects(float dt) {
        for (int i = 0; i < this.gameObjects.size(); i++) {
            if (this.gameObjects.get(i) == null) {
                this.gameObjects.remove(i);
            } else {
                this.gameObjects.get(i).update(dt);
            }
        }
    }

    public void renderAllGameObjects() {
        for (int i = 0; i < this.gameObjects.size(); i++) {
            if (this.gameObjects.get(i) == null) {
                this.gameObjects.remove(i);
            } else {
                this.gameObjects.get(i).render();
            }
        }
    }

    /**
     * reset <code>GameObject</code> in the main <code>GameObject</code>
     * <code>ArrayList</code>.
     */
    public void resetGameObjects() {
        this.gameObjects = new ArrayList<>();
    }

    /**
     * clears all <code>GameObject</code> in the main <code>GameObject</code>
     * <code>ArrayList</code>.
     */
    public void clearGameObjects() {
        this.gameObjects.clear();
    }

    /**
     * @return the number of <code>GameObject</code> in the main
     * <code>GameObject</code> <code>ArrayList</code>.
     */
    public int getNumGameObjects() {
        return this.gameObjects.size();
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
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(gameObjects);
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
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        } else if (super.getClass() != obj.getClass()) {
            return false;
        }

        final Game that = (Game) obj;

        if (!Objects.equals(this.gameObjects, that.gameObjects)) {
            return false;
        }
        return true;
    }

    public void destroyAllGameObjects() {
        for (GameObject gameObject : this.gameObjects) {
            if (gameObject != null && gameObject instanceof Destroyable) {
                Destroyable obj = (Destroyable) gameObject;
                obj.destroy();
            }
        }
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
        return "Game[" + getTitle() + "]";
    }

    public void close(boolean asCrash) {
        this.destroy();
//        System.gc();
        System.exit(asCrash ? 1 : 0);
    }

    /**
     * "destroys" the <code>Game</code>.
     *
     * @see enigne.engine.core.Engine#destroy()
     * @see enigne.Game#destroyAllGameObjects()
     */
    @Override
    public void destroy() {
        this.destroyAllGameObjects();
        super.destroy();
    }

    /**
     * @return <code>this</code>
     */
    public Game get() {
        return this;
    }

    /**
     * @return the title of the game
     */
    public String getTitle() {
        return super.Display().getTitle();
    }

    /**
     * @return the parent of the game window
     */
    public Canvas getParent() {
        return super.Display().getParent();
    }
}
