package game.assets;

import engine.GameObject;
import org.lwjgl.util.vector.ReadableVector3f;

public abstract class Item extends java.lang.Object implements GameObject {

    /**
     * The name of the <code>Item</code>.
     */
    private final String name;

    /**
     * @param name the name of the <code>Item</code>
     */
    public Item(String name) {
        super();

        this.name = name;
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render() {
    }

    /**
     * @return the position of the <code>Item</code> in the world
     */
    public ReadableVector3f getPosition() {
        return null;
    }

    public void die() {
    }

    /**
     * @return the name of the <code>Item</code>
     */
    public String getName() {
        return name;
    }
}
