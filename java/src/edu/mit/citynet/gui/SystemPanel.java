/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.EdgeType;
import edu.mit.citynet.core.Layer;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.core.NodeType;
import edu.mit.citynet.gui.SystemTreeModel.MutableEdgeRegionTreeNode;
import edu.mit.citynet.gui.SystemTreeModel.MutableEdgeTypeTreeNode;
import edu.mit.citynet.gui.SystemTreeModel.MutableLayerTreeNode;
import edu.mit.citynet.gui.SystemTreeModel.MutableNodeRegionTreeNode;
import edu.mit.citynet.gui.SystemTreeModel.MutableNodeTypeTreeNode;
import edu.mit.citynet.util.CityNetIcon;
import edu.mit.citynet.viz.VizLayeredPane;

/**
 * The SystemVizPanel class provides a system-level visualization of cells, 
 * node regions and nodes, and edge regions and edges. The node region and
 * edge region visibility can be toggled using two tables.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class SystemPanel extends JSplitPane {
	private static final long serialVersionUID = -3650203268180181634L;

	private CityTabbedPane cityPanel;
	private CitySystem system;
	private VizLayeredPane layeredPane;
	private SystemTreeModel systemTreeModel;
	private SystemTree systemTree;
	private LayerPanel layerPanel;
	private NodeTypePanel nodeTypePanel;
	private EdgeTypePanel edgeTypePanel;
	private NodeRegionPanel nodeRegionPanel;
	private EdgeRegionPanel edgeRegionPanel;
	
	/**
	 * Instantiates a new system viz panel.
	 *
	 * @param cityPanel the city panel
	 * @param system the system
	 */
	public SystemPanel(CityTabbedPane cityPanel, CitySystem system) {
		if (system==null) {
			throw new IllegalArgumentException("System cannot be null.");
		}
		this.cityPanel = cityPanel;
		this.system = system;
		layerPanel = new LayerPanel(this);
		nodeTypePanel = new NodeTypePanel();
		edgeTypePanel = new EdgeTypePanel();
		nodeRegionPanel = new NodeRegionPanel(this);
		edgeRegionPanel = new EdgeRegionPanel(this);
		initializePanel();
	}
	
	/**
	 * Initializes the panel.
	 */
	private void initializePanel() {
		setResizeWeight(0);
		setDividerLocation(200);
		setOneTouchExpandable(true);
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		systemTreeModel = new SystemTreeModel(system);
		systemTree = new SystemTree(systemTreeModel);
		systemTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				layeredPane.setSelectedNodeRegion(systemTree.getSelectedNodeRegion());
				layeredPane.setSelectedEdgeRegion(systemTree.getSelectedEdgeRegion());
			}
		});
		systemTree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2 && systemTree.getSelectionCount() > 0) {
					if(systemTree.getSelectionPath().getLastPathComponent() 
							== systemTree.getModel().nodeRegionsTreeNode)
						editNodeRegionTableCommand();
					else if(systemTree.getSelectedNodeRegion()!=null)
						editNodeRegionCommand(systemTree.getSelectedNodeRegion());
					else if(systemTree.getSelectionPath().getLastPathComponent() 
							== systemTree.getModel().edgeRegionsTreeNode)
						editEdgeRegionTableCommand();
					else if(systemTree.getSelectedEdgeRegion()!=null)
						editEdgeRegionCommand(systemTree.getSelectedEdgeRegion());
					else if(systemTree.getSelectionPath().getLastPathComponent() 
							== systemTree.getModel().layersTreeNode)
						addLayerCommand();
					else if(systemTree.getSelectedLayer()!=null)
						editLayerCommand(systemTree.getSelectedLayer());
					else if(systemTree.getSelectionPath().getLastPathComponent() 
							== systemTree.getModel().nodeTypesTreeNode)
						editNodeTypeTableCommand();
					else if(systemTree.getSelectedNodeType()!=null)
						editNodeTypeCommand(systemTree.getSelectedNodeType());
					else if(systemTree.getSelectionPath().getLastPathComponent() 
							== systemTree.getModel().edgeTypesTreeNode)
						addEdgeTypeCommand();
					else if(systemTree.getSelectedEdgeType()!=null)
						editEdgeTypeCommand(systemTree.getSelectedEdgeType());
				}
			}
			public void mousePressed(MouseEvent e) {
				if(systemTree.getPathForLocation(e.getX(), e.getY())==null) 
					systemTree.getSelectionModel().clearSelection();
				maybeShowPopup(e);
			}
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}
			private void maybeShowPopup(MouseEvent e) {
				if(e.isPopupTrigger()) {
					createSystemTreePopupMenu(systemTree.getSelectionPath()).show(
							e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		leftPanel.add(new JScrollPane(systemTree),c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel generationButtonPanel = new JPanel();
		generationButtonPanel.setLayout(new BoxLayout(generationButtonPanel,BoxLayout.LINE_AXIS));
		JButton generateNodesButton = new JButton("Generate",CityNetIcon.GENERATE.getIcon());
		generateNodesButton.setToolTipText("Generate nodes and edges from regions");
		generateNodesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateNodesCommand(system.getNodeRegions());
				generateEdgesCommand(system.getEdgeRegions());
				layeredPane.repaint();
			}
		});
		generationButtonPanel.add(generateNodesButton);
		JButton clearButton = new JButton("Clear",CityNetIcon.DELETE.getIcon());
		clearButton.setToolTipText("Clear all nodes and edges");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearNodesCommand();
				layeredPane.repaint();
			}
		});
		generationButtonPanel.add(clearButton);
		leftPanel.add(generationButtonPanel, c);
		setLeftComponent(leftPanel);
		JPanel rightPanel = new JPanel(new BorderLayout());
		layeredPane = new VizLayeredPane(cityPanel.getCity(), system);
		rightPanel.add(layeredPane,BorderLayout.CENTER);
		setRightComponent(rightPanel);
	}
	
	/**
	 * Creates the node region popup menu.
	 *
	 * @param regions the regions
	 * @return the j popup menu
	 */
	private JPopupMenu createSystemTreePopupMenu(TreePath path) {
		JPopupMenu systemTreePopupMenu = new JPopupMenu();
		if(path==null) return systemTreePopupMenu;
		if(path.getLastPathComponent()==systemTree.getModel().layersTreeNode
				|| path.getLastPathComponent() instanceof MutableLayerTreeNode) {
			JMenuItem addLayerMenuItem = new JMenuItem("Add Layer");
			addLayerMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addLayerCommand();
				}
			});
			systemTreePopupMenu.add(addLayerMenuItem);
			JMenuItem editLayerMenuItem = new JMenuItem("Edit Layer");
			editLayerMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editLayerCommand(systemTree.getSelectedLayer());
				}
			});
			editLayerMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableLayerTreeNode);
			systemTreePopupMenu.add(editLayerMenuItem);
			JMenuItem deleteLayerMenuItem = new JMenuItem("Delete Layer");
			deleteLayerMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteLayerCommand(systemTree.getSelectedLayer());
				}
			});
			deleteLayerMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableLayerTreeNode);
			systemTreePopupMenu.add(deleteLayerMenuItem);
		} else if(path.getLastPathComponent()==systemTree.getModel().nodeTypesTreeNode
				|| path.getLastPathComponent() instanceof MutableNodeTypeTreeNode) {
			JMenuItem addNodeTypeMenuItem = new JMenuItem("Add Node Type");
			addNodeTypeMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addNodeTypeCommand();
				}
			});
			systemTreePopupMenu.add(addNodeTypeMenuItem);
			JMenuItem editNodeTypeMenuItem = new JMenuItem("Edit Node Type");
			editNodeTypeMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editNodeTypeCommand(systemTree.getSelectedNodeType());
				}
			});
			editNodeTypeMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableNodeTypeTreeNode);
			systemTreePopupMenu.add(editNodeTypeMenuItem);
			JMenuItem deleteNodeTypeMenuItem = new JMenuItem("Delete Node Type");
			deleteNodeTypeMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteNodeTypeCommand(systemTree.getSelectedNodeType());
				}
			});
			deleteNodeTypeMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableNodeTypeTreeNode);
			systemTreePopupMenu.add(deleteNodeTypeMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem editNodeTypeTableMenuItem = new JMenuItem("Edit Node Types Table");
			editNodeTypeTableMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editNodeTypeTableCommand();
				}
			});
			systemTreePopupMenu.add(editNodeTypeTableMenuItem);
		} else if(path.getLastPathComponent()==systemTree.getModel().edgeTypesTreeNode
				|| path.getLastPathComponent() instanceof MutableEdgeTypeTreeNode) {
			JMenuItem addEdgeTypeMenuItem = new JMenuItem("Add Edge Type");
			addEdgeTypeMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addEdgeTypeCommand();
				}
			});
			systemTreePopupMenu.add(addEdgeTypeMenuItem);
			JMenuItem editEdgeTypeMenuItem = new JMenuItem("Edit Edge Type");
			editEdgeTypeMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editEdgeTypeCommand(systemTree.getSelectedEdgeType());
				}
			});
			editEdgeTypeMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableEdgeTypeTreeNode);
			systemTreePopupMenu.add(editEdgeTypeMenuItem);
			JMenuItem deleteEdgeTypeMenuItem = new JMenuItem("Delete Edge Type");
			deleteEdgeTypeMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteEdgeTypeCommand(systemTree.getSelectedEdgeType());
				}
			});
			deleteEdgeTypeMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableEdgeTypeTreeNode);
			systemTreePopupMenu.add(deleteEdgeTypeMenuItem);
		} else if(path.getLastPathComponent()==systemTree.getModel().nodeRegionsTreeNode
				|| path.getLastPathComponent() instanceof MutableNodeRegionTreeNode) {
			JMenuItem addNodeRegionMenuItem = new JMenuItem("Add Node Region");
			addNodeRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addNodeRegionCommand();
				}
			});
			systemTreePopupMenu.add(addNodeRegionMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem editNodeRegionMenuItem = new JMenuItem("Edit Node Region");
			editNodeRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editNodeRegionCommand(systemTree.getSelectedNodeRegion());
				}
			});
			editNodeRegionMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableNodeRegionTreeNode);
			systemTreePopupMenu.add(editNodeRegionMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem copyNodeRegionMenuItem = new JMenuItem("Copy Node Region");
			copyNodeRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					copyNodeRegionCommand(systemTree.getSelectedNodeRegion());
				}
			});
			copyNodeRegionMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableNodeRegionTreeNode);
			systemTreePopupMenu.add(copyNodeRegionMenuItem);
			JMenuItem deleteNodeRegionMenuItem = new JMenuItem("Delete Node Region");
			deleteNodeRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteNodeRegionCommand(systemTree.getSelectedNodeRegion());
				}
			});
			deleteNodeRegionMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableNodeRegionTreeNode);
			systemTreePopupMenu.add(deleteNodeRegionMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem editNodeRegionsMenuItem = new JMenuItem("Edit Node Regions Table");
			editNodeRegionsMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editNodeRegionTableCommand();
				}
			});
			systemTreePopupMenu.add(editNodeRegionsMenuItem);
		} else if(path.getLastPathComponent()==systemTree.getModel().edgeRegionsTreeNode
				|| path.getLastPathComponent() instanceof MutableEdgeRegionTreeNode) {
			JMenuItem addEdgeRegionMenuItem = new JMenuItem("Add Edge Region");
			addEdgeRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addEdgeRegionCommand();
				}
			});
			systemTreePopupMenu.add(addEdgeRegionMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem editEdgeRegionMenuItem = new JMenuItem("Edit Edge Region");
			editEdgeRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editEdgeRegionCommand(systemTree.getSelectedEdgeRegion());
				}
			});
			editEdgeRegionMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableEdgeRegionTreeNode);
			systemTreePopupMenu.add(editEdgeRegionMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem copyEdgeRegionMenuItem = new JMenuItem("Copy Edge Region");
			copyEdgeRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					copyEdgeRegionCommand(systemTree.getSelectedEdgeRegion());
				}
			});
			copyEdgeRegionMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableEdgeRegionTreeNode);
			systemTreePopupMenu.add(copyEdgeRegionMenuItem);
			JMenuItem deleteEdgeRegionMenuItem = new JMenuItem("Delete Edge Region");
			deleteEdgeRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteEdgeRegionCommand(systemTree.getSelectedEdgeRegion());
				}
			});
			deleteEdgeRegionMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableEdgeRegionTreeNode);
			systemTreePopupMenu.add(deleteEdgeRegionMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem editEdgeRegionsMenuItem = new JMenuItem("Edit Edge Regions Table");
			editEdgeRegionsMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editEdgeRegionTableCommand();
				}
			});
			systemTreePopupMenu.add(editEdgeRegionsMenuItem);
		}
		return systemTreePopupMenu;
	}
	
	/**
	 * Adds the layer command.
	 */
	private void addLayerCommand() {
		System.out.println("Add Layer Command");
		Layer layer = new Layer();
		layerPanel.loadLayer(layer);
		int value = JOptionPane.showConfirmDialog(this, layerPanel,
				"City.Net | Layer", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			layerPanel.saveLayerCommand();
			system.addLayer(layer);
			systemTree.getModel().addLayer(layer);
		}
	}
	
	/**
	 * Edits the layer command.
	 *
	 * @param layer the layer
	 */
	private void editLayerCommand(Layer layer) {
		System.out.println("Edit Layer Command");
		layerPanel.loadLayer(layer);
		int value = JOptionPane.showConfirmDialog(this, layerPanel, 
				"City.Net | Layer", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			layerPanel.saveLayerCommand();
			systemTree.getModel().updateLayer(layer);
			layeredPane.repaint();
		}
	}
	
	/**
	 * Delete layers command.
	 *
	 * @param layers the layers
	 */
	private void deleteLayerCommand(Layer layer) {
		System.out.println("Delete Layer Command");
		Set<NodeRegion> dependentNodeRegions = new HashSet<NodeRegion>();
		for(NodeRegion nodeRegion : system.getNodeRegions()) {
			if(nodeRegion.getLayer().equals(layer))
				dependentNodeRegions.add(nodeRegion);
		}
		Set<EdgeRegion> dependentEdgeRegions = new HashSet<EdgeRegion>();
		for(EdgeRegion edgeRegion : system.getEdgeRegions()) {
			if(edgeRegion.getLayers().contains(layer))
				dependentEdgeRegions.add(edgeRegion);
		}
		String message = "Do you want to delete the " + layer.getName() + " layer?";
		if(dependentNodeRegions.size()>0 || dependentEdgeRegions.size()>0) {
			message += "\nThe following dependent regions will also be deleted:\n";
			for(NodeRegion nodeRegion : dependentNodeRegions)
				message += nodeRegion.getDescription() + "\n";
			for(EdgeRegion edgeRegion : dependentEdgeRegions)
				message += edgeRegion.getDescription() + "\n";
		}
		int value = JOptionPane.showConfirmDialog(this, message, "City.Net | Warning", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			for(NodeRegion nodeRegion : dependentNodeRegions) {
				system.removeNodeRegion(nodeRegion);
				systemTree.getModel().removeNodeRegion(nodeRegion);
			}
			for(EdgeRegion edgeRegion : dependentEdgeRegions) {
				system.removeEdgeRegion(edgeRegion);
				systemTree.getModel().removeEdgeRegion(edgeRegion);
			}
			system.removeLayer(layer);
			systemTree.getModel().removeLayer(layer);
			layeredPane.repaint();
		}
	}
	
	/**
	 * Adds the node type command.
	 */
	private void addNodeTypeCommand() {
		System.out.println("Add Node Type Command");
		NodeType nodeType = new NodeType();
		nodeTypePanel.loadNodeType(nodeType);
		int value = JOptionPane.showConfirmDialog(this, nodeTypePanel,
				"City.Net | NodeType", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			nodeTypePanel.saveNodeTypeCommand();
			system.addNodeType(nodeType);
			systemTree.getModel().addNodeType(nodeType);
		}
	}
	
	/**
	 * Edits the node type command.
	 *
	 * @param nodeType the node type
	 */
	private void editNodeTypeCommand(NodeType nodeType) {
		System.out.println("Edit Node Type Command");
		nodeTypePanel.loadNodeType(nodeType);
		int value = JOptionPane.showConfirmDialog(this, nodeTypePanel, 
				"City.Net | NodeType", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			nodeTypePanel.saveNodeTypeCommand();
			systemTree.getModel().updateNodeType(nodeType);
			layeredPane.repaint();
		}
	}
	
	/**
	 * Delete node type command.
	 *
	 * @param nodeTypes the node types
	 */
	private void deleteNodeTypeCommand(NodeType nodeType) {
		System.out.println("Delete Node Type Command");
		Set<NodeRegion> dependentNodeRegions = new HashSet<NodeRegion>();
		for(NodeRegion nodeRegion : system.getNodeRegions()) {
			if(nodeRegion.getNodeType().equals(nodeType))
				dependentNodeRegions.add(nodeRegion);
		}
		String message = "Do you want to delete the " + nodeType.getName() + " node type?";
		if(dependentNodeRegions.size()>0) {
			message += "\nThe following dependent node regions will also be deleted:\n";
			for(NodeRegion nodeRegion : dependentNodeRegions)
				message += nodeRegion.getDescription() + "\n";
		}
		int value = JOptionPane.showConfirmDialog(this, message, "City.Net | Warning", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			for(NodeRegion nodeRegion : dependentNodeRegions) {
				system.removeNodeRegion(nodeRegion);
				systemTree.getModel().removeNodeRegion(nodeRegion);
			}
			system.removeNodeType(nodeType);
			systemTree.getModel().removeNodeType(nodeType);
			layeredPane.repaint();
		}
	}
	
	/**
	 * Adds the edge type command.
	 */
	private void addEdgeTypeCommand() {
		System.out.println("Add Node Type Command");
		EdgeType edgeType = new EdgeType();
		edgeTypePanel.loadEdgeType(edgeType);
		int value = JOptionPane.showConfirmDialog(this, edgeTypePanel,
				"City.Net | EdgeType", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			edgeTypePanel.saveEdgeTypeCommand();
			system.addEdgeType(edgeType);
			systemTree.getModel().addEdgeType(edgeType);
			layeredPane.repaint();
		}
	}
	
	/**
	 * Edits the edge type command.
	 *
	 * @param edgeType the edge type
	 */
	private void editEdgeTypeCommand(EdgeType edgeType) {
		System.out.println("Edit Edge Type Command");
		edgeTypePanel.loadEdgeType(edgeType);
		int value = JOptionPane.showConfirmDialog(this, edgeTypePanel, 
				"City.Net | EdgeType", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			edgeTypePanel.saveEdgeTypeCommand();
			systemTree.getModel().updateEdgeType(edgeType);
			layeredPane.repaint();
		}
	}
	
	/**
	 * Delete edge types command.
	 *
	 * @param edgeTypes the edge types
	 */
	private void deleteEdgeTypeCommand(EdgeType edgeType) {
		System.out.println("Delete Edge Type Command");
		Set<EdgeRegion> dependentEdgeRegions = new HashSet<EdgeRegion>();
		for(EdgeRegion edgeRegion : system.getEdgeRegions()) {
			if(edgeRegion.getEdgeType().equals(edgeType))
				dependentEdgeRegions.add(edgeRegion);
		}
		String message = "Do you want to delete the " + edgeType.getName() + " edge type?";
		if(dependentEdgeRegions.size()>0) {
			message += "\nThe following dependent edge regions will also be deleted:\n";
			for(EdgeRegion edgeRegion : dependentEdgeRegions)
				message += edgeRegion.getDescription() + "\n";
		}
		int value = JOptionPane.showConfirmDialog(this, message, "City.Net | Warning", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			for(EdgeRegion edgeRegion : dependentEdgeRegions) {
				system.removeEdgeRegion(edgeRegion);
				systemTree.getModel().removeEdgeRegion(edgeRegion);
			}
			system.removeEdgeType(edgeType);
			systemTree.getModel().removeEdgeType(edgeType);
			layeredPane.repaint();
		}
	}
	
	/**
	 * Adds the node region command.
	 */
	private void addNodeRegionCommand() {
		System.out.println("Add Node Region Command");
		NodeRegion nodeRegion = new NodeRegion();
		nodeRegionPanel.loadNodeRegion(nodeRegion);
		int value = JOptionPane.showConfirmDialog(this, nodeRegionPanel,
				"City.Net | Node Region", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			nodeRegionPanel.saveNodeRegionCommand();
			system.addNodeRegion(nodeRegion);
			systemTree.getModel().addNodeRegion(nodeRegion);
		}
	}
	
	/**
	 * Edits the node region command.
	 *
	 * @param nodeRegion the node region
	 */
	private void editNodeRegionCommand(NodeRegion nodeRegion) {
		System.out.println("Edit Node Region Command");
		nodeRegionPanel.loadNodeRegion(nodeRegion);
		int value = JOptionPane.showConfirmDialog(this, nodeRegionPanel, 
				"City.Net | Node Region", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			nodeRegionPanel.saveNodeRegionCommand();
			systemTree.getModel().updateNodeRegion(nodeRegion);
			layeredPane.repaint();
		}
	}
	
	/**
	 * Command to copy a node region.
	 */
	private void copyNodeRegionCommand(NodeRegion region) {
		System.out.println("Copy Node Region Command");
		NodeRegion nodeRegion = region.clone();
		nodeRegionPanel.loadNodeRegion(nodeRegion);
		int value = JOptionPane.showConfirmDialog(this, nodeRegionPanel,
				"City.Net | Node Region", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			nodeRegionPanel.saveNodeRegionCommand();
			system.addNodeRegion(nodeRegion);
			systemTree.getModel().addNodeRegion(nodeRegion);
		}
	}
	
	/**
	 * Command to copy an edge region.
	 */
	private void copyEdgeRegionCommand(EdgeRegion region) {
		System.out.println("Copy Edge Region Command");
		EdgeRegion edgeRegion = region.clone();
		edgeRegionPanel.loadEdgeRegion(edgeRegion);
		int value = JOptionPane.showConfirmDialog(this, nodeRegionPanel,
				"City.Net | Edge Region", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			edgeRegionPanel.saveEdgeRegionCommand();
			system.addEdgeRegion(edgeRegion);
			systemTree.getModel().addEdgeRegion(edgeRegion);
		}
	}
	
	/**
	 * Delete node regions command.
	 *
	 * @param nodeRegions the node regions
	 */
	private void deleteNodeRegionCommand(NodeRegion nodeRegion) {
		System.out.println("Delete Node Region Command");
		int value = JOptionPane.showConfirmDialog(this, "Do you want to delete the " 
				+ nodeRegion.getDescription() + " node region?", "City.Net | Warning", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			system.removeNodeRegion(nodeRegion);
			systemTree.getModel().removeNodeRegion(nodeRegion);
			layeredPane.repaint();
		}
	}
	
	/**
	 * Adds the edge region command.
	 */
	private void addEdgeRegionCommand() {
		System.out.println("Add Edge Region Command");
		EdgeRegion edgeRegion = new EdgeRegion();
		edgeRegionPanel.loadEdgeRegion(edgeRegion);
		int value = JOptionPane.showConfirmDialog(this, edgeRegionPanel,
				"City.Net | Edge Region", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			edgeRegionPanel.saveEdgeRegionCommand();
			system.addEdgeRegion(edgeRegion);
			systemTree.getModel().addEdgeRegion(edgeRegion);
		}
	}
	
	/**
	 * Edits the edge region command.
	 *
	 * @param edgeRegion the edge region
	 */
	private void editEdgeRegionCommand(EdgeRegion edgeRegion) {
		System.out.println("Edit Edge Region Command");
		edgeRegionPanel.loadEdgeRegion(edgeRegion);
		int value = JOptionPane.showConfirmDialog(this, edgeRegionPanel,
				"City.Net | Edge Region", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			edgeRegionPanel.saveEdgeRegionCommand();
			systemTree.getModel().updateEdgeRegion(edgeRegion);
			layeredPane.repaint();
		}
	}
	
	/**
	 * Delete edge regions command.
	 */
	private void deleteEdgeRegionCommand(EdgeRegion edgeRegion) {
		System.out.println("Delete Edge Region Command");
		int value = JOptionPane.showConfirmDialog(this, "Do you want to delete the "
				+ edgeRegion.getDescription() + " edge region?", "City.Net | Warning", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			system.removeEdgeRegion(edgeRegion);
			systemTree.getModel().removeEdgeRegion(edgeRegion);
			layeredPane.repaint();
		}
	}

	/**
	 * Clear nodes command.
	 */
	public void clearNodesCommand() {
		System.out.println("Clear Nodes Command");
		if(!system.getEdges().isEmpty())
			clearEdgesCommand();
		system.removeAllNodes(system.getNodes());
	}
	
	/**
	 * Generate nodes command.
	 *
	 * @param nodeRegions the node regions
	 */
	public void generateNodesCommand(List<NodeRegion> nodeRegions) {
		System.out.println("Generate Nodes Command");
		if(!system.getNodes().isEmpty())
			clearNodesCommand();
		if(!system.getEdges().isEmpty())
			clearEdgesCommand();
		for(NodeRegion r : nodeRegions) {
			r.generateNodes(system);
		}
	}
	
	/**
	 * Clear edges command.
	 */
	public void clearEdgesCommand() {
		System.out.println("Clear Edges Command");
		system.removeAllEdges(system.getEdges());
	}
	
	/**
	 * Generate edges command.
	 *
	 * @param edgeRegions the edge regions
	 */
	public void generateEdgesCommand(List<EdgeRegion> edgeRegions) {
		System.out.println("Generate Edges Command");
		if(!system.getEdges().isEmpty())
			clearEdgesCommand();
		for(EdgeRegion r : edgeRegions) {
			r.generateEdges(system);
		}
	}
	
	/**
	 * Gets the system.
	 *
	 * @return the system
	 */
	public CitySystem getSystem() {
		return system;
	}
	
	/**
	 * Gets the city panel.
	 *
	 * @return the city panel
	 */
	public CityTabbedPane getCityPanel() {
		return cityPanel;
	}
	
	/**
	 * Edits the node region table command.
	 */
	private void editNodeRegionTableCommand() {
		System.out.println("Edit Node Region Table Command");
		NodeRegionsTable table = new NodeRegionsTable(system);
		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.addMouseListener(table.getMouseAdapter());
		JOptionPane.showMessageDialog(this, tableScroll, 
				"City.Net | Node Regions", JOptionPane.PLAIN_MESSAGE);
		if(table.getCellEditor()!=null) table.getCellEditor().stopCellEditing();
		systemTree.getModel().setSystem(system); // hacked update... bleh
		layeredPane.repaint();
	}
	
	/**
	 * Edits the node region table command.
	 */
	private void editEdgeRegionTableCommand() {
		System.out.println("Edit Edge Region Table Command");
		EdgeRegionsTable table = new EdgeRegionsTable(system);
		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.addMouseListener(table.getMouseAdapter());
		JOptionPane.showMessageDialog(this, tableScroll, 
				"City.Net | Edge Regions", JOptionPane.PLAIN_MESSAGE);
		if(table.getCellEditor()!=null) table.getCellEditor().stopCellEditing();
		systemTree.getModel().setSystem(system); // hacked update... bleh
		layeredPane.repaint();
	}
	
	/**
	 * Edits the node type table command.
	 */
	private void editNodeTypeTableCommand() {
		System.out.println("Edit Node Type Table Command");
		NodeTypesTable table = new NodeTypesTable(system);
		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.addMouseListener(table.getMouseAdapter());
		JOptionPane.showMessageDialog(this, tableScroll,
				"City.Net | Node Types", JOptionPane.PLAIN_MESSAGE);
		if(table.getCellEditor()!=null) table.getCellEditor().stopCellEditing();
		systemTree.getModel().setSystem(system); // hacked update... bleh
		layeredPane.repaint();
	}
}
