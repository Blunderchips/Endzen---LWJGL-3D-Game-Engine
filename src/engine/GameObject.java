package engine;

/**
 * Description of anything that can be rendered and/or updated by the game
 * engine.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public interface GameObject {

    /**
     * Callback function used to update the state of the game every frame.
     *
     * @param dt Delta Time is the time it takes for the computer to go through
     * all the processing/rendering for a single frame. It is dynamically
     * updated, so it can fluctuate depending on what level of processing the
     * last frame required.
     */
    public abstract void update(float dt);

    /**
     * Callback function used to render on the screen every frame.
     */
    public abstract void render();
}
