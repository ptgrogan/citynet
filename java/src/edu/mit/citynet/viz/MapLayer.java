package edu.mit.citynet.viz;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JPanel;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * The MapLayer class is a transparent JPanel used to display a background map 
 * in a layered pane used for visualization.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class MapLayer extends JPanel {
	private static final long serialVersionUID = 5737804310077531840L;
	private VizLayeredPane vizPane;
	
	/**
	 * Instantiates a new map layer.
	 *
	 * @param vizPane the viz pane
	 */
	public MapLayer(VizLayeredPane vizPane) {
		this.vizPane = vizPane;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image image = vizPane.getCity().getImage();
		if(image == null) return;
		// scale the image such that it fits in the area
		double x1 = Double.MAX_VALUE, x2 = Double.MIN_VALUE, 
			y1 = Double.MAX_VALUE, y2 = Double.MIN_VALUE;
		for(Coordinate coordinate : vizPane.getCity().getImagePolygon().getCoordinates()) {
			x1 = Math.min(x1, coordinate.x);
			x2 = Math.max(x2, coordinate.x);
			y1 = Math.min(y1, coordinate.y);
			y2 = Math.max(y2, coordinate.y);
		}
		Point p1 = vizPane.getPoint(new Coordinate(x1,y1));
		Point p2 = vizPane.getPoint(new Coordinate(x2,y2));
		g.drawImage(image.getScaledInstance(Math.abs(p2.x-p1.x), 
				Math.abs(p2.y-p1.y), Image.SCALE_SMOOTH), p1.x, p1.y, null);
	}
}
