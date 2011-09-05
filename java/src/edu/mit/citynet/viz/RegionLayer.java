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

import edu.mit.citynet.core.NodeGenerationType;
import edu.mit.citynet.core.Region;

/**
 * The RegionLayer class is a transparent JPanel used to display regions in 
 * a layered pane used for visualization. It draws each selected region on the 
 * existing city image.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class RegionLayer extends JPanel {
	private static final long serialVersionUID = -5721250566037759894L;
	private VizLayeredPane vizPane;
	
	/**
	 * Instantiates a new node region layer.
	 *
	 * @param vizPane the viz pane
	 */
	public RegionLayer(VizLayeredPane vizPane) {
		this.vizPane = vizPane;
		setOpaque(false);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(vizPane.getSystem() == null) return;
		
		for(Region region : vizPane.getSystem().getRegions()) {
			if(vizPane.getDisplayOptions().isLayersFiltered() 
					&& vizPane.getDisplayOptions().getDisplayHeight()!=region.getLayer().getDisplayHeight()) 
				continue;
			boolean selected = region.equals(vizPane.getSelectedRegion());
			int[] xPoints = new int[region.getCoordinateList().size()];
			int[] yPoints = new int[region.getCoordinateList().size()];
			for(int i=0; i<region.getCoordinateList().size(); i++) {
				// convert distance units to pixels
				Point p = vizPane.getPoint(region.getCoordinateList().getCoordinate(i));
				xPoints[i] = p.x;
				yPoints[i] = p.y;
			}
			if(g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D)g;
				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 
						vizPane.getDisplayOptions().getNodeRegionOpacity()));
				if(selected) {
					g2d.setStroke(new BasicStroke(5f));
					g2d.setColor(Color.WHITE);
				} else {
					g2d.setStroke(new BasicStroke(2f));
					g2d.setColor(region.getNodeType().getColor());
				}
				// TODO: what if edge-only?
				if(region.getNodeGenerationType()==NodeGenerationType.POLYGON) {
					// draw a semi-transparent polygon with the node type color 
					// with a solid outline of the same color
					g2d.drawPolygon(xPoints, yPoints, xPoints.length);
					g2d.fillPolygon(xPoints, yPoints, xPoints.length);
				} else if(region.getNodeGenerationType()==NodeGenerationType.POLYLINE) {
					// draw a polyline outline with the node type color 
					g2d.drawPolyline(xPoints, yPoints, xPoints.length);
				} else if(region.getNodeGenerationType()==NodeGenerationType.POLYPOINT) {
					// draw an opaque circle with the node type color
					for(int i=0; i<xPoints.length; i++) {
						g2d.fillOval(xPoints[i]-3, yPoints[i]-3, 6, 6);
					}
				}
			}
		}
	}
}
