package edu.mit.citynet.viz;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.gui.EdgeRegionPanel;
import edu.mit.citynet.gui.NodeRegionPanel;
import edu.mit.citynet.gui.SystemPanel;
import edu.mit.citynet.util.CityNetIcon;

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
	private RegionTableModel<NodeRegion> nodeRegionTableModel;
	private RegionTable<NodeRegion> nodeRegionTable;
	private NodeRegionPanel nodeRegionPanel;
	private JButton editNodeRegionButton, deleteNodeRegionsButton;
	private RegionTableModel<EdgeRegion> edgeRegionTableModel;
	private RegionTable<EdgeRegion> edgeRegionTable;
	private EdgeRegionPanel edgeRegionPanel;
	private JButton editEdgeRegionButton, deleteEdgeRegionsButton;
	
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
		nodeRegionTableModel = new RegionTableModel<NodeRegion>();
		nodeRegionTableModel.setRegions(system.getNodeRegions());
		nodeRegionTableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				layeredPane.repaint();
			}
		});
		nodeRegionTable = new RegionTable<NodeRegion>(nodeRegionTableModel);
		nodeRegionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				repaint();
			}
		});
		nodeRegionTable.getTableHeader().setReorderingAllowed(false);
		nodeRegionTable.getColumnModel().getColumn(0).setMaxWidth(25);
		nodeRegionTable.getColumnModel().getColumn(0).setHeaderValue(null);
		nodeRegionTable.getColumnModel().getColumn(1).setHeaderValue("Node Region");
		nodeRegionTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 2092491034324672219L;
			
			/* (non-Javadoc)
			 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
			 */
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(value instanceof NodeRegion) {
					setText(((NodeRegion)value).getDescription());
					setIcon(((NodeRegion)value).getNodeType().getIcon());
				}
				return this;
			}
		});
		nodeRegionTable.setPreferredScrollableViewportSize(new Dimension(200,200));
		MouseAdapter nodeRegionMouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					if(nodeRegionTable.getSelectionModel().isSelectionEmpty())
						addNodeRegionCommand();
					else
						editNodeRegionCommand();
				}
			}
			public void mousePressed(MouseEvent e) {
				if(e.getComponent()!=nodeRegionTable)
					nodeRegionTable.getSelectionModel().clearSelection();
				maybeShowPopup(e);
			}
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}
			private void maybeShowPopup(MouseEvent e) {
				if(e.isPopupTrigger()) {
					int row = nodeRegionTable.rowAtPoint(e.getPoint());
					NodeRegion region = (NodeRegion)nodeRegionTableModel.getRegionAt(row);
					if(!nodeRegionTable.getSelectedRegions().contains(region)) {
						nodeRegionTable.getSelectionModel().addSelectionInterval(row, row);
					}
					createNodeRegionPopupMenu(nodeRegionTable.getSelectedRegions()).show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
		nodeRegionTable.addMouseListener(nodeRegionMouseAdapter);
		nodeRegionTable.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_DELETE) {
					deleteNodeRegionsCommand();
				} else if(e.getKeyCode()==KeyEvent.VK_ENTER && nodeRegionTable.getSelectedRowCount()==1){
					editNodeRegionCommand();
				}
			}
		});
		nodeRegionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				editNodeRegionButton.setEnabled(nodeRegionTable.getSelectedRowCount()==1);
				deleteNodeRegionsButton.setEnabled(nodeRegionTable.getSelectedRowCount()>0);
			}
		});
		JScrollPane nodeRegionScroll = new JScrollPane(nodeRegionTable);
		nodeRegionScroll.addMouseListener(nodeRegionMouseAdapter);
		leftPanel.add(nodeRegionScroll,c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel nodeRegionButtonPanel = new JPanel();
		nodeRegionButtonPanel.setLayout(new BoxLayout(nodeRegionButtonPanel,BoxLayout.LINE_AXIS));
		JButton addNodeRegionButton = new JButton("Add");
		addNodeRegionButton.setToolTipText("Add a new node region");
		addNodeRegionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNodeRegionCommand();
			}
		});
		nodeRegionButtonPanel.add(addNodeRegionButton);
		editNodeRegionButton = new JButton("Edit");
		editNodeRegionButton.setToolTipText("Edit an existing node region");
		editNodeRegionButton.setEnabled(false);
		editNodeRegionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editNodeRegionCommand();
			}
		});
		nodeRegionButtonPanel.add(editNodeRegionButton);
		deleteNodeRegionsButton = new JButton("Delete");
		deleteNodeRegionsButton.setToolTipText("Delete an existing node region");
		deleteNodeRegionsButton.setEnabled(false);
		deleteNodeRegionsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteNodeRegionsCommand();
			}
		});
		nodeRegionButtonPanel.add(deleteNodeRegionsButton);
		leftPanel.add(nodeRegionButtonPanel, c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel nodeButtonPanel = new JPanel();
		nodeButtonPanel.setLayout(new BoxLayout(nodeButtonPanel,BoxLayout.LINE_AXIS));
		JButton generateNodesButton = new JButton("Generate",CityNetIcon.NODE_REGIONS.getIcon());
		generateNodesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				systemPanel.generateNodesCommand(nodeRegionTableModel.getCheckedRegions());
				repaint();
			}
		});
		nodeButtonPanel.add(generateNodesButton);
		JButton clearNodesButton = new JButton("Clear",CityNetIcon.DELETE.getIcon());
		clearNodesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				systemPanel.clearNodesCommand();
				repaint();
			}
		});
		nodeButtonPanel.add(clearNodesButton);
		leftPanel.add(nodeButtonPanel, c);
		c.gridy++;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		edgeRegionTableModel = new RegionTableModel<EdgeRegion>();
		edgeRegionTableModel.setRegions(system.getEdgeRegions());
		edgeRegionTableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				layeredPane.repaint();
			}
		});
		edgeRegionTable = new RegionTable<EdgeRegion>(edgeRegionTableModel);
		edgeRegionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				repaint();
			}
		});
		edgeRegionTable.getTableHeader().setReorderingAllowed(false);
		edgeRegionTable.getColumnModel().getColumn(0).setMaxWidth(25);
		edgeRegionTable.getColumnModel().getColumn(0).setHeaderValue(null);
		edgeRegionTable.getColumnModel().getColumn(1).setHeaderValue("Edge Region");
		edgeRegionTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 2092491034324672219L;
			
			/* (non-Javadoc)
			 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
			 */
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(value instanceof EdgeRegion) {
					setText(((EdgeRegion)value).getDescription());
					setIcon(((EdgeRegion)value).getEdgeType().getIcon());
				}
				return this;
			}
		});
		edgeRegionTable.setPreferredScrollableViewportSize(new Dimension(200,200));
		MouseAdapter edgeRegionMouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					if(edgeRegionTable.getSelectionModel().isSelectionEmpty())
						addEdgeRegionCommand();
					else
						editEdgeRegionCommand();
				}
			}
			public void mousePressed(MouseEvent e) {
				if(e.getComponent()!=edgeRegionTable)
					edgeRegionTable.getSelectionModel().clearSelection();
				maybeShowPopup(e);
			}
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}
			private void maybeShowPopup(MouseEvent e) {
				if(e.isPopupTrigger()) {
					int row = edgeRegionTable.rowAtPoint(e.getPoint());
					EdgeRegion region = (EdgeRegion)edgeRegionTableModel.getRegionAt(row);
					if(!edgeRegionTable.getSelectedRegions().contains(region)) {
						edgeRegionTable.getSelectionModel().addSelectionInterval(row, row);
					}
					createEdgeRegionPopupMenu(edgeRegionTable.getSelectedRegions()).show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
		edgeRegionTable.addMouseListener(edgeRegionMouseAdapter);
		edgeRegionTable.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_DELETE) {
					deleteEdgeRegionsCommand();
				} else if(e.getKeyCode()==KeyEvent.VK_ENTER && edgeRegionTable.getSelectedRowCount()==1){
					editEdgeRegionCommand();
				}
			}
		});
		edgeRegionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				editEdgeRegionButton.setEnabled(edgeRegionTable.getSelectedRowCount()==1);
				deleteEdgeRegionsButton.setEnabled(edgeRegionTable.getSelectedRowCount()>0);
			}
		});
		JScrollPane edgeRegionScroll = new JScrollPane(edgeRegionTable);
		edgeRegionScroll.addMouseListener(edgeRegionMouseAdapter);
		leftPanel.add(edgeRegionScroll,c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel edgeRegionButtonPanel = new JPanel();
		edgeRegionButtonPanel.setLayout(new BoxLayout(edgeRegionButtonPanel,BoxLayout.LINE_AXIS));
		JButton addEdgeRegionButton = new JButton("Add");
		addEdgeRegionButton.setToolTipText("Add a new edge region");
		addEdgeRegionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addEdgeRegionCommand();
			}
		});
		edgeRegionButtonPanel.add(addEdgeRegionButton);
		editEdgeRegionButton = new JButton("Edit");
		editEdgeRegionButton.setToolTipText("Edit an existing edge region");
		editEdgeRegionButton.setEnabled(false);
		editEdgeRegionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editEdgeRegionCommand();
			}
		});
		edgeRegionButtonPanel.add(editEdgeRegionButton);
		deleteEdgeRegionsButton = new JButton("Delete");
		deleteEdgeRegionsButton.setToolTipText("Delete an existing edge region");
		deleteEdgeRegionsButton.setEnabled(false);
		deleteEdgeRegionsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteEdgeRegionsCommand();
			}
		});
		edgeRegionButtonPanel.add(deleteEdgeRegionsButton);
		leftPanel.add(edgeRegionButtonPanel, c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel edgeButtonPanel = new JPanel();
		edgeButtonPanel.setLayout(new BoxLayout(edgeButtonPanel,BoxLayout.LINE_AXIS));
		JButton generateEdgesButton = new JButton("Generate",CityNetIcon.EDGE_REGIONS.getIcon());
		generateEdgesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				systemPanel.generateEdgesCommand(edgeRegionTableModel.getCheckedRegions());
				repaint();
			}
		});
		edgeButtonPanel.add(generateEdgesButton);
		JButton clearEdgesButton = new JButton("Clear",CityNetIcon.DELETE.getIcon());
		clearEdgesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				systemPanel.clearEdgesCommand();
				repaint();
			}
		});
		edgeButtonPanel.add(clearEdgesButton);
		leftPanel.add(edgeButtonPanel, c);
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
	private JPopupMenu createNodeRegionPopupMenu(Set<NodeRegion> regions) {
		JPopupMenu nodeRegionPopupMenu = new JPopupMenu();
		if(regions.size()>0) {
			JMenuItem editNodeRegionMenuItem = new JMenuItem("Edit Node Region");
			editNodeRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editNodeRegionCommand();
				}
			});
			editNodeRegionMenuItem.setEnabled(regions.size()==1);
			nodeRegionPopupMenu.add(editNodeRegionMenuItem);
			JMenuItem deleteNodeRegionMenuItem = new JMenuItem("Delete Node Region" + (regions.size()>1?"s":""));
			deleteNodeRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteNodeRegionsCommand();
				}
			});
			nodeRegionPopupMenu.add(deleteNodeRegionMenuItem);
		} else {
			JMenuItem addNodeRegionMenuItem = new JMenuItem("Add Node Region");
			addNodeRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addNodeRegionCommand();
				}
			});
			nodeRegionPopupMenu.add(addNodeRegionMenuItem);
		}
		return nodeRegionPopupMenu;
	}
	
	/**
	 * Creates the edge region popup menu.
	 *
	 * @param regions the regions
	 * @return the j popup menu
	 */
	private JPopupMenu createEdgeRegionPopupMenu(Set<EdgeRegion> regions) {
		JPopupMenu edgeRegionPopupMenu = new JPopupMenu();
		if(regions.size()>0) {
			JMenuItem editEdgeRegionMenuItem = new JMenuItem("Edit Edge Region");
			editEdgeRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editEdgeRegionCommand();
				}
			});
			editEdgeRegionMenuItem.setEnabled(regions.size()==1);
			edgeRegionPopupMenu.add(editEdgeRegionMenuItem);
			JMenuItem deleteEdgeRegionMenuItem = new JMenuItem("Delete Edge Region" + (regions.size()>1?"s":""));
			deleteEdgeRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteEdgeRegionsCommand();
				}
			});
			edgeRegionPopupMenu.add(deleteEdgeRegionMenuItem);
		} else {
			JMenuItem addEdgeRegionMenuItem = new JMenuItem("Add Edge Region");
			addEdgeRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addEdgeRegionCommand();
				}
			});
			edgeRegionPopupMenu.add(addEdgeRegionMenuItem);
		}
		return edgeRegionPopupMenu;
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
			// TODO: should only add new node region to table model and update
			nodeRegionTableModel.setRegions(systemPanel.getSystem().getNodeRegions());
			repaint();
		}
	}
	
	/**
	 * Edits the node region command.
	 */
	private void editNodeRegionCommand() {
		System.out.println("Edit Node Region Command");
		nodeRegionPanel.loadNodeRegion(nodeRegionTable.getSelectedRegion());
		int value = JOptionPane.showConfirmDialog(this,nodeRegionPanel,"City.Net | Node Region", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			nodeRegionPanel.saveNodeRegionCommand();
			repaint();
		}
	}
	
	/**
	 * Delete node regions command.
	 */
	private void deleteNodeRegionsCommand() {
		System.out.println("Delete Node Regions Command");
		int value = JOptionPane.showConfirmDialog(this, "Do you want to delete these node regions?", 
				"City.Net | Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			Set<NodeRegion> regions = systemPanel.getSystem().getNodeRegions();
			regions.removeAll(nodeRegionTable.getSelectedRegions());
			// TODO: should move removeAll method to the city
			systemPanel.getSystem().setNodeRegions(regions);
			// TODO: should remove node region from table model and update
			nodeRegionTableModel.setRegions(systemPanel.getSystem().getNodeRegions());
			repaint();
		}
	}
	/**
	 * Adds the edge region command.
	 */
	private void addEdgeRegionCommand() {
		System.out.println("Add Edge Region Command");
		EdgeRegion edgeRegion = new EdgeRegion();
		edgeRegionPanel.loadEdgeRegion(edgeRegion);
		int value = JOptionPane.showConfirmDialog(this,edgeRegionPanel,"City.Net | Edge Region", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			edgeRegionPanel.saveEdgeRegionCommand();
			systemPanel.getSystem().addEdgeRegion(edgeRegion);
			// TODO: should only add new edge region to table model and update
			edgeRegionTableModel.setRegions(systemPanel.getSystem().getEdgeRegions());
			repaint();
		}
	}
	
	/**
	 * Edits the edge region command.
	 */
	private void editEdgeRegionCommand() {
		System.out.println("Edit Edge Region Command");
		edgeRegionPanel.loadEdgeRegion(edgeRegionTable.getSelectedRegion());
		int value = JOptionPane.showConfirmDialog(this,edgeRegionPanel,"City.Net | Edge Region", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			edgeRegionPanel.saveEdgeRegionCommand();
			repaint();
		}
	}
	
	/**
	 * Delete edge regions command.
	 */
	private void deleteEdgeRegionsCommand() {
		System.out.println("Delete Edge Regions Command");
		int value = JOptionPane.showConfirmDialog(this, "Do you want to delete these edge regions?", 
				"City.Net | Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			Set<EdgeRegion> regions = systemPanel.getSystem().getEdgeRegions();
			regions.removeAll(edgeRegionTable.getSelectedRegions());
			// TODO: should move removeAll method to the city
			systemPanel.getSystem().setEdgeRegions(regions);
			// TODO: should remove edge region from table model and update
			edgeRegionTableModel.setRegions(systemPanel.getSystem().getEdgeRegions());
			repaint();
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
		return nodeRegionTableModel.getCheckedRegions();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz.AbstractVizPanel#getSelectedEdgeRegions()
	 */
	public Set<EdgeRegion> getCheckedEdgeRegions() {
		return edgeRegionTableModel.getCheckedRegions();
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
		return nodeRegionTable.getSelectedRegions();
	}

	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz.AbstractVizPanel#getSelectedEdgeRegions()
	 */
	public Set<EdgeRegion> getSelectedEdgeRegions() {
		return edgeRegionTable.getSelectedRegions();
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
