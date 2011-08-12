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
	
	/**
	 * Instantiates a new display options.
	 */
	public DisplayOptions() {
		gridDisplayed = true;
		mapDisplayed = true;
		gridSpacing = 1;
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
