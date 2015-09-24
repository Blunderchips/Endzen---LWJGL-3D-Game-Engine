/*
 * Based off Oskar Veerhoek height map class
 */
package engine.world;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import engine.Destroyable;
import engine.DwarfException;
import engine.GameObject;
import engine.lwjgl.Texture;

import org.lwjgl.util.Color;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.ReadableVector3f;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_EXP2;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_SHININESS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;

public class Heightmap extends Object implements GameObject, Destroyable {

    /**
     * The points of the height. The first dimension represents the
     * Z-coordinate. The second dimension represents the X-coordinate. The float
     * value represents the height.
     */
    private final float[][] values;
    /**
     * The display list that will contain the height-map's vertex values.
     */
    private int displayList;
    /**
     * the position of the <code>Heightmap</code> in the world.
     */
    private Vector3f position;

    private Texture texture;
    private final float modifier;
    private boolean wireFrame;

    /**
     * Constructs a new <code>Heightmap</code>.
     *
     * @param path the path to the the image to be used to create the
     * <code>Heightmap</code>
     * @throws IOException image file not found
     */
    public Heightmap(String path) throws IOException {
        this(ImageIO.read(new File(path)), false);
    }

    /**
     * Constructs a new <code>Heightmap</code>.
     *
     * @param path the path to the the image to be used to create the
     * <code>Heightmap</code>
     * @param isWireFrame if true the <code>Heightmap</code> will be rendered in
     * wireframe mode
     * @throws IOException image file not found
     */
    public Heightmap(String path, boolean isWireFrame) throws IOException {
        this(ImageIO.read(new File(path)), isWireFrame);
    }

    /**
     * Constructs a new <code>Heightmap</code>.
     *
     * @param img the the image to be used to create the <code>Heightmap</code>
     * @throws DwarfException shit happens
     */
    public Heightmap(BufferedImage img) throws DwarfException {
        this(img, false);
    }

    /**
     * Constructs a new <code>Heightmap</code>.
     *
     * @param img the the image to be used to create the <code>Heightmap</code>
     * @param isWireFrame if true the <code>Heightmap</code>. will be rendered
     * in wireframe mode
     * @throws DwarfException shit happens
     */
    public Heightmap(BufferedImage img, boolean isWireFrame) throws DwarfException {
        super();

        this.modifier = 0.5f; // Y modifier value
        this.values = new float[img.getWidth()][img.getHeight()];

        for (int z = 0; z < values.length; z++) {
            for (int x = 0; x < values[z].length; x++) {
                values[z][x] = new Color(img.getRGB(z, x), 0, 0).getRed();
            }
        }

        this.displayList = GL11.glGenLists(1);
        GL11.glNewList(displayList, GL_COMPILE);
        {

//            GL11.glMaterialf(GL_FRONT, GL_SHININESS, 120);
//            for (int z = 0; z < values.length - 1; z++) {
//                GL11.glBegin(GL_TRIANGLE_STRIP);
//                {
//                    for (int x = 0; x < values[z].length; x++) {
//                        GL11.glVertex3f(x, interpolateHeight(x, z), z);
//                        GL11.glVertex3f(x, interpolateHeight(x, z + 1), z + 1);
//                    }
//                }
//                GL11.glEnd();
//                GL11.glFlush();
//            }
//            
            GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

            GL30.glGenerateMipmap(GL_TEXTURE_2D);
            {
                GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_EXP2);
                GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_EXP2);
                GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
            }

            GL11.glMaterialf(GL_FRONT, GL_SHININESS, 120);
            for (int z = 0; z < values.length - 1; z++) {

                float t0 = (float) z / (values.length - 1);
                float t1 = (float) (z + 1) / (values.length - 1);

                GL11.glBegin(GL_TRIANGLE_STRIP);
                {
                    for (int x = 0; x < values[z].length; x++) {
                        float s = (float) x / (values[z].length - 1);

//                        Vector3f normal = this.getNormal(
//                                new Vector3f(x, interpolateHeight(x, z), z),
//                                new Vector3f(x, interpolateHeight(x, z + 1), z + 1),
//                                new Vector3f(x + 1, interpolateHeight(x + 1, z), z)
//                        );
//                        GL11.glNormal3f(normal.getX(), normal.getY(), normal.getZ());
                        GL11.glTexCoord2f(s, t0);
                        GL11.glVertex3f(x, interpolateHeight(x, z), z);
                        GL11.glTexCoord2f(s, t1);
                        GL11.glVertex3f(x, interpolateHeight(x, z + 1), z + 1);
                    }
                }
                GL11.glEnd();
                GL11.glFlush();
            }
        }
        GL11.glEndList();

        this.wireFrame = isWireFrame;
        this.position = new Vector3f(0, 0, 0);

        // --
