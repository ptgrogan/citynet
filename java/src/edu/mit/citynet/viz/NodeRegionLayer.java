package edu.mit.citynet.viz;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.NodeRegion;

public class NodeRegionLayer extends JPanel {
	private static final long serialVersionUID = -5721250566037759894L;
	private CityVizPanel cityVizPanel;
	private NodeRegion nodeRegion;
	
	/**
	 * Instantiates a new cell region layer.
	 *
	 * @param city the city
	 */
	public NodeRegionLayer(CityVizPanel cityVizPanel, NodeRegion nodeRegion) {
		this.cityVizPanel = cityVizPanel;
		this.nodeRegion = nodeRegion;
		setOpaque(false);
	}
	
	/**
	 * Sets the node region.
	 *
	 * @param nodeRegion the new node region
	 */
	public void setNodeRegion(NodeRegion nodeRegion) {
		this.nodeRegion = nodeRegion;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(cityVizPanel.getCity().getImage() == null 
				|| cityVizPanel.getCity().getImagePolygon() == null) return;
		
		Set<NodeRegion> nodeRegions = new HashSet<NodeRegion>();
		if(nodeRegion!=null) {
			nodeRegions.add(nodeRegion);
		} else {
			for(CitySystem system : cityVizPanel.getCity().getSystems()) {
				nodeRegions.addAll(system.getNodeRegions());
			}
		}
		
		for(NodeRegion nodeRegion : nodeRegions) {
			int[] xPoints = new int[nodeRegion.getCoordinateList().size()];
			int[] yPoints = new int[nodeRegion.getCoordinateList().size()];
			for(int i=0; i<nodeRegion.getCoordinateList().size(); i++) {
				double x = nodeRegion.getCoordinateList().getCoordinate(i).x;
				double y = nodeRegion.getCoordinateList().getCoordinate(i).y;
				int[] ij = cityVizPanel.xy2ij(x,y);
				xPoints[i] = ij[0];
				yPoints[i] = ij[1];
			}
			if(g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D)g;
				g2d.setStroke(new BasicStroke(2f));
				g2d.setColor(nodeRegion.getNodeType().getColor());
				if(nodeRegion.getNodeRegionType()==NodeRegion.NodeRegionType.POLYGON) {
					g2d.drawPolygon(xPoints, yPoints, xPoints.length);
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					g2d.fillPolygon(xPoints, yPoints, xPoints.length);
				} else if(nodeRegion.getNodeRegionType()==NodeRegion.NodeRegionType.POLYLINE) {
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
					g2d.drawPolyline(xPoints, yPoints, xPoints.length);
				} else if(nodeRegion.getNodeRegionType()==NodeRegion.NodeRegionType.POLYPOINT) {
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
					for(int i=1; i<xPoints.length; i++) {
						g2d.drawOval(xPoints[i]-1, yPoints[i]-1, 2, 2);
					}
				}
			}
		}
	}
}
