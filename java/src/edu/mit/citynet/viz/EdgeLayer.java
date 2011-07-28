package edu.mit.citynet.viz;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Set;

import javax.swing.JPanel;

import edu.mit.citynet.core.Edge;

/**
 * The EdgeLayer class is a transparent JPanel used to display edges in a 
 * layered pane used for visualization. It draws each edge on the existing 
 * city image.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class EdgeLayer extends JPanel {
	private static final long serialVersionUID = -5721250566037759894L;
	private VizLayeredPane vizPane;
	
	/**
	 * Instantiates a new cell layer.
	 *
	 * @param vizPane the viz pane
	 */
	public EdgeLayer(VizLayeredPane vizPane) {
		this.vizPane = vizPane;
		setOpaque(false);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(vizPane.getCity().getImage() == null 
				|| vizPane.getCity().getImagePolygon() == null
				|| vizPane.getSystem() == null) return;
		
		Set<Edge> edges = vizPane.getSystem().getEdges();
		
		for(Edge edge : edges) {
			double x1 = edge.getOrigin().getCell().getPolygon().getCentroid().getCoordinate().x;
			double y1 = edge.getOrigin().getCell().getPolygon().getCentroid().getCoordinate().y;
			// convert distance units to pixels
			int[] ij1 = vizPane.xy2ij(x1, y1);
			Point origin = new Point(ij1[0],ij1[1]);
			double x2 = edge.getDestination().getCell().getPolygon().getCentroid().getCoordinate().x;
			double y2 = edge.getDestination().getCell().getPolygon().getCentroid().getCoordinate().y;
			// convert distance units to pixels
			int[] ij2 = vizPane.xy2ij(x2, y2);
			Point destination = new Point(ij2[0],ij2[1]);
			if(g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D)g;
				// draw a line from origin to destination using the edge
				// type color. also draw arrows to indicate directionality
				g2d.setStroke(new BasicStroke(2f));
				g2d.setColor(edge.getEdgeType().getColor());
				g2d.drawLine(origin.x, origin.y, destination.x, destination.y);
				g2d.fill(createArrowShape(origin,destination));
				if(!edge.isDirected()) g2d.fill(createArrowShape(destination,origin));
			}
		}
	}
	
	/**
	 * Creates the arrow shape.
	 *
	 * @param origin the origin point
	 * @param destination the destination point
	 * @return the arrow shape
	 */
	private static Shape createArrowShape(Point origin, Point destination) {
	    Polygon arrowHead = new Polygon();
	    // arrow is a simple triangle
	    arrowHead.addPoint(0,0);
	    arrowHead.addPoint(-4,-9);
	    arrowHead.addPoint(4,-9);

	    AffineTransform tx = new AffineTransform();
	    tx.setToIdentity();
	    double angle = Math.atan2(destination.y-origin.y, destination.x-origin.x);
	    // translate and rotate arrow to point along line
	    tx.translate(destination.x, destination.y);
	    tx.rotate((angle-Math.PI/2d));  

	    return tx.createTransformedShape(arrowHead);
	}
}