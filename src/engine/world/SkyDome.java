/*
 * Copyright (c) 2003, jMonkeyEngine - Mojo Monkey Coding
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer. 
 * 
 * Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution. 
 * 
 * Neither the name of the Mojo Monkey Coding, jME, jMonkey Engine, nor the 
 * names of its contributors may be used to endorse or promote products derived 
 * from this software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package engine.world;

import engine.GameObject;
import engine.Destroyable;
import engine.lwjgl.Texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.ReadableVector3f;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

/**
 * <code>SkyDome</code> defines an implementation of the sky interface where the
 * sky is represented as a dome. The dome or half sphere is rendered over the
 * camera and gives a smooth look and allow for transitions of the texture and
 * color. This will allow for day/night effects and other natural phenomenon.
 * Edited for (1e+100)++
 *
 * For more information see
 * <a href="http://www.spheregames.com/files/SkyDomesPDF.zip">this</a>
 * PDF file.
 *
 * @author Mark Powell
 * @author Matthew Van der Bijl
 */
public class SkyDome extends Object implements GameObject, Destroyable {

    private int displayList;
    private final Texture texture;
    private ReadableVector3f center;

    private float rotation;
    private float rotationSpeed;

    /**
     * Constructor instantiates a new <code>SkyDome</code> object. All
     * attributes are stored and the dome is generated.
     *
     * @param radius the radius of the sphere.
     * @param dLon the spacing between longitude lines in degrees.
     * @param dLat the spacing between latitude lines in degrees.
     * @param hTile no idea
     * @param vTile no idea
     * @param texture the texture to be bound to the sky dome
     *
     * @see IllegalArgumentException
     * @throws IllegalArgumentException dLon nor dLat can be equal to zero
     */
    public SkyDome(Texture texture, float radius, float dLon, float dLat,
            float hTile, float vTile) throws IllegalArgumentException {
        super();

        this.texture = texture;
        this.rotationSpeed = 0.05f;
        this.center = new Vector3f(0f, 0f, 0f);

        this.init(dLon, dLat, hTile, vTile, radius);
    }

    public Texture getTexture() {
        return this.texture;
    }

    /**
     * <code>update</code> calculates the new position of the dome and the
     * texture based on the speeds.
     */
    @Override
    public void update(float dt) {
        this.rotation += rotationSpeed * dt;
        while (rotation > 360 || rotation < 0) {
            if (rotation > 360) {
                this.rotation -= 360;
            } else if (rotation < 0) {
                this.rotation += 360;
            }
        }
    }

    /**
     * <code>render</code> draws the dome around the camera and applies the
     * texture.
     */
    @Override
    public void render() {
        GL11.glPushMatrix();
        {
            GL11.glTranslatef(center.getX(), center.getY(), center.getZ());
            GL11.glRotatef(rotation, 0, 1, 0);

            GL11.glCallList(displayList);
        }
        GL11.glPopMatrix();
    }

