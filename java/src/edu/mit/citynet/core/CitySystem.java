package edu.mit.citynet.core;

import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;

import edu.mit.citynet.util.CityNetIcon;

/**
 * The CitySystem class  contains node and edge information on multiple layers.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CitySystem {
	private int id;
	private CitySystemType type;
	private String name, description;
	private Set<Layer> layers;
	private Set<NodeType> nodeTypes;
	private Set<EdgeType> edgeTypes;
	private Set<Node> nodes;
	private Set<Edge> edges;
	private Set<NodeRegion> nodeRegions;
	private Set<EdgeRegion> edgeRegions;
	
	/**
	 * The CitySystemType enumeration defines the possible system types.
	 * 
	 * @author Paul Grogan, ptgrogan@mit.edu
	 */
	public enum CitySystemType {
		BUILDING("Building"), ENERGY("Energy"), 
		TRANSPORTATION("Transportation"), WASTE("Waste"), WATER("Water"), 
		UNDEFINED("Undefined");
		private String name;
		
		/**
		 * Instantiates a new city system type.
		 *
		 * @param name the name
		 */
		private CitySystemType(String name) {
			this.name = name;
		}
		
		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		public String getName() { 
			return name;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		public String toString() {
			return name;
		}
		
		/**
		 * Gets the single instance of CitySystemType.
		 *
		 * @param name the name
		 * @return single instance of CitySystemType
		 */
		public static CitySystemType getInstance(String name) {
			for(CitySystemType type : CitySystemType.values()) {
				if(type.getName().toLowerCase().equals(name.toLowerCase()))
					return type;
			}
			return UNDEFINED;
		}
		
		/**
		 * Gets the icon.
		 *
		 * @return the icon
		 */
		public ImageIcon getIcon() {
			switch(this) {
			case BUILDING: return CityNetIcon.BUILDING_SYSTEM.getIcon();
			case ENERGY: return CityNetIcon.ENERGY_SYSTEM.getIcon();
			case TRANSPORTATION: return CityNetIcon.TRANSPORTATION_SYSTEM.getIcon();
			case WASTE: return CityNetIcon.WASTE_SYSTEM.getIcon();
			case WATER: return CityNetIcon.WATER_SYSTEM.getIcon();
			default: return CityNetIcon.UNDEFINED_SYSTEM.getIcon();
			}
		}
	}
	
	/**
	 * Instantiates a new city system.
	 */
	public CitySystem() {
		name = "New System";
		type = CitySystemType.UNDEFINED;
		layers = new HashSet<Layer>();
		nodeTypes = new HashSet<NodeType>();
		edgeTypes = new HashSet<EdgeType>();
		nodes = new HashSet<Node>();
		edges = new HashSet<Edge>();
		nodeRegions = new HashSet<NodeRegion>();
		edgeRegions = new HashSet<EdgeRegion>();
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public CitySystemType getType() {
		return type==null?CitySystemType.UNDEFINED:type;
	}
	
	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(CitySystemType type) {
		this.type = type;
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
	
	/**
	 * Adds the layer.
	 *
	 * @param layer the layer
	 * @return true, if successful
	 */
	public boolean addLayer(Layer layer) {
		return layers.add(layer);
	}
	
	/**
	 * Removes the layer.
	 *
	 * @param layer the layer
	 * @return true, if successful
	 */
	public boolean removeLayer(Layer layer) {
		return layers.remove(layer);
	}
	
	/**
	 * Adds the node region.
	 *
	 * @param region the region
	 * @return true, if successful
	 */
	public boolean addNodeRegion(NodeRegion region) {
		return nodeRegions.add(region);
	}
	
	/**
	 * Removes the node region.
	 *
	 * @param region the region
	 * @return true, if successful
	 */
	public boolean removeNodeRegion(NodeRegion region) {
		return nodeRegions.remove(region);
	}
	
	/**
	 * Adds the edge region.
	 *
	 * @param region the region
	 * @return true, if successful
	 */
	public boolean addEdgeRegion(EdgeRegion region) {
		return edgeRegions.add(region);
	}
	
	/**
	 * Removes the edge region.
	 *
	 * @param region the region
	 * @return true, if successful
	 */
	public boolean removeEdgeRegion(EdgeRegion region) {
		return edgeRegions.remove(region);
	}
	
	/**
	 * Adds the node type.
	 *
	 * @param nodeType the node type
	 * @return true, if successful
	 */
	public boolean addNodeType(NodeType nodeType) {
		return nodeTypes.add(nodeType);
	}
	
	/**
	 * Removes the node type.
	 *
	 * @param nodeType the node type
	 * @return true, if successful
	 */
	public boolean removeNodeType(NodeType nodeType) {
		return nodeTypes.remove(nodeType);
	}
	
	/**
	 * Adds the edge type.
	 *
	 * @param edgeType the edge type
	 * @return true, if successful
	 */
	public boolean addEdgeType(EdgeType edgeType) {
		return edgeTypes.add(edgeType);
	}
	
	/**
	 * Removes the edge type.
	 *
	 * @param edgeType the edge type
	 * @return true, if successful
	 */
	public boolean removeEdgeType(EdgeType edgeType) {
		return edgeTypes.remove(edgeTypes);
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
