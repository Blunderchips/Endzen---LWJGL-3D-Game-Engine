
import game.Game;

/**
 * The sole purpose of this class is to hold the main method. Any other use
 * should be placed in a separate class.
 *
 * @author Matthew 'siD' van der bijl
 */
public final class Main {

    /**
     * You can not instantiate this class.
     */
    @Deprecated
    public Main() throws UnsupportedOperationException {
        // Prevents instantiation of this class.
        throw new UnsupportedOperationException(
                "You can not instantiate this class.");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Game.Start();
    }
}
