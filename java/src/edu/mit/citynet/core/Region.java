package edu.mit.citynet.core;



/**
 * The Region class unifies previous concepts of node and edge regions
 * to generate nodes and edges within a system.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public abstract class Region extends AbstractRegion implements Cloneable {
	private int id;
	
	/**
	 * Instantiates a new region.
	 */
	public Region() {
		super();
		setDescription("New Region");
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
	 * @see java.lang.Object#clone()
	 */
	public abstract Region clone();
	
	/**
	 * Generate region.
	 *
	 * @param system the system
	 */
	public abstract void generateRegion(CitySystem system);
}