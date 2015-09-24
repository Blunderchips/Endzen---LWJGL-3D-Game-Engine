//package engine.gfx;
//
//import java.util.List;
//import java.util.ArrayList;
//
//import engine.Math;
//
//import org.lwjgl.util.vector.Vector3f;
//import org.lwjgl.util.vector.Quaternion;
//import org.lwjgl.util.vector.ReadableVector3f;
//
///**
// * A basic wrapper around the values need for a 3D cube class.
// *
// * @author Matthew 'siD' Van der Bijl
// */
//public class Cube extends Polyhedron {
//
//    public Cube(float lineLength, float xPos, float yPos, float zPos,
//            Quaternion rotation, Colour colour) {
//        this(lineLength, new Vector3f(zPos, zPos, zPos), rotation, colour, false);
//    }
//
//    public Cube(float lineLength, ReadableVector3f position,
//            Quaternion rotation, Colour colour) {
//        this(lineLength, position, rotation, colour, false);
//    }
//
//    public Cube(float lineLength, float xPos, float yPos, float zPos,
//            Quaternion rotation, Colour colour, boolean isWireFrame) {
//        this(lineLength, new Vector3f(zPos, zPos, zPos), rotation, colour, isWireFrame);
//    }
//
//    public Cube(float lineLength, ReadableVector3f position,
//            Quaternion rotation, Colour colour, boolean isWireFrame) {
//        super(null, position, rotation, colour, isWireFrame);
//
//        this.setLineLength(lineLength);
//    }
//
//    public Cube(Polyhedron polyhedron) {
//        super(polyhedron);
//    }
//
//    @Override
//    public void render() {
//        engine.gfx.Draw3D.cube(
//                Math.distance(vertices[0], vertices[1]),
//                position, rotation, colour, wireFrame
//        );
//    }
//
//    /**
//     * TODO: optimize
//     *
//     * @param length the length of each line segment
//     */
//    public final void setLineLength(float length) {
//        List<Vector3f> values = new ArrayList<>();
//
//        {
//            values.add(new Vector3f(length, length, 0));
//            values.add(new Vector3f(0, length, 0));
//            values.add(new Vector3f(0, length, length));
//            values.add(new Vector3f(length, length, length));
//        }
//        {
//            values.add(new Vector3f(length, 0, length));
//            values.add(new Vector3f(0, 0, length));
//            values.add(new Vector3f(0, 0, 0));
//            values.add(new Vector3f(length, 0, 0));
//        }
//        {
//            values.add(new Vector3f(length, length, length));
//            values.add(new Vector3f(0, length, length));
//            values.add(new Vector3f(0, 0, length));
//            values.add(new Vector3f(length, 0, length));
//        }
//        {
//            values.add(new Vector3f(length, 0, 0));
//            values.add(new Vector3f(0, 0, 0));
//            values.add(new Vector3f(0, length, 0));
//            values.add(new Vector3f(length, length, 0));
//        }
//        {
//            values.add(new Vector3f(0, length, length));
//            values.add(new Vector3f(0, length, 0));
//            values.add(new Vector3f(0, 0, 0));
//            values.add(new Vector3f(0, 0, length));
//        }
//        {
//            values.add(new Vector3f(length, length, 0));
//            values.add(new Vector3f(length, length, length));
//            values.add(new Vector3f(length, 0, length));
//            values.add(new Vector3f(length, 0, 0));
//        }
//
//        ReadableVector3f[] arr = new Vector3f[values.size()];
//        for (int i = 0; i < arr.length; i++) {
//            arr[i] = values.get(i);
//        }
//
//        super.setVertices(arr);
//    }
//}
