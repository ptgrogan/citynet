/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.core;

import javax.swing.ImageIcon;

import edu.mit.citynet.util.CityNetIcon;

/**
 * The NodeGenerationType enumeration provides the types of node generation 
 * methods within regions.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public enum NodeGenerationType {
	NONE("None","Does not generate nodes"),
	POLYGON("Polygon","Generates nodes at cells within a polygonal region"),
	POLYLINE("Polyline","Generates nodes at cells intersecting a polyline"),
	POLYPOINT("Polypoint","Generates nodes at cells containing points");

	private String name, description;
	
	/**
	 * Instantiates a new node generation type.
	 *
	 * @param name the name
	 * @param description the description
	 */
	private NodeGenerationType(String name, String description) {
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
		case POLYGON: return CityNetIcon.POLYGON_REGION.getIcon();
		case POLYLINE: return CityNetIcon.POLYLINE_REGION.getIcon();
		case POLYPOINT: return CityNetIcon.POLYPOINT_REGION.getIcon();
		default: return CityNetIcon.UNDEFINED_REGION.getIcon();
		}
	}
	
	/**
	 * Gets the node generation type.
	 *
	 * @param name the name
	 * @return the node generation type
	 */
	public static NodeGenerationType getNodeGenerationType(String name) {
		for(NodeGenerationType t : NodeGenerationType.values()) {
			if(t.getName().toLowerCase().equals(name.toLowerCase())) return t;
		}
		return NONE;
	}
}
