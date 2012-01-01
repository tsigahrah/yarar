package yarar.rikai;

/**
 * This class provides logging facilities.
 * 
 * @author Dimo Vanchev
 */
public final class Logger {

    /** The single instance of this class. */
    private static Logger selfInstance;

    /** Parser of application-level settings. */
    private final ConfigParser cf;
    /** Logging level, as defined in the application-level settings. */
    private final String configLogging;
    /** The logging level, see {@link RikaiLoggingLevel} enum. */
    private final RikaiLoggingLevel rikaiLoggingLevel;

    /**
     * Singleton private constructor.
     */
    private Logger() {
	Logger.selfInstance = this;
	// loading properties from the the relevant properties file
	cf = new ConfigParser("config.properties");
	configLogging = cf.getValue("logging");

	// determine logging level
	if (configLogging.equals("console")) {
	    rikaiLoggingLevel = RikaiLoggingLevel.CONSOLE;
	} else {
	    rikaiLoggingLevel = RikaiLoggingLevel.NONE;
	}
    }

    /**
     * Singleton instance getter.
     * 
     * @return The single instance of this class.
     */
    public static final Logger getInstance() {
	if (Logger.selfInstance == null) {
	    new Logger();
	}

	return Logger.selfInstance;
    }

    /**
     * Logs a string.
     * 
     * @param str The String
     */
    public static void print(final String str) {
	switch (Logger.selfInstance.rikaiLoggingLevel) {
	    case CONSOLE:
		System.out.println(str);
		break;
	    case NONE:
	    default:
		// do nothing
		break;
	}
    }

    /**
     * Logs exception's stack trace.
     * 
     * @param e The Exception
     */
    public static void print(final Exception e) {
	switch (Logger.selfInstance.rikaiLoggingLevel) {
	    case CONSOLE:
		e.printStackTrace();
		break;
	    case NONE:
	    default:
		// do nothing
		break;
	}
    }

    /**
     * Logs a Object, by calling {@link Object#toString()} method.
     * 
     * @param o The Object
     */
    public static void print(final Object o) {
	Logger.print(o.toString());
    }

    public static void treeBranchPrint(int deep, final String str) {
	String d = "";
	while (deep > 0) {
	    d += "+ ";
	    deep--;
	}
	d += str;
	switch (Logger.selfInstance.rikaiLoggingLevel) {
	    case CONSOLE:
		System.out.println(d);
		break;
	    case NONE:
	    default:
		// do nothing
		break;
	}
    }
}
