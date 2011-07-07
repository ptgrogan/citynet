package edu.mit.citynet.viz;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.vividsolutions.jts.geom.Coordinate;

import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.City;

public class CellRegionLayer extends JPanel {
	private static final long serialVersionUID = -5721250566037759894L;
	private City city;
	
	/**
	 * Instantiates a new cell region layer.
	 *
	 * @param city the city
	 */
	public CellRegionLayer(City city) {
		this.city = city;
		setOpaque(false);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(city.getImage() == null || city.getImagePolygon() == null) return;
		
		for(CellRegion cellRegion : city.getCellRegions()) {
			int[] xPoints = new int[cellRegion.getCoordinateList().size()];
			int[] yPoints = new int[cellRegion.getCoordinateList().size()];
			for(int i=0; i<cellRegion.getCoordinateList().size(); i++) {
				double x = cellRegion.getCoordinateList().getCoordinate(i).x;
				double y = cellRegion.getCoordinateList().getCoordinate(i).y;
				int[] ij = xy2ij(x,y);
				xPoints[i] = ij[0];
				yPoints[i] = ij[1];
			}
			if(g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D)g;
				g2d.setStroke(new BasicStroke(2f));
				g2d.setColor(Color.BLACK);
				g2d.drawPolygon(xPoints, yPoints, xPoints.length);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				g2d.fillPolygon(xPoints, yPoints, xPoints.length);
			}
		}
	}
	
	/**
	 * Converts x-y coordinates to i-j coordinates.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the ij coordinates
	 */
	private int[] xy2ij(double x, double y) {
		int[] ij = new int[2];
		double scale = Math.min(((double)getWidth())/city.getImage().getWidth(null), 
				((double)getHeight())/city.getImage().getHeight(null));
		int pX = (int)(getWidth()-scale*city.getImage().getWidth(null))/2;
		int pY = (int)(getHeight()-scale*city.getImage().getHeight(null))/2;
		Coordinate[] imageCoords = city.getImagePolygon().getCoordinates();
		double bx1 = Math.min(imageCoords[0].x,Math.min(imageCoords[1].x,
				Math.min(imageCoords[2].x,imageCoords[3].x)));
		double by1 = Math.min(imageCoords[0].y,Math.min(imageCoords[1].y,
				Math.min(imageCoords[2].y,imageCoords[3].y)));
		double bx2 = Math.max(imageCoords[0].x,Math.max(imageCoords[1].x,
				Math.max(imageCoords[2].x,imageCoords[3].x)));
		double by2 = Math.max(imageCoords[0].y,Math.max(imageCoords[1].y,
				Math.max(imageCoords[2].y,imageCoords[3].y)));
		
		ij[0] = pX + (int)(scale*(x-bx1)/(bx2-bx1)*city.getImage().getWidth(null));
		ij[1] = pY + (int)(scale*(y-by1)/(by2-by1)*city.getImage().getHeight(null));
		return ij;
	}
}
