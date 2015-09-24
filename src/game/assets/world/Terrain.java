package game.assets.world;

import game.World;
import engine.lwjgl.TextureLoader;

public final class Terrain extends engine.world.Terrain {

    /**
     * the default seed to be used to generate the terrain.
     */
    private static final int SEED = 8;

    /**
     * the <code>World</code> that the <code>Terrain</code> is attached to.
     */
    private World world;

    public Terrain(World world) {
        super(SEED, world.getWidth(), world.getDepth(), false);

        this.world = world;

        super.setTexture(new TextureLoader().getTexture(
                "game/res/gfx/grass_map.png")
        );
    }

    public World getWorld() {
        return this.world;
    }

    public void attachWorld(World world) {
        this.world = world;
    }

    public void detachWorld() {
        this.world = null;
    }
}
