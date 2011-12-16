package yarar.rikai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * This class provides basic functionality for property files loading, parsing and values
 * retrieving.
 * 
 * @author Dimo Vanchev
 */
public class ConfigParser {

    /**
     * Object holding the parsed properties.
     */
    private final Properties pl;

    /**
     * By creating a new ConfigParser, the property file is read and parsed. Looks up a resource
     * named 'path' in the classpath. The resource must map to a file with .properties extension.
     * The name is assumed to be absolute and can use either "/" or "." for package segment
     * separation with an optional leading "/" and optional ".properties" suffix. Thus, the
     * following names refer to the same resource:
     * 
     * <pre>
     * some.pkg.Resource
     * some.pkg.Resource.properties
     * some/pkg/Resource
     * some/pkg/Resource.properties
     * /some/pkg/Resource
     * /some/pkg/Resource.properties
     * </pre>
     * 
     * @param path Path to the property file.
     */
    public ConfigParser(final String path) {
	pl = (new PropertyLoader()).loadProperties(path);
    }

    /**
     * Searches for the property with the specified key in this property list. If the key is not
     * found in this property list, the default property list, and its defaults, recursively, are
     * then checked. The method returns <code>null</code> if the property is not found.
     * 
     * @param key the property key.
     * @return the value of the property list with the specified key value.
     */
    public String getValue(final String key) {
	return pl.getProperty(key);
    }

    /**
     * Returns all keys as {@link Set}
     * 
     * @return All keys of the properties
     */
    public Set<Object> getAllKeys() {
	return pl.keySet();
    }

    /**
     * Converts {@link InputStream} to {@link String}
     * 
     * @param inputStream The stream that needs to be converted
     * @return The string The converted string.
     * @throws IOException if an I/O error occurs.
     */
    public static String convertStreamToString(final InputStream inputStream)
	    throws IOException {
	/*
	 * To convert the InputStream to String we use the Reader.read(char[] buffer) method. We
	 * iterate until the Reader return -1 which means there's no more data to read. We use the
	 * StringWriter class to produce the string.
	 */
	final Writer writer = new StringWriter();

	final char[] buffer = new char[1024];
	try {
	    final Reader reader = new BufferedReader(
		    new InputStreamReader(inputStream, "UTF-8"));
	    int n;
	    while ((n = reader.read(buffer)) != -1) {
		writer.write(buffer, 0, n);
	    }
	} finally {
	    inputStream.close();
	}
	return writer.toString();
    }

    /**
     * Internal class handling the real part of property loading and parsing.
     * 
     * @author Unknown (taken from some website)
     */
    private class PropertyLoader {

	/**
	 * If set to <code>true</code> and loading fails an {@link IllegalArgumentException} will be
	 * thrown
	 */
	private static final boolean THROW_ON_LOAD_FAILURE = true;
	/**
	 * Whether to load file as <i>resource bundle</i> (<code>true</code>) or as a <i>class
	 * loader resource</i> (<code>false</code>).
	 */
	private static final boolean LOAD_AS_RESOURCE_BUNDLE = false;

	/**
	 * The expected suffix for properties files
	 */
	private static final String SUFFIX = ".properties";

	/**
	 * Looks up a resource named 'name' in the classpath. The resource must map to a file with
	 * .properties extension. The name is assumed to be absolute and can use either "/" or "."
	 * for package segment separation with an optional leading "/" and optional ".properties"
	 * suffix. Thus, the following names refer to the same resource:
	 * 
	 * <pre>
	 * some.pkg.Resource
	 * some.pkg.Resource.properties
	 * some/pkg/Resource
	 * some/pkg/Resource.properties
	 * /some/pkg/Resource
	 * /some/pkg/Resource.properties
	 * </pre>
	 * 
	 * @param name classpath resource name [may not be null]
	 * @param loader classloader through which to load the resource [null is equivalent to the
	 *        application loader]
	 * @return resource converted to java.util.Properties [may be null if the resource was not
	 *         found and THROW_ON_LOAD_FAILURE is false]
	 * @throws IllegalArgumentException if the resource was not found and THROW_ON_LOAD_FAILURE
	 *         is true
	 */
	private Properties loadProperties(String name, ClassLoader loader) {
	    if (name == null) {
		throw new IllegalArgumentException("null input: name");
	    }

	    if (name.startsWith("/")) {
		name = name.substring(1);
	    }

	    if (name.endsWith(PropertyLoader.SUFFIX)) {
		name = name.substring(0,
			name.length() - PropertyLoader.SUFFIX.length());
	    }

	    Properties result = null;

	    InputStream in = null;
	    try {
		if (loader == null) {
		    loader = ClassLoader.getSystemClassLoader();
		}

		if (PropertyLoader.LOAD_AS_RESOURCE_BUNDLE) {
		    name = name.replace('/', '.');
		    // Throws MissingResourceException on lookup failures:
		    final ResourceBundle rb = ResourceBundle.getBundle(name,
			    Locale.getDefault(), loader);

		    result = new Properties();
		    for (final Enumeration<String> keys = rb.getKeys(); keys
			    .hasMoreElements();) {
			final String key = keys.nextElement();
			final String value = rb.getString(key);

			result.put(key, value);
		    }
		} else {
		    name = name.replace('.', '/');

		    if (!name.endsWith(PropertyLoader.SUFFIX)) {
			name = name.concat(PropertyLoader.SUFFIX);
		    }

		    // Returns null on lookup failures:
		    in = loader.getResourceAsStream(name);
		    if (in != null) {
			result = new Properties();
			result.load(in); // Can throw IOException
		    }
		}
	    } catch (final Exception e) {
		result = null;
	    } finally {
		if (in != null) {
		    try {
			in.close();
		    } catch (final Throwable ignore) {
		    }
		}
	    }

	    if (PropertyLoader.THROW_ON_LOAD_FAILURE && (result == null)) {
		throw new IllegalArgumentException(
			"could not load ["
				+ name
				+ "]"
				+ " as "
				+ (PropertyLoader.LOAD_AS_RESOURCE_BUNDLE ? "a resource bundle"
					: "a classloader resource"));
	    }

	    return result;
	}

	/**
	 * A convenience overload of {@link #loadProperties(String, ClassLoader)} that uses the
	 * current thread's context ClassLoader.
	 * 
	 * @param name classpath resource name [may not be null]
	 * @return resource converted to java.util.Properties [may be null if the resource was not
	 *         found and THROW_ON_LOAD_FAILURE is false]
	 */
	private Properties loadProperties(final String name) {
	    return this.loadProperties(name, Thread.currentThread()
		    .getContextClassLoader());
	}

    }

}
