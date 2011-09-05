/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.core;

import javax.swing.ImageIcon;

import edu.mit.citynet.util.CityNetIcon;

/**
 * The EdgeGenerationType enumeration provides the types of edge generation 
 * methods within regions.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public enum EdgeGenerationType {
	NONE("None","Does not generate edges"),
	ORTHOGONAL("Orthogonal","Generates edges within region between orthogonally adjacent nodes"),
	ADJACENT("Adjacent","Generates edges within region between adjacent nodes"),
	CONNECTED("Connected","Generates edges within region between all nodes"),
	SEQUENTIAL("Sequential","Generates edges between sequential points");
	
	private String name, description;
	
	/**
	 * Instantiates a new edge generation type.
	 *
	 * @param name the name
	 */
	private EdgeGenerationType(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return getName();
	}
	
	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public ImageIcon getIcon() {
		switch(this) {
		// TODO: differentiate icons
		case ORTHOGONAL: return CityNetIcon.POLYGON_REGION.getIcon();
		case ADJACENT: return CityNetIcon.POLYGON_REGION.getIcon();
		case CONNECTED: return CityNetIcon.POLYGON_REGION.getIcon();
		case SEQUENTIAL: return CityNetIcon.POLYPOINT_REGION.getIcon();
		default: return CityNetIcon.UNDEFINED_REGION.getIcon();
		}
	}
	
	/**
	 * Gets the edge generation type.
	 *
	 * @param name the name
	 * @return the edge generation type
	 */
	public static EdgeGenerationType getEdgeGenerationType(String name) {
		for(EdgeGenerationType t : EdgeGenerationType.values()) {
			if(t.getName().toLowerCase().equals(name.toLowerCase())) return t;
		}
		return NONE;
	}
}
