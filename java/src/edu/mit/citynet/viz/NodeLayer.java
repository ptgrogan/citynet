/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.viz;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JPanel;

import edu.mit.citynet.core.Node;

/**
 * The NodeLayer class is a transparent JPanel used to display nodes in a 
 * layered pane used for visualization. It draws each node on the existing 
 * city image.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class NodeLayer extends JPanel {
	private static final long serialVersionUID = -5721250566037759894L;
	private VizLayeredPane vizPane;
	
	/**
	 * Instantiates a new cell layer.
	 *
	 * @param vizPane the viz pane
	 */
	public NodeLayer(VizLayeredPane vizPane) {
		this.vizPane = vizPane;
		setOpaque(false);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(vizPane.getSystem() == null) return;
		
		for(Node node : vizPane.getSystem().getNodes()) {
			int[] xPoints = new int[node.getCell().getPolygon().getCoordinates().length];
			int[] yPoints = new int[node.getCell().getPolygon().getCoordinates().length];
			for(int i=0; i<node.getCell().getPolygon().getCoordinates().length; i++) {
				// ocnvert distance units to pixels
				Point p = vizPane.getPoint(node.getCell().getPolygon().getCoordinates()[i]);
				xPoints[i] = p.x;
				yPoints[i] = p.y;
			}
			if(g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D)g;
				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 
						vizPane.getDisplayOptions().getNodeOpacity()));
				// draw a semi-transparent square with the node type color
				// and an outline of the same color
				g2d.setStroke(new BasicStroke(2f));
				g2d.setColor(node.getNodeType().getColor());
				g2d.drawPolygon(xPoints, yPoints, xPoints.length);
				g2d.fillPolygon(xPoints, yPoints, xPoints.length);
			}
		}
	}
}
