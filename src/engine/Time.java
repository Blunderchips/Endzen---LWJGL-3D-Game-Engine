//package engine;
//
//import java.util.Date;
//import java.text.SimpleDateFormat;
//
///**
// * Provides an interface to your system's clock and other time base utilities to
// * the user.
// *
// * @author Matthew 'siD' Van der Bijl
// */
//public final strictfp class Time {
//
//    /**
//     * You can not instantiate this class.
//     */
//    @Deprecated
//    public Time() throws UnsupportedOperationException {
//        // Prevents instantiation of this class.
//        throw new UnsupportedOperationException(
//                "You can not instantiate this class.");
//    }
//
//    /**
//     * Amount of Nanosecond is a second.
//     */
//    public static final long SECOND = 0x5f5e100L;
//    /**
//     * Delta Time is the time it takes for the computer to go through all the
//     * processing/rendering for a single frame. It is dynamically updated, so it
//     * can fluctuate depending on what level of processing the last frame
//     * required.
//     */
//    private static float dt;
//
//    /**
//     * Returns the value of a timer with an unspecified starting time. The time
//     * is accurate to the microsecond.
//     *
//     * @return the value of a timer with an unspecified starting time
//     */
//    public static long getNano() {
//        return System.nanoTime();
//    }
//
//    public static String getTime() {
//        return new SimpleDateFormat("HH:mm:ss").format(new Date());
//    }
//
//    public static String getDate() {
//        return new SimpleDateFormat("dd/MM/yy").format(new Date());
//    }
//
//    /**
//     * Delta Time is the time it takes for the computer to go through all the
//     * processing/rendering for a single frame. It is dynamically updated, so it
//     * can fluctuate depending on what level of processing the last frame
//     * required.
//     *
//     * @return the time between the last two frames
//     */
//    public static float getDeltaTime() {
//        return Time.dt;
//    }
//
//    /**
//     * time.dt = (time.getNano() - (double)lastTime) / time.SECOND. <b>DO NOT
//     * USE THIS METHOD.</b> To be used in the main engine loop only.
//     *
//     * @param lastTime the last frame time
//     * @deprecated used by the core engine to set Delta Time (dt).
//     */
//    @Deprecated
//    public static void setDelta(long lastTime) {
//        Time.dt = (Time.getNano() - (float) lastTime) / Time.SECOND;
//    }
//}
