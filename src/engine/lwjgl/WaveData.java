/* 
 * Copyright (c) 2002-2008 LWJGL Project
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are 
 * met:
 * 
 * * Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of 
 *   its contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package engine.lwjgl;

import engine.DwarfException;
import com.sun.media.sound.WaveFileReader;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Objects;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO8;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO8;

/**
 * utility class for loading wavefiles.
 *
 * @see java.lang.Object
 * @see java.lang.Cloneable
 */
public class WaveData extends java.lang.Object implements Cloneable {

    /**
     * actual wave data.
     */
    private ByteBuffer data;

    /**
     * format type of data.
     */
    private int format;

    /**
     * sample rate of data.
     */
    private int samplerate;

    /**
     * Default constructor.
     */
    public WaveData() {
        super();
    }

    /**
     * Creates a new WaveData
     *
     * @param data actual wavedata
     * @param format format of wave data
     * @param samplerate sample rate of data
     */
    public WaveData(ByteBuffer data, int format, int samplerate) {
        super();

        this.data = data;
        this.format = format;
        this.samplerate = samplerate;
    }

    public WaveData(WaveData waveData) {
        this(waveData.getData(), waveData.getFormat(), waveData.getFormat());
    }

    /**
     * Disposes the wavedata
     */
    public void dispose() {
        data.clear();
    }

    /**
     * Creates a WaveData container from the specified url
     *
     * @param path URL to file
     * @return WaveData containing data, or null if a failure occurred
     */
    public static WaveData create(URL path) throws DwarfException {
        try {
            // due to an issue with AudioSystem.getAudioInputStream
            // and mixing unsigned and signed code
            // we will use the reader directly
            return create(new WaveFileReader().getAudioInputStream(new BufferedInputStream(path.openStream())));
        } catch (IOException | UnsupportedAudioFileException ex) {
            throw new DwarfException(ex);
        }
    }

    /**
     * Creates a WaveData container from the specified in the classpath
     *
     * @param path path to file (relative, and in classpath)
     * @return WaveData containing data, or null if a failure occurred
     */
    public static WaveData create(String path) {
        return create(Thread.currentThread().getContextClassLoader().getResource(path));
    }

    /**
     * Creates a WaveData container from the specified inputstream
     *
     * @param is InputStream to read from
     * @return WaveData containing data, or null if a failure occurred
     */
    public static WaveData create(InputStream is) throws DwarfException {
        try {
            return create(
                    AudioSystem.getAudioInputStream(is));
        } catch (UnsupportedAudioFileException | IOException ex) {
            throw new DwarfException(ex);
        }
    }

    /**
     * Creates a WaveData container from the specified bytes
     *
     * @param buffer array of bytes containing the complete wave file
     * @return WaveData containing data, or null if a failure occurred
     */
    public static WaveData create(byte[] buffer) throws DwarfException {
        try {
            return create(
                    AudioSystem.getAudioInputStream(
                            new BufferedInputStream(new ByteArrayInputStream(buffer))));
        } catch (UnsupportedAudioFileException | IOException ex) {
            throw new DwarfException(ex);
        }
    }

    /**
     * Creates a WaveData container from the specified ByetBuffer. If the buffer
     * is backed by an array, it will be used directly, else the contents of the
     * buffer will be copied using get(byte[]).
     *
     * @param buffer ByteBuffer containing sound file
     * @return WaveData containing data, or null if a failure occured
     */
    public static WaveData create(ByteBuffer buffer) throws DwarfException {
        try {
            byte[] bytes = null;

            if (buffer.hasArray()) {
                bytes = buffer.array();
            } else {
                bytes = new byte[buffer.capacity()];
                buffer.get(bytes);
            }
            return create(bytes);
        } catch (engine.DwarfException ex) {
            throw new DwarfException(ex);
        }
    }

