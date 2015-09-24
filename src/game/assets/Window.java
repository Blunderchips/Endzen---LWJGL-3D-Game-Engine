package game.assets;

import game.Game;
import engine.GameObject;
import engine.Destroyable;

/**
 * TODO.
 *
 * @author Matthew
 */
public interface Window extends GameObject, Destroyable {

    public abstract Game getGame();

    /**
     * @return the name of the <code>Window</code>
     */
    public abstract String getName();
}