//        this.displayList = DisplayListUtil.attachShader(displayList,
//                GLSL.loadShaderPair(
//                        "game/assets/shaders/phongShader.vsh",
//                        "game/assets/shaders/phongShader.fsh"
//                ));
        // --
    }

    @Override
    public void update(float dt) {
    }

    /**
     * renders the <code>Heightmap</code> to the screen.
     */
    @Override
    public void render() {
        GL11.glPushMatrix();
        {
            if (this.hasTexture()) {
                texture.bind();
            }

            GL11.glTranslatef(position.getX(), position.getY(), position.getZ());

            if (isWireFrame()) {
                GL11.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            } else {
                GL11.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            }
            GL11.glCallList(displayList);

            if (isWireFrame()) {
                GL11.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            }

            if (this.hasTexture()) {
                this.texture.release();
            }
        }
        GL11.glPopMatrix();
    }

    public int getDisplayList() {
        return this.displayList;
    }

    public float[][] getValues() {
        return this.values;
    }

    public boolean isWireFrame() {
        return this.wireFrame;
    }

    public void setWireFrame(boolean isWireFrame) {
        this.wireFrame = isWireFrame;
    }

    /**
     * Destroys the <code>Heightmap</code>.
     */
    @Override
    public void destroy() {
        this.texture.destroy();
        GL11.glDeleteLists(displayList, 1);
        this.displayList = -1;
    }

    /**
     * @return the position of the <code>Heightmap</code> in the world
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * @param position the new position of the <code>Heightmap</code> in the
     * world.
     */
    @Deprecated
    public void setPosition(Vector3f position) {
        this.position = new Vector3f(position);
    }

    /**
     * @param deltaX the change along the X axis
     * @param deltaY the change along the Y axis
     * @param deltaZ the change along the Z axis
     */
    @Deprecated
    public void translate(float deltaX, float deltaY, float deltaZ) {
        this.position.set(
                this.position.getX() + deltaX,
                this.position.getY() + deltaY,
                this.position.getZ() + deltaZ
        );
    }

    public final float interpolateHeight(ReadableVector3f point) {
        return this.interpolateHeight(point.getX(), point.getZ());
    }

    public final float interpolateHeight(float xPos, float zPos) {
        //TODO: use barycentric for more accurate (what he said?)
        try {
            int x = (int) java.lang.Math.floor(xPos);
            int z = (int) java.lang.Math.floor(zPos);
            float x0 = xPos - x;
            float z0 = zPos - z;

            return (((values[x][z] * (1.0f - x0) + values[x + 1][z] * x0) * (1.0f - z0))
                    + (z0 * (values[x][z + 1] * (1.0f - x0) + values[x + 1][z + 1] * x0))) * modifier;
        } catch (ArrayIndexOutOfBoundsException outOfBoundsException) {
            return 0;
        }
    }

    public float getModifier() {
        return this.modifier;
    }

    public ReadableVector3f[][] getVertices() {
        ReadableVector3f[][] vertices = new ReadableVector3f[this.values.length][];

        for (int z = 0; z < values.length - 1; z++) {
            vertices[z] = new ReadableVector3f[values[z].length];
            for (int x = 0; x < values[z].length; x++) {
                vertices[z][x] = new Vector3f(x, interpolateHeight(x, z), z);
            }
        }

        return vertices;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean hasTexture() {
        return texture != null;
    }

    public boolean isBelow(ReadableVector3f point) {
        return this.interpolateHeight(point.getX(), point.getZ()) > point.getY();
    }

    public boolean isBelow(float xPos, float yPos, float zPos) {
        return this.interpolateHeight(xPos, zPos) > yPos;
    }

    private Vector3f getNormal(Vector3f p1, Vector3f p2, Vector3f p3) {
        Vector3f v = new Vector3f(
                p2.getX() - p1.getX(),
                p2.getY() - p1.getY(),
                p2.getZ() - p1.getZ()
        );
        Vector3f w = new Vector3f(
                p3.getX() - p1.getX(),
                p3.getY() - p1.getY(),
                p3.getZ() - p1.getZ()
        );

        return (Vector3f) new Vector3f(
                (v.getY() * w.getZ()) - (v.getZ() * w.getY()),
                (v.getZ() * w.getX()) - (v.getX() * w.getZ()),
                (v.getX() * w.getY()) - (v.getY() * w.getX())
        ).normalise();
    }
}
