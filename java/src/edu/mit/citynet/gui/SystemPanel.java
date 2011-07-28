package edu.mit.citynet.gui;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.Edge;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.Node;
import edu.mit.citynet.core.NodeRegion;

/**
 * The SystemPanel class is an abstract panel to display system information. 
 * It is used as a common base class for the SystemPanelViz and the editor.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public abstract class SystemPanel extends JPanel {
	private static final long serialVersionUID = -2586548029906436001L;

	protected CityPanel cityPanel;
	protected CitySystem system;
	
	/**
	 * Instantiates a new system panel.
	 *
	 * @param cityPanel the city panel
	 * @param system the system
	 */
	public SystemPanel(CityPanel cityPanel, CitySystem system) {
		if (system==null) {
			throw new IllegalArgumentException("System cannot be null.");
		}
		this.cityPanel = cityPanel;
		this.system = system;
	}
	
	/**
	 * Gets the city panel.
	 *
	 * @return the city panel
	 */
	public CityPanel getCityPanel() {
		return cityPanel;
	}
	
	/**
	 * Gets the system.
	 *
	 * @return the system
	 */
	public CitySystem getSystem() {
		return system;
	}

	/**
	 * Clear nodes command.
	 */
	public void clearNodesCommand() {
		System.out.println("Clear Nodes Command");
		if(!system.getEdges().isEmpty())
			clearEdgesCommand();
		system.setNodes(new HashSet<Node>());
	}
	
	/**
	 * Generate nodes command.
	 *
	 * @param nodeRegions the node regions
	 */
	public void generateNodesCommand(Set<NodeRegion> nodeRegions) {
		System.out.println("Generate Nodes Command");
		if(!system.getNodes().isEmpty())
			clearNodesCommand();
		if(!system.getEdges().isEmpty())
			clearEdgesCommand();
		for(NodeRegion r : nodeRegions) {
			r.generateNodes(system);
		}
	}
	
	/**
	 * Clear edges command.
	 */
	public void clearEdgesCommand() {
		System.out.println("Clear Edges Command");
		system.setEdges(new HashSet<Edge>());
	}
	
	/**
	 * Generate edges command.
	 *
	 * @param edgeRegions the edge regions
	 */
	public void generateEdgesCommand(Set<EdgeRegion> edgeRegions) {
		System.out.println("Generate Edges Command");
		if(!system.getEdges().isEmpty())
			clearEdgesCommand();
		for(EdgeRegion r : edgeRegions) {
			r.generateEdges(system);
		}
	}
}