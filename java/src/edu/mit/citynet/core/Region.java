package edu.mit.citynet.core;



/**
 * The Region class unifies previous concepts of node and edge regions
 * to generate nodes and edges within a system.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public abstract class Region extends AbstractRegion implements Cloneable {
	private int id;
	private EdgeType edgeType;
	private EdgeDirection edgeDirection;
	
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

	/**
	 * Gets the edge type.
	 *
	 * @return the edge type
	 */
	public EdgeType getEdgeType() {
		return edgeType;
	}

	/**
	 * Sets the edge type.
	 *
	 * @param edgeType the new edge type
	 */
	public void setEdgeType(EdgeType edgeType) {
		this.edgeType = edgeType;
	}

	/**
	 * Gets the edge direction.
	 *
	 * @return the edge direction
	 */
	public EdgeDirection getEdgeDirection() {
		return edgeDirection;
	}

	/**
	 * Sets the edge direction.
	 *
	 * @param edgeDirection the new edge direction
	 */
	public void setEdgeDirection(EdgeDirection edgeDirection) {
		this.edgeDirection = edgeDirection;
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