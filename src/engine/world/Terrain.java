package engine.world;

import engine.util.PerlinNoiseGenerator;

/**
 * Generates terrain using Perlin noise.
 *
 * @author Matthew 'siD' Van der Bijl
 *
 * @see enigne.world.Heightmap
 * @see enigne.util.PerlinNoiseGenerator
 */
public class Terrain extends Heightmap {

    /**
     * Default seed to use for the random terrain generation.
     */
    private static final int DEFAULT_SEED = 0x3;

    public Terrain(int seed, int width, int height) {
        this(seed, width, height, false);
    }

    public Terrain(int width, int height) {
        this(DEFAULT_SEED, width, height, false);
    }

    public Terrain(int width, int height, boolean isWireFrame) {
        this(DEFAULT_SEED, width, height, isWireFrame);
    }

    public Terrain(int seed, int width, int height, boolean isWireFrame) {
        super(new PerlinNoiseGenerator(seed).getBufferedImage(seed, width + 1, height + 1), isWireFrame);
    }
}
