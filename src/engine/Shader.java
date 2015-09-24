package engine;

import org.lwjgl.opengl.GL20;

/**
 * Handles all shader files and shader operation. A Shader is a program designed
 * to run on some stage of a graphics processor. Its purpose is to execute one
 * of the programmable stages of the rendering pipeline.
 *
 * @author Matthew 'siD' Van der Bijl
 *
 * @see <a href='http://en.wikipedia.org/wiki/Shader'>wikipedia</a>
 * @see enigne.GLSL
 * @see java.lang.Object
 * @see java.lang.Cloneable
 */
public class Shader extends GLSL implements Destroyable {

    private int program;

    /**
     * Default constructor.
     */
    public Shader() {
        this(-1);
    }

    public Shader(String path, int type) throws DwarfException {
        this(GLSL.createShader(path, type));
    }

    public Shader(Shader shader) {
        this(shader.getProgram());
    }

    public Shader(int program) {
        this.program = program;
    }

    /**
     * uses the <code>Shader</code>.
     */
    public void bind() {
        GLSL.useProgram(program);
    }

    /**
     * release the <code>Shader</code>.
     */
    public void realese() {
        GL20.glUseProgram(0);
    }

    /**
     * destroys the <code>Shader</code>.
     */
    @Override
    public void destroy() {
        GLSL.destroy(program);
    }

    /**
     * Class Object is the root of the class hierarchy.
     * <p>
     * Every class has Object as a superclass. All objects, including arrays,
     * implement the methods of this class.</p>
     *
     * @return a hash code value for this object.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + program;
        return hash;
    }

    /**
     * Returns true if the <code>this</code> is equal to the argument and false
     * otherwise.
     * <p>
     * Consequently, if both argument are null, true is returned, false is
     * returned. Otherwise, equality is determined by using the equals method of
     * the first argument.</p>
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

        final Shader shader = (Shader) obj;

        if (this.program != shader.program) {
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
        return "Shader[" + program + "]";
    }

    public int getProgram() {
        return this.program;
    }

    public void setProgram(int program) {
        this.program = program;
    }
}
