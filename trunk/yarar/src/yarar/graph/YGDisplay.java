package yarar.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.PolarPoint;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.util.Animator;

/**
 * This class serves to display graphical representation of a {@link YGGraph}. The graphics open in
 * a <code>JFrame</code> with a closing button.
 * 
 * @author Dimo Vanchev
 */
public class YGDisplay {
    /** Radial layout used when displaying {@link Forest}s */
    private RadialTreeLayout<YGVertex, YGEdge> radialLayout;
    /** Classical tree layout used when displaying {@link Forest}s */
    private TreeLayout<YGVertex, YGEdge> treeLayout;
    /** The graph that we want to display. */
    private final YGGraph graph;
    /** The main visualisation viewer object. */
    private VisualizationViewer<YGVertex, YGEdge> vv;
    /** The rings used in background when RadialTreeLayout needs to be displayed. */
    private VisualizationServer.Paintable rings;

    /** graph visualisation layouts */
    public static enum Layouts {
	/**
	 * Implements the Fruchterman-Reingold force-directed algorithm for node layout. Behaviour
	 * is determined by the following settable parameters:
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

    /**
     * This constructor is used internally upon calling the static
     * {@link #display(YGGraph, int, int, YGDisplay.Layouts)} method and has solely internal use.
     * 
     * @param graph The graph object.
     */
    private YGDisplay(final YGGraph graph) {
	this.graph = graph;
    }

    /**
     * Provides graphical representation of a {@link YGGraph} within a {@link JFrame}.
     * 
     * @param g The graph to be displayed.
     * @param width Windows width.
     * @param height Windows height.
     * @param l The desired layout type.
     */
    public static void display(final YGGraph g, final int width, final int height,
	    final YGDisplay.Layouts l) {
	Layout<YGVertex, YGEdge> layout;
	final YGDisplay ygd = new YGDisplay(g);

	switch (l) {
	    case RADIAL_TREE:
		ygd.radialLayout = new RadialTreeLayout<YGVertex, YGEdge>(g);
		ygd.radialLayout.setSize(new Dimension(width, height));
		ygd.rings = ygd.new Rings();
		// layout = ygd.radialLayout;
		ygd.treeLayout = new TreeLayout<YGVertex, YGEdge>(g, 100, 100);
		layout = ygd.treeLayout;
		break;
	    case FR:
		layout = new FRLayout<YGVertex, YGEdge>(g);
		break;
	    default:
		layout = new FRLayout2<YGVertex, YGEdge>(g);
		break;
	}

	if (l != Layouts.RADIAL_TREE) {
	    layout.setSize(new Dimension(width, height));
	}

	ygd.vv = new VisualizationViewer<YGVertex, YGEdge>(
		layout, new Dimension(width, height));
	final VisualizationViewer<YGVertex, YGEdge> vv = ygd.vv;
	vv.getRenderContext().setVertexFillPaintTransformer(YGDisplay.getVertexPaint());
	vv.getRenderContext().setVertexStrokeTransformer(YGDisplay.getVertexStrokeTransformer());
	vv.getRenderContext().setVertexShapeTransformer(YGDisplay.getVertexShapeTransformer());
	vv.getRenderContext().setEdgeStrokeTransformer(YGDisplay.getEdgeStrokeTransformer());
	vv.getRenderContext().setEdgeDrawPaintTransformer(YGDisplay.getEdgeDrawPaintTransformer());
	vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<YGVertex>());
	vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<YGEdge>());
	vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
	vv.setBackground(Color.WHITE);

	final JFrame frame = new JFrame("YGGraph: " + g.toString());
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
	final Container content = frame.getContentPane();
	content.add(panel);

	@SuppressWarnings("rawtypes")
	final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
	vv.setGraphMouse(graphMouse);

	if (l == Layouts.RADIAL_TREE) {
	    YGDisplay.addControls(ygd, content);
	}

