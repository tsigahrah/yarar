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
    private final Feldtyp type;
    /** The value */
    private final Object value;

    /**
     * Creates a new TypeValuePair instance.
     * 
     * @param type The type
     * @param value The value
     */
    public TypeValuePair(final Feldtyp type, final Object value) {
	this.type = type;
	this.value = value;
    }

    /**
     * Gets the type.
     * 
     * @return The type.
     */
    public Feldtyp getType() {
	return type;
    }

    /**
     * Gets the value object.
     * 
     * @return The value.
     */
    public Object getValue() {
	return value;
    }
}
