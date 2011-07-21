package edu.mit.citynet.viz;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Set;

import javax.swing.JPanel;

import edu.mit.citynet.core.EdgeRegion;

public class EdgeRegionLayer extends JPanel {
	private static final long serialVersionUID = -5721250566037759894L;
	private VizLayeredPane vizPane;
	
	/**
	 * Instantiates a new edge region layer.
	 *
	 * @param vizPane the viz pane
	 */
	public EdgeRegionLayer(VizLayeredPane vizPane) {
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
		
		Set<EdgeRegion> edgeRegions = vizPane.getVizPanel().getSelectedEdgeRegions();
		
		for(EdgeRegion edgeRegion : edgeRegions) {
			int[] xPoints = new int[edgeRegion.getCoordinateList().size()];
			int[] yPoints = new int[edgeRegion.getCoordinateList().size()];
			for(int i=0; i<edgeRegion.getCoordinateList().size(); i++) {
				double x = edgeRegion.getCoordinateList().getCoordinate(i).x;
				double y = edgeRegion.getCoordinateList().getCoordinate(i).y;
				int[] ij = vizPane.xy2ij(x,y);
				xPoints[i] = ij[0];
				yPoints[i] = ij[1];
			}
			if(g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D)g;
				g2d.setStroke(new BasicStroke(2f));
				g2d.setColor(edgeRegion.getEdgeType().getColor());
				if(edgeRegion.getEdgeRegionType()==EdgeRegion.EdgeRegionType.POLYGON_ADJACENT
						|| edgeRegion.getEdgeRegionType()==EdgeRegion.EdgeRegionType.POLYGON_CONNECTED
						|| edgeRegion.getEdgeRegionType()==EdgeRegion.EdgeRegionType.POLYGON_ORTHOGONAL) {
					g2d.drawPolygon(xPoints, yPoints, xPoints.length);
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					g2d.fillPolygon(xPoints, yPoints, xPoints.length);
				} else if(edgeRegion.getEdgeRegionType()==EdgeRegion.EdgeRegionType.POLYLINE
						|| edgeRegion.getEdgeRegionType()==EdgeRegion.EdgeRegionType.POLYPOINT) {
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
					g2d.drawPolyline(xPoints, yPoints, xPoints.length);
				}
			}
		}
	}
}
