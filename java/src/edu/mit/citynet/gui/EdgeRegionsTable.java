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
import edu.mit.citynet.core.EdgeDirection;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.EdgeRegion.EdgeRegionType;

/**
 * The EdgeRegionsTable class provides a view for editing multiple edge regions
 * at the same time in a tabular format.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class EdgeRegionsTable extends JTable {
	private static final long serialVersionUID = 5057151807789312277L;
	private CitySystem system;
	private MouseAdapter mouseAdapter;
	/**
	 * Instantiates a new edge regions table.
	 *
	 * @param model the model
	 */
	public EdgeRegionsTable(CitySystem system) {
		super(new EdgeRegionsTableModel(system));
		this.system = system;
		initializeTable();
	}
	
	/**
	 * Initialize table.
	 */
	private void initializeTable() {
		getTableHeader().setReorderingAllowed(false);
		getColumnModel().getColumn(0).setHeaderValue("Description");
		getColumnModel().getColumn(1).setHeaderValue("Edge Type");
		getColumnModel().getColumn(1).setCellRenderer(RendererFactory.createEdgeTypeTableCellRenderer());
		JComboBox edgeTypeCombo = new JComboBox(system.getEdgeTypes().toArray());
		edgeTypeCombo.setRenderer(RendererFactory.createEdgeTypeListCellRenderer());
		getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(edgeTypeCombo));
		getColumnModel().getColumn(2).setHeaderValue("Region Type");
		getColumnModel().getColumn(2).setCellRenderer(RendererFactory.createEdgeRegionTypeTableCellRenderer());
		JComboBox edgeRegionTypeCombo = new JComboBox(EdgeRegionType.values());
		edgeRegionTypeCombo.setRenderer(RendererFactory.createEdgeRegionTypeListCellRenderer());
		getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(edgeRegionTypeCombo));
		getColumnModel().getColumn(3).setHeaderValue("Direction");
		getColumnModel().getColumn(3).setCellRenderer(RendererFactory.createEdgeDirectionTableCellRenderer());
		JComboBox directionCombo = new JComboBox(EdgeDirection.values());
		directionCombo.setRenderer(RendererFactory.createEdgeDirectionListCellRenderer());
		getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(directionCombo));
		getColumnModel().getColumn(4).setHeaderValue("X-Coordinates");
		getColumnModel().getColumn(5).setHeaderValue("Y-Coordinates");
		getColumnModel().getColumn(6).setHeaderValue("Layers");
		setPreferredScrollableViewportSize(new Dimension(600,400));
		mouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(! (e.getSource() instanceof EdgeRegionsTable)) {
					getSelectionModel().clearSelection();
				}
				if(e.getClickCount()==2 && ! (e.getSource() instanceof EdgeRegionsTable)) {
					addEdgeRegionCommand();
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
					createEdgeRegionPopupMenu(getSelectedEdgeRegions()).show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
		addMouseListener(mouseAdapter);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_DELETE) {
					deleteEdgeRegionsCommand(getSelectedEdgeRegions());
				}
			}
		});
	}
	
	/**
	 * Adds the edge region command.
	 */
	private void addEdgeRegionCommand() {
		System.out.println("Add Edge Region Command");
		EdgeRegion region = new EdgeRegion();
		if(system.getEdgeTypes().size()>0) region.setEdgeType(system.getEdgeTypes().get(0));
		region.setEdgeRegionType(EdgeRegionType.POLYGON_ORTHOGONAL);
		region.setCoordinateList(new CoordinateList(new Coordinate[]{new Coordinate(0,0)}));
		if(system.getLayers().size()>0) region.getLayers().add(system.getLayers().get(0));
		system.addEdgeRegion(region);
		getModel().fireTableRowsInserted(system.getEdgeRegions().indexOf(region), 
				system.getEdgeRegions().indexOf(region));
	}
	
	/**
	 * Copy edge region command.
	 *
	 * @param region the region
	 */
	private void copyEdgeRegionCommand(EdgeRegion region) {
		System.out.println("Copy Edge Region Command");
		EdgeRegion clone = region.clone();
		system.addEdgeRegion(clone);
		getModel().fireTableRowsInserted(system.getEdgeRegions().indexOf(clone), 
				system.getEdgeRegions().indexOf(clone));
		
	}
	
	/**
	 * Delete edge regions command.
	 *
	 * @param regions the regions
	 */
	private void deleteEdgeRegionsCommand(List<EdgeRegion> regions) {
		System.out.println("Delete Edge Regions Command");
		int value = JOptionPane.showConfirmDialog(this, 
				"Do you want to delete " + (regions.size()>1?"these":"this") 
				+ " edge region" + (regions.size()>1?"s":"") + "?", 
				"City.Net | Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			system.removeAllEdgeRegions(regions);
			getModel().fireTableDataChanged();
		}
	}
	
	/**
	 * Creates the node region popup menu.
	 *
	 * @param regions the regions
	 * @return the j popup menu
	 */
	private JPopupMenu createEdgeRegionPopupMenu(final List<EdgeRegion> regions) {
		JPopupMenu nodeRegionPopupMenu = new JPopupMenu();
		JMenuItem addEdgeRegionMenuItem = new JMenuItem("Add Edge Region");
		addEdgeRegionMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addEdgeRegionCommand();
			}
		});
		nodeRegionPopupMenu.add(addEdgeRegionMenuItem);
		nodeRegionPopupMenu.add(new JSeparator());
		JMenuItem copyEdgeRegionMenuItem = new JMenuItem("Copy Edge Region");
		copyEdgeRegionMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyEdgeRegionCommand(regions.get(0));
			}
		});
		copyEdgeRegionMenuItem.setEnabled(regions.size()==1);
		nodeRegionPopupMenu.add(copyEdgeRegionMenuItem);
		JMenuItem deleteEdgeRegionsMenuItem = new JMenuItem("Delete Edge Region" + (regions.size()>1?"s":""));
		deleteEdgeRegionsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteEdgeRegionsCommand(regions);
			}
		});
		deleteEdgeRegionsMenuItem.setEnabled(regions.size()>0);
		nodeRegionPopupMenu.add(deleteEdgeRegionsMenuItem);
		return nodeRegionPopupMenu;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JTable#getModel()
	 */
	public EdgeRegionsTableModel getModel() {
		return (EdgeRegionsTableModel)super.getModel();
	}
	
	/**
	 * Gets the selected edge regions.
	 *
	 * @return the selected edge regions
	 */
	public List<EdgeRegion> getSelectedEdgeRegions() {
		List<EdgeRegion> regions = new ArrayList<EdgeRegion>();
		for(int i : getSelectedRows()) regions.add(system.getEdgeRegions().get(i));
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
