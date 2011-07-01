package edu.mit.citynet.core;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * The EdgeRegion class specifies a spatial area over which edges should be
 * generated. There are several types of regions, including:
 * POLYLINE_PERIMETER (link edges between adjacent vertices),
 * ORTHOGONAL_NEIGHBORS (link edges between all orthogonal nodes inside
 * region, assuming square grid spacing), ALL_NEIGHBORS (link edges between
 * all nodes, orthogonal and diagonal, inside region, assuming square grid
 * spacing), FULLY_CONNECTED (link edges between all nodes contained within
 * region.
 * 
 * The EdgeRegion class was created to be able to specify edges without
 * relying on cellular definitions.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class EdgeRegion extends AbstractRegion {
	/**
	 * The NodeRegionType enumeration. Defines the set of possible regions that
	 * can be specified, each with differing node generation behaviors.
	 */
	public enum EdgeRegionType {
		UNDEFINED,POLYGON_ORTHOGONAL,POLYGON_ADJACENT,POLYGON_CONNECTED,
		POLYLINE,POLYPOINT;
	}
	private int id;
	private EdgeRegionType edgeRegionType;
	private Vector<Layer> layers;
	private EdgeType edgeType;
	private boolean directed;
	private String description;
	
	/**
	 * Instantiates a new edge region.
	 */
	public EdgeRegion() {
		layers = new Vector<Layer>();
	}
	
	public Set<Edge> generateEdges(System system) {
		//TODO
		return new HashSet<Edge>();
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
	 * Gets the edge region type.
	 *
	 * @return the edge region type
	 */
	public EdgeRegionType getEdgeRegionType() {
		return edgeRegionType;
	}

	/**
	 * Sets the edge region type.
	 *
	 * @param edgeRegionType the new edge region type
	 */
	public void setEdgeRegionType(EdgeRegionType edgeRegionType) {
		this.edgeRegionType = edgeRegionType;
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
	 * Gets the layers.
	 *
	 * @return the layers
	 */
	public Vector<Layer> getLayers() {
		return layers;
	}

	/**
	 * Sets the layers.
	 *
	 * @param layers the new layers
	 */
	public void setLayers(Vector<Layer> layers) {
		this.layers = layers;
	}
}
