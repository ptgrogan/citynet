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

import edu.mit.citynet.core.Cell;

/**
 * The CellLayer class is a transparent JPanel used to display cells in 
 * a layered pane used for visualization. It draws each cell to overlay
 * on the existing city image.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CellLayer extends JPanel {
	private static final long serialVersionUID = -5721250566037759894L;
	private VizLayeredPane vizPane;
	
	/**
	 * Instantiates a new cell layer.
	 *
	 * @param vizPane the viz pane
	 */
	public CellLayer(VizLayeredPane vizPane) {
		this.vizPane = vizPane;
		setOpaque(false);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for(Cell cell : vizPane.getCity().getCells()) {
			int[] xPoints = new int[cell.getPolygon().getCoordinates().length];
			int[] yPoints = new int[cell.getPolygon().getCoordinates().length];
			for(int i=0; i<cell.getPolygon().getCoordinates().length; i++) {
				// convert distance units to pixels
				Point p = vizPane.getPoint(cell.getPolygon().getCoordinates()[i]);
				xPoints[i] = p.x;
				yPoints[i] = p.y;
			}
			if(g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D)g;
				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 
						vizPane.getDisplayOptions().getCellOpacity()));
				// draw transparent square with thin solid black outline
				g2d.setStroke(new BasicStroke(0.5f));
				g2d.setColor(Color.BLACK);
				g2d.drawPolygon(xPoints, yPoints, xPoints.length);
			}
		}
	}
}
