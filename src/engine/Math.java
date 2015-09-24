package engine;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.ReadableVector2f;
import org.lwjgl.util.vector.ReadableVector3f;

/**
 * Provides an interface for Math functions and constants.
 *
 * @author Matthew 'siD' Van der Bijl
 *
 * @see java.lang.Math
 */
public final class Math {

    /**
     * You can not instantiate this class.
     */
    @Deprecated
    public Math() throws UnsupportedOperationException {
        // Prevents instantiation of this class.
        throw new UnsupportedOperationException(
                "You can not instantiate this class.");
    }

    /**
     * Returns the value of the first (base) argument raised to the power of the
     * second argument (exp).
     *
     * @see java.lang.Math#pow(double, double)
     *
     * @param base the base (float)
     * @param exp the exponent (float)
     * @return the value <code>base</code><sup><code>exp</code></sup>
     */
    public static float pow(float base, float exp) {
        return (float) java.lang.Math.pow(base, exp);
    }

    /**
     * Squares a given value.
     *
     * @param fValue the value to be squared (float)
     * @return the squared value
     */
    public static float sqr(float fValue) {
        return fValue * fValue;
    }

    /**
     * Returns the square root of a given value.
     *
     * @see java.lang.Math#sqrt(double)
     *
     * @param fValue The value to sqrt (float)
     * @return The square root of the given value
     */
    public static float sqrt(float fValue) {
        return (float) java.lang.Math.sqrt(fValue);
    }

    /**
     * Returns the distance between two given points.Squares the
     * <code>distanceSq</code> method.
     *
     * @see engine.Math#distanceSq(org.lwjgl.util.vector.ReadableVector2f,
     * org.lwjgl.util.vector.ReadableVector2f)
     *
     * @param pointA the first point to be tested
     * @param pointB the second point to be tested
     * @return the distance between the two points
     */
    public static float distance(ReadableVector2f pointA, ReadableVector2f pointB) {
        return (float) Math.sqrt(Math.distanceSq(pointA, pointB));
    }

    /**
     * Returns the squared distance between two given points a float.
     *
     * @param pointA the first point to be tested
     * @param pointB the second point to be tested
     * @return the squared distance between the two points
     */
    public static float distanceSq(ReadableVector2f pointA, ReadableVector2f pointB) {
        return (float) (Math.pow((pointA.getX() - pointB.getX()), 2)
                + Math.pow((pointA.getY() - pointB.getY()), 2));
    }

    /**
     * Returns the distance between two given points a float. Squares the
     * <code>distanceSq</code> method.
     *
     * @see engine.Math#distanceSq(org.lwjgl.util.vector.ReadableVector3f,
     * org.lwjgl.util.vector.ReadableVector3f)
     *
     * @param pointA the first point to be tested
     * @param pointB the second point to be tested
     * @return the distance between the two points
     */
    public static float distance(ReadableVector3f pointA, ReadableVector3f pointB) {
        return (float) Math.sqrt(Math.distanceSq(pointA, pointB));
    }

    /**
     * Returns the distance between two given points as a float.
     *
     * @param pointA the first point to be tested
     * @param pointB the second point to be tested
     * @return the squared distance between the two points
     */
    public static float distanceSq(ReadableVector3f pointA, ReadableVector3f pointB) {
        return (float) (java.lang.Math.pow((pointA.getX() - pointB.getX()), 2)
                + Math.pow((pointA.getY() - pointB.getY()), 2)
                + Math.pow((pointA.getZ() - pointB.getZ()), 2));
    }

    /**
     * Get the closest greater power of 2 to the fold number.
     *
     * @param fold The target number
     * @return The power of 2
     */
    public static int get2Fold(int fold) {
        int ret = 2;
        while (ret < fold) {
            ret *= 2;
        }
        return ret;
    }

    public static ReadableVector3f max(ReadableVector3f a, ReadableVector3f b) {
        return new Vector3f(
                java.lang.Math.max(a.getX(), b.getX()),
                java.lang.Math.max(a.getY(), b.getY()),
                java.lang.Math.max(a.getZ(), b.getZ())
        );
    }

    public static float max(ReadableVector3f vector) {
        return java.lang.Math.max(vector.getX(), java.lang.Math.max(vector.getY(), vector.getZ()));
    }

    public static ReadableVector3f min(ReadableVector3f a, ReadableVector3f b) {
        return new Vector3f(
                java.lang.Math.min(a.getX(), b.getX()),
                java.lang.Math.min(a.getY(), b.getY()),
                java.lang.Math.min(a.getZ(), b.getZ())
        );
    }

