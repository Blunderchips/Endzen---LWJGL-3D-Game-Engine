package game.assets.world;

import engine.DwarfException;
import engine.GameObject;
import game.World;
import java.nio.FloatBuffer;
import java.util.Objects;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_EXP;
import static org.lwjgl.opengl.GL11.GL_EXP2;
import static org.lwjgl.opengl.GL11.GL_FOG;
import static org.lwjgl.opengl.GL11.GL_FOG_COLOR;
import static org.lwjgl.opengl.GL11.GL_FOG_DENSITY;
import static org.lwjgl.opengl.GL11.GL_FOG_END;
import static org.lwjgl.opengl.GL11.GL_FOG_MODE;
import static org.lwjgl.opengl.GL11.GL_FOG_START;
import static org.lwjgl.opengl.GL11.GL_LINEAR;

/**
 * Basic Fog class.
 */
public final class Fog implements GameObject {

    /**
     * the <code>World</code> that the <code>Fog</code> is attached to.
     */
    private World world;
    private int fogMode;
    private float startDist;
    private float endDist;

    public Fog(World world) {
        super();

        this.world = world;

        // default settings:
        this.startDist = 5.0f;
        this.endDist = 20.0f;
        this.fogMode = GL_EXP2;
        // -- 

        this.initGL();
    }

    private void initGL() {
        GL11.glEnable(GL_FOG);
        {
            GL11.glFogf(GL_FOG_MODE, fogMode);
            GL11.glFogf(GL_FOG_DENSITY, 0.008f);
            GL11.glFogf(GL_FOG_START, startDist);
            GL11.glFogf(GL_FOG_END, endDist);
        }
    }

    @Override
    public void update(float dt) {
//            if (isDay()) {
//                GL11.glFogf(GL_FOG_DENSITY, (float) (0.1 * (1 - getTime()) + 0.003 * getTime())); // (0,0.1) (1.031,0) 
//            }
    }

    @Override
    public void render() {
        FloatBuffer fogColour = BufferUtils.createFloatBuffer(4); // the colour of the fog
        fogColour.put((float) world.getSky().getSkyColour().getRed()).put(
                (float) world.getSky().getSkyColour().getGreen()).put(
                        (float) world.getSky().getSkyColour().getBlue()).put(
                        (float) world.getSky().getSkyColour().getAlpha()).flip();
        GL11.glFog(GL_FOG_COLOR, fogColour);
    }

    /**
     * attaches the Fog to a world.
     */
    public void attachWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }

    public float getStartDist() {
        return startDist;
    }

    public void setStartDist(float startDist) {
        this.startDist = startDist;
    }

    public float getEndDist() {
        return this.endDist;
    }

    public void setEndDist(float endDist) {
        this.endDist = endDist;
    }

    /**
     * reinitialize the <code>Fog</code>.
     */
    public void reinitialize() {
        this.initGL();
    }

    /**
     * <img src="http://wiki.delphigl.com/images/8/86/GlFog_equation_diagram.png"/>.
     * @param fogMode GL_LINEAR or GL_EXP or GL_EXP2
     * @throws DwarfException illegal argument
     */
    public void setFogMode(int fogMode) throws DwarfException {
        if (fogMode == GL_LINEAR || fogMode == GL_EXP || fogMode == GL_EXP2) {
            this.fogMode = fogMode;
        } else {
            throw new DwarfException("illegal argument.");
        }
    }

    @Override
    public String toString() {
        return "Fog[fogMode: " + fogMode + ", "
                + "startDist: " + startDist + ", "
                + "endDist: " + endDist + "]";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.world);
        hash = 53 * hash + this.fogMode;
        hash = 53 * hash + Float.floatToIntBits(this.startDist);
        hash = 53 * hash + Float.floatToIntBits(this.endDist);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (super.getClass() != obj.getClass()) {
            return false;
        }

        final Fog other = (Fog) obj;

        if (this.fogMode != other.fogMode) {
            return false;
        } else if (Float.floatToIntBits(this.startDist)
                != Float.floatToIntBits(other.startDist)) {
            return false;
        } else if (Float.floatToIntBits(this.endDist)
                != Float.floatToIntBits(other.endDist)) {
            return false;
        }
        return true;
    }

    public void detachWorld() {
        this.world = null;
    }
}
