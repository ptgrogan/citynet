/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.core;

/**
 * The NodeTypeAttribute class is used to describe an aspect of a particular 
 * EdgeType. Each EdgeTypeAttribute is given a name and numeric value.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class EdgeTypeAttribute extends AbstractAttribute implements Cloneable {
	private int id;
	
	/**
	 * Instantiates a new edge type attribute.
	 */
	public EdgeTypeAttribute() {
		setName("New Edge Type Attribute");
		setUnits("-");
		setBounds("[0,inf)");
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if(object instanceof EdgeTypeAttribute) {
			return id==((EdgeTypeAttribute)object).getId();
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "EdgeTypeAttribute" + id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public EdgeTypeAttribute clone() {
		EdgeTypeAttribute clone = new EdgeTypeAttribute();
		clone.setId(0);
		clone.setName(getName() + " (Copy)");
		clone.setDescription(getDescription());
		clone.setUnits(getUnits());
		clone.setBounds(getBounds());
		clone.setValue(getValue());
		return clone;
	}
}