/*
 * 
 */
package edu.mit.citynet.viz;

import java.awt.Dimension;

import javax.swing.JLayeredPane;

import com.vividsolutions.jts.geom.Coordinate;

import edu.mit.citynet.core.City;

/**
 * The Class CityVizPanel.
 */
public class CityVizPanel extends JLayeredPane {
	private static final long serialVersionUID = 3994034732879260199L;
	
	private City city;
	private MapLayer mapLayer;
	private CellRegionLayer cellRegionLayer;
	private NodeRegionLayer nodeRegionLayer;
	
	/**
	 * Instantiates a new city viz panel.
	 *
	 * @param city the city
	 */
	public CityVizPanel(City city) {
		if (city==null) {
			throw new IllegalArgumentException("City cannot be null.");
		}
		this.city = city;
		setPreferredSize(new Dimension(250,250));
		mapLayer = new MapLayer(city.getImage());
		add(mapLayer, new Integer(1));
		cellRegionLayer = new CellRegionLayer(this,null);
		add(cellRegionLayer, new Integer(2));
		nodeRegionLayer = new NodeRegionLayer(this,null);
		add(nodeRegionLayer, new Integer(3));
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#setBounds(int, int, int, int)
	 */
	public void setBounds(int x, int y, int width, int height) {
		// this function is used to synchronize the size of the layers
		super.setBounds(x,y,width,height);
		mapLayer.setBounds(0,0,width,height);
		cellRegionLayer.setBounds(0,0,width,height);
		nodeRegionLayer.setBounds(0,0,width,height);
	}
	
	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public City getCity() {
		return city;
	}
	
	/**
	 * Converts x-y coordinates to i-j coordinates.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the ij coordinates
	 */
	public int[] xy2ij(double x, double y) {
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
