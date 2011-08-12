/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.viz;

/**
 * The DisplayOptions class.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class DisplayOptions {
	private boolean gridDisplayed, mapDisplayed;
	private double gridSpacing;
	private float cellRegionOpacity, cellOpacity, nodeRegionOpacity, 
		nodeOpacity, edgeRegionOpacity, edgeOpacity;
	
	/**
	 * Instantiates a new display options.
	 */
	public DisplayOptions() {
		gridDisplayed = true;
		mapDisplayed = true;
		gridSpacing = 1;
		cellRegionOpacity = 0.5f;
		cellOpacity = 0.5f;
		nodeRegionOpacity = 0.5f;
		nodeOpacity = 0.5f;
		edgeRegionOpacity = 0.5f;
		edgeOpacity = 0.5f;
	}
	
	public float getCellOpacity() {
		return cellOpacity;
	}

	public void setCellOpacity(float cellOpacity) {
		this.cellOpacity = cellOpacity;
	}

	public float getNodeRegionOpacity() {
		return nodeRegionOpacity;
	}

	public void setNodeRegionOpacity(float nodeRegionOpacity) {
		this.nodeRegionOpacity = nodeRegionOpacity;
	}

	public float getNodeOpacity() {
		return nodeOpacity;
	}

	public void setNodeOpacity(float nodeOpacity) {
		this.nodeOpacity = nodeOpacity;
	}

	public float getEdgeRegionOpacity() {
		return edgeRegionOpacity;
	}

	public void setEdgeRegionOpacity(float edgeRegionOpacity) {
		this.edgeRegionOpacity = edgeRegionOpacity;
	}

	public float getEdgeOpacity() {
		return edgeOpacity;
	}

	public void setEdgeOpacity(float edgeOpacity) {
		this.edgeOpacity = edgeOpacity;
	}
	
	public float getCellRegionOpacity() {
		return cellRegionOpacity;
	}

	public void setCellRegionOpacity(float cellRegionOpacity) {
		this.cellRegionOpacity = cellRegionOpacity;
	}

	public double getGridSpacing() {
		return gridSpacing;
	}

	public void setGridSpacing(double gridSpacing) {
		this.gridSpacing = gridSpacing;
	}

	public boolean isGridDisplayed() {
		return gridDisplayed;
	}

	public void setGridDisplayed(boolean gridDisplayed) {
		this.gridDisplayed = gridDisplayed;
	}

	public boolean isMapDisplayed() {
		return mapDisplayed;
	}

	public void setMapDisplayed(boolean mapDisplayed) {
		this.mapDisplayed = mapDisplayed;
	}
}
