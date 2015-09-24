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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.stream.ImageInputStream;

import engine.DwarfException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;

/**
 * A utility class to load textures for OpenGL. This source is based on a
 * texture that can be found in the Java Gaming (<a
 * href="http://www.java-gaming.org/">www.javagaming.org</a>) Wiki. It has been
 * simplified slightly for explicit 2D graphics use.
 *
 * OpenGL uses a particular image format. Since the images that are loaded from
 * disk may not match this format this loader introduces a intermediate image
 * which the source image is copied into. In turn, this image is used as source
 * for the OpenGL texture.
 *
 * <br/>
 * This is a modified version of the TextureLoader class from:
 * org.lwjgl.examples.spaceinvaders.TextureLoader
 *
 * @author Kevin Glass
 * @author Brian Matzon
 */
public final class TextureLoader extends Object {

    private final HashMap<String, Texture> table;
    /**
     * The colour model including alpha for the GL image.
     */
    private final ColorModel glAlphaColorModel;
    /**
     * The colour model for the GL image.
     */
    private final ColorModel glColorModel;
    /**
     * Scratch buffer for texture ID's
     */
    private static final IntBuffer textureIDBuffer = BufferUtils.createIntBuffer(1);

    /**
     * Constructs a new TextureLoader.
     */
    public TextureLoader() {
        super();
        this.table = new HashMap<>();
        glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[]{8, 8, 8, 8},
                true,
                false,
                ComponentColorModel.TRANSLUCENT,
                DataBuffer.TYPE_BYTE
        );

        glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[]{8, 8, 8, 0},
                false,
                false,
                ComponentColorModel.OPAQUE,
                DataBuffer.TYPE_BYTE
        );
    }

    /**
     * Create a new texture ID.
     *
     * @return A new texture ID
     */
    private int createTextureID() {
        GL11.glGenTextures(textureIDBuffer);
        return textureIDBuffer.get(0);
    }

    /**
     * Load a texture.
     *
     * @param path The location of the resource to load
     * @return The loaded texture
     * @throws DwarfException Indicates a failure to access the resource
     */
    public Texture getTexture(String path) throws DwarfException {
        Texture tex = table.get(path);

        if (tex != null) {
            return tex;
        }

        tex = this.getTexture(path,
                GL_TEXTURE_2D, // target
                GL_RGBA, // dst pixel format
                GL_LINEAR, // min filter (unused)
                GL_LINEAR);

        table.put(path, tex);

        if (tex == null) {
            throw new DwarfException("texture not loaded correctly");
        } else {
            return tex;
        }
    }

