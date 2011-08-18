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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTable;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.core.NodeType;

/**
 * The NodeTypesTable class provides a view for editing multiple node types
 * at the same time in a tabular format.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class NodeTypesTable extends JTable {
	private static final long serialVersionUID = 5057151807789312277L;
	private CitySystem system;
	private MouseAdapter mouseAdapter;
	/**
	 * Instantiates a new node regions table.
	 *
	 * @param model the model
	 */
	public NodeTypesTable(CitySystem system) {
		super(new NodeTypesTableModel(system));
		this.system = system;
		initializeTable();
	}
	
	/**
	 * Initialize table.
	 */
	private void initializeTable() {
		getTableHeader().setReorderingAllowed(false);
		getColumnModel().getColumn(0).setHeaderValue("Name");
		getColumnModel().getColumn(1).setHeaderValue("Description");
		getColumnModel().getColumn(2).setHeaderValue("Color");
		getColumnModel().getColumn(2).setCellRenderer(RendererFactory.createHexColorTableCellRenderer());
		getColumnModel().getColumn(3).setHeaderValue("Attributes");
		// TODO: atributes renderer
		setPreferredScrollableViewportSize(new Dimension(600,400));
		mouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(! (e.getSource() instanceof NodeTypesTable)) {
					getSelectionModel().clearSelection();
				}
				if(e.getClickCount()==2 && ! (e.getSource() instanceof NodeTypesTable)) {
					addNodeTypeCommand();
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
					createNodeTypePopupMenu(getSelectedNodeTypes()).show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
		addMouseListener(mouseAdapter);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_DELETE) {
					deleteNodeTypesCommand(getSelectedNodeTypes());
				}
			}
		});
	}
	
	/**
	 * Adds the node type command.
	 */
	private void addNodeTypeCommand() {
		System.out.println("Add Node Type Command");
		NodeType type = new NodeType();
		system.addNodeType(type);
		getModel().fireTableRowsInserted(system.getNodeRegions().indexOf(type), 
				system.getNodeRegions().indexOf(type));
	}
	
	/**
	 * Copy node type command.
	 *
	 * @param type the type
	 */
	private void copyNodeTypeCommand(NodeType type) {
		System.out.println("Copy Node Type Command");
		NodeType clone = type.clone();
		system.addNodeType(clone);
		getModel().fireTableRowsInserted(system.getNodeTypes().indexOf(clone), 
				system.getNodeTypes().indexOf(clone));
		
	}
	
	/**
	 * Delete node types command.
	 *
	 * @param types the types
	 */
	private void deleteNodeTypesCommand(List<NodeType> types) {
		System.out.println("Delete Node Types Command");
		Set<NodeRegion> dependentNodeRegions = new HashSet<NodeRegion>();
		for(NodeType type : types) {
			for(NodeRegion nodeRegion : system.getNodeRegions()) {
				if(nodeRegion.getNodeType().equals(type))
					dependentNodeRegions.add(nodeRegion);
			}
		}
		String message = "Do you want to delete " + (types.size()>1?"these":"this") 
			+ " node type" + (types.size()>1?"s":"") + "?";
		if(dependentNodeRegions.size()>0) {
			message += "\nThe following dependent node regions will also be deleted:\n";
			for(NodeRegion nodeRegion : dependentNodeRegions)
				message += nodeRegion.getDescription() + "\n";
		}
		int value = JOptionPane.showConfirmDialog(this, message, "City.Net | Warning", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			system.removeAllNodeRegions(dependentNodeRegions);
			system.removeAllNodeTypes(types);
			getModel().fireTableDataChanged();
		}
	}
	
	/**
	 * Creates the node type popup menu.
	 *
	 * @param types the types
	 * @return the j popup menu
	 */
	private JPopupMenu createNodeTypePopupMenu(final List<NodeType> types) {
		JPopupMenu nodeRegionPopupMenu = new JPopupMenu();
		JMenuItem addNodeTypeMenuItem = new JMenuItem("Add Node Type");
		addNodeTypeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNodeTypeCommand();
			}
		});
		nodeRegionPopupMenu.add(addNodeTypeMenuItem);
		nodeRegionPopupMenu.add(new JSeparator());
		JMenuItem copyNodeTypeMenuItem = new JMenuItem("Copy Node Type");
		copyNodeTypeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyNodeTypeCommand(types.get(0));
			}
		});
		copyNodeTypeMenuItem.setEnabled(types.size()==1);
		nodeRegionPopupMenu.add(copyNodeTypeMenuItem);
		JMenuItem deleteNodeTypesMenuItem = new JMenuItem("Delete Node Type" + (types.size()>1?"s":""));
		deleteNodeTypesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteNodeTypesCommand(types);
			}
		});
		deleteNodeTypesMenuItem.setEnabled(types.size()>0);
		nodeRegionPopupMenu.add(deleteNodeTypesMenuItem);
		return nodeRegionPopupMenu;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JTable#getModel()
	 */
	public NodeTypesTableModel getModel() {
		return (NodeTypesTableModel)super.getModel();
	}
	
	/**
	 * Gets the selected node types.
	 *
	 * @return the selected node types
	 */
	public List<NodeType> getSelectedNodeTypes() {
		List<NodeType> types = new ArrayList<NodeType>();
		for(int i : getSelectedRows()) types.add(system.getNodeTypes().get(i));
		return types;
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
