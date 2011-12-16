package yarar.graph;

import java.util.Collection;

import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * This is Yarar's main class for <code>Graph</code> manipulations.
 * 
 * @author Dimo Vanchev
 */
public class YGGraph implements Graph<YGVertex, YGEdge>, Forest<YGVertex, YGEdge> {

    /** The private <code>Graph</code> object. */
    private final Graph<YGVertex, YGEdge> graph;

    /** Directed <code>Edge</code> type */
    public static final EdgeType EDGE_DIRECTED = EdgeType.DIRECTED;
    /** Undirected <code>Edge</code> type */
    public static final EdgeType EDGE_UNDIRECTED = EdgeType.UNDIRECTED;

    /**
     * Creates a new <code>YGGraph</code> instance.
     * 
     * @param graphType Please use one of the following:
     *        <ul>
     *        <li>{@link YGGraphTypes#SPARSE}</li>
     *        <li>{@link YGGraphTypes#SPARSE_DIRECTED}</li>
     *        </ul>
     */
    public YGGraph(final YGGraphTypes graphType) {
	switch (graphType) {
	    case SPARSE:
		graph = new SparseMultigraph<YGVertex, YGEdge>();
		break;
	    case SPARSE_DIRECTED:
		graph = new DirectedSparseMultigraph<YGVertex, YGEdge>();
		break;
	    case FOREST:
		graph = new DelegateForest<YGVertex, YGEdge>();
		break;
	    default:
		throw new Error("Unknown YGGraph type:" + graphType);
		// break;
	}
    }

    /**
     * Clones a branch of a tree-graph. <br>
     * Please note that this method should be called explicitly for {@link YGGraphTypes#FOREST}-type
     * of graphs. Otherwise the recursion could easily become endless.
     * 
     * @param v The Vertex to branch off.
     * @return the newly created branch.
     */
    public YGGraph cloneBranch(final YGVertex v) {
	final YGGraph branch = new YGGraph(YGGraphTypes.FOREST);
	branch.addVertex(v);
	cloneBranch(branch, v, 1);
	return branch;
    }

