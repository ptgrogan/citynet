package edu.mit.citynet.viz;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class MapLayer extends JPanel {
	private static final long serialVersionUID = 5737804310077531840L;
	private Image image;
	
	/**
	 * Instantiates a new map layer.
	 *
	 * @param image the image
	 */
	public MapLayer(Image image) {
		this.image = image;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(image == null) return;
		double scale = Math.min(((double)getWidth())/image.getWidth(null), 
				((double)getHeight())/image.getHeight(null));
		int pX = (int)(getWidth()-scale*image.getWidth(null))/2;
		int pY = (int)(getHeight()-scale*image.getHeight(null))/2;
		g.drawImage(image.getScaledInstance((int)(scale*image.getWidth(null)), 
				(int)(scale*image.getHeight(null)), Image.SCALE_SMOOTH), pX, pY, null);
	}
	
	/**
	 * Sets the image.
	 *
	 * @param image the new image
	 */
	public void setImage(Image image) {
		this.image = image;
		repaint();
	}
}
