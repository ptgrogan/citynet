package edu.mit.citynet.viz;

import java.util.Set;

import javax.swing.JSplitPane;

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
public abstract class AbstractVizPanel extends JSplitPane {
	private static final long serialVersionUID = 3664632230067588258L;

	/**
	 * Gets the set of checked cell regions.
	 *
	 * @return the checked cell regions
	 */
	public abstract Set<CellRegion> getCheckedCellRegions();
	
	/**
	 * Gets the set of checked node regions.
	 *
	 * @return the checked node regions
	 */
	public abstract Set<NodeRegion> getCheckedNodeRegions();
	
	/**
	 * Gets the set of checked edge regions.
	 *
	 * @return the checked edge regions
	 */
	public abstract Set<EdgeRegion> getCheckedEdgeRegions();
	
	/**
	 * Gets the selected cell regions.
	 *
	 * @return the selected cell regions
	 */
	public abstract Set<CellRegion> getSelectedCellRegions();
	
	/**
	 * Gets the selected node regions.
	 *
	 * @return the selected node regions
	 */
	public abstract Set<NodeRegion> getSelectedNodeRegions();
	
	/**
	 * Gets the selected edge regions.
	 *
	 * @return the selected edge regions
	 */
	public abstract Set<EdgeRegion> getSelectedEdgeRegions();
}
