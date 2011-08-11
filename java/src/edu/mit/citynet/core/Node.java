/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.core;

// TODO: Auto-generated Javadoc
/**
 * The Node class is used to define contents of a particular city cell and 
 * layer within a system.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class Node {
	
	/** The id. */
	private int id;
	
	/** The cell. */
	private Cell cell;
	
	/** The layer. */
	private Layer layer;
	
	/** The node type. */
	private NodeType nodeType;
	
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
	 * Gets the cell.
	 *
	 * @return the cell
	 */
	public Cell getCell() {
		return cell;
	}
	
	/**
	 * Sets the cell.
	 *
	 * @param cell the new cell
	 */
	public void setCell(Cell cell) {
		this.cell = cell;
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
	 * Gets the node type attribute value.
	 *
	 * @param attributeName the attribute name
	 * @return the node type attribute value
	 */
	public Double getNodeTypeAttributeValue(String attributeName) {
		for(NodeTypeAttribute attribute : nodeType.getAttributes()) {
			if(attribute.getName().equals(attributeName))
				return attribute.getValue();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if(object instanceof Node) {
			return id==((Node)object).getId();
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Node" + id;
	}
}
