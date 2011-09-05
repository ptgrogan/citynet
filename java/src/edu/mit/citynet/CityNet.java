/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.infonode.gui.laf.InfoNodeLookAndFeel;
import net.infonode.gui.laf.InfoNodeLookAndFeelThemes;

import com.vividsolutions.jts.geom.GeometryFactory;

import edu.mit.citynet.core.Cell;
import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.Edge;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.EdgeType;
import edu.mit.citynet.core.EdgeTypeAttribute;
import edu.mit.citynet.core.Layer;
import edu.mit.citynet.core.Node;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.core.NodeType;
import edu.mit.citynet.core.NodeTypeAttribute;
import edu.mit.citynet.core.Region;
import edu.mit.citynet.gui.CityNetFrame;
import edu.mit.citynet.util.DistanceUnit;
import edu.mit.citynet.viz.DisplayOptions;

/**
 * Synthesis template class.
 * 
 * Launches the City.Net application and stores program-wide options and 
 * settings.
 * 
 * Uses singleton design pattern to ensures only one instance is created.
 * 
 * Implements Bill Pugh's "double-checked locking" solution for thread-safe 
 * use. See http://en.wikipedia.org/wiki/Double-checked_locking for more 
 * information.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CityNet {
	private DistanceUnit distanceUnit;
	private City city;
	private DisplayOptions displayOptions;
	private AtomicInteger nextNodeTypeId,nextNodeTypeAttributeId,
		nextEdgeTypeId,nextEdgeTypeAttributeId,nextCellId,nextCellRegionId,
		nextLayerId,nextSystemId,nextNodeId,nextNodeRegionId,nextEdgeId,
		nextEdgeRegionId, nextRegionId;
	private GeometryFactory geometryFactory;
	
	/**
	 * Instantiates a new city net.
	 */
	private CityNet() { 
		distanceUnit = DistanceUnit.KILOMETERS;
		displayOptions = new DisplayOptions();
		nextNodeTypeId = new AtomicInteger();
		nextNodeTypeAttributeId = new AtomicInteger();
		nextEdgeTypeId = new AtomicInteger();
		nextEdgeTypeAttributeId = new AtomicInteger();
		nextCellId = new AtomicInteger();
		nextCellRegionId = new AtomicInteger();
		nextLayerId = new AtomicInteger();
		nextSystemId = new AtomicInteger();
		nextNodeId = new AtomicInteger();
		nextNodeRegionId = new AtomicInteger();
		nextEdgeId = new AtomicInteger();
		nextEdgeRegionId = new AtomicInteger();
		nextRegionId = new AtomicInteger();
		geometryFactory = new GeometryFactory();
	}
	
	/**
	 * The Class SingletonHolder.
	 */
	private static class SingletonHolder { 
		public static final CityNet INSTANCE = new CityNet();
	}
	
	/**
	 * Gets the display options.
	 *
	 * @return the display options
	 */
	public DisplayOptions getDisplayOptions() {
		return displayOptions;
	}
	
	/**
	 * Gets the single instance of SynthesisTemplate.
	 *
	 * @return single instance of SynthesisTemplate
	 */
	public static CityNet getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	/**
	 * Geometry factory.
	 */
	public GeometryFactory getGeometryFactory() {
		return geometryFactory;
	}
	
	/**
	 * Gets the distance unit.
	 *
	 * @return the distance unit
	 */
	public DistanceUnit getDistanceUnit() {
		return distanceUnit;
	}
	
	/**
	 * Sets the distance unit.
	 *
	 * @param distanceUnit the new distance unit
	 */
	public void setDistanceUnit(DistanceUnit distanceUnit) { 
		this.distanceUnit = distanceUnit;
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
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(City city) { 
		this.city = city;
		updateIds();
	}
	
	/**
	 * Gets the next node type id.
	 *
	 * @return the next node type id
	 */
	public int getNextNodeTypeId() { 
		return nextNodeTypeId.incrementAndGet();
	}
	
	/**
	 * Gets the next node type attribute id.
	 *
	 * @return the next node type attribute id
	 */
	public int getNextNodeTypeAttributeId() { 
		return nextNodeTypeAttributeId.incrementAndGet();
	}
	
	/**
	 * Gets the next edge type id.
	 *
	 * @return the next edge type id
	 */
	public int getNextEdgeTypeId() { 
		return nextEdgeTypeId.incrementAndGet();
	}
	
	/**
	 * Gets the next edge type attribute id.
	 *
	 * @return the next edge type attribute id
	 */
	public int getNextEdgeTypeAttributeId() { 
		return nextEdgeTypeAttributeId.incrementAndGet();
	}
	
	/**
	 * Gets the next cell id.
	 *
	 * @return the next cell id
	 */
	public int getNextCellId() { 
		return nextCellId.incrementAndGet();
	}
	
	/**
	 * Gets the next cell region id.
	 *
	 * @return the next cell region id
	 */
	public int getNextCellRegionId() { 
		return nextCellRegionId.incrementAndGet();
	}
	
	/**
	 * Gets the next layer id.
	 *
	 * @return the next layer id
	 */
	public int getNextLayerId() { 
		return nextLayerId.incrementAndGet();
	}
	
	/**
	 * Gets the next system id.
	 *
	 * @return the next system id
	 */
	public int getNextSystemId() { 
		return nextSystemId.incrementAndGet();
	}
	
	/**
	 * Gets the next node id.
	 *
	 * @return the next node id
	 */
	public int getNextNodeId() { 
		return nextNodeId.incrementAndGet();
	}
	
	/**
	 * Gets the next node region id.
	 *
	 * @return the next node region id
	 */
	public int getNextNodeRegionId() { 
		return nextNodeRegionId.incrementAndGet();
	}
	
	/**
	 * Gets the next edge id.
	 *
	 * @return the next edge id
	 */
	public int getNextEdgeId() { 
		return nextEdgeId.incrementAndGet();
	}
	
	/**
	 * Gets the next edge region id.
	 *
	 * @return the next edge region id
	 */
	public int getNextEdgeRegionId() { 
		return nextEdgeRegionId.incrementAndGet();
	}
	
	/**
	 * Gets the next region id.
	 *
	 * @return the next region id
	 */
	public int getNextRegionId() {
		return nextRegionId.incrementAndGet();
	}
	
	/**
	 * Update ids, used after loading a city from a template.
	 */
	public void updateIds() {
		nextCellId.set(1);
		nextCellRegionId.set(1);
		nextSystemId.set(1);
		nextNodeTypeId.set(1);
		nextNodeTypeAttributeId.set(1);
		nextEdgeTypeId.set(1);
		nextEdgeTypeAttributeId.set(1);
		nextLayerId.set(1);
		nextNodeId.set(1);
		nextEdgeId.set(1);
		nextNodeRegionId.set(1);
		nextEdgeRegionId.set(1);
		nextRegionId.set(1);
		if(city == null) return;
		for(Cell cell : city.getCells()) {
			nextCellId.set(Math.max(nextCellId.get(), cell.getId()+1));
		}
		for(CellRegion cellRegion : city.getCellRegions()) {
			nextCellRegionId.set(Math.max(nextCellRegionId.get(), cellRegion.getId()+1));
		}
		for(CitySystem system : city.getSystems()) {
			nextSystemId.set(Math.max(nextSystemId.get(), system.getId()+1));
			for(NodeType nodeType : system.getNodeTypes()) {
				nextNodeTypeId.set(Math.max(nextNodeTypeId.get(), nodeType.getId()+1));
				for(NodeTypeAttribute nodeTypeAttrib : nodeType.getAttributes()) {
					nextNodeTypeAttributeId.set(Math.max(nextNodeTypeAttributeId.get(), nodeTypeAttrib.getId()+1));
				}
			}
			for(EdgeType edgeType : system.getEdgeTypes()) {
				nextEdgeTypeId.set(Math.max(nextEdgeTypeId.get(), edgeType.getId()+1));
				for(EdgeTypeAttribute edgeTypeAttrib : edgeType.getAttributes()) {
					nextEdgeTypeAttributeId.set(Math.max(nextEdgeTypeAttributeId.get(), edgeTypeAttrib.getId()+1));
				}
			}
			for(Layer layer : system.getLayers()) {
				nextLayerId.set(Math.max(nextLayerId.get(), layer.getId()+1));
			}
			for(Node node : system.getNodes()) {
				nextNodeId.set(Math.max(nextNodeId.get(), node.getId()+1));
			}
			for(Edge edge : system.getEdges()) {
				nextEdgeId.set(Math.max(nextEdgeId.get(), edge.getId()+1));
			}
			for(NodeRegion nodeRegion : system.getNodeRegions()) {
				nextNodeRegionId.set(Math.max(nextNodeRegionId.get(), nodeRegion.getId()+1));
			}
			for(EdgeRegion edgeRegion : system.getEdgeRegions()) {
				nextEdgeRegionId.set(Math.max(nextEdgeRegionId.get(), edgeRegion.getId()+1));
			}
			for(Region region : system.getRegions()) {
				nextRegionId.set(Math.max(nextRegionId.get(), region.getId()+1));
			}
		}
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// set default unhandled exception handler
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				JPanel errorPanel = new JPanel(new BorderLayout());
				errorPanel.add(new JLabel("<html><p>An exception of type " + e.getClass().getSimpleName() 
			    		+ " occurred.</p><p>" + e.getClass() + ": " + e.getMessage() 
			    		+ "</p><p>Stack trace (for debug purposes):</p></html>"), BorderLayout.NORTH);
				errorPanel.add(new JScrollPane(new JList(e.getStackTrace())), BorderLayout.CENTER);
			    JOptionPane.showMessageDialog(findActiveFrame(), errorPanel, 
			    		"CityNet | Error", JOptionPane.ERROR_MESSAGE);
			    	e.printStackTrace();
		    }
		});
		// use a separate thread for the GUI
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
					UIManager.setLookAndFeel(new InfoNodeLookAndFeel(
							InfoNodeLookAndFeelThemes.getSoftGrayTheme()));
            	} catch(UnsupportedLookAndFeelException e) {
            		JOptionPane.showMessageDialog(findActiveFrame(), 
            				"The look and feel could not be loaded." +
            				"\nPlease consult the stack " + 
            				"trace for more information.",
    			    		"CityNet | Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
				System.out.println("Launching City.Net...");
				CityNetFrame f = new CityNetFrame();
				f.setSize(new Dimension(800,600));
				f.setLocationRelativeTo(null);
				f.setVisible(true);
            }
		});
	}
	private static Frame findActiveFrame() {
		for(Frame frame : JFrame.getFrames()) {
			if(frame.isVisible()) return frame;
		}
		return null;
	}
}