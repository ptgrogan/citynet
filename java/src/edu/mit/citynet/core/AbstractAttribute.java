package edu.mit.citynet.core;

/**
 * The AbstractAttribute class is used to describe an aspect of a particular 
 * object. It serves as a superclass to both NodeTypeAttribute and 
 * EdgeTypeAttribute.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public abstract class AbstractAttribute {
	private String _name, _description, _units, _bounds;
	private double _value;

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		_name = name;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return _description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		_description = description;
	}

	/**
	 * Gets the units.
	 *
	 * @return the units
	 */
	public String getUnits() {
		return _units;
	}

	/**
	 * Sets the units.
	 *
	 * @param units the new units
	 */
	public void setUnits(String units) {
		_units = units;
	}

	/**
	 * Gets the bounds.
	 *
	 * @return the bounds
	 */
	public String getBounds() {
		return _bounds;
	}

	/**
	 * Sets the bounds.
	 *
	 * @param bounds the new bounds
	 */
	public void setBounds(String bounds) {
		_bounds = bounds;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public double getValue() {
		return _value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(double value) {
		_value = value;
	}
}
