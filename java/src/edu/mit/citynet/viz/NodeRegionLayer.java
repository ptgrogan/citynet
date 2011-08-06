package edu.mit.citynet.viz;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Set;

import javax.swing.JPanel;

import edu.mit.citynet.core.NodeRegion;

/**
 * The NodeRegionLayer class is a transparent JPanel used to display node
 * regions in a layered pane used for visualization. It draws each selected
 * node region on the existing city image.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class NodeRegionLayer extends JPanel {
	private static final long serialVersionUID = -5721250566037759894L;
	private VizLayeredPane vizPane;
	
	/**
	 * Instantiates a new node region layer.
	 *
	 * @param vizPane the viz pane
	 */
	public NodeRegionLayer(VizLayeredPane vizPane) {
		this.vizPane = vizPane;
		setOpaque(false);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(vizPane.getSystem() == null) return;
		
		Set<NodeRegion> nodeRegions = vizPane.getVizPanel().getCheckedNodeRegions();
		
		for(NodeRegion nodeRegion : nodeRegions) {
			boolean selected = vizPane.getVizPanel().getSelectedNodeRegions().contains(nodeRegion);
			int[] xPoints = new int[nodeRegion.getCoordinateList().size()];
			int[] yPoints = new int[nodeRegion.getCoordinateList().size()];
			for(int i=0; i<nodeRegion.getCoordinateList().size(); i++) {
				// convert distance units to pixels
				Point p = vizPane.getPoint(nodeRegion.getCoordinateList().getCoordinate(i));
				xPoints[i] = p.x;
				yPoints[i] = p.y;
			}
			if(g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D)g;
				if(selected) {
					g2d.setStroke(new BasicStroke(5f));
					g2d.setColor(Color.WHITE);
				} else {
					g2d.setStroke(new BasicStroke(2f));
					g2d.setColor(nodeRegion.getNodeType().getColor());
				}
				if(nodeRegion.getNodeRegionType()==NodeRegion.NodeRegionType.POLYGON) {
					// draw a semi-transparent polygon with the node type color 
					// with a solid outline of the same color
					g2d.drawPolygon(xPoints, yPoints, xPoints.length);
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					g2d.fillPolygon(xPoints, yPoints, xPoints.length);
				} else if(nodeRegion.getNodeRegionType()==NodeRegion.NodeRegionType.POLYLINE) {
					// draw a polyline outline with the node type color 
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
					g2d.drawPolyline(xPoints, yPoints, xPoints.length);
				} else if(nodeRegion.getNodeRegionType()==NodeRegion.NodeRegionType.POLYPOINT) {
					// draw an opaque circle with the node type color
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
					for(int i=0; i<xPoints.length; i++) {
						g2d.fillOval(xPoints[i]-3, yPoints[i]-3, 6, 6);
					}
				}
			}
		}
	}
}
