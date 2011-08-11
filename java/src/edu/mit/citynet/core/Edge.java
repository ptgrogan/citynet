/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.core;

import com.vividsolutions.jts.geom.LineSegment;

/**
 * The Edge class is used to define connections between nodes within a system.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class Edge {
	private int id;
	private Node origin, destination;
	private EdgeType edgeType;
	private boolean directed;
	
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
	 * Gets the origin.
	 *
	 * @return the origin
	 */
	public Node getOrigin() {
		return origin;
	}
	
	/**
	 * Sets the origin.
	 *
	 * @param origin the new origin
	 */
	public void setOrigin(Node origin) {
		this.origin = origin;
	}
	
	/**
	 * Gets the destination.
	 *
	 * @return the destination
	 */
	public Node getDestination() {
		return destination;
	}
	
	/**
	 * Sets the destination.
	 *
	 * @param destination the new destination
	 */
	public void setDestination(Node destination) {
		this.destination = destination;
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
	 * Checks if is directed.
	 *
	 * @return true, if is directed
	 */
	public boolean isDirected() {
		return directed;
	}
	
	/**
	 * Sets the directed.
	 *
	 * @param directed the new directed
	 */
	public void setDirected(boolean directed) {
		this.directed = directed;
	}
	
	/**
	 * Gets the Euclidean length.
	 *
	 * @return the Euclidean length
	 */
	public double getEuclideanLength() {
		if(origin != null && destination != null) {
			LineSegment ls = new LineSegment(
					origin.getCell().getPolygon().getCentroid().getCoordinate(),
					destination.getCell().getPolygon().getCentroid().getCoordinate());
			return ls.getLength();
		} else return 0;
	}
	
	/**
	 * Gets the edge type attribute value.
	 *
	 * @param attributeName the attribute name
	 * @return the edge type attribute value
	 */
	public Double getEdgeTypeAttributeValue(String attributeName) {
		for(EdgeTypeAttribute attribute : edgeType.getAttributes()) {
			if(attribute.getName().equals(attributeName))
				return attribute.getValue();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if(object instanceof Edge) {
			return id==((Edge)object).getId();
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Edge" + id;
	}
}
