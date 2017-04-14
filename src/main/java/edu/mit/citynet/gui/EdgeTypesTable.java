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
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.EdgeType;

/**
 * The EdgeTypesTable class provides a view for editing multiple edge types
 * at the same time in a tabular format.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class EdgeTypesTable extends JTable {
	private static final long serialVersionUID = 5057151807789312277L;
	private CitySystem system;
	private MouseAdapter mouseAdapter;
	/**
	 * Instantiates a new edge regions table.
	 *
	 * @param model the model
	 */
	public EdgeTypesTable(CitySystem system) {
		super(new EdgeTypesTableModel(system));
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
		// TODO: attributes renderer
		setPreferredScrollableViewportSize(new Dimension(600,400));
		mouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(! (e.getSource() instanceof EdgeTypesTable)) {
					getSelectionModel().clearSelection();
				}
				if(e.getClickCount()==2 && ! (e.getSource() instanceof EdgeTypesTable)) {
					addEdgeTypeCommand();
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
					createEdgeTypePopupMenu(getSelectedEdgeTypes()).show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
		addMouseListener(mouseAdapter);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_DELETE) {
					deleteEdgeTypesCommand(getSelectedEdgeTypes());
				}
			}
		});
	}
	
	/**
	 * Adds the edge type command.
	 */
	private void addEdgeTypeCommand() {
		System.out.println("Add Edge Type Command");
		EdgeType type = new EdgeType();
		system.addEdgeType(type);
		getModel().fireTableRowsInserted(system.getEdgeRegions().indexOf(type), 
				system.getEdgeRegions().indexOf(type));
	}
	
	/**
	 * Copy edge type command.
	 *
	 * @param type the type
	 */
	private void copyEdgeTypeCommand(EdgeType type) {
		System.out.println("Copy Edge Type Command");
		EdgeType clone = type.clone();
		system.addEdgeType(clone);
		getModel().fireTableRowsInserted(system.getEdgeTypes().indexOf(clone), 
				system.getEdgeTypes().indexOf(clone));
		
	}
	
	/**
	 * Delete edge types command.
	 *
	 * @param types the types
	 */
	private void deleteEdgeTypesCommand(List<EdgeType> types) {
		System.out.println("Delete Edge Types Command");
		Set<EdgeRegion> dependentEdgeRegions = new HashSet<EdgeRegion>();
		for(EdgeType type : types) {
			for(EdgeRegion edgeRegion : system.getEdgeRegions()) {
				if(edgeRegion.getEdgeType().equals(type))
					dependentEdgeRegions.add(edgeRegion);
			}
		}
		String message = "Do you want to delete " + (types.size()>1?"these":"this") 
			+ " edge type" + (types.size()>1?"s":"") + "?";
		if(dependentEdgeRegions.size()>0) {
			message += "\nThe following dependent edge regions will also be deleted:\n";
			for(EdgeRegion edgeRegion : dependentEdgeRegions)
				message += edgeRegion.getDescription() + "\n";
		}
		int value = JOptionPane.showConfirmDialog(this, message, "City.Net | Warning", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			system.removeAllEdgeRegions(dependentEdgeRegions);
			system.removeAllEdgeTypes(types);
			getModel().fireTableDataChanged();
		}
	}
	
	/**
	 * Creates the edge type popup menu.
	 *
	 * @param types the types
	 * @return the j popup menu
	 */
	private JPopupMenu createEdgeTypePopupMenu(final List<EdgeType> types) {
		JPopupMenu edgeRegionPopupMenu = new JPopupMenu();
		JMenuItem addEdgeTypeMenuItem = new JMenuItem("Add Edge Type");
		addEdgeTypeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addEdgeTypeCommand();
			}
		});
		edgeRegionPopupMenu.add(addEdgeTypeMenuItem);
		edgeRegionPopupMenu.add(new JSeparator());
		JMenuItem copyEdgeTypeMenuItem = new JMenuItem("Copy Edge Type");
		copyEdgeTypeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyEdgeTypeCommand(types.get(0));
			}
		});
		copyEdgeTypeMenuItem.setEnabled(types.size()==1);
		edgeRegionPopupMenu.add(copyEdgeTypeMenuItem);
		JMenuItem deleteEdgeTypesMenuItem = new JMenuItem("Delete Edge Type" + (types.size()>1?"s":""));
		deleteEdgeTypesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteEdgeTypesCommand(types);
			}
		});
		deleteEdgeTypesMenuItem.setEnabled(types.size()>0);
		edgeRegionPopupMenu.add(deleteEdgeTypesMenuItem);
		return edgeRegionPopupMenu;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JTable#getModel()
	 */
	public EdgeTypesTableModel getModel() {
		return (EdgeTypesTableModel)super.getModel();
	}
	
	/**
	 * Gets the selected edge types.
	 *
	 * @return the selected edge types
	 */
	public List<EdgeType> getSelectedEdgeTypes() {
		List<EdgeType> types = new ArrayList<EdgeType>();
		for(int i : getSelectedRows()) types.add(system.getEdgeTypes().get(i));
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
