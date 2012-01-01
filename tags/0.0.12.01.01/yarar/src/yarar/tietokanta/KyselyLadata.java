package yarar.tietokanta;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

import yarar.rikai.ConfigParser;
import yarar.rikai.Logger;

/**
 * This is a class that loads all SQL calls of the application.<br>
 * <b>Kysely</b> is a <a href="http://en.wikipedia.org/wiki/Finnish_language"
 * target="_blank">Finnish</a> word for <i>query</i>. <br>
 * <b>Ladata</b> is a <a href="http://en.wikipedia.org/wiki/Finnish_language"
 * target="_blank">Finnish</a> verb with meaning <i>load, charge</i>.
 * 
 * @author Dimo Vanchev
 * 
 */
public final class KyselyLadata {

    /**
     * Parser of configuration with SQLs.
     */
    private final ConfigParser configParser;

    /**
     * {@link Enumeration} containing all keys from the relevant configuration file.
     */
    private final Set<Object> keys;

    /**
     * The single instance of this class.
     */
    private static KyselyLadata selfInstance;

    /**
     * This {@link HashMap} contains all SQLs needed for application's operations, wrapped in
     * {@link Kysely} objects.
     */
    private HashMap<String, Kysely> sqls;

    /** Singleton private constructor. */
    private KyselyLadata() {
	KyselyLadata.selfInstance = this;

	// loading properties from the the relevant properties file.
	configParser = new ConfigParser("kyselyladata.properties");
	keys = configParser.getAllKeys();
	loadSQLs();
    }

    /**
     * Singleton instance getter.
     * 
     * @return The single instance of this class.
     */
    public static KyselyLadata getInstance() {
	if (KyselyLadata.selfInstance == null) {
	    new KyselyLadata();
	}

	return KyselyLadata.selfInstance;
    }

    /**
     * Returns the {@link Kysely} object assigned to the relevant key.
     * 
     * @param key
     *            To search in the sql's <code>HashMap</code>
     * @return the found <code>Kysely</code> object.
     */
    public Kysely getKysely(final String key) {
	final Kysely kysely = sqls.get(key);
	if (kysely == null) {
	    throw new KyselyNotFoundException(key);
	}
	return kysely;
    }

    /**
     * Loads all SQLs and wraps them in {@link Kysely} objects.
     */
    private void loadSQLs() {
	// initiating the HashMap
	sqls = new HashMap<String, Kysely>(keys.size());
	// taking current thread as a loader
	final ClassLoader loader = Thread.currentThread()
		.getContextClassLoader();

	// start iterating over all keys
	for (final Object key : keys) {
	    final String fileName = configParser.getValue((String) key);
	    final String sql = readFile(fileName, loader);
	    sqls.put((String) key, new Kysely(sql));
	}
    }

    /**
     * Performs the reading from the specified file and returns the content as {@link String}.
     * 
     * @param fileName
     *            The name of the file. It should be located in the classpath.
     * @param loader
     *            Preferably use <code>Thread.currentThread().getContextClassLoader()</code>
     * @return File's content
     */
    private String readFile(final String fileName, ClassLoader loader) {
	InputStream inputStream = null;
	String result = null;
	try {
	    if (loader == null) {
		loader = ClassLoader.getSystemClassLoader();
	    }

	    // Returns null on lookup failures:
	    inputStream = loader.getResourceAsStream(fileName);
	    if (inputStream != null) {
		result = ConfigParser.convertStreamToString(inputStream);
	    } else {
		throw new Exception("InputStream is <null> when trying to read: " + fileName);
	    }

	} catch (final Exception e) {
	    Logger.print(e);
	} finally {
	    if (inputStream != null) {
		try {
		    inputStream.close();
		} catch (final Throwable ignore) {
		    // do nothing
		}
	    }
	}
	// Logger.print("The SQL loaded from [" + fileName + "] is:\n" + result);
	return result;
    }
}
