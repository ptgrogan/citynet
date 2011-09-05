/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ImageIcon;

import edu.mit.citynet.CityNet;
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
	private List<Layer> layers;
	private List<NodeType> nodeTypes;
	private List<EdgeType> edgeTypes;
	private List<Node> nodes;
	private List<Edge> edges;
	private List<NodeRegion> nodeRegions;
	private List<EdgeRegion> edgeRegions;
	private List<Region> regions;
	
	/**
	 * The CitySystemType enumeration defines the possible system types.
	 * 
	 * @author Paul Grogan, ptgrogan@mit.edu
	 */
	public enum CitySystemType {
		BUILDING("Building"),
		ENERGY("Energy"),
		TRANSPORTATION("Transportation"), 
		WASTE("Waste"), 
		WATER("Water"), 
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
		layers = new ArrayList<Layer>();
		nodeTypes = new ArrayList<NodeType>();
		edgeTypes = new ArrayList<EdgeType>();
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		nodeRegions = new ArrayList<NodeRegion>();
		edgeRegions = new ArrayList<EdgeRegion>();
		regions = new ArrayList<Region>();
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
	public List<EdgeRegion> getEdgeRegions() {
		return new ArrayList<EdgeRegion>(edgeRegions);
	}
	
	/**
	 * Gets the regions.
	 *
	 * @return the regions
	 */
	public List<Region> getRegions() {
		return new ArrayList<Region>(regions);
	}

	/**
	 * Gets a copy of the edges.
	 *
	 * @return the edges
	 */
	public List<Edge> getEdges() {
		return new ArrayList<Edge>(edges);
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
	public List<EdgeType> getEdgeTypes() {
		return new ArrayList<EdgeType>(edgeTypes);
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
	public List<Layer> getLayers() {
		return new ArrayList<Layer>(layers);
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
	public List<NodeRegion> getNodeRegions() {
		return new ArrayList<NodeRegion>(nodeRegions);
	}

	/**
	 * Gets a copy of the nodes.
	 *
	 * @return the nodes
	 */
	public List<Node> getNodes() {
		return new ArrayList<Node>(nodes);
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
	public List<NodeType> getNodeTypes() {
		return new ArrayList<NodeType>(nodeTypes);
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
	 * @return true, if successful
	 */
	public boolean addAllEdgeRegions(Collection<EdgeRegion> edgeRegions) {
		return this.edgeRegions.addAll(edgeRegions);
	}
	
	/**
	 * Removes the all regions.
	 *
	 * @param regions the regions
	 * @return true, if successful
	 */
	public boolean removeAllRegions(Collection<Region> regions) {
		return this.regions.removeAll(regions);
	}

	/**
	 * Sets the regions.
	 *
	 * @param regions the new regions
	 * @return true, if successful
	 */
	public boolean addAllRegions(Collection<Region> regions) {
		return this.regions.addAll(regions);
	}
	
	/**
	 * Removes the all edge regions.
	 *
	 * @param edgeRegions the edge regions
	 * @return true, if successful
	 */
	public boolean removeAllEdgeRegions(Collection<EdgeRegion> edgeRegions) {
		return this.edgeRegions.removeAll(edgeRegions);
	}

	/**
	 * Sets the edges.
	 *
	 * @param edges the new edges
	 * @return true, if successful
	 */
	public boolean addAllEdges(Collection<Edge> edges) {
		return this.edges.addAll(edges);
	}
	
	/**
	 * Removes the all edges.
	 *
	 * @param edges the edges
	 * @return true, if successful
	 */
	public boolean removeAllEdges(Collection<Edge> edges) {
		return this.edges.removeAll(edges);
	}

	/**
	 * Sets the edge types.
	 *
	 * @param edgeTypes the new edge types
	 * @return true, if successful
	 */
	public boolean addAllEdgeTypes(Collection<EdgeType> edgeTypes) {
		return this.edgeTypes.addAll(edgeTypes);
	}
	
	/**
	 * Removes the all edge types.
	 *
	 * @param edgeTypes the edge types
	 * @return true, if successful
	 */
	public boolean removeAllEdgeTypes(Collection<EdgeType> edgeTypes) {
		return this.edgeTypes.removeAll(edgeTypes);
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
	 * @return true, if successful
	 */
	public boolean addAllLayers(Collection<Layer> layers) {
		return this.layers.addAll(layers);
	}
	
	/**
	 * Removes the all layers.
	 *
	 * @param layers the layers
	 * @return true, if successful
	 */
	public boolean removeAllLayers(Collection<Layer> layers) {
		return this.layers.removeAll(layers);
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
	 * @return true, if successful
	 */
	public boolean addAllNodeRegions(Collection<NodeRegion> nodeRegions) {
		return this.nodeRegions.addAll(nodeRegions);
	}
	
	/**
	 * Removes the all node regions.
	 *
	 * @param nodeRegions the node regions
	 * @return true, if successful
	 */
	public boolean removeAllNodeRegions(Collection<NodeRegion> nodeRegions) {
		return this.nodeRegions.removeAll(nodeRegions);
	}

	/**
	 * Sets the nodes.
	 *
	 * @param nodes the new nodes
	 * @return true, if successful
	 */
	public boolean addAllNodes(Collection<Node> nodes) {
		return this.nodes.addAll(nodes);
	}
	
	/**
	 * Removes the all nodes.
	 *
	 * @param nodes the nodes
	 * @return true, if successful
	 */
	public boolean removeAllNodes(Collection<Node> nodes) {
		return this.nodes.removeAll(nodes);
	}

	/**
	 * Sets the node types.
	 *
	 * @param nodeTypes the new node types
	 * @return true, if successful
	 */
	public boolean addAllNodeTypes(Collection<NodeType> nodeTypes) {
		return this.nodeTypes.addAll(nodeTypes);
	}
	
	/**
	 * Removes the all node types.
	 *
	 * @param nodeTypes the node types
	 * @return true, if successful
	 */
	public boolean removeAllNodeTypes(Collection<NodeType> nodeTypes) {
		return this.nodeTypes.removeAll(nodeTypes);
	}
	
	/**
	 * Adds the layer.
	 *
	 * @param layer the layer
	 * @return true, if successful
	 */
	public boolean addLayer(Layer layer) {
		if(layer.getId()==0)
			layer.setId(CityNet.getInstance().getNextLayerId());
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
		if(region.getId()==0)
			region.setId(CityNet.getInstance().getNextNodeRegionId());
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
		if(region.getId()==0)
			region.setId(CityNet.getInstance().getNextEdgeRegionId());
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
	 * Adds the region.
	 *
	 * @param region the region
	 * @return true, if successful
	 */
	public boolean addRegion(Region region) {
		if(region.getId()==0)
			region.setId(CityNet.getInstance().getNextRegionId());
		return regions.add(region);
	}
	
	/**
	 * Removes the region.
	 *
	 * @param region the region
	 * @return true, if successful
	 */
	public boolean removeRegion(Region region) {
		return regions.remove(region);
	}
	
	/**
	 * Adds the node type.
	 *
	 * @param nodeType the node type
	 * @return true, if successful
	 */
	public boolean addNodeType(NodeType nodeType) {
		if(nodeType.getId()==0)
			nodeType.setId(CityNet.getInstance().getNextNodeTypeId());
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
		if(edgeType.getId()==0)
			edgeType.setId(CityNet.getInstance().getNextEdgeTypeId());
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
	
	/**
	 * Move node region to a new index.
	 *
	 * @param region the region
	 * @param index the index
	 * @return true, if successful
	 */
	public boolean moveNodeRegionTo(NodeRegion region, int index) {
		if(!nodeRegions.contains(region) || index < 0 || index >= nodeRegions.size())
			return false;
		int oldIndex = nodeRegions.indexOf(region);
		NodeRegion otherRegion = nodeRegions.get(index);
		nodeRegions.set(index, region);
		nodeRegions.set(oldIndex, otherRegion);
		return true;
	}
	
	/**
	 * Move edge region to a new index.
	 *
	 * @param region the region
	 * @param index the index
	 * @return true, if successful
	 */
	public boolean moveEdgeRegionTo(EdgeRegion region, int index) {
		if(!edgeRegions.contains(region) || index < 0 || index >= edgeRegions.size())
			return false;
		int oldIndex = edgeRegions.indexOf(region);
		EdgeRegion otherRegion = edgeRegions.get(index);
		edgeRegions.set(index, region);
		edgeRegions.set(oldIndex, otherRegion);
		return true;
	}
	
	/**
	 * Move region to a new index.
	 *
	 * @param region the region
	 * @param index the index
	 * @return true, if successful
	 */
	public boolean moveRegionTo(Region region, int index) {
		if(!regions.contains(region) || index < 0 || index >= regions.size())
			return false;
		int oldIndex = regions.indexOf(region);
		Region otherRegion = regions.get(index);
		regions.set(index, region);
		regions.set(oldIndex, otherRegion);
		return true;
	}
	
	/**
	 * Move node type to a new index.
	 *
	 * @param type the type
	 * @param index the index
	 * @return true, if successful
	 */
	public boolean moveNodeTypeTo(NodeType type, int index) {
		if(!nodeTypes.contains(type) || index < 0 || index >= nodeTypes.size())
			return false;
		int oldIndex = nodeTypes.indexOf(type);
		NodeType otherType = nodeTypes.get(index);
		nodeTypes.set(index, type);
		nodeTypes.set(oldIndex, otherType);
		return true;
	}
	
	/**
	 * Move edge type to a new index.
	 *
	 * @param type the type
	 * @param index the index
	 * @return true, if successful
	 */
	public boolean moveEdgeTypeTo(EdgeType type, int index) {
		if(!edgeTypes.contains(type) || index < 0 || index >= edgeTypes.size())
			return false;
		int oldIndex = edgeTypes.indexOf(type);
		EdgeType otherType = edgeTypes.get(index);
		edgeTypes.set(index, type);
		edgeTypes.set(oldIndex, otherType);
		return true;
	}
	
	/**
	 * Move layer to a new index.
	 *
	 * @param layer the layer
	 * @param index the index
	 * @return true, if successful
	 */
	public boolean moveLayerTo(Layer layer, int index) {
		if(!layers.contains(layer) || index < 0 || index >= layers.size())
			return false;
		int oldIndex = layers.indexOf(layer);
		Layer otherLayer = layers.get(index);
		layers.set(index, layer);
		layers.set(oldIndex, otherLayer);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "System" + id;
	}
}
