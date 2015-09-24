package engine.gfx;

public interface Colours {

    /**
     * The colour white. In the default sRGB space.
     */
    public final static Colour white = new Colour(0xff, 0xff, 0xff);

    /**
     * The colour white. In the default sRGB space.
     */
    public final static Colour WHITE = white;

    /**
     * The colour grey. In the default sRGB space.
     */
    public final static Colour grey = new Colour(0.5f, 0.5f, 0.5f, 0xff);

    /**
     * The colour grey. In the default sRGB space.
     */
    public final static Colour GREY = grey;

    /**
     * The colour dark grey. In the default sRGB space.
     */
    public final static Colour darkGrey = new Colour(0.3f, 0.3f, 0.3f, 0xff);

    /**
     * The colour dark grey. In the default sRGB space.
     */
    public final static Colour DARK_GREY = darkGrey;

    /**
     * The colour black. In the default sRGB space.
     */
    public final static Colour black = new Colour(0x0, 0x0, 0x0);

    /**
     * The colour black. In the default sRGB space.
     */
    public final static Colour BLACK = black;

    /**
     * The colour red. In the default sRGB space.
     */
    public final static Colour red = new Colour(0xff, 0x0, 0x0);

    /**
     * The colour red. In the default sRGB space.
     */
    public final static Colour RED = red;

    /**
     * The colour yellow. In the default sRGB space.
     */
    public final static Colour yellow = new Colour(0xff, 0xff, 0x0);

    /**
     * The colour yellow. In the default sRGB space.
     */
    public final static Colour YELLOW = yellow;

    /**
     * The colour lime. In the default sRGB space.
     */
    public final static Colour lime = new Colour(0x0, 0xff, 0x0);

    /**
     * The colour lime. In the default sRGB space.
     */
    public final static Colour LIME = lime;

    /**
     * The colour magenta. In the default sRGB space.
     */
    public final static Colour magenta = new Colour(0xff, 0x0, 0xff);

    /**
     * The colour magenta. In the default sRGB space.
     */
    public final static Colour MAGENTA = magenta;

    /**
     * The colour aqua. In the default sRGB space.
     */
    public final static Colour aqua = new Colour(0x0, 0xff, 0xff);

    /**
     * The colour aqua. In the default sRGB space.
     */
    public final static Colour AQUA = aqua;

    /**
     * The color blue. In the default sRGB space.
     */
    public final static Colour blue = new Colour(0x0, 0x0, 0xff);

    /**
     * The color blue. In the default sRGB space.
     */
    public final static Colour BLUE = blue;

    /**
     * The color brown. In the default sRGB space.
     */
    public final static Colour brown = new Colour(0.647059f, 0.164706f, 0.164706f);

    /**
     * The color brown. In the default sRGB space.
     */
    public final static Colour BROWN = brown;

    /**
     * The color orange. In the default sRGB space.
     */
    public final static Colour orange = new Colour(1f, 0.5f, 0.0f);

    /**
     * The color orange. In the default sRGB space.
     */
    public final static Colour ORANGE = orange;

    /**
     * The color pink. In the default sRGB space.
     */
    public final static Colour pink = new Colour(0.737255f, 0.560784f, 0.560784f);

    /**
     * The color pink. In the default sRGB space.f
     */
    public final static Colour PINK = pink;

    /**
     * The color scarlet. In the default sRGB space.
     */
    public final static Colour scarlet = new Colour(0.55f, 0.09f, 0.09f);

    /**
     * The color scarlet. In the default sRGB space.
     */
    public final static Colour SCARLET = scarlet;

    /**
     * The color violet. In the default sRGB space.
     */
    public final static Colour violet = new Colour(0.309804f, 0.184314f, 0.309804f);

    /**
     * The color violet. In the default sRGB space.
     */
    public final static Colour VIOLET = violet;

}