	frame.pack();
	frame.setVisible(true);

    }

    /**
     * Invokes the in-class defined colour for edge visualisation.
     * 
     * @return the transformer object
     */
    private static Transformer<YGEdge, Paint> getEdgeDrawPaintTransformer() {
	return new Transformer<YGEdge, Paint>() {
	    @Override
	    public Paint transform(final YGEdge i) {
		return i.getDisplayColor();
	    }
	};
    }

    /**
     * Invokes the in-class defined stroke for edge visualisation.
     * 
     * @return the transformer object
     */
    private static Transformer<YGEdge, Stroke> getEdgeStrokeTransformer() {
	return new Transformer<YGEdge, Stroke>() {
	    @Override
	    public Stroke transform(final YGEdge s) {
		return s.getDisplayStroke();
	    }
	};
    }

    /**
     * Invokes the in-class defined Shape for vertex visualisation.
     * 
     * @return the transformer object
     */
    private static Transformer<YGVertex, Shape> getVertexShapeTransformer() {
	return new Transformer<YGVertex, Shape>() {
	    @Override
	    public Shape transform(final YGVertex s) {
		return s.getDisplayShape();
	    }
	};
    }

    /**
     * Invokes the in-class defined Stroke for vertex visualisation.
     * 
     * @return the transformer object
     */
    private static Transformer<YGVertex, Stroke> getVertexStrokeTransformer() {
	return new Transformer<YGVertex, Stroke>() {
	    @Override
	    public Stroke transform(final YGVertex s) {
		return s.getDisplayStroke();
	    }
	};
    }

    /**
     * Invokes the in-class defined colour for vertex visualisation.
     * 
     * @return the transformer object
     */
    private static Transformer<YGVertex, Paint> getVertexPaint() {
	// Setup up a new vertex to paint transformer...
	return new Transformer<YGVertex, Paint>() {
	    @Override
	    public Paint transform(final YGVertex i) {
		return i.getDisplayColor();
	    }
	};
    }

    /**
     * Creates and returns a listener for the layout toggeling button.
     * 
     * @param ygd The YGDisplay instance
     * @return the layout item listener
     */
    private static ItemListener getLayoutItemListener(final YGDisplay ygd) {
	return new ItemListener() {
	    @Override
	    public void itemStateChanged(final ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {

		    final LayoutTransition<YGVertex, YGEdge> lt =
			    new LayoutTransition<YGVertex, YGEdge>(ygd.vv, ygd.treeLayout,
				    ygd.radialLayout);
		    final Animator animator = new Animator(lt);
		    animator.start();
		    ygd.vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
		    ygd.vv.addPreRenderPaintable(ygd.rings);
		} else {
		    final LayoutTransition<YGVertex, YGEdge> lt =
			    new LayoutTransition<YGVertex, YGEdge>(ygd.vv, ygd.radialLayout,
				    ygd.treeLayout);
		    final Animator animator = new Animator(lt);
		    animator.start();
		    ygd.vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
		    ygd.vv.removePreRenderPaintable(ygd.rings);
		}
		ygd.vv.repaint();
	    }
	};
    }

    /**
     * Adds display controls (zoom-in, zoom-out, etc.) at the bottom of the container.
     * 
     * @param ygd The YGDisplay instance
     * @param content The container
     */
    private static void addControls(final YGDisplay ygd, final Container content) {

	final ScalingControl scaler = new CrossoverScalingControl();
	final JButton plus = new JButton("+");
	plus.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(final ActionEvent e) {
		scaler.scale(ygd.vv, 1.1f, ygd.vv.getCenter());
	    }
	});
	final JButton minus = new JButton("-");
	minus.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(final ActionEvent e) {
		scaler.scale(ygd.vv, 1 / 1.1f, ygd.vv.getCenter());
	    }
	});

	final JToggleButton radial = new JToggleButton("Radial");
	radial.addItemListener(YGDisplay.getLayoutItemListener(ygd));

	final JPanel controls = new JPanel();
	final JPanel scaleGrid = new JPanel(new GridLayout(1, 0));
	scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));
	scaleGrid.add(plus);
	scaleGrid.add(minus);
	controls.add(radial);
	controls.add(scaleGrid);
	content.add(controls, BorderLayout.SOUTH);
    }

    /**
     * Displays sample graph. Use it for quick testing.
     */
    public static void testDisplay() {
	// Graph<V, E> where V is the type of the vertices and E is the type of the edges
	final YGGraph ygg = new YGGraph(YGGraphTypes.FOREST);// SPARSE_DIRECTED

	final YGVertex a = new YGVertex("az");
	final YGVertex b = new YGVertex("sam");
	final YGVertex c = new YGVertex("mnogo");
	final YGVertex d = new YGVertex("silno");
	final YGVertex e = new YGVertex("dobiche");
	final YGVertex f = new YGVertex("rogato");
	final YGVertex g = new YGVertex("zabato");

	ygg.addVertex(a);
	ygg.addVertex(b);
	ygg.addVertex(c);
	ygg.addVertex(d);
	ygg.addVertex(e);
	ygg.addVertex(f);
	ygg.addVertex(g);

	ygg.addEdge(new YGEdge("podlog"), b, a);
	ygg.addEdge(new YGEdge("sastoianie"), b, e);
	ygg.addEdge(new YGEdge("poiasnenie"), d, e);
	ygg.addEdge(new YGEdge("kolichesto"), c, d);
	ygg.addEdge(new YGEdge("poiasnenie"), f, e);
	ygg.addEdge(new YGEdge("poiasnenie"), g, e);
	ygg.addEdge(new YGEdge("vrashtane"), e, g);
	// ygg.addEdge(new YGEdge("podlog"), b, a, EdgeType.DIRECTED);
	// ygg.addEdge(new YGEdge("sastoianie"), b, e, EdgeType.DIRECTED);
	// ygg.addEdge(new YGEdge("poiasnenie"), d, e, EdgeType.DIRECTED);
	// ygg.addEdge(new YGEdge("kolichesto"), c, d, EdgeType.DIRECTED);
	// ygg.addEdge(new YGEdge("poiasnenie"), f, e, EdgeType.DIRECTED);
	// ygg.addEdge(new YGEdge("poiasnenie"), g, e, EdgeType.DIRECTED);
	// ygg.addEdge(new YGEdge("vrashtane"), e, g, EdgeType.DIRECTED);

	YGDisplay.display(ygg, 400, 400, Layouts.FR);
    }

    /**
     * A main() for testing purposes. Calls {@link YGDisplay#testDisplay()} method.
     * 
     * @param args All args will be ignored.
     */
    public static void main(final String[] args) {
	YGDisplay.testDisplay();
    }

    /**
     * Rings are circles displayed in background when <code>RadialTreeLayout</code> is used.
     * 
     * @author Dimo Vanchev
     */
    private class Rings implements VisualizationServer.Paintable {

	/** internally used PollarPoint coordinates for the rings */
	private final Collection<Double> depths;

	/**
	 * Default and only constructor.
	 */
	protected Rings() {
	    depths = getDepths();
	}

	/**
	 * Depths are internally used PollarPoint coordinates for the rings
	 * 
	 * @return the depths
	 */
	private Collection<Double> getDepths() {
	    final Set<Double> depths = new HashSet<Double>();
	    final Map<YGVertex, PolarPoint> polarLocations = radialLayout.getPolarLocations();
	    final Collection<YGVertex> c = graph.getVertices();
	    for (final YGVertex v : c) {
		final PolarPoint pp = polarLocations.get(v);
		depths.add(pp.getRadius());
	    }
	    return depths;
	}

	@Override
	public void paint(final Graphics g) {
	    g.setColor(Color.lightGray);

	    final Graphics2D g2d = (Graphics2D) g;
	    final Point2D center = radialLayout.getCenter();

	    final Ellipse2D ellipse = new Ellipse2D.Double();
	    for (final double d : depths) {
		ellipse.setFrameFromDiagonal(center.getX() - d, center.getY() - d,
			center.getX() + d, center.getY() + d);
		final Shape shape = vv.getRenderContext().getMultiLayerTransformer()
			.getTransformer(Layer.LAYOUT).transform(ellipse);
		g2d.draw(shape);
	    }
	}

	@Override
	public boolean useTransform() {
	    return true;
	}
    }

}
