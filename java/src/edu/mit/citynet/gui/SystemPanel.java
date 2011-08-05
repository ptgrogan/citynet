package edu.mit.citynet.gui;

import java.awt.BorderLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.Edge;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.Node;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.viz.SystemVizPanel;

/**
 * The SystemPanel class is a panel to display system information.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class SystemPanel extends JPanel {
	private static final long serialVersionUID = -2586548029906436001L;

	private CityPanel cityPanel;
	private CitySystem system;
	private SystemVizPanel systemVizPanel;
	
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
		initializePanel();
	}

	/**
	 * Initializes the panel.
	 */
	private void initializePanel() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		systemVizPanel = new SystemVizPanel(this, system);
		add(systemVizPanel,BorderLayout.CENTER);
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
