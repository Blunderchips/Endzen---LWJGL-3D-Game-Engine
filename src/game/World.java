package game;

import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

import engine.Math;
import engine.Destroyable;
import engine.Random;
import game.assets.Unit;
import game.assets.Window;
import game.assets.Player;
import game.assets.entitys.Grass;
import game.assets.entitys.Tree;
import game.assets.world.Fog;
import game.assets.world.Sky;
import game.assets.world.Sun;
import game.assets.world.Terrain;
import game.assets.Entity;

import com.google.common.collect.TreeMultimap;
import engine.physics.AABB;
import engine.physics.IntersectData;

import org.lwjgl.util.vector.Vector3f;

public final class World implements Window {

    private final int timeCycle;
    /**
     * The <code>Game</code> that the <code>World</code> is attached to.
     */
    private final Game game;
    /**
     * The number of days in a year.
     */
    private final int daysInYear;
    /**
     * The width of the <code>World</code>
     */
    private final int width;
    /**
     * The depth of the <code>World</code>
     */
    private final int depth;
    /**
     * The current <code>World</code> time.
     */
    private double time;
    /**
     * The current day of the year.
     */
    private int currentDayOfYear;
    /**
     * True if it is day and false if night.
     */
    private boolean day;

    private float renderDist;

    private final Fog fog;
    private final Sun sun;
    private final Sky sky;
    private final Terrain terrain;
    private final Player player;

    private final List<game.assets.Unit> units;
    private final TreeMultimap<Integer, Entity> objects;

    public World(Game game) {
        super();

        this.depth = 1024;
        this.width = 1024;
        this.daysInYear = 256;
        this.timeCycle = (int) (30 * (2048));
        this.time = 0.0d;
        this.renderDist = 256;
        this.currentDayOfYear = 0;
        this.day = true;
        this.game = game;

        this.player = new Player(
                this, new Vector3f((width / 2), 0, -(depth / 2))
        );

        this.fog = new Fog(this);
        this.sun = new Sun(this);
        this.sky = new Sky(this);
        this.terrain = new Terrain(this);

        this.units = new ArrayList<>();
        this.objects = TreeMultimap.create();

        this.populate();
    }

    @Override
    public String getName() {
        return "World";
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void update(float dt) {
        if (time < timeCycle) {
            this.time += dt;
        } else {
            this.day = !day;
            this.time = -timeCycle;

            if (isDay()) {
                if (currentDayOfYear < daysInYear) {
                    currentDayOfYear++;
                } else {
                    currentDayOfYear = 0;
                }

                this.sun.enable();
            } else {
                this.sun.disable();
            }
        }

        // other:
        this.player.update(dt);
        this.sky.update(dt);
        this.fog.update(dt);
        this.sun.update(dt);
        this.terrain.update(dt);

        this.updateUnits(dt);
        this.updateObjects(dt);

        this.player.updateChildren(dt);
    }

    @Override
    public void render() {
        this.player.render();
        this.sky.render();
        this.sun.render();
        this.fog.render();
        this.terrain.render();

        this.renderUnits();
        this.renderObjects();

        this.player.renderChildren();
    }

    /**
     * returns the current time of the day. Ranges between 0 and 1 with 0 being
     * midnight and noon and 1 being dusk and dawn.
     *
     * @return the current time of day
     */
    public float getTime() {
        return (float) (1 - (java.lang.Math.abs(time) / timeCycle));
    }

    /**
     * @param time the new world time
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * @return whether it is day or not
     */
    public boolean isDay() {
        return this.day;
    }

    /**
     * @return whether it is night or not
     */
    public boolean isNight() {
        return !isDay();
    }

    /**
     * @return the current day of the year
     */
    public int getCurrentDayOfYear() {
        return this.currentDayOfYear;
    }

    public Fog getFog() {
        return this.fog;
    }

    public Sun getSun() {
        return this.sun;
    }

    /**
     * @return whether it is summer or not
     */
    public boolean isSummer() {
        return currentDayOfYear > (daysInYear / 2);
    }

    /**
     * Winter is coming.
     *
     * @return whether it is winter or not
     */
    public boolean isWinter() {
        return !isSummer();
    }

    public Sky getSky() {
        return this.sky;
    }

    @Override
    public void destroy() {
        this.sun.destroy();
        this.sky.destroy();
        this.terrain.destroy();

        this.destroyAllObjects();
        this.destroyAllUnits();
    }

    public Terrain getTerrain() {
        return this.terrain;
    }

    @Override
    public String toString() {
        return "World[time: " + time + ", currentDayOfYear: " + currentDayOfYear + "]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (Double.doubleToLongBits(time)
                ^ (Double.doubleToLongBits(time) >>> 32));
        hash = 17 * hash + this.currentDayOfYear;
        hash = 17 * hash + Objects.hashCode(fog);
        hash = 17 * hash + Objects.hashCode(sun);
        hash = 17 * hash + Objects.hashCode(sky);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        } else if (super.getClass() != obj.getClass()) {
            return false;
        }

