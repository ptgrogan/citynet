package edu.mit.citynet.core;

/**
 * The NodeTypeAttribute class is used to describe an aspect of a particular 
 * NodeType. Each NodeTypeAttribute is given a name and numeric value.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class NodeTypeAttribute extends AbstractAttribute {
	private int _id;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return _id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		_id = id;
	}
}