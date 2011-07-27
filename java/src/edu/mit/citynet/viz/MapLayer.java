package edu.mit.citynet.viz;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

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
		double scale = Math.min(((double)getWidth())/image.getWidth(null), 
				((double)getHeight())/image.getHeight(null));
		// convert from distance units to pixels
		int pX = (int)(getWidth()-scale*image.getWidth(null))/2;
		int pY = (int)(getHeight()-scale*image.getHeight(null))/2;
		g.drawImage(image.getScaledInstance((int)(scale*image.getWidth(null)), 
				(int)(scale*image.getHeight(null)), Image.SCALE_SMOOTH), pX, pY, null);
	}
}
