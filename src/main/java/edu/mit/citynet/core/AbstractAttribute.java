/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.core;

/**
 * The AbstractAttribute class is used to describe an aspect of a particular 
 * object. It serves as a superclass to both NodeTypeAttribute and 
 * EdgeTypeAttribute.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public abstract class AbstractAttribute {
	private String name, description, units, bounds;
	private double value;

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the units.
	 *
	 * @return the units
	 */
	public String getUnits() {
		return units;
	}

	/**
	 * Sets the units.
	 *
	 * @param units the new units
	 */
	public void setUnits(String units) {
		this.units = units;
	}

	/**
	 * Gets the bounds.
	 *
	 * @return the bounds
	 */
	public String getBounds() {
		return bounds;
	}

	/**
	 * Sets the bounds.
	 *
	 * @param bounds the new bounds
	 */
	public void setBounds(String bounds) {
		this.bounds = bounds;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(double value) {
		this.value = value;
	}
}
