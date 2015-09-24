package engine;

import java.io.InputStream;

import engine.lwjgl.WaveData;

import org.lwjgl.openal.AL10;

import static org.lwjgl.openal.AL10.AL_BUFFER;

/**
 * play Sound effect (sfx) using <a href='http://www.openal.org/'>OpenAL</a>.
 * (Open Audio Library) Load various sound formats for use with <a
 * href='http://www.openal.org/'>OpenAL</a> (.wav, .xm, .ogg, etc).
 *
 * @author Matthew 'siD' Van der Bijl
 *
 * @see java.lang.Object
 * @see java.lang.Cloneable
 * @see enigne.engine.core.openAL
 * @see <a href='http://www.openal.org/'>openal.org</a>
 */
public class Sfx extends Object implements Destroyable {

    private final int source;
    private final int buffer;

    public Sfx(InputStream stream)   {
        super();

        WaveData data = WaveData.create(stream);
        this.buffer = AL10.alGenBuffers();
        AL10.alBufferData(buffer, data.getFormat(), data.getData(), data.getSamplerate());
        data.dispose();
        this.source = AL10.alGenSources();
        AL10.alSourcei(source, AL_BUFFER, buffer);
    }

    /**
     * plays the sound.
     */
    public void play() {
        AL10.alSourcePlay(source);
    }

    /**
     * pauses the sound.
     */
    public void pause() {
        AL10.alSourcePause(source);
    }

    /**
     * stops the sound.
     */
    public void stop() {
        AL10.alSourceStop(source);
    }

    /**
     * rewinds the sound.
     */
    public void rewind() {
        AL10.alSourceRewind(source);
    }

    /**
     * dispose if the sound.
     */
    @Override
    public void destroy() {
        AL10.alDeleteBuffers(buffer);
    }

    public int getSource() {
        return this.source;
    }

    public int getBuffer() {
        return this.buffer;
    }

    /**
     * Class Object is the root of the class hierarchy. Every class has Object
     * as a superclass. All objects, including arrays, implement the methods of
     * this class.
     *
     * @return a hash code value for this object.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.source;
        hash = 37 * hash + this.buffer;
        return hash;
    }

    /**
     * Returns true if the <code>this</code> is equal to the argument and false
     * otherwise. Consequently, if both argument are null, true is returned,
     * false is returned. Otherwise, equality is determined by using the equals
     * method of the first argument.
     *
     * @param obj the <code>Object</code> to be tested
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * @return true if the argument is equal to <code>this</code> other and
     * false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        final Sfx other = (Sfx) obj;

        if (this.source != other.source) {
            return false;
        } else if (this.buffer != other.buffer) {
            return false;
        }

        return true;
    }

    /**
     * Returns a string representation of the object.
     * <p>
     * In general, the toString method returns a string that "textually
     * represents" this object. The result should be a concise but informative
     * representation that is easy for a person to read. It is recommended that
     * all subclasses override this method.</p>
     *
     * @return a textually representation of this object
     */
    @Override
    public String toString() {
        return "Sound["
                + "source: " + getSource() + ", "
                + "buffer: " + getBuffer() + ", "
                + "]";
    }

    public Sfx get() {
        return this;
    }
}
