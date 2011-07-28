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
 * The VizLayeredPane class produces a multi-layered visualization of different
 * components within a city. Its layers include: a background map, cell regions,
 * cells, node regions, nodes, edge regions, and edges.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class VizLayeredPane extends JLayeredPane {
	private static final long serialVersionUID = -7010621460240642200L;
	private AbstractVizPanel vizPanel;
	private MapLayer mapLayer;
	private CellRegionLayer cellRegionLayer;
	private CellLayer cellLayer;
	private NodeRegionLayer nodeRegionLayer;
	private NodeLayer nodeLayer;
	private EdgeRegionLayer edgeRegionLayer;
	private EdgeLayer edgeLayer;
	private City city;
	private CitySystem system;
	
	/**
	 * Instantiates a new viz layered pane.
	 *
	 * @param vizPanel the viz panel
	 * @param city the city
	 * @param system the system
	 */
	public VizLayeredPane(AbstractVizPanel vizPanel, City city, CitySystem system) {
		if (city==null) {
			throw new IllegalArgumentException("City cannot be null.");
		}
		this.vizPanel = vizPanel;
		this.city = city;
		this.system = system;
		setPreferredSize(new Dimension(250,250));
		mapLayer = new MapLayer(this);
		add(mapLayer, new Integer(1));
		cellRegionLayer = new CellRegionLayer(this);
		add(cellRegionLayer, new Integer(2));
		cellLayer = new CellLayer(this);
		add(cellLayer, new Integer(3));
		nodeRegionLayer = new NodeRegionLayer(this);
		add(nodeRegionLayer, new Integer(4));
		nodeLayer = new NodeLayer(this);
		add(nodeLayer, new Integer(5));
		edgeRegionLayer = new EdgeRegionLayer(this);
		add(edgeRegionLayer, new Integer(6));
		edgeLayer = new EdgeLayer(this);
		add(edgeLayer,new Integer(7));
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#setBounds(int, int, int, int)
	 */
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x,y,width,height);
		mapLayer.setBounds(0,0,width,height);
		cellRegionLayer.setBounds(0,0,width,height);
		cellLayer.setBounds(0,0,width,height);
		nodeRegionLayer.setBounds(0,0,width,height);
		nodeLayer.setBounds(0,0,width,height);
		edgeRegionLayer.setBounds(0,0,width,height);
		edgeLayer.setBounds(0,0,width,height);
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
	 * Converts x-y (distance) coordinates to i-j (pixel) coordinates. Uses 
	 * the known size of the image to set the boundaries of the display.
	 * Will always scale square (e.g. same scale applied in x- and y-axes).
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