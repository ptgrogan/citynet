package edu.mit.citynet.core;

import java.util.HashSet;
import java.util.Set;

/**
 * The NodeRegion class describes nodes that will be generated at cells over a 
 * region of space. It was created to be able to specify nodes without relying
 * on cellular definitions.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class NodeRegion extends AbstractRegion {
	
	/**
	 * The NodeRegionType enumeration. Defines the set of possible regions that
	 * can be specified, each with differing node generation behaviors.
	 */
	public enum NodeRegionType {
		UNDEFINED,POLYGON,POLYLINE,POLYPOINT;
	}
	private int id;
	private NodeRegionType nodeRegionType;
	private Layer layer;
	private NodeType nodeType;
	private String description;
	
	/**
	 * Instantiates a new node region.
	 */
	public NodeRegion() {
		
	}
	
	/**
	 * Generate nodes.
	 *
	 * @param system the system
	 * @return the set of generated nodes
	 */
	public Set<Node> generateNodes(System system) {
		//TODO
		return new HashSet<Node>();
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
	 * Gets the node region type.
	 *
	 * @return the node region type
	 */
	public NodeRegionType getNodeRegionType() {
		return nodeRegionType;
	}

	/**
	 * Sets the node region type.
	 *
	 * @param nodeRegiontype the new node region type
	 */
	public void setNodeRegionType(NodeRegionType nodeRegionType) {
		this.nodeRegionType = nodeRegionType;
	}

	/**
	 * Gets the layer.
	 *
	 * @return the layer
	 */
	public Layer getLayer() {
		return layer;
	}

	/**
	 * Sets the layer.
	 *
	 * @param layer the new layer
	 */
	public void setLayer(Layer layer) {
		this.layer = layer;
	}

	/**
	 * Gets the node type.
	 *
	 * @return the node type
	 */
	public NodeType getNodeType() {
		return nodeType;
	}

	/**
	 * Sets the node type.
	 *
	 * @param nodeType the new node type
	 */
	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
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
}