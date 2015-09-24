package engine.obj;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;

/**
 * @author Matthew Van der Bijl
 */
public class Obj extends Object {

    private final List<Vector3f> vertices;
    private final List<Vector2f> textureCoords;
    private final List<Vector3f> normals;
    private final List<Face> faces;
    private boolean enableSmoothShading;

    public Obj(List<Vector3f> vertices, List<Vector2f> textureCoords,
            List<Vector3f> normals, List<Face> faces, boolean enableSmoothShading) {
        super();

        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.faces = faces;
        this.enableSmoothShading = enableSmoothShading;
    }

    public Obj() {
        this(new ArrayList<Vector3f>(), new ArrayList<Vector2f>(),
                new ArrayList<Vector3f>(), new ArrayList<Face>(), true);
    }

    public void enableStates() {
        if (this.isSmoothShadingEnabled()) {
            GL11.glShadeModel(GL_SMOOTH);
        } else {
            GL11.glShadeModel(GL_FLAT);
        }
    }

    public boolean hasTextureCoordinates() {
        return this.getTextureCoordinates().size() > 0;
    }

    public boolean hasNormals() {
        return this.getNormals().size() > 0;
    }

    public List<Vector3f> getVertices() {
        return this.vertices;
    }

    public List<Vector2f> getTextureCoordinates() {
        return this.textureCoords;
    }

    public List<Vector3f> getNormals() {
        return this.normals;
    }

    public List<Face> getFaces() {
        return this.faces;
    }

    public boolean isSmoothShadingEnabled() {
        return this.enableSmoothShading;
    }

    public void setSmoothShadingEnabled(boolean isSmoothShadingEnabled) {
        this.enableSmoothShading = isSmoothShadingEnabled;
    }

    public static class Face extends Object {

        private final int[] vertexIndices;
        private final int[] normalIndices;
        private final int[] textureCoordinateIndices;

        public boolean hasNormals() {
            return this.normalIndices != null;
        }

        public boolean hasTextureCoords() {
            return this.textureCoordinateIndices != null;
        }

        public int[] getVertices() {
            return this.vertexIndices;
        }

        public int[] getTextureCoords() {
            return this.textureCoordinateIndices;
        }

        public int[] getNormals() {
            return this.normalIndices;
        }

        public Face(int[] vertexIndices, int[] textureCoordinateIndices,
                int[] normalIndices) {
            super();

            this.vertexIndices = vertexIndices;
            this.normalIndices = normalIndices;
            this.textureCoordinateIndices = textureCoordinateIndices;
        }

        @Override
        public String toString() {
            return String.format("Face[vertexIndices%s normalIndices%s textureCoordinateIndices%s]",
                    Arrays.toString(vertexIndices), Arrays.toString(normalIndices), Arrays.toString(textureCoordinateIndices));
        }
    }

    public static final class Math extends Object {

        /**
         * You can not instantiate this class.
         */
        @Deprecated
        public Math() throws UnsupportedOperationException {
            // Prevents instantiation of this class.
            throw new UnsupportedOperationException(
                    "You can not instantiate this class.");
        }

        public static Vector3f getCenterPos(Obj model) {
            float xMax = model.getVertices().get(0).getX();
            float yMax = model.getVertices().get(0).getX();
            float zMax = model.getVertices().get(0).getY();
            float xMin = model.getVertices().get(0).getY();
            float yMin = model.getVertices().get(0).getZ();
            float zMin = model.getVertices().get(0).getZ();

            for (Vector3f vertex : model.getVertices()) {
                if (xMax < vertex.getX()) {
                    xMax = vertex.getX();
                } else if (xMin > vertex.getX()) {
                    xMin = vertex.getX();
                }
                if (yMax < vertex.getY()) {
                    yMax = vertex.getY();
                } else if (yMin > vertex.getY()) {
                    yMin = vertex.getY();
                }
                if (zMax < vertex.getZ()) {
                    zMax = vertex.getZ();
                } else if (zMin > vertex.getZ()) {
                    zMin = vertex.getX();
                }
            }

            return new Vector3f(
                    -(xMax + xMin) / 2.0f,
                    -(xMax + yMin) / 2.0f,
                    -(xMax + zMin) / 2.0f
            );
//            
//            float x = 0;
//            float y = 0;
//            float z = 0;
//
//            for (Vector3f vertex : model.getVertices()) {
//                x += vertex.getX();
//                y += vertex.getY();
//                z += vertex.getZ();
//            }
//
//            return new Vector3f(
//                    x / model.getVertices().size(),
//                    y / model.getVertices().size(),
//                    z / model.getVertices().size()
//            );

        }

        public static float getRadiusSq(Obj model) {
            Vector3f centerPos = Obj.Math.getCenterPos(model);
            float radius = 0;

            for (Vector3f vertex : model.getVertices()) {
                float dist = engine.Math.distanceSq(centerPos, vertex);
                if (dist > radius) {
                    radius = dist;
                }
            }

            return radius;
        }

        public static float getRadius(Obj model) {
            return (float) java.lang.Math.sqrt(Obj.Math.getRadiusSq(model));
        }

        public static Vector3f max(Obj model) {
            Vector3f max = model.getVertices().get(0);

            for (Vector3f vertex : model.getVertices()) {
                max = (Vector3f) engine.Math.max(max, vertex);
            }

            return max;
        }

        public static Vector3f min(Obj model) {
            Vector3f min = model.getVertices().get(0);

            for (Vector3f vertex : model.getVertices()) {
                min = (Vector3f) engine.Math.min(min, vertex);
            }

            return min;
        }
    }
}
