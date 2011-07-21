package edu.mit.citynet.viz;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;

import javax.swing.JPanel;

import edu.mit.citynet.core.Edge;

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
			System.out.println(edge + " " + edge.getOrigin() + " " + edge.getDestination());
			int[] xPoints = new int[2];
			int[] yPoints = new int[2];
			double x1 = edge.getOrigin().getCell().getPolygon().getCentroid().getCoordinate().x;
			double y1 = edge.getOrigin().getCell().getPolygon().getCentroid().getCoordinate().y;
			int[] ij1 = vizPane.xy2ij(x1, y1);
			xPoints[0] = ij1[0];
			yPoints[0] = ij1[1];
			double x2 = edge.getDestination().getCell().getPolygon().getCentroid().getCoordinate().x;
			double y2 = edge.getDestination().getCell().getPolygon().getCentroid().getCoordinate().y;
			int[] ij2 = vizPane.xy2ij(x2, y2);
			xPoints[1] = ij2[0];
			yPoints[1] = ij2[1];
			if(g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D)g;
				g2d.setStroke(new BasicStroke(2f));
				g2d.setColor(edge.getEdgeType().getColor());
				g2d.drawLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1]);
				/*Polygon arrowHead = new Polygon();
				arrowHead.addPoint(0,5);
				arrowHead.addPoint(-5,-5);
				arrowHead.addPoint(5,-5);
				AffineTransform tx = new AffineTransform();
				tx.setToIdentity();
				tx.translate(xPoints[1], yPoints[1]);
				tx.rotate(Math.atan2(yPoints[1]-yPoints[0],xPoints[1]-xPoints[0]));
				g2d.setTransform(tx);
				g2d.fill(arrowHead);*/
			}
		}
	}
}