    public static float min(ReadableVector3f vector) {
        return java.lang.Math.min(vector.getX(), java.lang.Math.min(vector.getY(), vector.getZ()));
    }

    public static ReadableVector3f normalized(ReadableVector3f vector) {
        float length = vector.length();
        return new Vector3f(
                vector.getX() / length,
                vector.getY() / length,
                vector.getZ() / length
        );
    }

    public static float min(float a, float b) {
        return java.lang.Math.min(a, b);
    }

    public static float max(float a, float b) {
        return java.lang.Math.max(a, b);
    }

    public static ReadableVector3f sub(ReadableVector3f vectorA, ReadableVector3f vectorB) {
        return new Vector3f(
                vectorA.getX() - vectorB.getX(),
                vectorA.getY() - vectorB.getY(),
                vectorA.getZ() - vectorB.getZ()
        );
    }

    public static ReadableVector3f add(ReadableVector3f vectorA, ReadableVector3f vectorB) {
        return new Vector3f(
                vectorA.getX() + vectorB.getX(),
                vectorA.getY() + vectorB.getY(),
                vectorA.getZ() + vectorB.getZ()
        );
    }

    public static ReadableVector3f multiply(ReadableVector3f vectorA, ReadableVector3f vectorB) {
        return new Vector3f(
                vectorA.getX() * vectorB.getX(),
                vectorA.getY() * vectorB.getY(),
                vectorA.getZ() * vectorB.getZ()
        );
    }

    public static ReadableVector3f divide(ReadableVector3f vectorA, ReadableVector3f vectorB) {
        return new Vector3f(
                vectorA.getX() / vectorB.getX(),
                vectorA.getY() / vectorB.getY(),
                vectorA.getZ() / vectorB.getZ()
        );
    }

    public static float dot(ReadableVector3f vectorA, ReadableVector3f vectorB) {
        return vectorA.getX() * vectorB.getX()
                + vectorA.getY() * vectorB.getY()
                + vectorA.getZ() * vectorB.getZ();
    }

    public static float abs(float fValue) {
        return java.lang.Math.abs(fValue);
    }

    public static ReadableVector3f cross(ReadableVector3f vectorA, ReadableVector3f vectorB) {
        return new Vector3f(
                vectorA.getY() * vectorB.getZ() - vectorA.getZ() * vectorB.getY(),
                vectorA.getZ() * vectorB.getX() - vectorA.getX() * vectorB.getZ(),
                vectorA.getX() * vectorB.getY() - vectorA.getY() * vectorB.getX()
        );
    }

    /**
     * Returns the trigonometric sine of an angle. Special cases:
     * <ul><li>If the argument is NaN or an infinity, then the result is NaN.
     * <li>If the argument is zero, then the result is a zero with the same sign
     * as the argument.</ul>
     *
     * <p>
     * The computed result must be within 1 ulp of the exact result. Results
     * must be semi-monotonic.
     *
     * @param fValue an angle, in radians.
     * @return the sine of the argument.
     */
    public static float sin(float fValue) {
        return (float) java.lang.Math.sin(fValue);
    }

    /**
     * Returns the trigonometric cosine of an angle. Special cases:
     * <ul><li>If the argument is NaN or an infinity, then the result is
     * NaN.</ul>
     *
     * <p>
     * The computed result must be within 1 ulp of the exact result. Results
     * must be semi-monotonic.
     *
     * @param fValue an angle, in radians.
     * @return the cosine of the argument.
     */
    public static float cos(float fValue) {
        return (float) java.lang.Math.cos(fValue);
    }

    /**
     * Returns the trigonometric tangent of an angle. Special cases:
     * <ul><li>If the argument is NaN or an infinity, then the result is NaN.
     * <li>If the argument is zero, then the result is a zero with the same sign
     * as the argument.</ul>
     *
     * <p>
     * The computed result must be within 1 ulp of the exact result. Results
     * must be semi-monotonic.
     *
     * @param fValue an angle, in radians.
     * @return the tangent of the argument.
     */
    public static float tan(float fValue) {
        return (float) java.lang.Math.tan(fValue);
    }

    /**
     * Converts an angle measured in degrees to an approximately equivalent
     * angle measured in radians. The conversion from degrees to radians is
     * generally inexact.
     *
     * @param angdeg an angle, in degrees
     * @return the measurement of the angle <code>angdeg</code> in radians.
     */
    public static float toRadians(float angdeg) {
        return (float) java.lang.Math.toRadians(angdeg);
    }
}
