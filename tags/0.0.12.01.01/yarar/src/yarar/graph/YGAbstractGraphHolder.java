package yarar.graph;

/**
 * Abstraction for classes that hold and manipulate {@link YGGraph} objects.
 * 
 * @author Dimo Vanchev
 */
public abstract class YGAbstractGraphHolder {

    /** Preferred layout for graph visualisation. */
    protected YGVisualisationLayouts preferredLayout;

    /** The internal <code>YGGraph</code> object. */
    protected YGGraph ygg;

    /**
     * Gets the internal <code>YGGraph</code> object.
     * 
     * @return The internal <code>YGGraph</code> object.
     */
    public YGGraph getGraph() {
	return ygg;
    }

    /**
     * Calls {@link YGDisplay#display(YGGraph, int, int, yarar.graph.YGVisualisationLayouts)} to
     * display the graph with predefined width and height, based on vertices count.
     */
    public void displaySelf() {
	final int[] wh = getDisplayWidthAndHeight(ygg);

	if (getPreferredLayout() == null) {
	    throw new NullPointerException(
		    "[preferredLayout] is not initialized. Subclasses should call setPreferredLayout().");
	}

	YGDisplay.display(ygg, wh[0], wh[1], getPreferredLayout());
    }

    /**
     * Calls {@link YGDisplay#display(YGGraph, int, int, yarar.graph.YGVisualisationLayouts)} to
     * display the graph with predefined width and height, based on vertices count.
     * 
     * @param g The graph to be displayed.
     */
    public void display(final YGGraph g) {
	final int[] wh = getDisplayWidthAndHeight(g);

	if (getPreferredLayout() == null) {
	    throw new NullPointerException(
		    "[preferredLayout] is not initialized. Subclasses should call setPreferredLayout().");
	}

	YGDisplay.display(g, wh[0], wh[1], getPreferredLayout());
    }

    /**
     * Gets the width and height used for graph visualisation.
     * 
     * @param g The graph that's going to be visualised.
     * @return array with [0] - the width, and [1] - the height.
     */
    private int[] getDisplayWidthAndHeight(final YGGraph g) {
	final int vCount = g.getVertexCount();
	int width = (vCount * 50) + 100;
	int height = (vCount * 20) + 100;
	// setting min and max width limitations
	if (width < 300) {
	    width = 300;
	} else if (width > 1000) {
	    width = 1000;
	}
	// setting min and max height limitations
	if (height < 250) {
	    height = 250;
	} else if (height > 600) {
	    height = 600;
	}
	final int[] dim = { width, height };
	return dim;
    }

    /**
     * Sets the preferred layout for graph visualisation.
     */
    protected abstract void setDisplayPreferences();

    /**
     * Gets the preferred layout for graph visualisation.
     * 
     * @return The preferred layout.
     */
    protected abstract YGVisualisationLayouts getPreferredLayout();

}