        final World that = (World) obj;

        if (Double.doubleToLongBits(this.time)
                != Double.doubleToLongBits(that.time)) {
            return false;
        } else if (this.currentDayOfYear != that.currentDayOfYear) {
            return false;
        } else if (!Objects.equals(this.fog, that.fog)) {
            return false;
        } else if (!Objects.equals(this.sun, that.sun)) {
            return false;
        } else if (!Objects.equals(this.sky, that.sky)) {
            return false;
        }

        return true;
    }

    public Player getPlayer() {
        return this.player;
    }

    public TreeMultimap<Integer, Entity> getObjects() {
        return this.objects;
    }

    public List<Unit> getUnits() {
        return this.units;
    }

    public void updateObjects(float dt) {
        final Vector3f playerPos = new Vector3f(
                player.getPosition().getX(),
                player.getPosition().getY(),
                player.getPosition().getZ()
        );
        AABB playerAABB = new AABB(playerPos, (Vector3f) (Math.add(playerPos, new Vector3f(25, 25, 25))));

        float minDist = Float.POSITIVE_INFINITY;

        for (int i = 0; i < this.objects.size(); i++) {
            for (Entity obj : this.objects.get(i)) {
                obj.update(dt);
            }
        }

        System.out.println(minDist);
    }

    public void renderObjects() {
        final Vector3f playerPos = new Vector3f(
                player.getPosition().getX(),
                player.getPosition().getY(),
                -player.getPosition().getZ()
        );
        final int dist = (int) playerPos.length();

        for (int i = (int) (renderDist - dist); i < dist + renderDist; i++) {
            for (Entity obj : this.objects.get(i)) {
                if (Math.distanceSq(
                        playerPos, obj.getPosition()
                ) < (renderDist * renderDist)) {
                    obj.render();
                }
            }
        }
    }

    public void updateUnits(float dt) {
        for (Unit unit : units) {
            unit.update(dt);
        }
    }

    public void renderUnits() {
        for (Unit unit : units) {
            unit.render();
        }
    }

    public void destroyAllUnits() {
        for (Unit unit : units) {
            if (unit instanceof Destroyable) {
                Destroyable n = (Destroyable) unit;
                n.destroy();
            }
        }
    }

    public void destroyAllObjects() {
        for (Entity obj : objects.values()) {
            if (obj instanceof Destroyable) {
                Destroyable n = (Destroyable) obj;
                n.destroy();
            }
        }
    }

    /**
     * "Populates" the <code>World</code>.
     */
    private void populate() {
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                if (Random.chance(6)) { // grass
                    Vector3f position = new Vector3f(x, terrain.interpolateHeight(x, z), z);
                    this.objects.put(
                            (int) position.length(), new Grass(position)
                    );
                    continue;
                }
                if (Random.chance(10)) { // trees
                    Vector3f position = new Vector3f(x, terrain.interpolateHeight(x, z), z);
                    this.objects.put(
                            (int) position.length(), new Tree(position)
                    );
                }
            }
        }
    }

    public int getTimeCycle() {
        return this.timeCycle;
    }

    public int getDaysInYear() {
        return this.daysInYear;
    }

    public int getWidth() {
        return this.width;
    }

    public int getDepth() {
        return this.depth;
    }

    @Deprecated
    public double getRawTime() {
        return this.time;
    }

    public float getRenderDist() {
        return this.renderDist;
    }

    public void setRenderDist(float renderDist) {
        this.renderDist = renderDist;
    }
}
