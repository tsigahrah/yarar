package yarar.tietokanta;

/**
 * This modified <code>NullPointerException</code> should be thrown when the system tries to
 * load/call not existing {@link Kysely} object.
 * 
 * @author Dimo Vanchev
 */
@SuppressWarnings("serial")
public class KyselyNotFoundException extends NullPointerException {
    /**
     * Exception constructor.
     * 
     * @param key The message for the exception.
     */
    public KyselyNotFoundException(final String key) {
	super("Kysely object not found: [" + key
		+ "]. Check if such key is defined in conf/kyselyladata.properties.");
    }
}
