/*
 * 
 */
package edu.mit.citynet.viz;

import java.awt.Dimension;

import javax.swing.JLayeredPane;

import com.vividsolutions.jts.geom.Coordinate;

import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;

/**
 * The Class VizLayeredPane.
 */
public class VizLayeredPane extends JLayeredPane {
	private static final long serialVersionUID = -7010621460240642200L;
	private AbstractVizPanel vizPanel;
	private MapLayer mapLayer;
	private CellRegionLayer cellRegionLayer;
	private NodeRegionLayer nodeRegionLayer;
	private EdgeRegionLayer edgeRegionLayer;
	private City city;
	private CitySystem system;
	
	/**
	 * Instantiates a new viz layered pane.
	 *
	 * @param source the source
	 */
	public VizLayeredPane(AbstractVizPanel vizPanel, City city, CitySystem system) {
		if (city==null) {
			throw new IllegalArgumentException("City cannot be null.");
		}
		this.vizPanel = vizPanel;
		this.city = city;
		this.system = system;
		setPreferredSize(new Dimension(250,250));
		mapLayer = new MapLayer(city.getImage());
		add(mapLayer, new Integer(1));
		cellRegionLayer = new CellRegionLayer(this);
		add(cellRegionLayer, new Integer(2));
		nodeRegionLayer = new NodeRegionLayer(this);
		add(nodeRegionLayer, new Integer(3));
		edgeRegionLayer = new EdgeRegionLayer(this);
		add(edgeRegionLayer, new Integer(4));
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#setBounds(int, int, int, int)
	 */
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x,y,width,height);
		mapLayer.setBounds(0,0,width,height);
		cellRegionLayer.setBounds(0,0,width,height);
		nodeRegionLayer.setBounds(0,0,width,height);
		edgeRegionLayer.setBounds(0,0,width,height);
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
	 * Gets the system.
	 *
	 * @return the city system
	 */
	public CitySystem getSystem() {
		return system;
	}
	
	/**
	 * Gets the viz panel.
	 *
	 * @return the viz panel
	 */
	public AbstractVizPanel getVizPanel() {
		return vizPanel;
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