    /**
     * <code>Init</code> builds the list of vertices that make up the dome and
     * sets the texture coordinates. NOTE: Code is from the sky dome tutorial at
     * www.spheregames.com.
     *
     * @see IllegalArgumentException
     * @throws IllegalArgumentException dLon nor dLat can be equal to zero
     */
    private void init(float dLon, float dLat, float hTile, float vTile,
            float radius) throws IllegalArgumentException {
        if (dLon == 0 || dLat == 0) {
            // System.err.println("dLon nor dLat can be equal to zero.");
            throw new IllegalArgumentException("dLon nor dLat can be equal to zero.");
        } else {
            final float DTOR = (float) java.lang.Math.PI / 180f;
//            final Vertex vertices[] = new Vertex[(int) ((360 / dLon) * (90 / dLat) * 4)];

            this.displayList = GL11.glGenLists(1);
            GL11.glNewList(displayList, GL_COMPILE);
            {
                this.texture.bind();

                GL11.glPushMatrix();
                {
                    GL11.glCullFace(GL_FRONT);
                    GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
                    GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

                    GL11.glRotatef(270, 1, 0, 0);

                    GL11.glBegin(GL_TRIANGLE_STRIP);
                    {
                        for (int lat = 0; lat <= 90 - dLat; lat += dLat) {
                            for (int lon = 0; lon <= 360 - dLon; lon += dLon) {
                                {
                                    Vertex vertex = new Vertex();
                                    vertex.x = (float) (radius * Math.sin(lat * DTOR) * Math.cos(DTOR * lon));
                                    vertex.y = (float) (radius * Math.sin(lat * DTOR) * Math.sin(DTOR * lon));
                                    vertex.z = (float) (radius * Math.cos(lat * DTOR));

                                    float vx = vertex.getX();
                                    float vy = vertex.getY();
                                    float vz = vertex.getZ();

                                    float mag = (float) Math.sqrt((vx * vx) + (vy * vy) + (vz * vz));
                                    vx /= mag;
                                    vy /= mag;
                                    vz /= mag;

                                    vertex.s = hTile * (float) (Math.atan2(vx, vz) / (Math.PI * 2)) + 0.5f;
                                    vertex.t = vTile * (float) (Math.asin(vy) / Math.PI) + 0.5f;

                                    GL11.glTexCoord2f(vertex.s, vertex.t);
                                    GL11.glVertex3f(vertex.x, vertex.y, vertex.z);
                                }

                                {
                                    Vertex vertex = new Vertex();
                                    vertex.x = (float) (radius * Math.sin((lat + dLat) * DTOR) * Math.cos(lon * DTOR));
                                    vertex.y = (float) (radius * Math.sin((lat + dLat) * DTOR) * Math.sin(lon * DTOR));
                                    vertex.z = (float) (radius * Math.cos((lat + dLat) * DTOR));

                                    float vx = vertex.getX();
                                    float vy = vertex.getY();
                                    float vz = vertex.getZ();

                                    float mag = (float) Math.sqrt((vx * vx) + (vy * vy) + (vz * vz));
                                    vx /= mag;
                                    vy /= mag;
                                    vz /= mag;

                                    vertex.s = (float) ((hTile * Math.atan2(vx, vz) / (Math.PI * 2)) + 0.5f);
                                    vertex.t = (float) (vTile * (Math.asin(vy) / Math.PI) + 0.5f);

                                    GL11.glTexCoord2f(vertex.s, vertex.t);
                                    GL11.glVertex3f(vertex.x, vertex.y, vertex.z);
                                }

                                {
                                    Vertex vertex = new Vertex();
                                    vertex.x = (float) (radius * Math.sin(DTOR * lat) * Math.cos(DTOR * (lon + dLon)));
                                    vertex.y = (float) (radius * Math.sin(DTOR * lat) * Math.sin(DTOR * (lon + dLon)));
                                    vertex.z = (float) (radius * Math.cos(DTOR * lat));

                                    float vx = vertex.getX();
                                    float vy = vertex.getY();
                                    float vz = vertex.getZ();

                                    float mag = (float) Math.sqrt((vx * vx) + (vy * vy) + (vz * vz));
                                    vx /= mag;
                                    vy /= mag;
                                    vz /= mag;

                                    vertex.s = (float) ((hTile * Math.atan2(vx, vz) / (Math.PI * 2)) + 0.5f);
                                    vertex.t = (float) (vTile * (Math.asin(vy) / Math.PI) + 0.5f);

                                    GL11.glTexCoord2f(vertex.s, vertex.t);
                                    GL11.glVertex3f(vertex.x, vertex.y, vertex.z);
                                }

                                if (lat > -90 && lat < 90) {
                                    Vertex vertex = new Vertex();

                                    vertex.x = (float) (radius * Math.sin((lat + dLat) * DTOR) * Math.cos(DTOR * (lon + dLon)));
                                    vertex.y = (float) (radius * Math.sin((lat + dLat) * DTOR) * Math.sin(DTOR * (lon + dLon)));
                                    vertex.z = (float) (radius * Math.cos((lat + dLat) * DTOR));

                                    // Calculate the texture coordinates
                                    float vx = vertex.getX();
                                    float vy = vertex.getY();
                                    float vz = vertex.getZ();

                                    float mag = (float) Math.sqrt((vx * vx) + (vy * vy) + (vz * vz));
                                    vx /= mag;
                                    vy /= mag;
                                    vz /= mag;

                                    vertex.s = (float) ((hTile * Math.atan2(vx, vz) / (Math.PI * 2)) + 0.5f);
                                    vertex.t = (float) (vTile * (Math.asin(vy) / Math.PI) + 0.5f);

                                    GL11.glTexCoord2f(vertex.s, vertex.t);
                                    GL11.glVertex3f(vertex.x, vertex.y, vertex.z);
                                }
                            }
                        }
                    }
                    GL11.glEnd();
                    GL11.glFlush();

                    GL11.glCullFace(GL_BACK);
                }
                GL11.glPopMatrix();

                this.texture.release();
            }
            GL11.glEndList();
        }
    }

    /**
     * <code>Vertex</code> provides an extension to <code>Vector3f</code> to
     * handle texture coordinates as well as position coordinates.
     */
    private final class Vertex extends Vector3f {

        /**
         * The s coordinate of the texture.
         */
        public float s;
        /**
         * The t coordinate of the texture.
         */
        public float t;
    }

    @Override
    public void destroy() {
        this.texture.destroy();
        GL11.glDeleteLists(displayList, 1);
        this.displayList = -1;
    }

    public ReadableVector3f getCenter() {
        return this.center;
    }

    public void setCenter(ReadableVector3f center) {
        this.center = center;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotationSpeed() {
        return this.rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public int getDisplayList() {
        return this.displayList;
    }
}
