package game.assets.world;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import game.World;
import engine.Util;
import engine.Destroyable;
import engine.GameObject;
import engine.lwjgl.Texture;
import engine.gfx.Colour;
import engine.lwjgl.TextureLoader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.ReadableVector3f;

import static engine.Util.asFlippedFloatBuffer;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FOG;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LIGHT_MODEL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW_MATRIX;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2d;

/**
 * Basic Sun class. TODO: sun colour
 */
public final class Sun extends Object implements Destroyable, GameObject {

    /**
     * The default height of the sun.
     */
    private static final int DEFAULT_HEIGHT = 300;

    private int glLight;
    private float height;

    /**
     * the <code>World</code> that the <code>Sun</code> is attached to.
     */
    private World world;
    /**
     * the position of the <code>Sun</code> in the <code>World</code>.
     */
    private Vector3f position;
    /**
     * the texture of the sun.
     */
    private Texture texture;
    /**
     * the colour of the sun
     */
    private Colour colour;
    private int displayList;

    public Sun(World world) {
        this(world, DEFAULT_HEIGHT);
    }

    public Sun(World world, float height) {
        super();

        this.glLight = GL11.GL_LIGHT7;

        this.world = world;
        this.height = height;
        this.colour = new Colour(0xFF9900);
        this.position = new Vector3f(0, 0, 0);

        this.texture = new TextureLoader().getTexture("game/res/gfx/sun.png");

        this.enable();

        ByteBuffer temp = ByteBuffer.allocateDirect(16);
        temp.order(ByteOrder.nativeOrder());

        GL11.glLightModel(GL_LIGHT_MODEL_AMBIENT, Util.asFlippedFloatBuffer(new float[]{
            0.05f, 0.05f, 0.05f, 1f
        }));
        GL11.glLight(glLight, GL11.GL_AMBIENT, (FloatBuffer) temp.asFloatBuffer().put(new float[]{
            0.05f, 0.05f, 0.05f, 1f
        }).flip());

        GL11.glLight(glLight, GL11.GL_DIFFUSE, (FloatBuffer) temp.asFloatBuffer().put(new float[]{
            0.05f, 0.05f, 0.05f, 1f
        }).flip());
        GL11.glLight(glLight, GL_POSITION, asFlippedFloatBuffer(new float[]{
            0, 0, 0, 1
        }));

        this.displayList = GL11.glGenLists(1);
        GL11.glNewList(displayList, GL_COMPILE);
        {
            GL11.glPushMatrix();
            {
                GL11.glDisable(GL_FOG);
                GL11.glCullFace(GL_FRONT);
                GL11.glDisable(GL_ALPHA_TEST);
                GL11.glDisable(GL_DEPTH_TEST);

                texture.bind();

                glBegin(GL_QUADS);
                {
                    glTexCoord2d(0, texture.getHeight());
                    glVertex2d(0, 0);

                    glTexCoord2d(0, 0);
                    glVertex2d(0, texture.getImageHeight());

                    glTexCoord2d(texture.getWidth(), 0);
                    glVertex2d(texture.getImageWidth(), texture.getImageHeight());

                    glTexCoord2d(texture.getWidth(), texture.getHeight());
                    glVertex2d(texture.getImageWidth(), 0);
                }
                glEnd();

                GL11.glEnable(GL_FOG);
                GL11.glCullFace(GL_BACK);
                GL11.glEnable(GL_DEPTH_TEST);
                GL11.glEnable(GL_ALPHA_TEST);

            }
            GL11.glPopMatrix();
        }
        GL11.glEndList();
    }

    @Override
    public void update(float dt) {
        if (world.isDay()) {
            { // position
                float p = (float) (world.getRawTime() / world.getTimeCycle());

                float xPos; // the X position of the sun
                float yPos; // the Y position of the sun
                float zPos; // the Y position of the sun

                if (world.getRawTime() >= 0) {
                    xPos = (float) ((world.getWidth() / 2) / 2 * p + (world.getWidth() / 2) * (1 - p));
                    yPos = (float) (height * (1 - p));
                    zPos = (float) ((world.getDepth() / 2) / 2 * p + (world.getDepth() / 2) * (1 - p));
                } else {
                    p = Math.abs(p);

                    xPos = (float) ((world.getWidth() / 2) * (1 - p));
                    yPos = (float) (height * (1 - p));
                    zPos = (float) ((world.getDepth() / 2) * (1 - p));
                }
                this.position.set(xPos, yPos, zPos);
            }
        }
    }