//    public int loadCubeMap(String[] texturePaths) {
//        int texID = GL11.glGenTextures();
//        GL13.glActiveTexture(GL13.GL_TEXTURE0);
//        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
//
//        int target = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X - 1;
//
//        for (String tex : texturePaths) {
//            try {
//                BufferedImage bufferedImage = loadImage(ClassLoader.getSystemResourceAsStream(tex));
//                ByteBuffer textureBuffer = convertImageData(bufferedImage, new Texture());
//
//                int srcPixelFormat;
//                if (bufferedImage.getColorModel().hasAlpha()) {
//                    srcPixelFormat = GL_RGBA;
//                } else {
//                    srcPixelFormat = GL_RGB;
//                }
//
//                GL11.glTexImage2D(
//                        target++,
//                        0,
//                        GL_RGBA,
//                        engine.Math.get2Fold(bufferedImage.getWidth()),
//                        engine.Math.get2Fold(bufferedImage.getHeight()),
//                        0,
//                        srcPixelFormat,
//                        GL_UNSIGNED_BYTE,
//                        textureBuffer
//                );
//            } catch (IOException ioe) {
//                throw new DwarfException(ioe);
//            }
//
//            GL11.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//            GL11.glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//        }
//        
//        return texID;
//    }

    /**
     * Load a texture into OpenGL from a image reference on disk.
     *
     * @param path The location of the resource to load
     * @param target The GL target to load the texture against
     * @param dstPixelFormat The pixel format of the screen
     * @param minFilter The minimizing filter
     * @param magFilter The magnification filter
     * @return The loaded texture
     * @throws DwarfException Indicates a failure to access the resource
     */
    public Texture getTexture(String path,
            int target,
            int dstPixelFormat,
            int minFilter,
            int magFilter) throws DwarfException {
        try {
            int srcPixelFormat;

            // create the texture ID for this texture
            int textureID = createTextureID();
            Texture texture = new Texture(target, textureID);

            // bind this texture
            GL11.glBindTexture(target, textureID);

            BufferedImage bufferedImage = loadImage(ClassLoader.getSystemResourceAsStream(path));
            texture.setWidth(bufferedImage.getWidth());
            texture.setHeight(bufferedImage.getHeight());

            if (bufferedImage.getColorModel().hasAlpha()) {
                srcPixelFormat = GL_RGBA;
            } else {
                srcPixelFormat = GL_RGB;
            }

            // convert that image into a byte buffer of texture data
            ByteBuffer textureBuffer = convertImageData(bufferedImage, texture);

            if (target == GL_TEXTURE_2D) {
                GL11.glTexParameteri(target, GL_TEXTURE_MIN_FILTER, minFilter);
                GL11.glTexParameteri(target, GL_TEXTURE_MAG_FILTER, magFilter);
            }

            // produce a texture from the byte buffer
            GL11.glTexImage2D(target,
                    0,
                    dstPixelFormat,
                    engine.Math.get2Fold(bufferedImage.getWidth()),
                    engine.Math.get2Fold(bufferedImage.getHeight()),
                    0,
                    srcPixelFormat,
                    GL_UNSIGNED_BYTE,
                    textureBuffer
            );

            return texture;
        } catch (NullPointerException npe) {
//            System.err.println(npe);
            throw new DwarfException("Texture '" + path + "' was not loaded correctly.");
        } catch (Exception ex) {
//            System.err.println(ex);
            throw new DwarfException(ex);
        }
    }

    /**
     * Convert the buffered image to a texture.
     *
     * @param bufferedImage The image to convert to a texture
     * @param texture The texture to store the data into
     * @return A buffer containing the data
     */
    private ByteBuffer convertImageData(BufferedImage bufferedImage, Texture texture) {
        ByteBuffer imageBuffer;
        WritableRaster raster;
        BufferedImage texImage;

        int texWidth = 2;
        int texHeight = 2;

        // find the closest power of 2 for the width and height
        // of the produced texture
        while (texWidth < bufferedImage.getWidth()) {
            texWidth *= 2;
        }
        while (texHeight < bufferedImage.getHeight()) {
            texHeight *= 2;
        }

        texture.setTextureHeight(texHeight);
        texture.setTextureWidth(texWidth);

        // create a raster that can be used by OpenGL as a source
        // for a texture
        if (bufferedImage.getColorModel().hasAlpha()) {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 4, null);
            texImage = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
        } else {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 3, null);
            texImage = new BufferedImage(glColorModel, raster, false, new Hashtable());
        }

        // copy the source image into the produced image
        Graphics gfx = texImage.getGraphics();
        gfx.setColor(new Color(0f, 0f, 0f, 0f));
        gfx.fillRect(0, 0, texWidth, texHeight);
        gfx.drawImage(bufferedImage, 0, 0, null);

        // build a byte buffer from the temporary image
        // that be used by OpenGL to produce a texture.
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

        imageBuffer = ByteBuffer.allocateDirect(data.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);
        imageBuffer.flip();

        return imageBuffer;
    }

    /**
     * Load a given resource as a buffered image.
     *
     * @param url The location of the resource to load
     * @return The loaded buffered image
     * @throws DwarfException Indicates a failure to find a resource
     */
    private BufferedImage loadImage(URL url) throws DwarfException {
        try {
            if (url != null) {
                return ImageIO.read(url);
            } else {
                throw new DwarfException(url + " was not found.");
            }
        } catch (IOException ioe) {
            throw new DwarfException(ioe);
        }
    }

    /**
     * Load a given resource as a buffered image.
     *
     * @param file The location of the resource to load
     * @return The loaded buffered image
     * @throws FileNotFoundException Indicates a failure to find a resource
     */
    private BufferedImage loadImage(File file) throws IOException, FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException(file + " was not found.");
        }
        return ImageIO.read(file);
    }

    /**
     * Load a given resource as a buffered image.
     *
     * @param file The location of the resource to load
     * @return The loaded buffered image
     */
    private BufferedImage loadImage(InputStream file) throws IOException {
        return ImageIO.read(file);
    }

    /**
     * Load a given resource as a buffered image.
     *
     * @param inputStream The location of the resource to load
     * @return The loaded buffered image
     */
    private BufferedImage loadImage(ImageInputStream inputStream) throws IOException {
        return ImageIO.read(inputStream);
    }

}
