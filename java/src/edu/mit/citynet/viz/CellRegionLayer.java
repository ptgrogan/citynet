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

import edu.mit.citynet.core.CellRegion;

/**
 * The CellRegionLayer class is a transparent JPanel used to display cell
 * regions in a layered pane used for visualization. It draws each selected
 * cell region on the existing city image.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CellRegionLayer extends JPanel {
	private static final long serialVersionUID = -5721250566037759894L;
	private VizLayeredPane vizPane;
	
	/**
	 * Instantiates a new cell region layer.
	 *
	 * @param vizPane the viz pane
	 */
	public CellRegionLayer(VizLayeredPane vizPane) {
		this.vizPane = vizPane;
		setOpaque(false);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for(CellRegion cellRegion : vizPane.getCity().getCellRegions()) {
			boolean selected = cellRegion.equals(vizPane.getSelectedCellRegion());
			int[] xPoints = new int[cellRegion.getCoordinateList().size()];
			int[] yPoints = new int[cellRegion.getCoordinateList().size()];
			for(int i=0; i<cellRegion.getCoordinateList().size(); i++) {
				// convert distance units to pixels
				Point p = vizPane.getPoint(cellRegion.getCoordinateList().getCoordinate(i));
				xPoints[i] = p.x;
				yPoints[i] = p.y;
			}
			if(g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D)g;
				// draw a semi-tranparent black square with a black outline
				if(selected) {
					g2d.setStroke(new BasicStroke(5f));
					g2d.setColor(Color.WHITE);
				} else {
					g2d.setStroke(new BasicStroke(2f));
					g2d.setColor(Color.BLACK);
				}
				g2d.drawPolygon(xPoints, yPoints, xPoints.length);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				g2d.fillPolygon(xPoints, yPoints, xPoints.length);
			}
		}
	}
}