    @Override
    public void render() {
        if (world.isDay()) {
            GL11.glPushMatrix();
            {
                GL11.glLight(glLight, GL_POSITION, Util.asFlippedFloatBuffer(
                        position.getX(), position.getY(), position.getZ(), 1
                ));
                GL11.glTranslated(
                        position.getX() - (texture.getImageWidth() / 2),
                        position.getY() - (texture.getImageHeight() / 2),
                        position.getZ()
                );

                { // billboarding:
                    // reset the FloatBuffer pointer to zero.
                    Vector3f xAxis = new Vector3f(0f, 0f, 0f);
                    Vector3f yAxis = new Vector3f(0f, 0f, 0f);
                    Vector3f zAxis = new Vector3f(0f, 0f, 0f);

                    FloatBuffer matrixData = BufferUtils.createFloatBuffer(16);
                    Matrix4f matrix = new Matrix4f();
                    Matrix4f billboardMatrix = new Matrix4f();

                    matrixData.position(0);

                    GL11.glGetFloat(GL_MODELVIEW_MATRIX, matrixData);

                    matrix.load(matrixData);

                    billboardMatrix.setIdentity();

                    billboardMatrix.m30 = matrix.m30;  //get the current translation and place it in the matrix used to billboard.
                    billboardMatrix.m31 = matrix.m31;
                    billboardMatrix.m32 = matrix.m32;

                    zAxis.x = matrix.m30;    //place the current translation i the zAxis
                    zAxis.y = matrix.m31;
                    zAxis.z = matrix.m32;

                    zAxis = (Vector3f) zAxis.normalise();  // normalise the z axis

                    zAxis.negate();  // get negative values

                    billboardMatrix.m20 = zAxis.x;  // set matrix to orientate the z axis of the glow in the world coordinate system
                    billboardMatrix.m21 = zAxis.y;  // to face the camera
                    billboardMatrix.m22 = zAxis.z;

                    // cross z with approximate x axis, (1,0,0)  to give y
                    yAxis.set(0, zAxis.z, -zAxis.y);

                    Vector3f.cross(yAxis, zAxis, xAxis);  // calculate the xAxis
                    Vector3f.cross(zAxis, xAxis, yAxis);  // recalculate the yAxis

                    billboardMatrix.m10 = yAxis.x;  // set the componet s of the matrix
                    billboardMatrix.m11 = yAxis.y;
                    billboardMatrix.m12 = yAxis.z;

                    billboardMatrix.m00 = xAxis.x;
                    billboardMatrix.m01 = xAxis.y;
                    billboardMatrix.m02 = xAxis.z;

                    matrixData.position(0);

                    billboardMatrix.store(matrixData);

                    matrixData.position(0);

                    GL11.glLoadMatrix(matrixData);
                }

                colour.bind();
                {
                    GL11.glCallList(displayList);
                }
                colour.realse();
            }
            GL11.glPopMatrix();
        }
    }

    public ReadableVector3f getPosition() {
        return this.position;
    }

    public void setPosition(ReadableVector3f position) {
        this.position = new Vector3f(position);
    }

    /**
     * @param deltaX the change along the X axis
     * @param deltaY the change along the Y axis
     * @param deltaZ the change along the Z axis
     */
    public void translate(float deltaX, float deltaY, float deltaZ) {
        this.position.set(
                this.position.getX() + deltaX,
                this.position.getY() + deltaY,
                this.position.getZ() + deltaZ
        );
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public final void enable() {
        GL11.glEnable(glLight);
    }

    public final void disable() {
        GL11.glDisable(glLight);
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

    @Override
    public String toString() {
        return "Sun[" + "position: " + position + "]";
    }

    public void detachWorld() {
        this.world = null;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public int getGlLight() {
        return this.glLight;
    }

    public void setGlLight(int glLight) {
        this.glLight = glLight;
    }

    @Override
    public void destroy() {
        GL11.glDeleteLists(displayList, 1);
        this.displayList = -1;
    }

    public int getDisplayList() {
        return this.displayList;
    }

    public boolean hasTexture() {
        return texture != null;
    }
}
