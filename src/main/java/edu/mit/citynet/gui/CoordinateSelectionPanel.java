/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;

import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.viz.VizLayeredPane;

/**
 * The Class CoordinateSelectionPanel.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CoordinateSelectionPanel extends VizLayeredPane {
	private static final long serialVersionUID = 4296517639840976256L;
	private CoordinateList coordinates;
	private Coordinate draggedCoordinate;
	private CoordinatesLayer coordinatesLayer;
	
	/**
	 * Instantiates a new coordinate selection panel.
	 *
	 * @param city the city
	 * @param system the system
	 */
	public CoordinateSelectionPanel(City city, CitySystem system) {
		super(city, system);
		setPreferredSize(new Dimension(400,400));
		this.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		coordinates = new CoordinateList();
		coordinatesLayer = new CoordinatesLayer(this);
		add(coordinatesLayer,new Integer(9));
		addMouseListener(new MouseAdapter() {
			@SuppressWarnings("unchecked")
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					coordinates.add(getCoordinate(e.getPoint()));
					repaint();
				}
			}
			
			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
			 */
			public void mousePressed(MouseEvent e) {
				if(draggedCoordinate==null) {
					panModeEnabled = true;
					for(Coordinate coord : coordinates.toCoordinateArray()) {
						if(getPoint(coord).x >= e.getX()-3
								&& getPoint(coord).x <= e.getX()+3
								&& getPoint(coord).y >= e.getY()-3
								&& getPoint(coord).y <= e.getY()+3) {
							draggedCoordinate = coord;
							panModeEnabled = false;
							break;
						}
					}
				}
			}
			public void mouseReleased(MouseEvent e) {
				draggedCoordinate = null;
				panModeEnabled = true;
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if(draggedCoordinate!=null) {
					draggedCoordinate.x = getCoordinate(e.getPoint()).x;
					draggedCoordinate.y = getCoordinate(e.getPoint()).y;
					repaint();
				}
			}
		});
	}
	
	/**
	 * Sets the coordinates.
	 *
	 * @param coordinates the new coordinates
	 */
	public void setCoordinates(CoordinateList coordinates) {
		this.coordinates = coordinates;
		repaint();
	}
	
	/**
	 * Gets the coordinates.
	 *
	 * @return the coordinates
	 */
	public CoordinateList getCoordinates() {
		return coordinates;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#setBounds(int, int, int, int)
	 */
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x,y,width,height);
		// synchronize all bounding boxes
		coordinatesLayer.setBounds(x,y,width,height);
	}
	
	/**
	 * The Class CoordinatesLayer.
	 * 
	 * @author Paul Grogan, ptgrogan@mit.edu
	 */
	class CoordinatesLayer extends JPanel {
		private static final long serialVersionUID = -1607691675474964644L;
		private CoordinateSelectionPanel coordPane;
		
		/**
		 * Instantiates a new coordinates layer.
		 *
		 * @param coordPane the coord pane
		 */
		public CoordinatesLayer(CoordinateSelectionPanel coordPane) {
			this.coordPane = coordPane;
			setOpaque(false);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.JComponent#paint(java.awt.Graphics)
		 */
		public void paint(Graphics g) {
			super.paint(g);
			int i = 1;
			for(Coordinate coord : coordPane.coordinates.toCoordinateArray()) {
				if(g instanceof Graphics2D) {
					Graphics2D g2d = (Graphics2D)g;
					g2d.setStroke(new BasicStroke(2f));
					g2d.setColor(Color.white);
					g2d.fillOval(getPoint(coord).x-3, getPoint(coord).y-3, 6, 6);
					g2d.drawString("(" + i + ")", getPoint(coord).x+4, getPoint(coord).y+3);
				}
				i++;
			}
		}
	}
}
