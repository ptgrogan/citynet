/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.viz;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.vividsolutions.jts.geom.Coordinate;

import edu.mit.citynet.CityNet;
import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.core.Region;
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
	private DisplayOptionsPanel displayOptionsPanel;
	private GridLayer gridLayer;
	private MapLayer mapLayer;
	private CellRegionLayer cellRegionLayer;
	private CellLayer cellLayer;
	private NodeLayer nodeLayer;
	private EdgeLayer edgeLayer;
	private RegionLayer regionLayer;
	private City city;
	private CitySystem system;
	private Point previousDrag;
	private CellRegion selectedCellRegion;
	private NodeRegion selectedNodeRegion;
	private EdgeRegion selectedEdgeRegion;
	private Region selectedRegion;
	protected boolean panModeEnabled = true;
	
	/**
	 * Instantiates a new viz layered pane.
	 *
	 * @param vizPanel the viz panel
	 * @param city the city
	 * @param system the system
	 */
	public VizLayeredPane(City city, CitySystem system) {
		if (city==null) {
			throw new IllegalArgumentException("City cannot be null.");
		}
		this.city = city;
		this.system = system;
		displayOptionsPanel = new DisplayOptionsPanel();
		initializePanel();
	}
	
	/**
	 * Gets the display options.
	 *
	 * @return the display options
	 */
	DisplayOptions getDisplayOptions() {
		return CityNet.getInstance().getDisplayOptions();
	}
	
	/**
	 * Initializes the panel.
	 */
	private void initializePanel() {
		gridLayer = new GridLayer(this);
		add(gridLayer, new Integer(1));
		mapLayer = new MapLayer(this);
		add(mapLayer, new Integer(2));
		cellRegionLayer = new CellRegionLayer(this);
		add(cellRegionLayer, new Integer(3));
		cellLayer = new CellLayer(this);
		add(cellLayer, new Integer(4));
		nodeLayer = new NodeLayer(this);
		add(nodeLayer, new Integer(5));
		edgeLayer = new EdgeLayer(this);
		add(edgeLayer,new Integer(6));
		regionLayer = new RegionLayer(this);
		add(regionLayer, new Integer(7));
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				updateCursor(false);
				requestFocusInWindow(true);
			}
			public void mouseClicked(MouseEvent e) {
				maybeShowPopup(e);
			}
			public void mousePressed(MouseEvent e) {
				updateCursor(true);
				if(panModeEnabled) previousDrag = e.getPoint();
			}
			public void mouseReleased(MouseEvent e) {
				updateCursor(false);
				if(panModeEnabled) previousDrag = null;
				maybeShowPopup(e);
			}
			public void mouseExited(MouseEvent e) {
				setCursor(Cursor.getDefaultCursor());
			}
			private void maybeShowPopup(MouseEvent e) {
				if(e.isPopupTrigger()) {
					createVisualizationPopupMenu().show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if(panModeEnabled) {
					panView(new Point(previousDrag.x - e.getPoint().x, 
							previousDrag.y - e.getPoint().y));
					updateCursor(true);
					previousDrag = e.getPoint();
				}
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
					getDisplayOptions().setViewOrigin(new Coordinate());
					repaint();
				} else if(e.getKeyCode()==KeyEvent.VK_ENTER){
					autofitView();
				}
			}
		});
	}
	
	/**
	 * Creates the visualization popup menu.
	 *
	 * @return the j popup menu
	 */
	private JPopupMenu createVisualizationPopupMenu() {
		JPopupMenu visualizationPopupMenu = new JPopupMenu();
		JMenuItem autofitItem = new JMenuItem("Autofit View");
		autofitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				autofitView();
			}
		});
		visualizationPopupMenu.add(autofitItem);
		JMenuItem centerViewItem = new JMenuItem("Center Here");
		centerViewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				centerView(getMousePosition());
			}
		});
		visualizationPopupMenu.add(centerViewItem);
		JMenuItem zoomInItem = new JMenuItem("Zoom In");
		zoomInItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoomView(1,getMousePosition());
			}
		});
		visualizationPopupMenu.add(zoomInItem);
		JMenuItem zoomOutItem = new JMenuItem("Zoom Out");
		zoomOutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoomView(-1,getMousePosition());
			}
		});
		visualizationPopupMenu.add(zoomOutItem);
		JMenuItem displayOptionsItem = new JMenuItem("Display Options");
		displayOptionsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editDisplayOptionsCommand();
			}
		});
		visualizationPopupMenu.add(displayOptionsItem);
		return visualizationPopupMenu;
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
		nodeLayer.setBounds(x,y,width,height);
		edgeLayer.setBounds(x,y,width,height);
		regionLayer.setBounds(x,y,width,height);
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
	 * Gets the point.
	 *
	 * @param coordinate the coordinate
	 * @return the point
	 */
	public Point getPoint(Coordinate coordinate) {
		return new Point((int)(Math.round((coordinate.x-getDisplayOptions().getViewOrigin().x)*getViewScale())),
				(int)(Math.round((coordinate.y-getDisplayOptions().getViewOrigin().y)*getViewScale())));
	}
	
	/**
	 * Gets the coordinate.
	 *
	 * @param point the point
	 * @return the coordinate
	 */
	public Coordinate getCoordinate(Point point) {
		return new Coordinate(getDisplayOptions().getViewOrigin().x + point.x/getDisplayOptions().getViewScale(),
				getDisplayOptions().getViewOrigin().y + point.y/getDisplayOptions().getViewScale());
	}
	
	/**
	 * Gets the scale (kilometers per pixel).
	 *
	 * @return the scale
	 */
	public double getViewScale() {
		return getDisplayOptions().getViewScale();
	}
	
	/**
	 * Gets the position of the upper-left hand corner.
	 *
	 * @return the position
	 */
	public Coordinate getViewOrigin() {
		return getDisplayOptions().getViewOrigin();
	}
	
	/**
	 * Gets the auto scale. Scales the view to fit the entire city image (if
	 * exists).
	 *
	 * @return the auto scale
	 */
	private double getAutoScale() {
		if(city.getImage()==null) return getDisplayOptions().getViewScale();
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
		if(city.getImage()==null) return new Coordinate(-getWidth()/2/getDisplayOptions().getViewScale(),-getHeight()/2/getDisplayOptions().getViewScale());
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
		Point p = getPoint(new Coordinate(getDisplayOptions().getViewOrigin().x,getDisplayOptions().getViewOrigin().y));
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
		getDisplayOptions().getViewOrigin().x = Math.min(Math.max(getDisplayOptions().getViewOrigin().x
				+ (distance.x)/getDisplayOptions().getViewScale(), 
				-DisplayOptions.MAX_X),DisplayOptions.MAX_X-(c2.x-c1.x));
		getDisplayOptions().getViewOrigin().y = Math.min(Math.max(getDisplayOptions().getViewOrigin().y
				+ (distance.y)/getDisplayOptions().getViewScale(), 
				-DisplayOptions.MAX_Y),DisplayOptions.MAX_Y-(c2.y-c1.y));
		repaint();
	}
	
	/**
	 * Centers the view.
	 *
	 * @param location the location
	 */
	private void centerView(Point location) {
		getDisplayOptions().getViewOrigin().x = getDisplayOptions().getViewOrigin().x 
			+ (location.x - getWidth()/2)/getDisplayOptions().getViewScale();
		getDisplayOptions().getViewOrigin().y = getDisplayOptions().getViewOrigin().y 
			+ (location.y - getHeight()/2)/getDisplayOptions().getViewScale();
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
		double newViewScale = Math.max(DisplayOptions.MIN_SCALE,
				Math.min(DisplayOptions.MAX_SCALE,
						getDisplayOptions().getViewScale()*scaleFactor));
		Coordinate coordinate = getCoordinate(location);
		getDisplayOptions().getViewOrigin().x = coordinate.x 
			- (coordinate.x-getDisplayOptions().getViewOrigin().x)*getDisplayOptions().getViewScale()/newViewScale;
		getDisplayOptions().getViewOrigin().y = coordinate.y 
			- (coordinate.y-getDisplayOptions().getViewOrigin().y)*getDisplayOptions().getViewScale()/newViewScale;
		getDisplayOptions().setViewScale(newViewScale);
		repaint();
	}
	
	/**
	 * Autofit view.
	 */
	private void autofitView() {
		getDisplayOptions().setViewScale(getAutoScale());
		getDisplayOptions().setViewOrigin(getAutoPosition());
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
	
	/**
	 * Gets the selected cell region.
	 *
	 * @return the selected cell region
	 */
	public CellRegion getSelectedCellRegion() {
		return selectedCellRegion;
	}
	
	/**
	 * Sets the selected cell region.
	 *
	 * @param cellRegion the new selected cell region
	 */
	public void setSelectedCellRegion(CellRegion cellRegion) {
		this.selectedCellRegion = cellRegion;
		repaint();
	}
	
	/**
	 * Gets the selected node region.
	 *
	 * @return the selected node region
	 */
	public NodeRegion getSelectedNodeRegion() {
		return selectedNodeRegion;
	}
	
	/**
	 * Sets the selected node region.
	 *
	 * @param nodeRegion the new selected node region
	 */
	public void setSelectedNodeRegion(NodeRegion nodeRegion) {
		this.selectedNodeRegion = nodeRegion;
		repaint();
	}
	
	/**
	 * Gets the selected edge region.
	 *
	 * @return the selected edge region
	 */
	public EdgeRegion getSelectedEdgeRegion() {
		return selectedEdgeRegion;
	}
	
	/**
	 * Sets the selected edge region.
	 *
	 * @param edgeRegion the new selected edge region
	 */
	public void setSelectedEdgeRegion(EdgeRegion edgeRegion) {
		this.selectedEdgeRegion = edgeRegion;
		repaint();
	}
	
	/**
	 * Gets the selected region.
	 *
	 * @return the selected region
	 */
	public Region getSelectedRegion() {
		return selectedRegion;
	}
	
	/**
	 * Sets the selected region.
	 *
	 * @param edgeRegion the new selected region
	 */
	public void setSelectedRegion(Region region) {
		this.selectedRegion = region;
		repaint();
	}
	
	/**
	 * Edits the display options command.
	 */
	public void editDisplayOptionsCommand() {
		System.out.println("Edit Display Options Command");
		displayOptionsPanel.loadDisplayOptions(getDisplayOptions());
		int value = JOptionPane.showConfirmDialog(this,displayOptionsPanel,"City.Net | Display Options", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			displayOptionsPanel.saveDisplayOptionsCommand();
			repaint();
		}
	}
}
