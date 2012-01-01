package yarar.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

/**
 * This is a superclass of {@link YGVertex} and {@link YGEdge}, providing some useful facilities as
 * default constructors for quicker testing needs and common <code>toString()</code> method.
 * 
 * @author Dimo Vanchev
 */
public abstract class YGAbstractElement {

    /** A <code>Color</code> used for visualisation needs. */
    protected Color displayColor;
    /** A <code>Stroke</code> used for visualisation needs. */
    protected Stroke displayStroke;
    /** Determines level of information used for the method {@link #toString()}. */
    protected YGDebugLevel debugLevel = YGDebugLevel.MIN;
    /** Internal id is used in graph creation and easier recognition */
    protected Object internalID;
    /**
     * A unique numeric ID for the <code>YGAbstractElement</code> instance.
     */
    private static int autoIncrementID = 0;
    /**
     * Has {@link #autoIncrementID} been set for the <code>YGAbstractElement</code> instance.
     */
    private boolean hasAutoIncrementedIdForThisInstance = false;

    /**
     * This default constructor auto-generates an internalID.
     */
    YGAbstractElement() {
	setInternalID(getElementType().toString() + YGAbstractElement.getAutoIncrementID(this));
    }

    /**
     * Creates a new YGAbstractElement.
     * 
     * @param internalID The object used as internalID.
     */
    YGAbstractElement(final Object internalID) {
	setInternalID(internalID);
    }

    /**
     * Returns <code>Color</code> used for visualisation needs.
     * 
     * @return The display colour.
     */
    public Color getDisplayColor() {
	return displayColor;
    }

    /**
     * Returns <code>Stroke</code> used for visualisation needs.
     * 
     * @return The display stroke.
     */
    public Stroke getDisplayStroke() {
	return displayStroke;
    }

    /**
     * Sets a <code>Color</code> for visualisation needs.
     * 
     * @param displayColor The display colour.
     */
    public final void setDisplayColor(final Color displayColor) {
	this.displayColor = displayColor;
    }

    /**
     * Sets a <code>Stroke</code> for visualisation needs. Use one of the types from
     * {@link YGStrokeType}.
     * 
     * @param strokeType The type of display stroke.
     */
    public final void setDisplayStroke(final YGStrokeType strokeType) {
	switch (strokeType) {
	    case DASHED:
		final float dash[] = { 2.0f };
		displayStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		break;
	    case SOLID:
	    default:
		displayStroke = null;
		break;
	}
    }

    @Override
    public final String toString() {
	switch (debugLevel) {
	    case FULL:
		return getElementType() + "[" + internalID + "]@" + super.toString();
	    case MORE:
		return "[" + internalID + "]@" + this.getClass().getName();
	    case MIN:
	    default:
		return internalID.toString();
	}
    }

    /**
     * Sets the <code>internalID</code> used mainly for the needs of {@link #toString()} method.
     * 
     * @param internalID The internal identifier.
     */
    protected final void setInternalID(final Object internalID) {
	this.internalID = internalID;
    }

    /**
     * This private method is used for assigning an unique numeric ID for each
     * <code>YGAbstractElement</code> instance.
     * 
     * @param ygae The <code>YGAbstractElement</code> instance.
     * @return The unique numeric ID.
     */
    private static final synchronized int getAutoIncrementID(final YGAbstractElement ygae) {
	if (!ygae.hasAutoIncrementedIdForThisInstance) {
	    YGAbstractElement.autoIncrementID++;
	    ygae.hasAutoIncrementedIdForThisInstance = true;
	}
	return YGAbstractElement.autoIncrementID;
    }

    /**
     * Returns a {@link YGElementType} representing one of the recognised graph element types
     * 
     * @return YGElementType representing one of the recognised graph element types.
     */
    protected abstract YGElementType getElementType();

}
