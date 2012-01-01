package yarar.graph;

/** Supported graph visualisation layouts. */
public enum YGVisualisationLayouts {
    /**
     * Implements the Fruchterman-Reingold force-directed algorithm for node layout. Behaviour is
     * determined by the following settable parameters:
     * <ul>
     * <li/>attraction multiplier: how much edges try to keep their vertices together
     * <li/>repulsion multiplier: how much vertices try to push each other apart
     * <li/>maximum iterations: how many iterations this algorithm will use before stopping
     * </ul>
     * Each of the first two defaults to 0.75; the maximum number of iterations defaults to 700.
     */
    FR,
    /** A radial layout for Tree or Forest graphs. */
    RADIAL_TREE

    // more layouts under: edu.uci.ics.jung.algorithms.layout.*
}