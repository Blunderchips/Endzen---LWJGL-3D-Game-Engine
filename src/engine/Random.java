package engine;

import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.ReadableVector2f;
import org.lwjgl.util.vector.ReadableVector3f;

/**
 * Provides an interface to handle all Random numbers and events to the user.
 *
 * @author Matthew 'siD' Van der Bijl
 *
 * @see java.lang.java.lang.Math#random()
 */
public final class Random {

    /**
     * You can not instantiate this class.
     */
    @Deprecated
    public Random() throws UnsupportedOperationException {
        // Prevents instantiation of this class.
        throw new UnsupportedOperationException(
                "You can not instantiate this class.");
    }

    /**
     * @return a random integer
     */
    public static int interger() {
        return java.lang.Math.abs((int) (java.lang.Math.random() * 100) - 100);
    }

    /**
     * @param minimum Minimum value. Must be smaller than maximum
     * @param maximum Maximum value. Must be greater than minimum
     *
     * @return Integer between min and max
     */
    public static int interger(int minimum, int maximum) {
        return java.lang.Math.abs((int) (java.lang.Math.random() * maximum) + minimum);
    }

    public static int interger(int maximum) {
        return (int) (java.lang.Math.random() * (maximum + 1));
    }

    /**
     * @return returns a Random boolean
     */
    public static boolean bool() {
        return (Random.interger() % 2) == 0;
    }

    public static boolean bool(int percent) {
        return interger() <= percent;
    }

    public static boolean chance(int chance) {
        for (int i = 0; i < chance; i++) {
            if (Random.bool() == false) {
                return false;
            }
        }
        return true;
    }

    public static ReadableVector2f vec2() {
        return new Vector2f(
                Random.interger(),
                Random.interger()
        );
    }

    public static ReadableVector2f vec2(int maximum) {
        return new Vector2f(
                Random.interger(maximum),
                Random.interger(maximum)
        );
    }

    public static ReadableVector2f vec2(int minimum, int maximum) {
        return new Vector2f(
                Random.interger(minimum, maximum),
                Random.interger(minimum, maximum)
        );
    }

    public static ReadableVector3f vec3() {
        return new Vector3f(
                Random.interger(),
                Random.interger(),
                Random.interger()
        );
    }

    public static ReadableVector3f vec3(int maximum) {
        return new Vector3f(
                Random.interger(maximum),
                Random.interger(maximum),
                Random.interger(maximum)
        );
    }

    public static ReadableVector3f vec3(int minimum, int maximum) {
        return new Vector3f(
                Random.interger(minimum, maximum),
                Random.interger(minimum, maximum),
                Random.interger(minimum, maximum)
        );
    }

    public static Object elementOfArray(Object[] array) {
        return array[interger(array.length - 1)];
    }

    public static Object elementOfArray(char[] array) {
        return array[interger(array.length - 1)];
    }

    public static Object elementOfArray(int[] array) {
        return array[interger(array.length - 1)];
    }

    /**
     * @param gender male or female
     * @return a random name
     */
    public static String name(String gender) {
        Scanner sc = null;
        Set<String> names = new HashSet<>();
        switch (gender) {
            case "male":
                if (ClassLoader.getSystemResourceAsStream("engine/misc/namesMale.txt") != null) {
                    sc = new Scanner(ClassLoader.getSystemResourceAsStream("engine/misc/namesMale.txt"));

                    while (sc.hasNextLine()) {
                        names.add(sc.nextLine());
                    }

                    sc.close();
                    return (String) Random.elementOfArray(names.toArray());
                } else {
                    throw new DwarfException("engine/misc/namesMale.txt not found.");
                }
            case "female":
                if (ClassLoader.getSystemResourceAsStream("engine/misc/namesFemale.txt") != null) {
                    sc = new Scanner(ClassLoader.getSystemResourceAsStream("engine/misc/namesFemale.txt"));

                    while (sc.hasNextLine()) {
                        names.add(sc.nextLine());
                    }

                    sc.close();
                    return (String) Random.elementOfArray(names.toArray());
                } else {
                    throw new DwarfException("engine/misc/namesFemale.txt not found.");
                }
            default:
                throw new DwarfException("illegal argument.");
        }
    }

    /**
     * @return a random name
     */
    public static String name() {
        if (Random.bool()) {
            return Random.name("male");
        } else {
            return Random.name("female");
        }
    }

    /**
     * @return a random crash comment
     */
    public static String crashComment() {
        if (ClassLoader.getSystemResourceAsStream("engine/misc/crashComments.txt") != null) {
            Set<String> comments = new HashSet<>();
            Scanner sc = new Scanner(ClassLoader.getSystemResourceAsStream("engine/misc/crashComments.txt"));

            while (sc.hasNextLine()) {
                comments.add(sc.nextLine());
            }

            sc.close();
            return (String) Random.elementOfArray(comments.toArray());
        } else {
            throw new DwarfException("engine/misc/crashComments.txt not found.");
        }
    }

    /**
     * @return a random splash comment
     */
    public static String splashComment() {
        if (ClassLoader.getSystemResourceAsStream("engine/misc/splashComments.txt") != null) {
            Set<String> comments = new HashSet<>();
            Scanner sc = new Scanner(ClassLoader.getSystemResourceAsStream("engine/misc/splashComments.txt"));

            while (sc.hasNextLine()) {
                comments.add(sc.nextLine());
            }

            sc.close();
            return (String) Random.elementOfArray(comments.toArray());
        } else {
            throw new DwarfException("engine/misc/splashComments.txt not found.");
        }
    }

    public static char character() {
        return (char) Random.elementOfArray("abcdefghijklmnopqrstuvwxyz".toCharArray());
    }

    public static String string(int length) {
        String output = "";

        for (int i = 0; i < length; i++) {
            output += Random.character();
        }

        return output;
    }

    public static String string() {
        return Random.string(Random.interger());
    }

    public static boolean odds(int odds) {
        return Random.interger(odds) == Random.interger(odds);
    }
}
