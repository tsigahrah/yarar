package yarar.graph;

import java.awt.Color;

/**
 * The most generic type for <b>edge</b> graph elements.
 * 
 * @author Dimo Vanchev
 */
public class YGEdge extends YGAbstractElement {

    /**
     * Creates a new <code>YGEdge</code> instance. Calls super constructor and sets some defaults.
     * 
     * @param internalID the internal identifier.
     */
    public YGEdge(final String internalID) {
	super(internalID);
	setDisplayColor(Color.BLACK);
	// setDisplayStroke(YGStrokeType.SOLID);
    }

    /**
     * Creates a new unlabelled <code>YGEdge</code> instance.
     */
    public YGEdge() {
	super();
	setDisplayColor(Color.BLACK);
	// setDisplayStroke(YGStrokeType.SOLID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see yarar.graph.YGAbstractElement#getElementType()
     */
    @Override
    protected final YGElementType getElementType() {
	return YGElementType.E;
    }
}
