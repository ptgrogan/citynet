/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.viz;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * The DisplayOptions class.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class DisplayOptions {
	public static final double MIN_SCALE = 10, MAX_SCALE = 1000, MAX_X = 50, MAX_Y = 50;
	private double viewScale;
	private Coordinate viewOrigin;
	private boolean gridDisplayed, mapDisplayed, layersFiltered;
	private double gridSpacing, displayHeight;
	private float cellRegionOpacity, cellOpacity, nodeRegionOpacity, 
		nodeOpacity, edgeRegionOpacity, edgeOpacity, regionOpacity;
	
	/**
	 * Instantiates a new display options.
	 */
	public DisplayOptions() {
		gridDisplayed = true;
		mapDisplayed = true;
		layersFiltered = false;
		displayHeight = 0d;
		gridSpacing = 1;
		cellRegionOpacity = 0.5f;
		cellOpacity = 0.5f;
		nodeRegionOpacity = 0.5f;
		nodeOpacity = 0.5f;
		edgeRegionOpacity = 0.5f;
		edgeOpacity = 0.5f;
		regionOpacity = 0.5f;
		viewScale = 100d;	// default: 100 px/km
		viewOrigin = new Coordinate();
	}
	
	public double getViewScale() {
		return viewScale;
	}

	public void setViewScale(double viewScale) {
		this.viewScale = viewScale;
	}

	public Coordinate getViewOrigin() {
		return viewOrigin;
	}

	public void setViewOrigin(Coordinate viewOrigin) {
		this.viewOrigin = viewOrigin;
	}

	public boolean isLayersFiltered() {
		return layersFiltered;
	}

	public void setLayersFiltered(boolean layersFiltered) {
		this.layersFiltered = layersFiltered;
	}

	public double getDisplayHeight() {
		return displayHeight;
	}

	public void setDisplayHeight(double displayHeight) {
		this.displayHeight = displayHeight;
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

	public float getRegionOpacity() {
		return regionOpacity;
	}

	public void setRegionOpacity(float regionOpacity) {
		this.regionOpacity = regionOpacity;
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
