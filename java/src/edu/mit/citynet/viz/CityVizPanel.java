package edu.mit.citynet.viz;

import java.awt.Dimension;

import javax.swing.JLayeredPane;

import edu.mit.citynet.core.City;

public class CityVizPanel extends JLayeredPane {
	private static final long serialVersionUID = 3994034732879260199L;
	private City city;
	private MapLayer mapLayer;
	private CellRegionLayer cellRegionLayer;
	
	/**
	 * Instantiates a new city viz panel.
	 */
	public CityVizPanel(City city) {
		if (city==null) {
			throw new IllegalArgumentException("City cannot be null.");
		}
		this.city = city;
		setPreferredSize(new Dimension(250,250));
		mapLayer = new MapLayer(city.getImage());
		add(mapLayer, new Integer(1));
		cellRegionLayer = new CellRegionLayer(city);
		add(cellRegionLayer, new Integer(2));
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#setBounds(int, int, int, int)
	 */
	public void setBounds(int x, int y, int width, int height) {
		// this function is used to synchronize the size of the layers
		super.setBounds(x,y,width,height);
		mapLayer.setBounds(0,0,width,height);
		cellRegionLayer.setBounds(0,0,width,height);
	}
}