    /**
     * Creates a WaveData container from the specified stream
     *
     * @param ais AudioInputStream to read from
     * @return WaveData containing data, or null if a failure occurred
     */
    public static WaveData create(AudioInputStream ais) throws DwarfException {
        //get format of data
        AudioFormat audioformat = ais.getFormat();

        // get channels
        int channels = 0;
        if (audioformat.getChannels() == 1) {
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = AL_FORMAT_MONO8;
            } else if (audioformat.getSampleSizeInBits() == 16) {
                channels = AL_FORMAT_MONO16;
            } else {
                assert false : "Illegal sample size";
            }
        } else if (audioformat.getChannels() == 2) {
            if (audioformat.getSampleSizeInBits() == 8) {
                channels = AL_FORMAT_STEREO8;
            } else if (audioformat.getSampleSizeInBits() == 16) {
                channels = AL_FORMAT_STEREO16;
            } else {
                assert false : "Illegal sample size";
            }
        } else {
            assert false : "Only mono or stereo is supported";
        }

        //read data into buffer
        ByteBuffer buffer = null;
        try {
            int available = ais.available();
            if (available <= 0) {
                available = ais.getFormat().getChannels() * (int) ais.getFrameLength() * ais.getFormat().getSampleSizeInBits() / 8;
            }
            byte[] buf = new byte[ais.available()];
            int read = 0, total = 0;
            while ((read = ais.read(buf, total, buf.length - total)) != -1
                    && total < buf.length) {
                total += read;
            }
            buffer = convertAudioBytes(buf, audioformat.getSampleSizeInBits() == 16, audioformat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        } catch (IOException ioe) {
            throw new DwarfException(ioe);
        }

        //create our result
        WaveData wavedata
                = new WaveData(buffer, channels, (int) audioformat.getSampleRate());

        //close stream
        try {
            ais.close();
        } catch (IOException ioe) {
            throw new DwarfException(ioe);
        }

        return wavedata;
    }

    private static ByteBuffer convertAudioBytes(byte[] audio_bytes, boolean two_bytes_data, ByteOrder order) {
        ByteBuffer dest = ByteBuffer.allocateDirect(audio_bytes.length);
        dest.order(ByteOrder.nativeOrder());
        ByteBuffer src = ByteBuffer.wrap(audio_bytes);
        src.order(order);
        if (two_bytes_data) {
            ShortBuffer dest_short = dest.asShortBuffer();
            ShortBuffer src_short = src.asShortBuffer();
            while (src_short.hasRemaining()) {
                dest_short.put(src_short.get());
            }
        } else {
            while (src.hasRemaining()) {
                dest.put(src.get());
            }
        }
        dest.rewind();

        return dest;
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
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(getData());
        hash = 17 * hash + getFormat();
        hash = 17 * hash + getSamplerate();
        return hash;
    }

    /**
     * Returns true if the <code>this</code> is equal to the argument and false
     * otherwise. Consequently, if both argument are null, true is returned,
     * false is returned. Otherwise, equality is determined by using the equals
     * method of the first argument.
     *
     * @param obj the object to be tested
     * @return true if the argument is equal to <code>this</code> other and
     * false otherwise
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        final WaveData waveData = (WaveData) obj;

        if (!Objects.equals(this.getData(), waveData.getData())) {
            return false;
        } else if (this.getFormat() != waveData.getFormat()) {
            return false;
        } else if (this.getSamplerate() != waveData.getSamplerate()) {
            return false;
        }

        return true;
    }

    public ByteBuffer getData() {
        return this.data;
    }

    public int getFormat() {
        return this.format;
    }

    public int getSamplerate() {
        return this.samplerate;
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
        return "WaveData[" + "data: " + data + ", " + "format: " + format
                + ", " + "samplerate: " + samplerate + "]";
    }

    public void set(ByteBuffer data, int format, int samplerate) {
        this.data = data;
        this.format = format;
        this.samplerate = samplerate;
    }

    public void set(WaveData waveData) {
        this.data = waveData.getData();
        this.format = waveData.getFormat();
        this.samplerate = waveData.getFormat();
    }

    @Override
    public WaveData clone() throws CloneNotSupportedException {
        return new WaveData(this);
    }
}
