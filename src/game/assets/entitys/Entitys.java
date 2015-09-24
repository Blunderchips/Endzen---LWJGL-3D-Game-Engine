package game.assets.entitys;

import game.Game;
import engine.Destroyable;
import engine.GameObject;
import engine.lwjgl.Texture;
import engine.lwjgl.TextureLoader;
import engine.obj.OBJLoader;
import engine.obj.Obj;
import engine.physics.AABB;
import engine.physics.AABBMesh;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_COMPILE;

public enum Entitys implements GameObject {

    TREE_1("game/res/obj/obj__tree1.obj", "game/res/gfx/tree1.png"),
    TREE_2("game/res/obj/obj__tree2.obj", "game/res/gfx/tree2.png"),
    TREE_3("game/res/obj/obj__tree3.obj", "game/res/gfx/tree3.png"),
    TREE_4("game/res/obj/obj__tree4.obj", "game/res/gfx/tree4.png"),
    TREE_5("game/res/obj/obj__tree5.obj", "game/res/gfx/tree5.png"),
    TREE_6("game/res/obj/obj__tree6.obj", "game/res/gfx/tree6.png"),
    BUILD_SITE("game/res/obj/buildSite.obj", "game/res/gfx/wood_01.jpg"),
    SHELTER("game/res/obj/shelter.obj", "game/res/gfx/wood_01.jpg"),
    CAMP_FIRE("game/res/obj/campFire.obj", "game/res/gfx/wood_01.jpg");

    private final AABB AABB;
    private final AABB[] mesh;
    private final int displayList;

    Entitys(String objPath, String texturePath) {
        final OBJLoader loader = new OBJLoader();
        final Texture texture = new TextureLoader().getTexture(texturePath);
        final Obj model = loader.loadModel(ClassLoader.getSystemResourceAsStream(objPath));

        this.displayList = GL11.glGenLists(1);
        GL11.glNewList(displayList, GL_COMPILE);
        {
            texture.bind();
            {
                loader.render(model);
            }
            texture.release();
        }
        GL11.glEndList();

        Game.addDestroyable(new Destroyable() {

            @Override
            public void destroy() {
                GL11.glDeleteLists(displayList, 1);
            }
        });

        Game.addDestroyable(new Destroyable() {

            @Override
            public void destroy() {
                GL11.glDeleteTextures(texture.getTextureID());
            }
        });

        this.mesh = AABBMesh.OBJtoMeshAABB(model);
        this.AABB = new AABB(Obj.Math.min(model), Obj.Math.max(model));
    }

    @Override
    public void update(float dt) {
    }

    /**
     * Renders the <code>Entitys</code>.
     */
    @Override
    public void render() {
        GL11.glCallList(displayList);
    }

    /**
     * @return the display list of the <code>Entitys</code>
     */
    public int getDisplayList() {
        return this.displayList;
    }

    public AABB[] getMesh() {
        return this.mesh;
    }

    public AABB getAABB() {
        return this.AABB;
    }
}
