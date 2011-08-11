package edu.mit.citynet.viz;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.gui.EdgeRegionPanel;
import edu.mit.citynet.gui.NodeRegionPanel;
import edu.mit.citynet.gui.SystemPanel;
import edu.mit.citynet.util.CityNetIcon;
import edu.mit.citynet.viz.SystemTreeModel.MutableEdgeRegionTreeNode;
import edu.mit.citynet.viz.SystemTreeModel.MutableNodeRegionTreeNode;

/**
 * The SystemVizPanel class provides a system-level visualization of cells, 
 * node regions and nodes, and edge regions and edges. The node region and
 * edge region visibility can be toggled using two tables.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class SystemVizPanel extends AbstractVizPanel {
	private static final long serialVersionUID = -3650203268180181634L;
	
	private SystemPanel systemPanel;
	private CitySystem system;
	private VizLayeredPane layeredPane;
	private SystemTreeModel systemTreeModel;
	private SystemTree systemTree;
	private NodeRegionPanel nodeRegionPanel;
	private EdgeRegionPanel edgeRegionPanel;
	
	/**
	 * Instantiates a new system viz panel.
	 *
	 * @param systemPanel the system panel
	 * @param system the system
	 */
	public SystemVizPanel(SystemPanel systemPanel, CitySystem system) {
		if (systemPanel==null) {
			throw new IllegalArgumentException("System Panel cannot be null.");
		}
		this.systemPanel = systemPanel;
		this.system = system;
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
				layeredPane.repaint();
			}
		});
		systemTree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2 && systemTree.getSelectionCount() > 0) {
					if(systemTree.getSelectionPath().getLastPathComponent() 
							== systemTree.getModel().nodeRegionsTreeNode)
						addNodeRegionCommand();
					else if(systemTree.getSelectedNodeRegion()!=null)
						editNodeRegionCommand(systemTree.getSelectedNodeRegion());
					else if(systemTree.getSelectionPath().getLastPathComponent() 
							== systemTree.getModel().edgeRegionsTreeNode)
						addEdgeRegionCommand();
					else if(systemTree.getSelectedEdgeRegion()!=null)
						editEdgeRegionCommand(systemTree.getSelectedEdgeRegion());
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
					createSystemTreePopupMenu(systemTree.getSelectionPaths()).show(
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
				systemPanel.generateNodesCommand(systemPanel.getSystem().getNodeRegions());
				systemPanel.generateEdgesCommand(systemPanel.getSystem().getEdgeRegions());
				layeredPane.repaint();
			}
		});
		generationButtonPanel.add(generateNodesButton);
		JButton clearButton = new JButton("Clear",CityNetIcon.DELETE.getIcon());
		clearButton.setToolTipText("Clear all nodes and edges");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				systemPanel.clearNodesCommand();
				layeredPane.repaint();
			}
		});
		generationButtonPanel.add(clearButton);
		leftPanel.add(generationButtonPanel, c);
		setLeftComponent(leftPanel);
		JPanel rightPanel = new JPanel(new BorderLayout());
		layeredPane = new VizLayeredPane(this, systemPanel.getCityPanel().getCity(), system);
		rightPanel.add(layeredPane,BorderLayout.CENTER);
		setRightComponent(rightPanel);
	}
	
	/**
	 * Creates the node region popup menu.
	 *
	 * @param regions the regions
	 * @return the j popup menu
	 */
	private JPopupMenu createSystemTreePopupMenu(TreePath[] paths) {
		JPopupMenu systemTreePopupMenu = new JPopupMenu();
		if(paths.length==1) {
			TreePath path = paths[0];
			if(path.getLastPathComponent()==systemTree.getModel().nodeRegionsTreeNode
					|| path.getLastPathComponent() instanceof MutableNodeRegionTreeNode) {
				JMenuItem addNodeRegionMenuItem = new JMenuItem("Add Node Region");
				addNodeRegionMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						addNodeRegionCommand();
					}
				});
				systemTreePopupMenu.add(addNodeRegionMenuItem);
				JMenuItem editNodeRegionMenuItem = new JMenuItem("Edit Node Region");
				editNodeRegionMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						editNodeRegionCommand(systemTree.getSelectedNodeRegion());
					}
				});
				editNodeRegionMenuItem.setEnabled(path.getLastPathComponent() 
						instanceof MutableNodeRegionTreeNode);
				systemTreePopupMenu.add(editNodeRegionMenuItem);
				JMenuItem deleteNodeRegionMenuItem = new JMenuItem("Delete Node Region");
				deleteNodeRegionMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						deleteNodeRegionCommand(systemTree.getSelectedNodeRegion());
					}
				});
				deleteNodeRegionMenuItem.setEnabled(path.getLastPathComponent() 
						instanceof MutableNodeRegionTreeNode);
				systemTreePopupMenu.add(deleteNodeRegionMenuItem);
			}
			if(path.getLastPathComponent()==systemTree.getModel().edgeRegionsTreeNode
					|| path.getLastPathComponent() instanceof MutableEdgeRegionTreeNode) {
				JMenuItem addEdgeRegionMenuItem = new JMenuItem("Add Edge Region");
				addEdgeRegionMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						addEdgeRegionCommand();
					}
				});
				systemTreePopupMenu.add(addEdgeRegionMenuItem);
				JMenuItem editEdgeRegionMenuItem = new JMenuItem("Edit Edge Region");
				editEdgeRegionMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						editEdgeRegionCommand(systemTree.getSelectedEdgeRegion());
					}
				});
				editEdgeRegionMenuItem.setEnabled(path.getLastPathComponent() 
						instanceof MutableEdgeRegionTreeNode);
				systemTreePopupMenu.add(editEdgeRegionMenuItem);
				JMenuItem deleteEdgeRegionMenuItem = new JMenuItem("Delete Edge Region");
				deleteEdgeRegionMenuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						deleteEdgeRegionCommand(systemTree.getSelectedEdgeRegion());
					}
				});
				deleteEdgeRegionMenuItem.setEnabled(path.getLastPathComponent() 
						instanceof MutableEdgeRegionTreeNode);
				systemTreePopupMenu.add(deleteEdgeRegionMenuItem);
			}
		}
		return systemTreePopupMenu;
	}
	
	/**
	 * Adds the node region command.
	 */
	private void addNodeRegionCommand() {
		System.out.println("Add Node Region Command");
		NodeRegion nodeRegion = new NodeRegion();
		nodeRegionPanel.loadNodeRegion(nodeRegion);
		int value = JOptionPane.showConfirmDialog(this,nodeRegionPanel,"City.Net | Node Region", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			nodeRegionPanel.saveNodeRegionCommand();
			systemPanel.getSystem().addNodeRegion(nodeRegion);
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
		int value = JOptionPane.showConfirmDialog(this,nodeRegionPanel,"City.Net | Node Region", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			nodeRegionPanel.saveNodeRegionCommand();
			systemTree.getModel().updateNodeRegion(nodeRegion);
		}
	}
	
	/**
	 * Delete node regions command.
	 *
	 * @param nodeRegions the node regions
	 */
	private void deleteNodeRegionCommand(NodeRegion nodeRegion) {
		System.out.println("Delete Node Region Command");
		int value = JOptionPane.showConfirmDialog(this, "Do you want to delete " 
				+ nodeRegion.getDescription() + "?", 
				"City.Net | Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			systemPanel.getSystem().removeNodeRegion(nodeRegion);
			systemTree.getModel().removeNodeRegion(nodeRegion);
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
			systemPanel.getSystem().addEdgeRegion(edgeRegion);
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
		}
	}
	
	/**
	 * Delete edge regions command.
	 */
	private void deleteEdgeRegionCommand(EdgeRegion edgeRegion) {
		System.out.println("Delete Edge Region Command");
		int value = JOptionPane.showConfirmDialog(this, "Do you want to delete "
				+ edgeRegion.getDescription() + "?", 
				"City.Net | Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			systemPanel.getSystem().removeEdgeRegion(edgeRegion);
			systemTree.getModel().removeEdgeRegion(edgeRegion);
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz.AbstractVizPanel#getSelectedCellRegions()
	 */
	public Set<CellRegion> getCheckedCellRegions() {
		return new HashSet<CellRegion>();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz2.AbstractVizPanel#getSelectedNodeRegions()
	 */
	public Set<NodeRegion> getCheckedNodeRegions() {
		return system.getNodeRegions();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz.AbstractVizPanel#getSelectedEdgeRegions()
	 */
	public Set<EdgeRegion> getCheckedEdgeRegions() {
		return system.getEdgeRegions();
	}

	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz.AbstractVizPanel#getSelectedCellRegions()
	 */
	public Set<CellRegion> getSelectedCellRegions() {
		return new HashSet<CellRegion>();
	}

	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz.AbstractVizPanel#getSelectedNodeRegions()
	 */
	public Set<NodeRegion> getSelectedNodeRegions() {
		Set<NodeRegion> nodeRegions = new HashSet<NodeRegion>();
		nodeRegions.add(systemTree.getSelectedNodeRegion());
		return nodeRegions;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz.AbstractVizPanel#getSelectedEdgeRegions()
	 */
	public Set<EdgeRegion> getSelectedEdgeRegions() {
		Set<EdgeRegion> edgeRegions = new HashSet<EdgeRegion>();
		edgeRegions.add(systemTree.getSelectedEdgeRegion());
		return edgeRegions;
	}
	
	/**
	 * Gets the system panel.
	 *
	 * @return the system panel
	 */
	public SystemPanel getSystemPanel() {
		return systemPanel;
	}
}
