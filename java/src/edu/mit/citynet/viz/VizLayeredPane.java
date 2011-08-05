/*
 * 
 */
package edu.mit.citynet.viz;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JLayeredPane;

import com.vividsolutions.jts.geom.Coordinate;

import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.util.CityNetCursor;

/**
 * The VizLayeredPane class produces a multi-layered visualization of different
 * components within a city. Its layers include: a background map, cell regions,
 * cells, node regions, nodes, edge regions, and edges.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class VizLayeredPane extends JLayeredPane {
	private static final long serialVersionUID = -7010621460240642200L;
	private static final double MIN_SCALE = 10, MAX_SCALE = 1000, MAX_X = 50, MAX_Y = 50;
	private AbstractVizPanel vizPanel;
	private GridLayer gridLayer;
	private MapLayer mapLayer;
	private CellRegionLayer cellRegionLayer;
	private CellLayer cellLayer;
	private NodeRegionLayer nodeRegionLayer;
	private NodeLayer nodeLayer;
	private EdgeRegionLayer edgeRegionLayer;
	private EdgeLayer edgeLayer;
	private City city;
	private CitySystem system;
	private double viewScale;
	private Coordinate viewOrigin;
	private Point previousDrag;
	
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
		this.viewScale = 100d;	// default: 100 px/km
		this.viewOrigin = new Coordinate();
		initializePanel();
	}
	
	/**
	 * Initializes the panel.
	 */
	private void initializePanel() {
		setPreferredSize(new Dimension(250,250));
		gridLayer = new GridLayer(this);
		add(gridLayer, new Integer(1));
		mapLayer = new MapLayer(this);
		add(mapLayer, new Integer(2));
		cellRegionLayer = new CellRegionLayer(this);
		add(cellRegionLayer, new Integer(3));
		cellLayer = new CellLayer(this);
		add(cellLayer, new Integer(4));
		nodeRegionLayer = new NodeRegionLayer(this);
		add(nodeRegionLayer, new Integer(5));
		nodeLayer = new NodeLayer(this);
		add(nodeLayer, new Integer(6));
		edgeRegionLayer = new EdgeRegionLayer(this);
		add(edgeRegionLayer, new Integer(7));
		edgeLayer = new EdgeLayer(this);
		add(edgeLayer,new Integer(8));
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				updateCursor(false);
				requestFocusInWindow(true);
			}
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					centerView(e.getPoint());
				}
			}
			public void mousePressed(MouseEvent e) {
				updateCursor(true);
				previousDrag = e.getPoint();
			}
			public void mouseReleased(MouseEvent e) {
				updateCursor(false);
				previousDrag = null;
			}
			public void mouseExited(MouseEvent e) {
				setCursor(Cursor.getDefaultCursor());
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				panView(new Point(previousDrag.x - e.getPoint().x, 
						previousDrag.y - e.getPoint().y));
				updateCursor(true);
				previousDrag = e.getPoint();
			}
		});
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				zoomView(-e.getWheelRotation(), e.getPoint());
			}
		});
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_PAGE_DOWN) {
					zoomView(-1, new Point(getWidth()/2, getHeight()/2));
				} else if (e.getKeyCode()==KeyEvent.VK_PAGE_UP) {
					zoomView(1, new Point(getWidth()/2, getHeight()/2));
				} else if(e.getKeyCode()==KeyEvent.VK_UP) {
					panView(new Point(0, 1));
				} else if(e.getKeyCode()==KeyEvent.VK_DOWN) {
					panView(new Point(0, -1));
				} else if(e.getKeyCode()==KeyEvent.VK_LEFT) {
					panView(new Point(1, 0));
				} else if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
					panView(new Point(-1, 0));
				} else if(e.getKeyCode()==KeyEvent.VK_HOME) {
					viewOrigin.x = 0;
					viewOrigin.y = 0;
					repaint();
				} else if(e.getKeyCode()==KeyEvent.VK_ENTER){
					viewScale = getAutoScale();
					viewOrigin = getAutoPosition();
					repaint();
				}
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#setBounds(int, int, int, int)
	 */
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x,y,width,height);
		// synchronize all bounding boxes
		gridLayer.setBounds(x,y,width,height);
		mapLayer.setBounds(x,y,width,height);
		cellRegionLayer.setBounds(x,y,width,height);
		cellLayer.setBounds(x,y,width,height);
		nodeRegionLayer.setBounds(x,y,width,height);
		nodeLayer.setBounds(x,y,width,height);
		edgeRegionLayer.setBounds(x,y,width,height);
		edgeLayer.setBounds(x,y,width,height);
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
	 * Gets the point.
	 *
	 * @param coordinate the coordinate
	 * @return the point
	 */
	public Point getPoint(Coordinate coordinate) {
		return new Point((int)(Math.round((coordinate.x-viewOrigin.x)*getViewScale())),
				(int)(Math.round((coordinate.y-viewOrigin.y)*getViewScale())));
	}
	
	/**
	 * Gets the coordinate.
	 *
	 * @param point the point
	 * @return the coordinate
	 */
	public Coordinate getCoordinate(Point point) {
		return new Coordinate(viewOrigin.x + point.x/viewScale,
				viewOrigin.y + point.y/viewScale);
	}
	
	/**
	 * Gets the scale (kilometers per pixel).
	 *
	 * @return the scale
	 */
	public double getViewScale() {
		return viewScale;
	}
	
	/**
	 * Gets the position of the upper-left hand corner.
	 *
	 * @return the position
	 */
	public Coordinate getViewOrigin() {
		return viewOrigin;
	}
	
	/**
	 * Gets the auto scale. Scales the view to fit the entire city image (if
	 * exists).
	 *
	 * @return the auto scale
	 */
	private double getAutoScale() {
		if(city.getImage()==null) return viewScale;
		double x1 = Double.MAX_VALUE, x2 = Double.MIN_VALUE, 
		y1 = Double.MAX_VALUE, y2 = Double.MIN_VALUE;
		for(Coordinate coordinate : city.getImagePolygon().getCoordinates()) {
			x1 = Math.min(x1, coordinate.x);
			x2 = Math.max(x2, coordinate.x);
			y1 = Math.min(y1, coordinate.y);
			y2 = Math.max(y2, coordinate.y);
		}
		return Math.min(getWidth()/Math.abs(x2-x1),getHeight()/Math.abs(y2-y1));
	}
	
	/**
	 * Gets the auto position.
	 *
	 * @return the auto position
	 */
	private Coordinate getAutoPosition() {
		if(city.getImage()==null) return viewOrigin;
		double x1 = Double.MAX_VALUE, x2 = Double.MIN_VALUE, 
		y1 = Double.MAX_VALUE, y2 = Double.MIN_VALUE;
		for(Coordinate coordinate : city.getImagePolygon().getCoordinates()) {
			x1 = Math.min(x1, coordinate.x);
			x2 = Math.max(x2, coordinate.x);
			y1 = Math.min(y1, coordinate.y);
			y2 = Math.max(y2, coordinate.y);
		}
		Point p1 = getPoint(new Coordinate(x1,y1));
		Point p2 = getPoint(new Coordinate(x2,y2));
		Point p = getPoint(new Coordinate(viewOrigin.x,viewOrigin.y));
		p.x = p1.x - (getWidth()-Math.abs(p2.x-p1.x))/2;
		p.y = p1.y - (getHeight()-Math.abs(p2.y-p1.y))/2;
		return getCoordinate(p);
	}
	
	/**
	 * Pans the view.
	 *
	 * @param distance the distance
	 */
	private void panView(Point distance) {
		Coordinate c1 = getCoordinate(new Point(0,0));
		Coordinate c2 = getCoordinate(new Point(getWidth(),getHeight()));
		viewOrigin.x = Math.min(Math.max(viewOrigin.x
				+ (distance.x)/viewScale, -MAX_X),MAX_X-(c2.x-c1.x));
		viewOrigin.y = Math.min(Math.max(viewOrigin.y
				+ (distance.y)/viewScale, -MAX_Y),MAX_Y-(c2.y-c1.y));
		repaint();
	}
	
	/**
	 * Centers the view.
	 *
	 * @param location the location
	 */
	private void centerView(Point location) {
		viewOrigin.x = viewOrigin.x + (location.x - getWidth()/2)/viewScale;
		viewOrigin.y = viewOrigin.y + (location.y - getHeight()/2)/viewScale;
		repaint();
	}
	
	/**
	 * Zooms the view, maintaining the location as constant display.
	 *
	 * @param level the level, zoom in >0, zoom out <0
	 * @param location the location
	 */
	private void zoomView(int level, Point location) {
		double scaleFactor = Math.pow(1.5, level);
		double newViewScale = Math.max(MIN_SCALE,Math.min(MAX_SCALE,viewScale*scaleFactor));
		Coordinate coordinate = getCoordinate(location);
		viewOrigin.x = coordinate.x - (coordinate.x-viewOrigin.x)*viewScale/newViewScale;
		viewOrigin.y = coordinate.y - (coordinate.y-viewOrigin.y)*viewScale/newViewScale;
		viewScale = newViewScale;
		repaint();
	}

	/**
	 * Updates the cursor to the correct cursor type.
	 *
	 * @param isClicking the is clicking
	 */
	private void updateCursor(boolean isClicking) {
		if(isClicking) {
			setCursor(CityNetCursor.CLOSED_HAND.getCursor());
		} else {
			setCursor(CityNetCursor.OPEN_HAND.getCursor());
		}
	}
}
