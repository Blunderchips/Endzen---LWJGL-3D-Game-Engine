package game.assets.world;

import java.util.Objects;

import game.World;
import engine.Destroyable;
import engine.GameObject;
import engine.gfx.Colour;
import engine.lwjgl.TextureLoader;
import engine.world.SkyDome;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * Basic Sky class to be attached to the world.
 */
public final class Sky implements GameObject, Destroyable {

    /**
     * the colour of the sky at midday.
     */
    private static final int SKY_BLUE = 0x0099CC;
    /**
     * the colour of the sky at twilight.
     */
    private static final int EVENING_ORANGE = 0xF2380A;

    private SkyDome skyDome;

    /**
     * the <code>World</code> that the <code>Sky</code> is attached to.
     */
    private World world;
    /**
     * the colour of the sky.
     */
    private final Colour skyColour;

    public Sky(World world) {
        super();

        this.world = world;
        this.skyColour = new Colour(SKY_BLUE); // defualt sky colour
        this.skyDome = new SkyDome(
                new TextureLoader().getTexture("proceduralsky4.jpg"),
                128, 1, 1, 1, 1
        );
    }

    @Override
    public void update(float dt) {
        /*
         * don't ask questions... it works... against all logic, it works... 
         */
        {
            float p = (float) world.getTime();

            float red; // the red value of the sky
            float green; // the green value of the sky
            float blue; // the blue value of the sky

            if (world.isDay()) { // day
                red = (float) (new Colour(SKY_BLUE).getRed() / 2 * p + new Colour(EVENING_ORANGE).getRed() * (1 - p));
                green = (float) (new Colour(SKY_BLUE).getGreen() / 2 * p + new Colour(EVENING_ORANGE).getGreen() * (1 - p));
                blue = (float) (new Colour(SKY_BLUE).getBlue() / 2 * p + new Colour(EVENING_ORANGE).getBlue() * (1 - p));
            } else { // night
                red = (float) (new Colour(EVENING_ORANGE).getRed() * (1 - p));
                green = (float) (new Colour(EVENING_ORANGE).getGreen() * (1 - p));
                blue = (float) (new Colour(EVENING_ORANGE).getBlue() * (1 - p));
            }
            this.skyColour.set(red, green, blue);
        }
        this.skyDome.setCenter(new Vector3f(
                world.getPlayer().getPosition().getX(),
                world.getPlayer().getPosition().getY() - (64),
                -world.getPlayer().getPosition().getZ()
        ));
        this.skyDome.update(dt);
    }

    @Override
    public void render() {
        GL11.glClearColor((float) skyColour.getRed(), (float) skyColour.getGreen(),
                (float) skyColour.getGreen(), (float) skyColour.getAlpha());

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        {
            this.skyDome.render();
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public World getWorld() {
        return this.world;
    }

    /**
     * attaches the Sky to a world.
     */
    public void attachWorld(World world) {
        this.world = world;
    }

    /**
     * @return the sky colour
     */
    public Colour getSkyColour() {
        return this.skyColour;
    }

    @Override
    public void destroy() {
        this.skyDome.destroy();
    }

    @Override
    public String toString() {
        return "Sky[skyColour: " + skyColour + "]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(skyDome);
        hash = 71 * hash + Objects.hashCode(skyColour);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (super.getClass() != obj.getClass()) {
            return false;
        }

        final Sky other = (Sky) obj;

        if (!Objects.equals(this.skyDome, other.skyDome)) {
            return false;
        } else if (!Objects.equals(this.skyColour, other.skyColour)) {
            return false;
        }
        return true;
    }

    public void detachWorld() {
        this.world = null;
    }

    public SkyDome getSkyDome() {
        return this.skyDome;
    }

    public void setSkyDome(SkyDome skyDome) {
        this.skyDome = skyDome;
    }
}
