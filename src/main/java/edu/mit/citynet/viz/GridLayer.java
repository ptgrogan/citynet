/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.viz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JPanel;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * The GridLayer class is a transparent JPanel used to display a background 
 * grid in a layered pane used for visualization.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class GridLayer extends JPanel {
	private static final long serialVersionUID = 5737804310077531840L;
	private VizLayeredPane vizPane;
	
	/**
	 * Instantiates a new map layer.
	 *
	 * @param vizPane the viz pane
	 */
	public GridLayer(VizLayeredPane vizPane) {
		this.vizPane = vizPane;
		setOpaque(false);
	}
	
	/**
	 * Gets the grid spacing.
	 *
	 * @return the grid spacing
	 */
	private double getGridSpacing() {
		return vizPane.getDisplayOptions().getGridSpacing();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(!vizPane.getDisplayOptions().isGridDisplayed()) return;
		// get extents of area
		Coordinate c1 = vizPane.getCoordinate(new Point(0,0));
		Coordinate c2 = vizPane.getCoordinate(
				new Point(vizPane.getWidth(), vizPane.getHeight()));
		g.setColor(new Color(0x00cccccc));
		int numX = (int)Math.floor((c2.x-c1.x)/getGridSpacing()+1);
		int numY = (int)Math.floor((c2.y-c1.y)/getGridSpacing()+1);
		double startX = Math.floor(c1.x / getGridSpacing()+1)*getGridSpacing();
		double startY = Math.floor(c1.y / getGridSpacing()+1)*getGridSpacing();
		for(int i = 0; i < numX; i++) {
			if(g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D)g;
				if(startX+i*getGridSpacing()==0) {
					g2d.setStroke(new BasicStroke(3f));
				} else {
					g2d.setStroke(new BasicStroke(1f));
				}
			}
			Point p1 = vizPane.getPoint(new Coordinate(startX+i*getGridSpacing(),c1.y));
			Point p2 = vizPane.getPoint(new Coordinate(startX+i*getGridSpacing(),c2.y));
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
		for(int i = 0; i < numY; i++) {
			if(g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D)g;
				if(startY+i*getGridSpacing()==0) {
					g2d.setStroke(new BasicStroke(3f));
				} else {
					g2d.setStroke(new BasicStroke(1f));
				}
			}
			Point p1 = vizPane.getPoint(new Coordinate(c1.x,startY+i*getGridSpacing()));
			Point p2 = vizPane.getPoint(new Coordinate(c2.x,startY+i*getGridSpacing()));
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
	}
}
