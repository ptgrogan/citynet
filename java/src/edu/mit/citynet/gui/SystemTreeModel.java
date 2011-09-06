/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.EdgeType;
import edu.mit.citynet.core.Layer;
import edu.mit.citynet.core.NodeType;
import edu.mit.citynet.core.Region;

public class SystemTreeModel extends DefaultTreeModel {
	private static final long serialVersionUID = -3479867919367893028L;
	private DefaultMutableTreeNode root;
	public final DefaultMutableTreeNode 
		layersTreeNode = new DefaultMutableTreeNode("Layers"), 
		nodeTypesTreeNode = new DefaultMutableTreeNode("Node Types"), 
		edgeTypesTreeNode = new DefaultMutableTreeNode("Edge Types"),
		regionsTreeNode = new DefaultMutableTreeNode("Regions");

	/**
	 * Instantiates a new system tree model.
	 *
	 * @param root the root
	 */
	public SystemTreeModel(CitySystem system) {
		super(createTreeNode(system));
		root = (DefaultMutableTreeNode)super.getRoot();
		setSystem(system);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeModel#getRoot()
	 */
	public DefaultMutableTreeNode getRoot() {
		return root;
	}
	
	/**
	 * Creates the tree node.
	 *
	 * @param system the system
	 * @return the default mutable tree node
	 */
	public static DefaultMutableTreeNode createTreeNode(CitySystem system) {
		return new DefaultMutableTreeNode(system);
	}
	
	/**
	 * Sets the system.
	 *
	 * @param system the new system
	 */
	public void setSystem(CitySystem system) {
		root.removeAllChildren();
		if(system==null) return;
		
		int index = 0;
		layersTreeNode.removeAllChildren();
		int layerIndex = 0;
		for(Layer layer : system.getLayers()) {
			layersTreeNode.insert(createTreeNode(layer), layerIndex++);
		}
		root.insert(layersTreeNode, index++);
		
		nodeTypesTreeNode.removeAllChildren();
		int nodeTypesIndex = 0;
		for(NodeType nodeType : system.getNodeTypes()) {
			nodeTypesTreeNode.insert(createTreeNode(nodeType), nodeTypesIndex++);
		}
		root.insert(nodeTypesTreeNode, index++);
		
		edgeTypesTreeNode.removeAllChildren();
		int edgeTypesIndex = 0;
		for(EdgeType edgeType : system.getEdgeTypes()) {
			edgeTypesTreeNode.insert(createTreeNode(edgeType), edgeTypesIndex++);
		}
		root.insert(edgeTypesTreeNode, index++);
		
		regionsTreeNode.removeAllChildren();
		int regionsIndex = 0;
		for(Region region : system.getRegions()) {
			regionsTreeNode.insert(createTreeNode(region), regionsIndex++);
		}
		root.insert(regionsTreeNode, index++);
		
		nodeStructureChanged(root);
	}
	
	/**
	 * Adds the layer.
	 *
	 * @param layer the layer
	 */
	public void addLayer(Layer layer) {
		layersTreeNode.add(createTreeNode(layer));
		nodesWereInserted(layersTreeNode, new int[]{layersTreeNode.getChildCount()-1});
	}
	
	/**
	 * Update layer.
	 *
	 * @param layer the layer
	 */
	public void updateLayer(Layer layer) {
		for(int i = 0; i < layersTreeNode.getChildCount(); i++) {
			if(layersTreeNode.getChildAt(i) instanceof MutableLayerTreeNode
					&& ((MutableLayerTreeNode)layersTreeNode.getChildAt(i)).getUserObject()==layer) {
				nodeChanged(layersTreeNode.getChildAt(i));
				break;
			}
		}
	}
	
	/**
	 * Removes the layer.
	 *
	 * @param layer the layer
	 */
	public void removeLayer(Layer layer) {
		for(int i = 0; i < layersTreeNode.getChildCount(); i++) {
			if(layersTreeNode.getChildAt(i) instanceof MutableLayerTreeNode
					&& ((MutableLayerTreeNode)layersTreeNode.getChildAt(i)).getUserObject()==layer) {
				layersTreeNode.remove(i);
				nodesWereRemoved(layersTreeNode,new int[]{i},null);
				break;
			}
		}
	}
	
	/**
	 * Adds the node type.
	 *
	 * @param nodeType the nodeType
	 */
	public void addNodeType(NodeType nodeType) {
		nodeTypesTreeNode.add(createTreeNode(nodeType));
		nodesWereInserted(nodeTypesTreeNode, new int[]{nodeTypesTreeNode.getChildCount()-1});
	}
	
	/**
	 * Update node type.
	 *
	 * @param nodeType the nodeType
	 */
	public void updateNodeType(NodeType nodeType) {
		for(int i = 0; i < nodeTypesTreeNode.getChildCount(); i++) {
			if(nodeTypesTreeNode.getChildAt(i) instanceof MutableNodeTypeTreeNode
					&& ((MutableNodeTypeTreeNode)nodeTypesTreeNode.getChildAt(i)).getUserObject()==nodeType) {
				nodeChanged(nodeTypesTreeNode.getChildAt(i));
				break;
			}
		}
	}
	
	/**
	 * Removes the node type.
	 *
	 * @param nodeType the nodeType
	 */
	public void removeNodeType(NodeType nodeType) {
		for(int i = 0; i < nodeTypesTreeNode.getChildCount(); i++) {
			if(nodeTypesTreeNode.getChildAt(i) instanceof MutableNodeTypeTreeNode
					&& ((MutableNodeTypeTreeNode)nodeTypesTreeNode.getChildAt(i)).getUserObject()==nodeType) {
				nodesWereRemoved(nodeTypesTreeNode,new int[]{i},null);
				break;
			}
		}
	}
	
	/**
	 * Adds the edge type.
	 *
	 * @param edgeType the edgeType
	 */
	public void addEdgeType(EdgeType edgeType) {
		edgeTypesTreeNode.add(createTreeNode(edgeType));
		nodesWereInserted(edgeTypesTreeNode, new int[]{edgeTypesTreeNode.getChildCount()-1});
	}
	
	/**
	 * Update edge type.
	 *
	 * @param edgeType the edgeType
	 */
	public void updateEdgeType(EdgeType edgeType) {
		for(int i = 0; i < edgeTypesTreeNode.getChildCount(); i++) {
			if(edgeTypesTreeNode.getChildAt(i) instanceof MutableEdgeTypeTreeNode
					&& ((MutableEdgeTypeTreeNode)edgeTypesTreeNode.getChildAt(i)).getUserObject()==edgeType) {
				nodeChanged(edgeTypesTreeNode.getChildAt(i));
				break;
			}
		}
	}
	
	/**
	 * Removes the edge type.
	 *
	 * @param edgeType the edgeType
	 */
	public void removeEdgeType(EdgeType edgeType) {
		for(int i = 0; i < edgeTypesTreeNode.getChildCount(); i++) {
			if(edgeTypesTreeNode.getChildAt(i) instanceof MutableEdgeTypeTreeNode
					&& ((MutableEdgeTypeTreeNode)edgeTypesTreeNode.getChildAt(i)).getUserObject()==edgeType) {
				edgeTypesTreeNode.remove(i);
				nodesWereRemoved(edgeTypesTreeNode,new int[]{i},null);
				break;
			}
		}
	}
	
	/**
	 * Adds the region.
	 *
	 * @param region the region
	 */
	public void addRegion(Region region) {
		regionsTreeNode.add(createTreeNode(region));
		nodesWereInserted(regionsTreeNode, new int[]{regionsTreeNode.getChildCount()-1});
	}
	
	/**
	 * Update region.
	 *
	 * @param region the region
	 */
	public void updateRegion(Region region) {
		for(int i = 0; i < regionsTreeNode.getChildCount(); i++) {
			if(regionsTreeNode.getChildAt(i) instanceof MutableRegionTreeNode
					&& ((MutableRegionTreeNode)regionsTreeNode.getChildAt(i)).getUserObject()==region) {
				nodeChanged(regionsTreeNode.getChildAt(i));
				break;
			}
		}
	}
	
	/**
	 * Removes the region.
	 *
	 * @param region the region
	 */
	public void removeRegion(Region region) {
		for(int i = 0; i < regionsTreeNode.getChildCount(); i++) {
			if(regionsTreeNode.getChildAt(i) instanceof MutableRegionTreeNode
					&& ((MutableRegionTreeNode)regionsTreeNode.getChildAt(i)).getUserObject()==region) {
				regionsTreeNode.remove(i);
				nodesWereRemoved(regionsTreeNode,new int[]{i},null);
				break;
			}
		}
	}
	
	/**
	 * Creates the tree node.
	 *
	 * @param layer the layer
	 * @return the default mutable tree node
	 */
	private static MutableLayerTreeNode createTreeNode(Layer layer) {
		return new MutableLayerTreeNode(layer);
	}
	
	/**
	 * Creates the tree node.
	 *
	 * @param nodeType the node type
	 * @return the default mutable tree node
	 */
	private static MutableNodeTypeTreeNode createTreeNode(NodeType nodeType) {
		return new MutableNodeTypeTreeNode(nodeType);
	}
	
	/**
	 * Creates the tree node.
	 *
	 * @param edgeType the edge type
	 * @return the default mutable tree node
	 */
	private static MutableEdgeTypeTreeNode createTreeNode(EdgeType edgeType) {
		return new MutableEdgeTypeTreeNode(edgeType);
	}
	
	/**
	 * Creates the tree node.
	 *
	 * @param region the region
	 * @return the default mutable tree node
	 */
	public static MutableRegionTreeNode createTreeNode(Region region) {
		return new MutableRegionTreeNode(region);
	}
	
	/**
	 * The Class MutableLayerTreeNode.
	 */
	public static class MutableLayerTreeNode extends DefaultMutableTreeNode {
		private static final long serialVersionUID = 5722045815777954877L;
		
		/**
		 * Instantiates a new mutable layer tree node.
		 *
		 * @param layer the layer
		 */
		public MutableLayerTreeNode(Layer layer) {
			super(layer);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.tree.DefaultMutableTreeNode#getUserObject()
		 */
		public Layer getUserObject() {
			return (Layer)super.getUserObject();
		}
	}
	
	/**
	 * The Class MutableNodeTypeTreeNode.
	 */
	public static class MutableNodeTypeTreeNode extends DefaultMutableTreeNode {
		private static final long serialVersionUID = 5722045815777954877L;
		
		/**
		 * Instantiates a new mutable node type tree node.
		 *
		 * @param nodeType the node type
		 */
		public MutableNodeTypeTreeNode(NodeType nodeType) {
			super(nodeType);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.tree.DefaultMutableTreeNode#getUserObject()
		 */
		public NodeType getUserObject() {
			return (NodeType)super.getUserObject();
		}
	}
	
	/**
	 * The Class MutableEdgeTypeTreeEdge.
	 */
	public static class MutableEdgeTypeTreeNode extends DefaultMutableTreeNode {
		private static final long serialVersionUID = 5722045815777954877L;
		
		/**
		 * Instantiates a new mutable edge type tree edge.
		 *
		 * @param edgeType the edge type
		 */
		public MutableEdgeTypeTreeNode(EdgeType edgeType) {
			super(edgeType);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.tree.DefaultMutableTreeEdge#getUserObject()
		 */
		public EdgeType getUserObject() {
			return (EdgeType)super.getUserObject();
		}
	}
	
	/**
	 * The Class MutableRegionTreeEdge.
	 */
	public static class MutableRegionTreeNode extends DefaultMutableTreeNode {
		private static final long serialVersionUID = 5722045815777954877L;
		
		/**
		 * Instantiates a new mutable region tree edge.
		 *
		 * @param region the region
		 */
		public MutableRegionTreeNode(Region region) {
			super(region);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.tree.DefaultMutableTreeEdge#getUserObject()
		 */
		public Region getUserObject() {
			return (Region)super.getUserObject();
		}
	}
}
