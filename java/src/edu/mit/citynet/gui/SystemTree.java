/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import java.awt.Component;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.EdgeType;
import edu.mit.citynet.core.Layer;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.core.NodeType;
import edu.mit.citynet.core.Region;
import edu.mit.citynet.gui.SystemTreeModel.MutableEdgeRegionTreeNode;
import edu.mit.citynet.gui.SystemTreeModel.MutableEdgeTypeTreeNode;
import edu.mit.citynet.gui.SystemTreeModel.MutableLayerTreeNode;
import edu.mit.citynet.gui.SystemTreeModel.MutableNodeRegionTreeNode;
import edu.mit.citynet.gui.SystemTreeModel.MutableNodeTypeTreeNode;
import edu.mit.citynet.gui.SystemTreeModel.MutableRegionTreeNode;

/**
 * The SystemTree class.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class SystemTree extends JTree {
	private static final long serialVersionUID = -1918743260118516463L;
	private SystemTreeModel model;

	/**
	 * Instantiates a new system tree.
	 *
	 * @param model the model
	 */
	public SystemTree(SystemTreeModel model) {
		super(model);
		this.model = model;
		setRootVisible(false);
		setShowsRootHandles(true);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setCellRenderer(new DefaultTreeCellRenderer() {
			private static final long serialVersionUID = 6981300046370175632L;
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, 
					boolean expanded, boolean leaf, int row, boolean hasFocus) {
				super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
				setFont(getFont().deriveFont(Font.PLAIN));
				if(value instanceof MutableLayerTreeNode) {
					// TODO define setIcon(((MutableLayerTreeNode)value).getUserObject().getIcon());
					setText(((MutableLayerTreeNode)value).getUserObject().getName());
				} else if(value instanceof MutableNodeRegionTreeNode) {
					setIcon(((MutableNodeRegionTreeNode)value).getUserObject().getNodeType().getIcon());
					setText(((MutableNodeRegionTreeNode)value).getUserObject().getDescription());
				} else if(value instanceof MutableEdgeRegionTreeNode) {
					setIcon(((MutableEdgeRegionTreeNode)value).getUserObject().getEdgeType().getIcon());
					setText(((MutableEdgeRegionTreeNode)value).getUserObject().getDescription());
				} else if(value instanceof MutableNodeTypeTreeNode) {
					setIcon(((MutableNodeTypeTreeNode)value).getUserObject().getIcon());
					setText(((MutableNodeTypeTreeNode)value).getUserObject().getName());
				} else if(value instanceof MutableEdgeTypeTreeNode) {
					setIcon(((MutableEdgeTypeTreeNode)value).getUserObject().getIcon());
					setText(((MutableEdgeTypeTreeNode)value).getUserObject().getName());
				} else if(value instanceof MutableRegionTreeNode) {
					// TODO define setIcon(((MutableRegionTreeNode)value).getUserObject().getIcon());
					setText(((MutableRegionTreeNode)value).getUserObject().getDescription());
				}  else {
					setFont(getFont().deriveFont(Font.BOLD));
				}
				return this;
			}
			
		});
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JTree#getModel()
	 */
	public SystemTreeModel getModel() {
		return model;
	}
	
	/**
	 * Gets the selected layer.
	 *
	 * @return the selected layer
	 */
	public Layer getSelectedLayer() {
		if(getSelectionModel().getSelectionPath() != null
				&& getSelectionModel().getSelectionPath().getLastPathComponent() 
				instanceof MutableLayerTreeNode) {
			return ((MutableLayerTreeNode)
					getSelectionModel().getSelectionPath().getLastPathComponent()).getUserObject();
		} else return null;
	}
	
	/**
	 * Gets the selected node type.
	 *
	 * @return the selected node type
	 */
	public NodeType getSelectedNodeType() {
		if(getSelectionModel().getSelectionPath() != null
				&& getSelectionModel().getSelectionPath().getLastPathComponent() 
				instanceof MutableNodeTypeTreeNode) {
			return ((MutableNodeTypeTreeNode)
					getSelectionModel().getSelectionPath().getLastPathComponent()).getUserObject();
		} else return null;
	}
	
	/**
	 * Gets the selected edge type.
	 *
	 * @return the selected edge type
	 */
	public EdgeType getSelectedEdgeType() {
		if(getSelectionModel().getSelectionPath() != null
				&& getSelectionModel().getSelectionPath().getLastPathComponent() 
				instanceof MutableEdgeTypeTreeNode) {
			return ((MutableEdgeTypeTreeNode)
					getSelectionModel().getSelectionPath().getLastPathComponent()).getUserObject();
		} else return null;
	}
	
	/**
	 * Gets the selected node region.
	 *
	 * @return the selected node region
	 */
	public NodeRegion getSelectedNodeRegion() {
		if(getSelectionModel().getSelectionPath() != null
				&& getSelectionModel().getSelectionPath().getLastPathComponent() 
				instanceof MutableNodeRegionTreeNode) {
			return ((MutableNodeRegionTreeNode)
					getSelectionModel().getSelectionPath().getLastPathComponent()).getUserObject();
		} else return null;
	}
	
	/**
	 * Gets the selected edge region.
	 *
	 * @return the selected edge region
	 */
	public EdgeRegion getSelectedEdgeRegion() {
		if(getSelectionModel().getSelectionPath() != null
				&& getSelectionModel().getSelectionPath().getLastPathComponent() 
				instanceof MutableEdgeRegionTreeNode) {
			return ((MutableEdgeRegionTreeNode)
					getSelectionModel().getSelectionPath().getLastPathComponent()).getUserObject();
		} else return null;
	}
	
	/**
	 * Gets the selected region.
	 *
	 * @return the selected region
	 */
	public Region getSelectedRegion() {
		if(getSelectionModel().getSelectionPath() != null
				&& getSelectionModel().getSelectionPath().getLastPathComponent() 
				instanceof MutableRegionTreeNode) {
			return ((MutableRegionTreeNode)
					getSelectionModel().getSelectionPath().getLastPathComponent()).getUserObject();
		} else return null;
	}
	
	/**
	 * Sets the system.
	 *
	 * @param system the new system
	 */
	public void setSystem(CitySystem system) {
		Enumeration<TreePath> expandedPaths = getExpandedDescendants(new TreePath(model.getRoot()));
		model.setSystem(system);
		while(expandedPaths.hasMoreElements()) {
			expandPath(expandedPaths.nextElement());
		}
	}
}
