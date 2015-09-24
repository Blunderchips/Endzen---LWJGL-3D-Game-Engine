package game.assets.entitys;

import java.util.Objects;

import engine.Crashform;
import engine.Destroyable;
import engine.DwarfException;
import engine.Random;
import engine.lwjgl.Texture;
import engine.lwjgl.TextureLoader;
import engine.physics.AABB;
import engine.physics.AABBMesh;
import game.Game;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.ReadableVector3f;

import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_EXP2;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;

/**
 *
 * @author matthew
 */
public final class Grass extends game.assets.Entity {

    private static final int GRASS_DISPLAY_LIST;

    /**
     * initialises the <code>Grass</code> display list.
     */
    static {
        final Texture tex = new TextureLoader().getTexture("game/res/gfx/grass.png");
        GRASS_DISPLAY_LIST = GL11.glGenLists(1);

        GL11.glNewList(GRASS_DISPLAY_LIST, GL_COMPILE);
        {
            GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
            GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

            GL30.glGenerateMipmap(GL_TEXTURE_2D);
            {
                GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_EXP2);
                GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_EXP2);
                GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
            }

            tex.bind();

            float size = 1;
            float angle = 0;
            int numFaces = 8;

            for (int i = 0; i < numFaces; i++) {
                GL11.glPushMatrix();
                {
                    GL11.glRotatef(angle, 0, 1, 0);

                    GL11.glPushMatrix();
                    {
                        GL11.glTranslatef(-(size / 2f), 0, 0);

                        GL11.glBegin(GL_QUADS);
                        {
                            GL11.glTexCoord2f(0, size);
                            GL11.glVertex2d(0, 0);

                            GL11.glTexCoord2d(0, 0);
                            GL11.glVertex2d(0, size);

                            GL11.glTexCoord2d(1, 0);
                            GL11.glVertex2d(size, size);

                            GL11.glTexCoord2d(size, size);
                            GL11.glVertex2d(size, 0);
                        }
                        GL11.glEnd();
                        GL11.glFlush();
                    }
                    GL11.glPopMatrix();

                    angle += (360 / numFaces);
                }
            }
            GL11.glPopMatrix();

            tex.release();
        }
        GL11.glEndList();

        Game.addDestroyable(new Destroyable() {

            @Override
            public void destroy() {
                GL11.glDeleteLists(GRASS_DISPLAY_LIST, 1);
            }
        });

        Game.addDestroyable(new Destroyable() {

            @Override
            public void destroy() {
                GL11.glDeleteTextures(tex.getTextureID());
            }
        });
    }
    /**
     * The rotation of the <code>Grass</code>.
     */
    private final int rotation;
    /**
     * The position of the <code>Grass</code> in the world.
     */
    private final Vector3f position;

    /**
     * Constructs a new <code>Grass</code>.
     *
     * @param position the position of the Grass in the World
     */
    public Grass(ReadableVector3f position) {
        super("Grass");

        this.rotation = Random.interger(360);
        this.position = new Vector3f(position);
    }

    /**
     * @return rotation of the <code>Grass</code>
     */
    @Override
    public int getRotation() {
        return this.rotation;
    }

    /**
     * @return the position of the <code>Grass</code> in the world
     */
    @Override
    public Vector3f getPosition() {
        return this.position;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Float.floatToIntBits(rotation);
        hash = 53 * hash + Objects.hashCode(position);
        return hash;
    }

    @Override
    public boolean equals(final java.lang.Object obj) {
        if (obj == null) {
            return false;
        } else if (super.getClass() != obj.getClass()) {
            return false;
        }

        final Grass other = (Grass) obj;

        if (Float.floatToIntBits(this.rotation) != Float.floatToIntBits(other.rotation)) {
            return false;
        } else if (!Objects.equals(this.position, other.position)) {
            return false;
        }

        return true;
    }

    @Override
    public int getDisplayList() {
        return GRASS_DISPLAY_LIST;
    }

    @Override
    public AABBMesh getMesh() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AABB getAABB() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
