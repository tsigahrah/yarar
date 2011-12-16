package yarar.tietokanta;

/**
 * As the name suggests, this object holds an {@link Object} (the value) and information about it's
 * type ({@link Feldtyp}).<br>
 * This comes handy for result-set manipulations, prepared statements, etc.
 * 
 * @author Dimo Vanchev
 */
public final class TypeValuePair {

    /** The type */
    private final Feldtyp t;
    /** The value */
    private final Object v;

    /**
     * Creates a new TypeValuePair instance.
     * 
     * @param t The type
     * @param v The value
     */
    public TypeValuePair(final Feldtyp t, final Object v) {
	this.t = t;
	this.v = v;
    }

    /**
     * Gets the type.
     * 
     * @return The type.
     */
    public Feldtyp getType() {
	return t;
    }

    /**
     * Gets the value object.
     * 
     * @return The value.
     */
    public Object getValue() {
	return v;
    }
}
