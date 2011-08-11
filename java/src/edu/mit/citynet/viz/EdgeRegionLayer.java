/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.viz;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JPanel;

import edu.mit.citynet.core.EdgeRegion;

/**
 * The EdgeRegionLayer class is a transparent JPanel used to display edge
 * regions in a layered pane used for visualization. It draws each selected
 * edge region on the existing city image.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
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
		if(vizPane.getSystem() == null) return;
		
		for(EdgeRegion edgeRegion : vizPane.getSystem().getEdgeRegions()) {
			boolean selected = edgeRegion.equals(vizPane.getSelectedEdgeRegion());
			int[] xPoints = new int[edgeRegion.getCoordinateList().size()];
			int[] yPoints = new int[edgeRegion.getCoordinateList().size()];
			for(int i=0; i<edgeRegion.getCoordinateList().size(); i++) {
				// convert distance units to pixels
				Point p = vizPane.getPoint(edgeRegion.getCoordinateList().getCoordinate(i));
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
					g2d.setColor(edgeRegion.getEdgeType().getColor());
				}
				if(edgeRegion.getEdgeRegionType()==EdgeRegion.EdgeRegionType.POLYGON_ADJACENT
						|| edgeRegion.getEdgeRegionType()==EdgeRegion.EdgeRegionType.POLYGON_CONNECTED
						|| edgeRegion.getEdgeRegionType()==EdgeRegion.EdgeRegionType.POLYGON_ORTHOGONAL) {
					// draw a semi-transparent polygon with the edge type color 
					// with a solid outline of the same color
					g2d.drawPolygon(xPoints, yPoints, xPoints.length);
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					g2d.fillPolygon(xPoints, yPoints, xPoints.length);
				} else if(edgeRegion.getEdgeRegionType()==EdgeRegion.EdgeRegionType.POLYLINE
						|| edgeRegion.getEdgeRegionType()==EdgeRegion.EdgeRegionType.POLYPOINT) {
					// draw a solid polygon outline with the edge type color
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
					g2d.drawPolyline(xPoints, yPoints, xPoints.length);
				}
			}
		}
	}
}
