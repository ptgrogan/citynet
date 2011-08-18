/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.core.NodeRegion.NodeRegionType;

/**
 * The NodeRegionsTable class provides a view for editing muliple node regions
 * at the same time in a tabular format.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class NodeRegionsTable extends JTable {
	private static final long serialVersionUID = 5057151807789312277L;
	private CitySystem system;
	private MouseAdapter mouseAdapter;
	/**
	 * Instantiates a new node regions table.
	 *
	 * @param model the model
	 */
	public NodeRegionsTable(CitySystem system) {
		super(new NodeRegionsTableModel(system));
		this.system = system;
		initializeTable();
	}
	
	/**
	 * Initialize table.
	 */
	private void initializeTable() {
		getTableHeader().setReorderingAllowed(false);
		getColumnModel().getColumn(0).setHeaderValue("Description");
		getColumnModel().getColumn(1).setHeaderValue("Node Type");
		getColumnModel().getColumn(1).setCellRenderer(RendererFactory.createNodeTypeTableCellRenderer());
		JComboBox nodeTypeCombo = new JComboBox(system.getNodeTypes().toArray());
		nodeTypeCombo.setRenderer(RendererFactory.createNodeTypeListCellRenderer());
		getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(nodeTypeCombo));
		getColumnModel().getColumn(2).setHeaderValue("Region Type");
		getColumnModel().getColumn(2).setCellRenderer(RendererFactory.createNodeRegionTypeTableCellRenderer());
		JComboBox nodeRegionTypeCombo = new JComboBox(NodeRegionType.values());
		nodeRegionTypeCombo.setRenderer(RendererFactory.createNodeRegionTypeListCellRenderer());
		getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(nodeRegionTypeCombo));
		getColumnModel().getColumn(3).setHeaderValue("Layer");
		getColumnModel().getColumn(3).setCellRenderer(RendererFactory.createLayerTableCellRenderer());
		JComboBox layerCombo = new JComboBox(system.getLayers().toArray());
		layerCombo.setRenderer(RendererFactory.createLayerListCellRenderer());
		getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(layerCombo));
		getColumnModel().getColumn(4).setHeaderValue("X-Coordinates");
		getColumnModel().getColumn(5).setHeaderValue("Y-Coordinates");
		setPreferredScrollableViewportSize(new Dimension(600,400));
		mouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(! (e.getSource() instanceof NodeRegionsTable)) {
					getSelectionModel().clearSelection();
				}
				if(e.getClickCount()==2 && ! (e.getSource() instanceof NodeRegionsTable)) {
					addNodeRegionCommand();
				}
			}
			public void mousePressed(MouseEvent e) {
				maybeShowPopup(e);
			}
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}
			private void maybeShowPopup(MouseEvent e) {
				if(e.isPopupTrigger()) {
					int row = rowAtPoint(e.getPoint());
					if(row > 0) 
						getSelectionModel().addSelectionInterval(row,row);
					createNodeRegionPopupMenu(getSelectedNodeRegions()).show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
		addMouseListener(mouseAdapter);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_DELETE) {
					deleteNodeRegionsCommand(getSelectedNodeRegions());
				}
			}
		});
	}
	
	/**
	 * Adds the node region command.
	 */
	private void addNodeRegionCommand() {
		System.out.println("Add Node Region Command");
		NodeRegion region = new NodeRegion();
		if(system.getNodeTypes().size()>0) region.setNodeType(system.getNodeTypes().get(0));
		region.setNodeRegionType(NodeRegionType.POLYGON);
		if(system.getLayers().size()>0) region.setLayer(system.getLayers().get(0));
		region.setCoordinateList(new CoordinateList(new Coordinate[]{new Coordinate(0,0)}));
		system.addNodeRegion(region);
		getModel().fireTableRowsInserted(system.getNodeRegions().indexOf(region), 
				system.getNodeRegions().indexOf(region));
	}
	
	private void copyNodeRegionCommand(NodeRegion region) {
		System.out.println("Copy Node Region Command");
		NodeRegion clone = region.clone();
		system.addNodeRegion(clone);
		getModel().fireTableRowsInserted(system.getNodeRegions().indexOf(clone), 
				system.getNodeRegions().indexOf(clone));
		
	}
	
	/**
	 * Delete node regions command.
	 *
	 * @param regions the regions
	 */
	private void deleteNodeRegionsCommand(List<NodeRegion> regions) {
		System.out.println("Delete Node Regions Command");
		int value = JOptionPane.showConfirmDialog(this, 
				"Do you want to delete " + (regions.size()>1?"these":"this") 
				+ " node region" + (regions.size()>1?"s":"") + "?", 
				"City.Net | Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			system.removeAllNodeRegions(regions);
			getModel().fireTableDataChanged();
		}
	}
	
	/**
	 * Creates the node region popup menu.
	 *
	 * @param regions the regions
	 * @return the j popup menu
	 */
	private JPopupMenu createNodeRegionPopupMenu(final List<NodeRegion> regions) {
		JPopupMenu nodeRegionPopupMenu = new JPopupMenu();
		JMenuItem addNodeRegionMenuItem = new JMenuItem("Add Node Region");
		addNodeRegionMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNodeRegionCommand();
			}
		});
		nodeRegionPopupMenu.add(addNodeRegionMenuItem);
		nodeRegionPopupMenu.add(new JSeparator());
		JMenuItem copyNodeRegionMenuItem = new JMenuItem("Copy Node Region");
		copyNodeRegionMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyNodeRegionCommand(regions.get(0));
			}
		});
		copyNodeRegionMenuItem.setEnabled(regions.size()==1);
		nodeRegionPopupMenu.add(copyNodeRegionMenuItem);
		JMenuItem deleteNodeRegionsMenuItem = new JMenuItem("Delete Node Region" + (regions.size()>1?"s":""));
		deleteNodeRegionsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteNodeRegionsCommand(regions);
			}
		});
		deleteNodeRegionsMenuItem.setEnabled(regions.size()>0);
		nodeRegionPopupMenu.add(deleteNodeRegionsMenuItem);
		return nodeRegionPopupMenu;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JTable#getModel()
	 */
	public NodeRegionsTableModel getModel() {
		return (NodeRegionsTableModel)super.getModel();
	}
	
	/**
	 * Gets the selected node regions.
	 *
	 * @return the selected node regions
	 */
	public List<NodeRegion> getSelectedNodeRegions() {
		List<NodeRegion> regions = new ArrayList<NodeRegion>();
		for(int i : getSelectedRows()) regions.add(system.getNodeRegions().get(i));
		return regions;
	}
	
	/**
	 * Gets the mouse adapter.
	 *
	 * @return the mouse adapter
	 */
	public MouseAdapter getMouseAdapter() {
		return mouseAdapter;
	}
}