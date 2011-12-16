package yarar.tietokanta;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import yarar.rikai.Logger;

/**
 * This class wraps SQL calls and provides basic manipulations with the {@link ResultSet}.<br>
 * <b>Kysely</b> is a <a href="http://en.wikipedia.org/wiki/Finnish_language"
 * target="_blank">Finnish</a> word for <i>query</i>.
 * 
 * @author Dimo Vanchev
 */
public class Kysely {

    /** The stored SQL-query. */
    private final String sql;
    /** A {@link PreparedStatement} returned by preparation of Kysely's SQL. */
    private PreparedStatement preparedStatement;
    /** {@link ResultSet} returned by the last execution of Kysely's SQL. */
    private ResultSet result;
    /** The {@link ResultSetMetaData} object associated with the last <code>result</code>. */
    private ResultSetMetaData rsmd;
    /** The column count of the last <code>result</code>. */
    private int columnCount = -1;

    /**
     * This constructor takes the SQL-query as its only parameter. And throws
     * {@link NullPointerException} in case the SQL is <code>null</code>.
     * 
     * @param sql
     *            The SQL query that will be executed. Should not be <code>null</code>.
     */
    public Kysely(final String sql) {
	if (sql == null) {
	    throw new NullPointerException("Empty SQL when creating Kysely");
	}

	this.sql = sql;
    }

    /**
     * Retrieves the stored SQL-query.
     * 
     * @return The SQL-query
     */
    public String getQuery() {
	return sql;
    }

    /**
     * Checks if the {@link ResultSet} returned by the last execution of Kysely's SQL is set or not.
     * It is a good approach to call this method before iterating over the results, as ResultSet
     * objects are automatically closed when the {@link Statement} object that generated it is
     * closed, re-executed, or used to retrieve the next result from a sequence of multiple results.
     * 
     * @return <code>true</code> if set, or <br>
     *         <code>false</code> otherwise
     */
    public boolean hasResult() {
	return (result != null);
    }

    /**
     * Checks if there is a {@link PreparedStatement} set.
     * 
     * @return true/false depending on if {@link #preparedStatement} is <code>null</code>;
     */
    public boolean hasPreparedStatement() {
	return (preparedStatement != null);
    }

    /**
     * Forces closing of related {@link ResultSet} and/or {@link Statement} objects. This method
     * will be called upon destroying the Kysely's object.
     * 
     * @param closeResult
     *            Set it <code>true</code> to call {@link ResultSet #close()} method.
     * @param closeStatement
     *            Set it <code>true</code> to call {@link Statement #close()} method.
     */
    public void close(final boolean closeResult, final boolean closeStatement) {
	try {
	    if (hasResult()) {
		if (closeStatement) {
		    result.getStatement().close();
		}
		if (closeResult) {
		    result.close();
		}
	    }
	} catch (final Exception e) {
	    Logger.print(e);
	}
    }

    /**
     * Moves the cursor forward one row from its current position. A Kysely's <code>ResultSet</code>
     * cursor is initially positioned before the first row; the first call to the method
     * <code>goNext</code> makes the first row the current row; the second call makes the second row
     * the current row, and so on.
     * <p>
     * When a call to the <code>next</code> method returns <code>false</code>, the cursor is
     * positioned after the last row.
     * </p>
     * For more info, see {@link ResultSet #next()} method.
     * 
     * @return <code>true</code> if the new current row is valid; <code>false</code> if there are no
     *         more rows, or if {@link SQLException} is caught.
     */
    public boolean goNext() {
	boolean success = false;
	try {
	    success = result.next();
	} catch (final SQLException e) {
	    // do nothing
	    // Logger.print(e);
	}
	return success;
    }

