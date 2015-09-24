package game.assets;

import engine.DwarfException;
import engine.GameObject;
import engine.Random;
import org.lwjgl.util.vector.ReadableVector3f;

public class Unit extends java.lang.Object implements GameObject {

    public enum Units {

        TROLL("Troll", "melee", 20, 100, 20, 25, 30),
        DRAGON("Dragon", "melee", 1000, 1000, 1000, 1000, 1000);

        private final int lvl; // level
        private final int hp; // health points
        private final int atk; // attack 
        private final int agg; // aggression
        private final int def; // defence

        private final String name; // the name of the mob
        private final String type; // the type of attack the mob is (melee or range)

        Units(String name, String type, int lvl, int hp, int atk,
                int def, int agg) throws DwarfException {

            if (type.equals("melee") || type.equals("range")) {
                this.type = type;
            } else {
                throw new DwarfException(type + " not recognised. (melee or range only)");
            }

            this.hp = hp;
            this.lvl = lvl;
            this.atk = atk;
            this.def = def;
            this.agg = agg;

            this.name = name;
        }

        @Override
        public String toString() {
            return String.format(
                    "Unit[name: %s, type: %s, hp: %d, atk %d, def: %d, agg: %d",
                    name, type, hp, atk, def, agg
            );
        }
    }

    /**
     * The name of the <code>Unit</code>.
     */
    private final String name;
    private final String type;
    // stats: 
    private final int lvl; // level
    private final int hp; // health points
    private final int atk; // attack 
    private final int agg; // aggression
    private final int def; // defence
    // --

    public Unit(Units unit) {
        super();

        this.lvl = Random.interger(unit.lvl, 1);

        this.hp = Random.interger(unit.hp * lvl, unit.hp);
        this.atk = Random.interger(unit.atk * lvl, unit.atk);
        this.def = Random.interger(unit.def * lvl, unit.def);
        this.agg = Random.interger(unit.agg * lvl, unit.agg);

        this.type = unit.type;
        this.name = unit.name;
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render() {
    }

    public void die() {
    }

    @Override
    public String toString() {
        return String.format(
                "Unit[name: %s, hp: %d, atk %d, def: %d, agg: %d",
                name, hp, atk, def, agg
        );
    }

    /**
     * @return the position of the <code>Unit</code> in the world
     */
    public ReadableVector3f getPosition() {
        return null;
    }

    /**
     * @return the name of the <code>Unit</code>
     */
    public String getName() {
        return name;
    }
}
