/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.core;

import javax.swing.ImageIcon;

import edu.mit.citynet.util.CityNetIcon;

// TODO: Auto-generated Javadoc
/**
 * The Enum EdgeDirection.
 */
public enum EdgeDirection {
	
	/** The DIRECTED. */
	DIRECTED("Directed"), 
 /** The UNDIRECTED. */
 UNDIRECTED("Undirected");
	
	private String name;
	
	/**
	 * Instantiates a new edge direction.
	 *
	 * @param name the name
	 */
	private EdgeDirection(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public ImageIcon getIcon() {
		if(this==DIRECTED) return CityNetIcon.DIRECTED.getIcon();
		else return CityNetIcon.UNDIRECTED.getIcon();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return name;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
