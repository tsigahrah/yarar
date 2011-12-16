package yarar.graph;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * The most generic type for <b>vertex</b> graph elements.
 * 
 * @author Dimo Vanchev
 */
public class YGVertex extends YGAbstractElement {

    /** The supported Shape types for vertex visualisation */
    protected static enum VShape {
	/** display shape as a circle */
	CIRCLE,
	/** display shape as an ellipse */
	ELLIPSE,
	/** display shape as a triangle */
	TRIANGLE,
	/** display shape as a rectangle */
	RECTANGLE,
	/** default shape (currently set to ELLIPSE) */
	DEFAULT
    }

    /** The {@link Shape} used for visualisation purposes */
    protected Shape displayShape;

    /**
     * Creates a new <code>YGVertex</code> instance. Calls super constructor and sets some defaults.
     * 
     * @param internalID the internal identifier.
     */
    public YGVertex(final String internalID) {
	super(internalID);
	setDisplayColor(Color.YELLOW);
	setDisplayStroke(YGStrokeType.DASHED);
	setDisplayShape(VShape.ELLIPSE);
    }

    /**
     * Default constructor. It's use should be avoided. TODO remove default constructor
     */
    protected YGVertex() {
	super();
    }

    /**
     * Returns the set display shape.
     * 
     * @return The display shape.
     */
    public final Shape getDisplayShape() {
	if (displayShape == null) {
	    setDisplayShape(VShape.DEFAULT);// goes to default
	}
	return displayShape;
    }

    /**
     * Set display shape for this vertex component.
     * 
     * @param shapeType Use one of <code>YGVertex.SHAPE_xxxx</code> types.
     */
    public final void setDisplayShape(final VShape shapeType) {
	switch (shapeType) {
	    case RECTANGLE:
		displayShape = new Rectangle(-30, -10, 60, 20);
		break;
	    case TRIANGLE:
		final int[] x = { 30, 15, 0 };
		final int[] y = { 0, 30, 0 };
		displayShape = new Polygon(x, y, 3);
		break;
	    case CIRCLE:
		displayShape = new Ellipse2D.Double(-20, -20, 40, 40);
		break;
	    case ELLIPSE:
	    default:
		displayShape = new Ellipse2D.Double(-25, -10, 50, 20);
		break;
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see yarar.graph.YGAbstractElement#getElementType()
     */
    @Override
    protected final YGElementType getElementType() {
	return YGElementType.V;
    }
}
