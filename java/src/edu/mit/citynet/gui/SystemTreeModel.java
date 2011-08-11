/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.EdgeType;
import edu.mit.citynet.core.Layer;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.core.NodeType;

public class SystemTreeModel extends DefaultTreeModel {
	private static final long serialVersionUID = -3479867919367893028L;
	private DefaultMutableTreeNode root;
	public final DefaultMutableTreeNode 
		layersTreeNode = new DefaultMutableTreeNode("Layers"), 
		nodeTypesTreeNode = new DefaultMutableTreeNode("Node Types"), 
		edgeTypesTreeNode = new DefaultMutableTreeNode("Edge Types"), 
		nodeRegionsTreeNode = new DefaultMutableTreeNode("Node Regions"), 
		edgeRegionsTreeNode = new DefaultMutableTreeNode("Edge Regions");

	/**
	 * Instantiates a new system tree model.
	 *
	 * @param root the root
	 */
	public SystemTreeModel(CitySystem system) {
		super(createTreeNode(system));
		root = (DefaultMutableTreeNode)getRoot();
		setSystem(system);
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
		
		nodeRegionsTreeNode.removeAllChildren();
		int nodeRegionsIndex = 0;
		for(NodeRegion nodeRegion : system.getNodeRegions()) {
			nodeRegionsTreeNode.insert(createTreeNode(nodeRegion), nodeRegionsIndex++);
		}
		root.insert(nodeRegionsTreeNode, index++);
		
		edgeRegionsTreeNode.removeAllChildren();
		int edgeRegionsIndex = 0;
		for(EdgeRegion edgeRegion : system.getEdgeRegions()) {
			edgeRegionsTreeNode.insert(createTreeNode(edgeRegion), edgeRegionsIndex++);
		}
		root.insert(edgeRegionsTreeNode, index++);
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
	 * Adds the node region.
	 *
	 * @param nodeRegion the node region
	 */
	public void addNodeRegion(NodeRegion nodeRegion) {
		nodeRegionsTreeNode.add(createTreeNode(nodeRegion));
		nodesWereInserted(nodeRegionsTreeNode, new int[]{nodeRegionsTreeNode.getChildCount()-1});
	}
	
	/**
	 * Update node region.
	 *
	 * @param nodeRegion the node region
	 */
	public void updateNodeRegion(NodeRegion nodeRegion) {
		for(int i = 0; i < nodeRegionsTreeNode.getChildCount(); i++) {
			if(nodeRegionsTreeNode.getChildAt(i) instanceof MutableNodeRegionTreeNode
					&& ((MutableNodeRegionTreeNode)nodeRegionsTreeNode.getChildAt(i)).getUserObject()==nodeRegion) {
				nodeChanged(nodeRegionsTreeNode.getChildAt(i));
				break;
			}
		}
	}
	
	/**
	 * Removes the node region.
	 *
	 * @param nodeRegion the node region
	 */
	public void removeNodeRegion(NodeRegion nodeRegion) {
		for(int i = 0; i < nodeRegionsTreeNode.getChildCount(); i++) {
			if(nodeRegionsTreeNode.getChildAt(i) instanceof MutableNodeRegionTreeNode
					&& ((MutableNodeRegionTreeNode)nodeRegionsTreeNode.getChildAt(i)).getUserObject()==nodeRegion) {
				nodeRegionsTreeNode.remove(i);
				nodesWereRemoved(nodeRegionsTreeNode,new int[]{i},null);
				break;
			}
		}
	}
	
	/**
	 * Adds the edge region.
	 *
	 * @param edgeRegion the edge region
	 */
	public void addEdgeRegion(EdgeRegion edgeRegion) {
		edgeRegionsTreeNode.add(createTreeNode(edgeRegion));
		nodesWereInserted(edgeRegionsTreeNode, new int[]{edgeRegionsTreeNode.getChildCount()-1});
	}
	
	/**
	 * Update edge region.
	 *
	 * @param edgeRegion the edge region
	 */
	public void updateEdgeRegion(EdgeRegion edgeRegion) {
		for(int i = 0; i < edgeRegionsTreeNode.getChildCount(); i++) {
			if(edgeRegionsTreeNode.getChildAt(i) instanceof MutableEdgeRegionTreeNode
					&& ((MutableEdgeRegionTreeNode)edgeRegionsTreeNode.getChildAt(i)).getUserObject()==edgeRegion) {
				nodeChanged(edgeRegionsTreeNode.getChildAt(i));
				break;
			}
		}
	}
	
	/**
	 * Removes the edge region.
	 *
	 * @param edgeRegion the edge region
	 */
	public void removeEdgeRegion(EdgeRegion edgeRegion) {
		for(int i = 0; i < edgeRegionsTreeNode.getChildCount(); i++) {
			if(edgeRegionsTreeNode.getChildAt(i) instanceof MutableEdgeRegionTreeNode
					&& ((MutableEdgeRegionTreeNode)edgeRegionsTreeNode.getChildAt(i)).getUserObject()==edgeRegion) {
				edgeRegionsTreeNode.remove(i);
				nodesWereRemoved(edgeRegionsTreeNode,new int[]{i},null);
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
	 * @param nodeRegion the node region
	 * @return the default mutable tree node
	 */
	private static MutableNodeRegionTreeNode createTreeNode(NodeRegion nodeRegion) {
		return new MutableNodeRegionTreeNode(nodeRegion);
	}
	
	/**
	 * Creates the tree node.
	 *
	 * @param edgeRegion the edge region
	 * @return the default mutable tree node
	 */
	public static MutableEdgeRegionTreeNode createTreeNode(EdgeRegion edgeRegion) {
		return new MutableEdgeRegionTreeNode(edgeRegion);
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
		 * Instantiates a new mutable layer tree node.
		 *
		 * @param layer the layer
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
		 * Instantiates a new mutable layer tree edge.
		 *
		 * @param layer the layer
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
	 * The Class MutableNodeRegionTreeNode.
	 */
	public static class MutableNodeRegionTreeNode extends DefaultMutableTreeNode {
		private static final long serialVersionUID = 5722045815777954877L;
		
		/**
		 * Instantiates a new mutable layer tree node.
		 *
		 * @param layer the layer
		 */
		public MutableNodeRegionTreeNode(NodeRegion nodeRegion) {
			super(nodeRegion);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.tree.DefaultMutableTreeNode#getUserObject()
		 */
		public NodeRegion getUserObject() {
			return (NodeRegion)super.getUserObject();
		}
	}
	
	/**
	 * The Class MutableEdgeRegionTreeEdge.
	 */
	public static class MutableEdgeRegionTreeNode extends DefaultMutableTreeNode {
		private static final long serialVersionUID = 5722045815777954877L;
		
		/**
		 * Instantiates a new mutable layer tree edge.
		 *
		 * @param layer the layer
		 */
		public MutableEdgeRegionTreeNode(EdgeRegion edgeRegion) {
			super(edgeRegion);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.tree.DefaultMutableTreeEdge#getUserObject()
		 */
		public EdgeRegion getUserObject() {
			return (EdgeRegion)super.getUserObject();
		}
	}
}
