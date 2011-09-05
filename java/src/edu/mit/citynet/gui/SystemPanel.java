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
import edu.mit.citynet.core.Region;
import edu.mit.citynet.gui.SystemTreeModel.MutableEdgeRegionTreeNode;
import edu.mit.citynet.gui.SystemTreeModel.MutableEdgeTypeTreeNode;
import edu.mit.citynet.gui.SystemTreeModel.MutableLayerTreeNode;
import edu.mit.citynet.gui.SystemTreeModel.MutableNodeRegionTreeNode;
import edu.mit.citynet.gui.SystemTreeModel.MutableNodeTypeTreeNode;
import edu.mit.citynet.gui.SystemTreeModel.MutableRegionTreeNode;
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
	private RegionPanel regionPanel;
	
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
		regionPanel = new RegionPanel(this);
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
				layeredPane.setSelectedRegion(systemTree.getSelectedRegion());
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
							== systemTree.getModel().regionsTreeNode)
						editRegionTableCommand();
					else if(systemTree.getSelectedRegion()!=null)
						editRegionCommand(systemTree.getSelectedRegion());
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
						editEdgeTypeTableCommand();
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
				generateRegionsCommand(system.getRegions());
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
			JMenuItem addMenuItem = new JMenuItem("Add Layer");
			addMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addLayerCommand();
				}
			});
			systemTreePopupMenu.add(addMenuItem);
			JMenuItem editMenuItem = new JMenuItem("Edit Layer");
			editMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editLayerCommand(systemTree.getSelectedLayer());
				}
			});
			editMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableLayerTreeNode);
			systemTreePopupMenu.add(editMenuItem);
			JMenuItem deleteMenuItem = new JMenuItem("Delete Layer");
			deleteMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteLayerCommand(systemTree.getSelectedLayer());
				}
			});
			deleteMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableLayerTreeNode);
			systemTreePopupMenu.add(deleteMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem moveUpMenuItem = new JMenuItem("Move Layer Up");
			moveUpMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Layer layer = systemTree.getSelectedLayer();
					if(system.moveLayerTo(layer, system.getLayers().indexOf(layer)-1))
						systemTree.setSystem(system); // hack-y update
				}
			});
			moveUpMenuItem.setEnabled(path.getLastPathComponent() instanceof MutableLayerTreeNode &&
					system.getLayers().indexOf(systemTree.getSelectedLayer())>0);
			systemTreePopupMenu.add(moveUpMenuItem);
			JMenuItem moveDownMenuItem = new JMenuItem("Move Layer Down");
			moveDownMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Layer layer = systemTree.getSelectedLayer();
					if(system.moveLayerTo(layer, system.getLayers().indexOf(layer)+1))
						systemTree.setSystem(system); // hack-y update
				}
			});
			moveDownMenuItem.setEnabled(path.getLastPathComponent() instanceof MutableLayerTreeNode &&
					system.getLayers().indexOf(systemTree.getSelectedLayer())<system.getLayers().size()-1);
			systemTreePopupMenu.add(moveDownMenuItem);
		} else if(path.getLastPathComponent()==systemTree.getModel().nodeTypesTreeNode
				|| path.getLastPathComponent() instanceof MutableNodeTypeTreeNode) {
			JMenuItem addMenuItem = new JMenuItem("Add Node Type");
			addMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addNodeTypeCommand();
				}
			});
			systemTreePopupMenu.add(addMenuItem);
			JMenuItem editMenuItem = new JMenuItem("Edit Node Type");
			editMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editNodeTypeCommand(systemTree.getSelectedNodeType());
				}
			});
			editMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableNodeTypeTreeNode);
			systemTreePopupMenu.add(editMenuItem);
			JMenuItem deleteMenuItem = new JMenuItem("Delete Node Type");
			deleteMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteNodeTypeCommand(systemTree.getSelectedNodeType());
				}
			});
			deleteMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableNodeTypeTreeNode);
			systemTreePopupMenu.add(deleteMenuItem);
			JMenuItem copyMenuItem = new JMenuItem("Copy Node Type");
			copyMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					copyNodeTypeCommand(systemTree.getSelectedNodeType());
				}
			});
			copyMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableNodeTypeTreeNode);
			systemTreePopupMenu.add(copyMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem editNodeTypeTableMenuItem = new JMenuItem("Edit Node Types Table");
			editNodeTypeTableMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editNodeTypeTableCommand();
				}
			});
			systemTreePopupMenu.add(editNodeTypeTableMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem moveUpMenuItem = new JMenuItem("Move Node Type Up");
			moveUpMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NodeType type = systemTree.getSelectedNodeType();
					if(system.moveNodeTypeTo(type, system.getNodeTypes().indexOf(type)-1))
						systemTree.setSystem(system); // hack-y update
				}
			});
			moveUpMenuItem.setEnabled(path.getLastPathComponent() instanceof MutableNodeTypeTreeNode &&
					system.getNodeTypes().indexOf(systemTree.getSelectedNodeType())>0);
			systemTreePopupMenu.add(moveUpMenuItem);
			JMenuItem moveDownMenuItem = new JMenuItem("Move Node Type Down");
			moveDownMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NodeType type = systemTree.getSelectedNodeType();
					if(system.moveNodeTypeTo(type, system.getNodeTypes().indexOf(type)+1))
						systemTree.setSystem(system); // hack-y update
				}
			});
			moveDownMenuItem.setEnabled(path.getLastPathComponent() instanceof MutableNodeTypeTreeNode &&
					system.getNodeTypes().indexOf(systemTree.getSelectedNodeType())<system.getNodeTypes().size()-1);
			systemTreePopupMenu.add(moveDownMenuItem);
		} else if(path.getLastPathComponent()==systemTree.getModel().edgeTypesTreeNode
				|| path.getLastPathComponent() instanceof MutableEdgeTypeTreeNode) {
			JMenuItem addMenuItem = new JMenuItem("Add Edge Type");
			addMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addEdgeTypeCommand();
				}
			});
			systemTreePopupMenu.add(addMenuItem);
			JMenuItem editMenuItem = new JMenuItem("Edit Edge Type");
			editMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editEdgeTypeCommand(systemTree.getSelectedEdgeType());
				}
			});
			editMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableEdgeTypeTreeNode);
			systemTreePopupMenu.add(editMenuItem);
			JMenuItem deleteMenuItem = new JMenuItem("Delete Edge Type");
			deleteMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteEdgeTypeCommand(systemTree.getSelectedEdgeType());
				}
			});
			deleteMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableEdgeTypeTreeNode);
			systemTreePopupMenu.add(deleteMenuItem);
			JMenuItem copyMenuItem = new JMenuItem("Copy Edge Type");
			copyMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					copyEdgeTypeCommand(systemTree.getSelectedEdgeType());
				}
			});
			copyMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableEdgeTypeTreeNode);
			systemTreePopupMenu.add(copyMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem editEdgeTypeTableMenuItem = new JMenuItem("Edit Edge Types Table");
			editEdgeTypeTableMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editEdgeTypeTableCommand();
				}
			});
			systemTreePopupMenu.add(editEdgeTypeTableMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem moveUpMenuItem = new JMenuItem("Move Edge Type Up");
			moveUpMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					EdgeType type = systemTree.getSelectedEdgeType();
					if(system.moveEdgeTypeTo(type, system.getEdgeTypes().indexOf(type)-1))
						systemTree.setSystem(system); // hack-y update
				}
			});
			moveUpMenuItem.setEnabled(path.getLastPathComponent() instanceof MutableEdgeTypeTreeNode &&
					system.getEdgeTypes().indexOf(systemTree.getSelectedEdgeType())>0);
			systemTreePopupMenu.add(moveUpMenuItem);
			JMenuItem moveDownMenuItem = new JMenuItem("Move Edge Type Down");
			moveDownMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					EdgeType type = systemTree.getSelectedEdgeType();
					if(system.moveEdgeTypeTo(type, system.getEdgeTypes().indexOf(type)+1))
						systemTree.setSystem(system); // hack-y update
				}
			});
			moveDownMenuItem.setEnabled(path.getLastPathComponent() instanceof MutableEdgeTypeTreeNode &&
					system.getEdgeTypes().indexOf(systemTree.getSelectedEdgeType())<system.getEdgeTypes().size()-1);
			systemTreePopupMenu.add(moveDownMenuItem);
		} else if(path.getLastPathComponent()==systemTree.getModel().nodeRegionsTreeNode
				|| path.getLastPathComponent() instanceof MutableNodeRegionTreeNode) {
			JMenuItem addMenuItem = new JMenuItem("Add Node Region");
			addMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addNodeRegionCommand();
				}
			});
			systemTreePopupMenu.add(addMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem editMenuItem = new JMenuItem("Edit Node Region");
			editMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editNodeRegionCommand(systemTree.getSelectedNodeRegion());
				}
			});
			editMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableNodeRegionTreeNode);
			systemTreePopupMenu.add(editMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem copyMenuItem = new JMenuItem("Copy Node Region");
			copyMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					copyNodeRegionCommand(systemTree.getSelectedNodeRegion());
				}
			});
			copyMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableNodeRegionTreeNode);
			systemTreePopupMenu.add(copyMenuItem);
			JMenuItem deleteMenuItem = new JMenuItem("Delete Node Region");
			deleteMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteNodeRegionCommand(systemTree.getSelectedNodeRegion());
				}
			});
			deleteMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableNodeRegionTreeNode);
			systemTreePopupMenu.add(deleteMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem editNodeRegionsMenuItem = new JMenuItem("Edit Node Regions Table");
			editNodeRegionsMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editNodeRegionTableCommand();
				}
			});
			systemTreePopupMenu.add(editNodeRegionsMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem moveUpMenuItem = new JMenuItem("Move Node Region Up");
			moveUpMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NodeRegion type = systemTree.getSelectedNodeRegion();
					if(system.moveNodeRegionTo(type, system.getNodeRegions().indexOf(type)-1))
						systemTree.setSystem(system); // hack-y update
				}
			});
			moveUpMenuItem.setEnabled(path.getLastPathComponent() instanceof MutableNodeRegionTreeNode &&
					system.getNodeRegions().indexOf(systemTree.getSelectedNodeRegion())>0);
			systemTreePopupMenu.add(moveUpMenuItem);
			JMenuItem moveDownMenuItem = new JMenuItem("Move Node Region Down");
			moveDownMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NodeRegion type = systemTree.getSelectedNodeRegion();
					if(system.moveNodeRegionTo(type, system.getNodeRegions().indexOf(type)+1))
						systemTree.setSystem(system); // hack-y update
				}
			});
			moveDownMenuItem.setEnabled(path.getLastPathComponent() instanceof MutableNodeRegionTreeNode &&
					system.getNodeRegions().indexOf(systemTree.getSelectedNodeRegion())<system.getNodeRegions().size()-1);
			systemTreePopupMenu.add(moveDownMenuItem);
		} else if(path.getLastPathComponent()==systemTree.getModel().edgeRegionsTreeNode
				|| path.getLastPathComponent() instanceof MutableEdgeRegionTreeNode) {
			JMenuItem addMenuItem = new JMenuItem("Add Edge Region");
			addMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addEdgeRegionCommand();
				}
			});
			systemTreePopupMenu.add(addMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem editMenuItem = new JMenuItem("Edit Edge Region");
			editMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editEdgeRegionCommand(systemTree.getSelectedEdgeRegion());
				}
			});
			editMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableEdgeRegionTreeNode);
			systemTreePopupMenu.add(editMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem copyMenuItem = new JMenuItem("Copy Edge Region");
			copyMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					copyEdgeRegionCommand(systemTree.getSelectedEdgeRegion());
				}
			});
			copyMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableEdgeRegionTreeNode);
			systemTreePopupMenu.add(copyMenuItem);
			JMenuItem deleteMenuItem = new JMenuItem("Delete Edge Region");
			deleteMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteEdgeRegionCommand(systemTree.getSelectedEdgeRegion());
				}
			});
			deleteMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableEdgeRegionTreeNode);
			systemTreePopupMenu.add(deleteMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem editEdgeRegionsMenuItem = new JMenuItem("Edit Edge Regions Table");
			editEdgeRegionsMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editEdgeRegionTableCommand();
				}
			});
			systemTreePopupMenu.add(editEdgeRegionsMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem moveUpMenuItem = new JMenuItem("Move Edge Region Up");
			moveUpMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					EdgeRegion type = systemTree.getSelectedEdgeRegion();
					if(system.moveEdgeRegionTo(type, system.getEdgeRegions().indexOf(type)-1))
						systemTree.setSystem(system); // hack-y update
				}
			});
			moveUpMenuItem.setEnabled(path.getLastPathComponent() instanceof MutableEdgeRegionTreeNode &&
					system.getEdgeRegions().indexOf(systemTree.getSelectedEdgeRegion())>0);
			systemTreePopupMenu.add(moveUpMenuItem);
			JMenuItem moveDownMenuItem = new JMenuItem("Move Edge Region Down");
			moveDownMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					EdgeRegion type = systemTree.getSelectedEdgeRegion();
					if(system.moveEdgeRegionTo(type, system.getEdgeRegions().indexOf(type)+1))
						systemTree.setSystem(system); // hack-y update
				}
			});
			moveDownMenuItem.setEnabled(path.getLastPathComponent() instanceof MutableEdgeRegionTreeNode &&
					system.getEdgeRegions().indexOf(systemTree.getSelectedEdgeRegion())<system.getEdgeRegions().size()-1);
			systemTreePopupMenu.add(moveDownMenuItem);
		} else if(path.getLastPathComponent()==systemTree.getModel().regionsTreeNode
				|| path.getLastPathComponent() instanceof MutableRegionTreeNode) {
			JMenuItem addMenuItem = new JMenuItem("Add Region");
			addMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addRegionCommand();
				}
			});
			systemTreePopupMenu.add(addMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem editMenuItem = new JMenuItem("Edit Region");
			editMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editRegionCommand(systemTree.getSelectedRegion());
				}
			});
			editMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableRegionTreeNode);
			systemTreePopupMenu.add(editMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem copyMenuItem = new JMenuItem("Copy Region");
			copyMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					copyRegionCommand(systemTree.getSelectedRegion());
				}
			});
			copyMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableRegionTreeNode);
			systemTreePopupMenu.add(copyMenuItem);
			JMenuItem deleteMenuItem = new JMenuItem("Delete Region");
			deleteMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteRegionCommand(systemTree.getSelectedRegion());
				}
			});
			deleteMenuItem.setEnabled(path.getLastPathComponent() 
					instanceof MutableRegionTreeNode);
			systemTreePopupMenu.add(deleteMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem editRegionsMenuItem = new JMenuItem("Edit Regions Table");
			editRegionsMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editRegionTableCommand();
				}
			});
			systemTreePopupMenu.add(editRegionsMenuItem);
			systemTreePopupMenu.add(new JSeparator());
			JMenuItem moveUpMenuItem = new JMenuItem("Move Region Up");
			moveUpMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Region type = systemTree.getSelectedRegion();
					if(system.moveRegionTo(type, system.getRegions().indexOf(type)-1))
						systemTree.setSystem(system); // hack-y update
				}
			});
			moveUpMenuItem.setEnabled(path.getLastPathComponent() instanceof MutableRegionTreeNode &&
					system.getRegions().indexOf(systemTree.getSelectedRegion())>0);
			systemTreePopupMenu.add(moveUpMenuItem);
			JMenuItem moveDownMenuItem = new JMenuItem("Move Region Down");
			moveDownMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Region type = systemTree.getSelectedRegion();
					if(system.moveRegionTo(type, system.getRegions().indexOf(type)+1))
						systemTree.setSystem(system); // hack-y update
				}
			});
			moveDownMenuItem.setEnabled(path.getLastPathComponent() instanceof MutableRegionTreeNode &&
					system.getRegions().indexOf(systemTree.getSelectedRegion())<system.getRegions().size()-1);
			systemTreePopupMenu.add(moveDownMenuItem);
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
				"City.Net | Node Type", JOptionPane.OK_CANCEL_OPTION, 
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
				"City.Net | Node Type", JOptionPane.OK_CANCEL_OPTION, 
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
	 * Copy node type command.
	 *
	 * @param type the type
	 */
	private void copyNodeTypeCommand(NodeType type) {
		System.out.println("Copy Node Type Command");
		NodeType nodeType = type.clone();
		nodeTypePanel.loadNodeType(nodeType);
		int value = JOptionPane.showConfirmDialog(this, nodeTypePanel,
				"City.Net | Node Type", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			nodeTypePanel.saveNodeTypeCommand();
			system.addNodeType(nodeType);
			systemTree.getModel().addNodeType(nodeType);
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
				"City.Net | Edge Type", JOptionPane.OK_CANCEL_OPTION, 
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
				"City.Net | Edge Type", JOptionPane.OK_CANCEL_OPTION, 
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
	 * Copy edge type command.
	 *
	 * @param type the type
	 */
	private void copyEdgeTypeCommand(EdgeType type) {
		System.out.println("Copy Edge Type Command");
		EdgeType edgeType = type.clone();
		edgeTypePanel.loadEdgeType(edgeType);
		int value = JOptionPane.showConfirmDialog(this, edgeTypePanel,
				"City.Net | Edge Type", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			edgeTypePanel.saveEdgeTypeCommand();
			system.addEdgeType(edgeType);
			systemTree.getModel().addEdgeType(edgeType);
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
			layeredPane.repaint();
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
			layeredPane.repaint();
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
	 * Adds the edge region command.
	 */
	private void addRegionCommand() {
		System.out.println("Add Region Command");
		Region region = new Region();
		regionPanel.loadRegion(region);
		int value = JOptionPane.showConfirmDialog(this, regionPanel,
				"City.Net | Region", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			regionPanel.saveRegionCommand();
			system.addRegion(region);
			systemTree.getModel().addRegion(region);
			layeredPane.repaint();
		}
	}
	
	/**
	 * Edits the  region command.
	 *
	 * @param region the edge region
	 */
	private void editRegionCommand(Region region) {
		System.out.println("Edit Region Command");
		regionPanel.loadRegion(region);
		int value = JOptionPane.showConfirmDialog(this, regionPanel,
				"City.Net | Region", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			regionPanel.saveRegionCommand();
			systemTree.getModel().updateRegion(region);
			layeredPane.repaint();
		}
	}
	
	/**
	 * Delete regions command.
	 *
	 * @param region the region
	 */
	private void deleteRegionCommand(Region region) {
		System.out.println("Delete Region Command");
		int value = JOptionPane.showConfirmDialog(this, "Do you want to delete the "
				+ region.getDescription() + " region?", "City.Net | Warning", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			system.removeRegion(region);
			systemTree.getModel().removeRegion(region);
			layeredPane.repaint();
		}
	}
	/**
	 * Command to copy a region.
	 */
	private void copyRegionCommand(Region region) {
		System.out.println("Copy Region Command");
		Region newRegion = region.clone();
		regionPanel.loadRegion(newRegion);
		int value = JOptionPane.showConfirmDialog(this, nodeRegionPanel,
				"City.Net | Region", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			regionPanel.saveRegionCommand();
			system.addRegion(newRegion);
			systemTree.getModel().addRegion(newRegion);
		}
	}
	
	/**
	 * Edits the region table command.
	 */
	private void editRegionTableCommand() {
		System.out.println("Edit Region Table Command");
		RegionsTable table = new RegionsTable(system);
		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.addMouseListener(table.getMouseAdapter());
		JOptionPane.showMessageDialog(this, tableScroll, 
				"City.Net | Regions", JOptionPane.PLAIN_MESSAGE);
		if(table.getCellEditor()!=null) table.getCellEditor().stopCellEditing();
		systemTree.setSystem(system); // hacked update... bleh
		layeredPane.repaint();
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
	 * Generate nodes and edges from regions command.
	 *
	 * @param regions the regions
	 */
	public void generateRegionsCommand(List<Region> regions) {
		System.out.println("Generate Regions Command");
		if(!system.getNodes().isEmpty())
			clearNodesCommand();
		if(!system.getEdges().isEmpty())
			clearEdgesCommand();
		for(Region r : regions) {
			r.generateRegion(system);
		}
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
		systemTree.setSystem(system); // hacked update... bleh
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
		systemTree.setSystem(system); // hacked update... bleh
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
		systemTree.setSystem(system); // hacked update... bleh
		layeredPane.repaint();
	}
	
	/**
	 * Edits the edge type table command.
	 */
	private void editEdgeTypeTableCommand() {
		System.out.println("Edit Edge Type Table Command");
		EdgeTypesTable table = new EdgeTypesTable(system);
		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.addMouseListener(table.getMouseAdapter());
		JOptionPane.showMessageDialog(this, tableScroll,
				"City.Net | Edge Types", JOptionPane.PLAIN_MESSAGE);
		if(table.getCellEditor()!=null) table.getCellEditor().stopCellEditing();
		systemTree.setSystem(system); // hacked update... bleh
		layeredPane.repaint();
	}
}