    /**
     * Gets the value of the designated column in the current row of the <code>ResultSet</code>
     * object as the Java type specified in <code>returnType</code> parameter.<br>
     * This method will throw {@link IndexOutOfBoundsException}, in case the specified
     * <code>fieldId</code> is not found.
     * 
     * @param fieldID
     *            This is the identifier of the column. Though it's an {@link Object}, the method
     *            expects this parameter to be one of the following:
     *            <ul>
     *            <li>{@link String} - searches the column by its label, example:<br>
     *            <code>myKysely.getFieldAs(<b>"label"</b>, Kysely.F_STRING)</code></li>
     *            <li>{@link Integer}, or primitive <code>int</code> - searches the column by its
     *            index, example:<br>
     *            <code>myKysely.getFieldAs(<b>new Integer(1)</b>, Kysely.F_STRING)</code>, or<br>
     *            <code>myKysely.getFieldAs(<b>1</b>, Kysely.F_STRING)</code></li>
     *            </ul>
     * @param returnType
     *            This parameter specifies the expected return type. It should be one of the
     *            following:
     *            <ul>
     *            <li><code>Feldtyp.OBJECT</code> returns the value of the given column as an
     *            {@link Object}. The type of the Java object will be the default Java object type
     *            corresponding to the column's SQL type, following the mapping for built-in types
     *            specified in the JDBC specification. If the value is an SQL <code>NULL</code>, the
     *            driver returns a Java <code>null</code>.</li>
     *            <li><code>Feldtyp.STRING</code> returns the value of the given column as a
     *            {@link String}.</li>
     *            <li><code>Feldtyp.INTEGER</code> returns the value of the given column as a
     *            {@link Integer}.</li>
     *            <li><code>Feldtyp.ARRAY</code> returns the value of the given column as an an
     *            {@link Array} object representing the SQL <code>ARRAY</code> value in the
     *            specified column.</li>
     *            <li><code>Feldtyp.BOOLEAN</code> returns the value of the given column as a
     *            <code>boolean</code>. If the designated column has a datatype of CHAR or VARCHAR
     *            and contains a "0" or has a datatype of BIT, TINYINT, SMALLINT, INTEGER or BIGINT
     *            and contains a 0, a value of <code>false</code> is returned. If the designated
     *            column has a datatype of CHAR or VARCHAR and contains a "1" or has a datatype of
     *            BIT, TINYINT, SMALLINT, INTEGER or BIGINT and contains a 1, a value of
     *            <code>true</code> is returned.</li>
     *            </ul>
     * @return Returns the <code>fieldID</code> value in the specified <code>returnType</code>, or
     *         <code>null</code>, in case of caught {@link SQLException}.
     * @see ResultSet
     */
    public Object getFieldAs(final Object fieldID, final Feldtyp returnType) {
	// determines whether int-based index will be used
	final boolean useIndex = (fieldID instanceof Integer);
	int ix = 0;

	try {
	    if (useIndex) {
		ix = ((Integer) fieldID).intValue();
	    } else {
		ix = result.findColumn((String) fieldID);
	    }
	} catch (final ClassCastException e) {
	    // set ix to some negative value for easier recognition
	    ix = -3;
	} catch (final SQLException e) {
	    // set ix to some negative value for easier recognition
	    ix = -2;
	} catch (final Exception e) {
	    // set ix to some negative value for easier recognition
	    ix = -1;
	}

	if ((ix < 1) || (ix > columnCount)) {
	    throw new IndexOutOfBoundsException("The provided index [" + ix
		    + "] is less than 1 or greater than " + columnCount
		    + " (the column count).");
	}

	Object o = null;
	try {
	    switch (returnType) {
	    case STRING:
		o = result.getString(ix);
		break;
	    case ARRAY:
		o = result.getArray(ix);
		break;
	    case BOOLEAN:
		o = result.getBoolean(ix);
		break;
	    case INTEGER:
		o = result.getInt(ix);
		break;
	    case OBJECT:
	    default:
		o = result.getObject(ix);
		break;
	    }
	} catch (final SQLException e) {
	    Logger.print(e);
	}
	return o;
    }

    /**
     * Retrieves the {@link ResultSet} returned by the last execution of Kysely's SQL.
     * 
     * @return the {@link ResultSet} returned by the last execution of Kysely's SQL.
     */
    protected ResultSet getResult() {
	return result;
    }

    /**
     * Retrieves the {@link PreparedStatement} returned by preparation of Kysely's SQL.
     * 
     * @return the {@link PreparedStatement} returned by preparation of Kysely's SQL.
     */
    protected PreparedStatement getPreparedStatement() {
	return preparedStatement;
    }

    /**
     * Sets the {@link ResultSet} returned by the last execution of Kysely's SQL
     * 
     * @param result
     *            The {@link ResultSet} returned by the last execution of Kysely's SQL
     */
    protected void setResult(final ResultSet result) {
	this.result = result;
	columnCount = -1;
	try {
	    rsmd = result.getMetaData();
	    columnCount = rsmd.getColumnCount();
	} catch (final SQLException e) {
	    Logger.print(e);
	}
    }

    /**
     * Sets the {@link PreparedStatement} returned by preparation of Kysely's SQL.
     * 
     * @param preparedStatement
     *            The {@link PreparedStatement} returned by preparation of Kysely's SQL.
     */
    protected void setPreparedStatement(final PreparedStatement preparedStatement) {
	this.preparedStatement = preparedStatement;
    }

    @Override
    protected void finalize() {
	Logger.print("Garbage collector reached Kysely: " + toString());
	close(true, true);
    }
}
