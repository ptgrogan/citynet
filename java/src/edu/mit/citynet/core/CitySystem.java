package edu.mit.citynet.core;

import java.util.HashSet;
import java.util.Set;

/**
 * The CitySystem class  contains node and edge information on multiple layers.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CitySystem {
	private int id;
	private String name, description;
	private Set<Layer> layers;
	private Set<NodeType> nodeTypes;
	private Set<EdgeType> edgeTypes;
	private Set<Node> nodes;
	private Set<Edge> edges;
	private Set<NodeRegion> nodeRegions;
	private Set<EdgeRegion> edgeRegions;
	
	/**
	 * Instantiates a new city system.
	 */
	public CitySystem() {
		layers = new HashSet<Layer>();
		nodeTypes = new HashSet<NodeType>();
		edgeTypes = new HashSet<EdgeType>();
		nodes = new HashSet<Node>();
		edges = new HashSet<Edge>();
		nodeRegions = new HashSet<NodeRegion>();
		edgeRegions = new HashSet<EdgeRegion>();
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
	 * Gets a copy of the edge regions.
	 *
	 * @return the edge regions
	 */
	public Set<EdgeRegion> getEdgeRegions() {
		return new HashSet<EdgeRegion>(edgeRegions);
	}

	/**
	 * Gets a copy of the edges.
	 *
	 * @return the edges
	 */
	public Set<Edge> getEdges() {
		return new HashSet<Edge>(edges);
	}
	
	/**
	 * Adds the edge.
	 *
	 * @param edge the edge
	 * @return true, if successful
	 */
	public boolean addEdge(Edge edge) {
		return edges.add(edge);
	}

	/**
	 * Gets a copy of the edge types.
	 *
	 * @return the edge types
	 */
	public Set<EdgeType> getEdgeTypes() {
		return new HashSet<EdgeType>(edgeTypes);
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
	 * Gets a copy of the layers.
	 *
	 * @return the layers
	 */
	public Set<Layer> getLayers() {
		return new HashSet<Layer>(layers);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets a copy of the node regions.
	 *
	 * @return the node regions
	 */
	public Set<NodeRegion> getNodeRegions() {
		return new HashSet<NodeRegion>(nodeRegions);
	}

	/**
	 * Gets a copy of the nodes.
	 *
	 * @return the nodes
	 */
	public Set<Node> getNodes() {
		return new HashSet<Node>(nodes);
	}
	
	/**
	 * Adds the node.
	 *
	 * @param node the node
	 * @return true, if successful
	 */
	public boolean addNode(Node node) {
		return nodes.add(node);
	}

	/**
	 * Gets a copy of the node types.
	 *
	 * @return the node types
	 */
	public Set<NodeType> getNodeTypes() {
		return new HashSet<NodeType>(nodeTypes);
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
	 * Sets the edge regions.
	 *
	 * @param edgeRegions the new edge regions
	 */
	public void setEdgeRegions(Set<EdgeRegion> edgeRegions) {
		this.edgeRegions = edgeRegions;
	}

	/**
	 * Sets the edges.
	 *
	 * @param edges the new edges
	 */
	public void setEdges(Set<Edge> edges) {
		this.edges = edges;
	}

	/**
	 * Sets the edge types.
	 *
	 * @param edgeTypes the new edge types
	 */
	public void setEdgeTypes(Set<EdgeType> edgeTypes) {
		this.edgeTypes = edgeTypes;
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
	 * Sets the layers.
	 *
	 * @param layers the new layers
	 */
	public void setLayers(Set<Layer> layers) {
		this.layers = layers;
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
	 * Sets the node regions.
	 *
	 * @param nodeRegions the new node regions
	 */
	public void setNodeRegions(Set<NodeRegion> nodeRegions) {
		this.nodeRegions = nodeRegions;
	}

	/**
	 * Sets the nodes.
	 *
	 * @param nodes the new nodes
	 */
	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Sets the node types.
	 *
	 * @param nodeTypes the new node types
	 */
	public void setNodeTypes(Set<NodeType> nodeTypes) {
		this.nodeTypes = nodeTypes;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if(object instanceof CitySystem) {
			return id==((CitySystem)object).getId();
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "System" + id;
	}
}
