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

import edu.mit.citynet.core.EdgeGenerationType;
import edu.mit.citynet.core.InterLayerRegion;
import edu.mit.citynet.core.IntraLayerRegion;
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
					&& region instanceof IntraLayerRegion
					&& vizPane.getDisplayOptions().getDisplayHeight()
					!= ((IntraLayerRegion)region).getLayer().getDisplayHeight()) 
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
			if(g instanceof Graphics2D && region instanceof IntraLayerRegion) {
				IntraLayerRegion intraLayerRegion = (IntraLayerRegion)region;
				Graphics2D g2d = (Graphics2D)g;
				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 
						vizPane.getDisplayOptions().getRegionOpacity()));
				if(selected) {
					g2d.setStroke(new BasicStroke(5f));
					g2d.setColor(Color.WHITE);
				} else {
					g2d.setStroke(new BasicStroke(2f));
					if(intraLayerRegion.getNodeType()!=null
							&& intraLayerRegion.getNodeGenerationType()!=NodeGenerationType.NONE)
						g2d.setColor(intraLayerRegion.getNodeType().getColor());
					else if(intraLayerRegion.getEdgeType()!=null
							&& intraLayerRegion.getEdgeGenerationType()!=EdgeGenerationType.NONE)
						g2d.setColor(intraLayerRegion.getEdgeType().getColor());
					else
						g2d.setColor(Color.WHITE);
				}
				if(intraLayerRegion.getNodeGenerationType()==NodeGenerationType.POLYGON) {
					// draw a semi-transparent polygon with the node type color 
					// with a solid outline of the same color
					g2d.drawPolygon(xPoints, yPoints, xPoints.length);
					g2d.fillPolygon(xPoints, yPoints, xPoints.length);
				} else if(intraLayerRegion.getNodeGenerationType()==NodeGenerationType.POLYLINE) {
					// draw a polyline outline with the node type color 
					g2d.drawPolyline(xPoints, yPoints, xPoints.length);
				} else if(intraLayerRegion.getNodeGenerationType()==NodeGenerationType.POLYPOINT) {
					// draw an opaque circle with the node type color
					for(int i=0; i<xPoints.length; i++) {
						g2d.fillOval(xPoints[i]-3, yPoints[i]-3, 6, 6);
					}
				} else if(intraLayerRegion.getNodeGenerationType()==NodeGenerationType.NONE) {
					if(intraLayerRegion.getEdgeGenerationType()==EdgeGenerationType.ADJACENT
							|| intraLayerRegion.getEdgeGenerationType()==EdgeGenerationType.CONNECTED
							|| intraLayerRegion.getEdgeGenerationType()==EdgeGenerationType.ORTHOGONAL) {
						// draw a semi-transparent polygon with the edge type color 
						// with a solid outline of the same color
						g2d.drawPolygon(xPoints, yPoints, xPoints.length);
						g2d.fillPolygon(xPoints, yPoints, xPoints.length);
					} else if(intraLayerRegion.getEdgeGenerationType()==EdgeGenerationType.SEQUENTIAL) {
						// draw a solid polygon outline with the edge type color
						g2d.drawPolyline(xPoints, yPoints, xPoints.length);
					}
				}
			}
			if(g instanceof Graphics2D && region instanceof InterLayerRegion) {
				InterLayerRegion interLayerRegion = (InterLayerRegion)region;
				Graphics2D g2d = (Graphics2D)g;
				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 
						vizPane.getDisplayOptions().getRegionOpacity()));
				if(selected) {
					g2d.setStroke(new BasicStroke(5f));
					g2d.setColor(Color.WHITE);
				} else {
					g2d.setStroke(new BasicStroke(2f));
					if(interLayerRegion.getEdgeType()!=null)
						g2d.setColor(interLayerRegion.getEdgeType().getColor());
				}
				// draw an concentric circles with the edge type color
				for(int i=0; i<xPoints.length; i++) {
					g2d.drawOval(xPoints[i]-6, yPoints[i]-6, 12, 12);
					g2d.fillOval(xPoints[i]-3, yPoints[i]-3, 6, 6);
				}
			}
		}
	}
}