    /**
     * This method recursively clones a branch from the self contained graph and adds it to the new
     * graph passed as parameter. <br>
     * Please note that this method should be called explicitly for {@link YGGraphTypes#FOREST}-type
     * of graphs. Otherwise the recursion could easily become endless.
     * 
     * @param br The graph, where the branch cloning should be added.
     * @param vertex A vertex which will be checked for sub-nodes.
     * @param deep Marks current deep level (for debugging needs only).
     */
    private void cloneBranch(final YGGraph br, final YGVertex vertex, final int deep) {
	final Collection<YGVertex> c = getChildren(vertex);
	for (final YGVertex v : c) {
	    br.addVertex(v);
	    br.addEdge(getParentEdge(v), vertex, v);
	    // Logger.treeBranchPrint(deep, v.toString());
	    cloneBranch(br, v, deep + 1);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#getEdges()
     */
    @Override
    public Collection<YGEdge> getEdges() {
	return graph.getEdges();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#getVertices()
     */
    @Override
    public Collection<YGVertex> getVertices() {
	return graph.getVertices();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#containsVertex(java.lang.Object)
     */
    @Override
    public boolean containsVertex(final YGVertex vertex) {
	return graph.containsVertex(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#containsEdge(java.lang.Object)
     */
    @Override
    public boolean containsEdge(final YGEdge edge) {
	return graph.containsEdge(edge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#getEdgeCount()
     */
    @Override
    public int getEdgeCount() {
	return graph.getEdgeCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#getVertexCount()
     */
    @Override
    public int getVertexCount() {
	return graph.getVertexCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#getNeighbors(java.lang.Object)
     */
    @Override
    public Collection<YGVertex> getNeighbors(final YGVertex vertex) {
	return graph.getNeighbors(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#getIncidentEdges(java.lang.Object)
     */
    @Override
    public Collection<YGEdge> getIncidentEdges(final YGVertex vertex) {
	return graph.getIncidentEdges(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#getIncidentVertices(java.lang.Object)
     */
    @Override
    public Collection<YGVertex> getIncidentVertices(final YGEdge edge) {
	return graph.getIncidentVertices(edge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#findEdge(java.lang.Object, java.lang.Object)
     */
    @Override
    public YGEdge findEdge(final YGVertex v1, final YGVertex v2) {
	return graph.findEdge(v1, v2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#findEdgeSet(java.lang.Object, java.lang.Object)
     */
    @Override
    public Collection<YGEdge> findEdgeSet(final YGVertex v1, final YGVertex v2) {
	return graph.findEdgeSet(v1, v2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#addVertex(java.lang.Object)
     */
    @Override
    public boolean addVertex(final YGVertex vertex) {
	return graph.addVertex(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#addEdge(java.lang.Object, java.util.Collection)
     */
    @Override
    public boolean addEdge(final YGEdge edge, final Collection<? extends YGVertex> vertices) {
	return graph.addEdge(edge, vertices);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#addEdge(java.lang.Object, java.util.Collection,
     * edu.uci.ics.jung.graph.util.EdgeType)
     */
    @Override
    public boolean addEdge(final YGEdge edge, final Collection<? extends YGVertex> vertices,
	    final EdgeType edge_type) {
	return graph.addEdge(edge, vertices, edge_type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#removeVertex(java.lang.Object)
     */
    @Override
    public boolean removeVertex(final YGVertex vertex) {
	return graph.removeVertex(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#removeEdge(java.lang.Object)
     */
    @Override
    public boolean removeEdge(final YGEdge edge) {
	return graph.removeEdge(edge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#isNeighbor(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean isNeighbor(final YGVertex v1, final YGVertex v2) {
	return graph.isNeighbor(v1, v2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#isIncident(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean isIncident(final YGVertex vertex, final YGEdge edge) {
	return graph.isIncident(vertex, edge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#degree(java.lang.Object)
     */
    @Override
    public int degree(final YGVertex vertex) {
	return graph.degree(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#getNeighborCount(java.lang.Object)
     */
    @Override
    public int getNeighborCount(final YGVertex vertex) {
	return graph.getNeighborCount(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#getIncidentCount(java.lang.Object)
     */
    @Override
    public int getIncidentCount(final YGEdge edge) {
	return graph.getIncidentCount(edge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#getEdgeType(java.lang.Object)
     */
    @Override
    public EdgeType getEdgeType(final YGEdge edge) {
	return graph.getEdgeType(edge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#getDefaultEdgeType()
     */
    @Override
    public EdgeType getDefaultEdgeType() {
	return graph.getDefaultEdgeType();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#getEdges(edu.uci.ics.jung.graph.util.EdgeType)
     */
    @Override
    public Collection<YGEdge> getEdges(final EdgeType edge_type) {
	return graph.getEdges();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Hypergraph#getEdgeCount(edu.uci.ics.jung.graph.util.EdgeType)
     */
    @Override
    public int getEdgeCount(final EdgeType edge_type) {
	return graph.getEdgeCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#getInEdges(java.lang.Object)
     */
    @Override
    public Collection<YGEdge> getInEdges(final YGVertex vertex) {
	return graph.getInEdges(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#getOutEdges(java.lang.Object)
     */
    @Override
    public Collection<YGEdge> getOutEdges(final YGVertex vertex) {
	return graph.getOutEdges(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#getPredecessors(java.lang.Object)
     */
    @Override
    public Collection<YGVertex> getPredecessors(final YGVertex vertex) {
	return graph.getPredecessors(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#getSuccessors(java.lang.Object)
     */
    @Override
    public Collection<YGVertex> getSuccessors(final YGVertex vertex) {
	return graph.getSuccessors(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#inDegree(java.lang.Object)
     */
    @Override
    public int inDegree(final YGVertex vertex) {
	return graph.inDegree(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#outDegree(java.lang.Object)
     */
    @Override
    public int outDegree(final YGVertex vertex) {
	return graph.outDegree(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#isPredecessor(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean isPredecessor(final YGVertex v1, final YGVertex v2) {
	return graph.isPredecessor(v1, v2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#isSuccessor(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean isSuccessor(final YGVertex v1, final YGVertex v2) {
	return graph.isSuccessor(v1, v2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#getPredecessorCount(java.lang.Object)
     */
    @Override
    public int getPredecessorCount(final YGVertex vertex) {
	return graph.getPredecessorCount(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#getSuccessorCount(java.lang.Object)
     */
    @Override
    public int getSuccessorCount(final YGVertex vertex) {
	return graph.getSuccessorCount(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#getSource(java.lang.Object)
     */
    @Override
    public YGVertex getSource(final YGEdge directed_edge) {
	return graph.getSource(directed_edge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#getDest(java.lang.Object)
     */
    @Override
    public YGVertex getDest(final YGEdge directed_edge) {
	return graph.getDest(directed_edge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#isSource(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean isSource(final YGVertex vertex, final YGEdge edge) {
	return graph.isSource(vertex, edge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#isDest(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean isDest(final YGVertex vertex, final YGEdge edge) {
	return graph.isDest(vertex, edge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#addEdge(java.lang.Object, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public boolean addEdge(final YGEdge e, final YGVertex v1, final YGVertex v2) {
	return graph.addEdge(e, v1, v2);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#addEdge(java.lang.Object, java.lang.Object,
     * java.lang.Object, edu.uci.ics.jung.graph.util.EdgeType)
     */
    @Override
    public boolean addEdge(final YGEdge e, final YGVertex v1, final YGVertex v2,
	    final EdgeType edgeType) {
	return graph.addEdge(e, v1, v2, edgeType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#getEndpoints(java.lang.Object)
     */
    @Override
    public Pair<YGVertex> getEndpoints(final YGEdge edge) {
	return graph.getEndpoints(edge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Graph#getOpposite(java.lang.Object, java.lang.Object)
     */
    @Override
    public YGVertex getOpposite(final YGVertex vertex, final YGEdge edge) {
	return graph.getOpposite(vertex, edge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
	return graph.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Forest#getTrees()
     */
    @Override
    public Collection<Tree<YGVertex, YGEdge>> getTrees() {
	return ((Forest<YGVertex, YGEdge>) graph).getTrees();
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Forest#getParent(java.lang.Object)
     */
    @Override
    public YGVertex getParent(final YGVertex vertex) {
	return ((Forest<YGVertex, YGEdge>) graph).getParent(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Forest#getParentEdge(java.lang.Object)
     */
    @Override
    public YGEdge getParentEdge(final YGVertex vertex) {
	return ((Forest<YGVertex, YGEdge>) graph).getParentEdge(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Forest#getChildren(java.lang.Object)
     */
    @Override
    public Collection<YGVertex> getChildren(final YGVertex vertex) {
	return ((Forest<YGVertex, YGEdge>) graph).getChildren(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Forest#getChildEdges(java.lang.Object)
     */
    @Override
    public Collection<YGEdge> getChildEdges(final YGVertex vertex) {
	return ((Forest<YGVertex, YGEdge>) graph).getChildEdges(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uci.ics.jung.graph.Forest#getChildCount(java.lang.Object)
     */
    @Override
    public int getChildCount(final YGVertex vertex) {
	return ((Forest<YGVertex, YGEdge>) graph).getChildCount(vertex);
    }
}
