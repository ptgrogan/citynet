package edu.mit.citynet;

import java.awt.Dimension;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.infonode.gui.laf.InfoNodeLookAndFeel;

import edu.mit.citynet.core.City;
import edu.mit.citynet.gui.CityNetFrame;
import edu.mit.citynet.util.DistanceUnit;

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
	private DistanceUnit _distanceUnit;
	private City _city;
	private AtomicInteger nextNodeTypeId,nextNodeTypeAttributeId,
		nextEdgeTypeId,nextEdgeTypeAttributeId,nextCellId,nextCellRegionId,
		nextLayerId,nextSystemId,nextNodeId,nextNodeRegionId,nextEdgeId,
		nextEdgeRegionId;
	
	private CityNet() { 
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
	}
	private static class SingletonHolder { 
		public static final CityNet INSTANCE
			= new CityNet();
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
	 * Gets the distance unit.
	 *
	 * @return the distance unit
	 */
	public DistanceUnit getDistanceUnit() {
		return _distanceUnit;
	}
	
	/**
	 * Sets the distance unit.
	 *
	 * @param distanceUnit the new distance unit
	 */
	public void setDistanceUnit(DistanceUnit distanceUnit) { 
		_distanceUnit = distanceUnit;
	}
	
	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public City getCity() { 
		return _city;
	}
	
	/**
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(City city) { 
		_city = city;
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
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// use a separate thread for the GUI
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
					UIManager.setLookAndFeel(new InfoNodeLookAndFeel());
					System.out.println("Launching City.Net...");
					CityNetFrame f = new CityNetFrame();
					f.setSize(new Dimension(600,400));
					f.setLocationRelativeTo(null);
					f.setVisible(true);
            	} catch(Exception e) {
            		JOptionPane.showMessageDialog(null, 
            				"A fatal exception of type " + 
            				e.getClass().getSimpleName() + "occurred while " + 
            				"launching City.Net.\nPlease consult the stack " + 
            				"trace for more information.");
					e.printStackTrace();
				}
            }
		});
	}
}