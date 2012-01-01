package yarar.graph;

/**
 * Enumeration of the supported graph types. Used in {@link YGGraph} constructor.
 * 
 * @author Dimo Vanchev
 */
public enum YGGraphTypes {

    /**
     * An implementation of <code>Graph</code> that is suitable for sparse graphs and permits
     * directed, undirected, and parallel edges.
     */
    SPARSE,
    /**
     * An implementation of <code>DirectedGraph</code>, suitable for sparse graphs, that permits
     * parallel edges.
     */
    SPARSE_DIRECTED,
    /**
     * An implementation of <code>Forest</code> that delegates to a specified
     * <code>DirectedGraph</code> instance.
     */
    FOREST

}
