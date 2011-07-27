package edu.mit.citynet.viz;

import java.util.Set;

import javax.swing.JPanel;

import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.NodeRegion;

/**
 * The AbstractVizPanel class provides superclass methods to abstract
 * the selection of cell, node, and edge regions between the city viz
 * panel and the system viz panel.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public abstract class AbstractVizPanel extends JPanel {
	private static final long serialVersionUID = 3664632230067588258L;

	/**
	 * Gets the set of selected cell regions.
	 *
	 * @return the selected cell regions
	 */
	public abstract Set<CellRegion> getSelectedCellRegions();
	
	/**
	 * Gets the set of selected node regions.
	 *
	 * @return the selected node regions
	 */
	public abstract Set<NodeRegion> getSelectedNodeRegions();
	
	/**
	 * Gets the set of selected edge regions.
	 *
	 * @return the selected edge regions
	 */
	public abstract Set<EdgeRegion> getSelectedEdgeRegions();
}
