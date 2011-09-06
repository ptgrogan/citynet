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
import edu.mit.citynet.core.EdgeGenerationType;
import edu.mit.citynet.core.IntraLayerRegion;
import edu.mit.citynet.core.NodeGenerationType;
import edu.mit.citynet.core.Region;

/**
 * The RegionsTable class provides a view for editing multiple regions
 * at the same time in a tabular format.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class RegionsTable extends JTable {
	private static final long serialVersionUID = 5057151807789312277L;
	private CitySystem system;
	private MouseAdapter mouseAdapter;
	/**
	 * Instantiates a new node regions table.
	 *
	 * @param model the model
	 */
	public RegionsTable(CitySystem system) {
		super(new RegionsTableModel(system));
		this.system = system;
		initializeTable();
	}
	
	/**
	 * Initialize table.
	 */
	private void initializeTable() {
		getTableHeader().setReorderingAllowed(false);
		getColumnModel().getColumn(0).setHeaderValue("Description");
		getColumnModel().getColumn(1).setHeaderValue("Layer");
		getColumnModel().getColumn(1).setCellRenderer(RendererFactory.createLayerTableCellRenderer());
		JComboBox layerCombo = new JComboBox(system.getLayers().toArray());
		layerCombo.setRenderer(RendererFactory.createLayerListCellRenderer());
		getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(layerCombo));
		getColumnModel().getColumn(2).setHeaderValue("Node Generation");
		getColumnModel().getColumn(2).setCellRenderer(RendererFactory.createNodeGenerationTypeTableCellRenderer());
		JComboBox nodeGenerationTypeCombo = new JComboBox(NodeGenerationType.values());
		nodeGenerationTypeCombo.setRenderer(RendererFactory.createNodeGenerationTypeListCellRenderer());
		getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(nodeGenerationTypeCombo));
		getColumnModel().getColumn(3).setHeaderValue("Node Type");
		getColumnModel().getColumn(3).setCellRenderer(RendererFactory.createNodeTypeTableCellRenderer());
		JComboBox nodeTypeCombo = new JComboBox(system.getNodeTypes().toArray());
		nodeTypeCombo.setRenderer(RendererFactory.createNodeTypeListCellRenderer());
		getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(nodeTypeCombo));
		getColumnModel().getColumn(4).setHeaderValue("Edge Generation");
		getColumnModel().getColumn(4).setCellRenderer(RendererFactory.createEdgeGenerationTypeTableCellRenderer());
		JComboBox edgeGenerationTypeCombo = new JComboBox(EdgeGenerationType.values());
		edgeGenerationTypeCombo.setRenderer(RendererFactory.createEdgeGenerationTypeListCellRenderer());
		getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(edgeGenerationTypeCombo));
		getColumnModel().getColumn(5).setHeaderValue("Edge Type");
		getColumnModel().getColumn(5).setCellRenderer(RendererFactory.createEdgeTypeTableCellRenderer());
		JComboBox edgeTypeCombo = new JComboBox(system.getEdgeTypes().toArray());
		edgeTypeCombo.setRenderer(RendererFactory.createEdgeTypeListCellRenderer());
		getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(edgeTypeCombo));
		getColumnModel().getColumn(6).setHeaderValue("Edge Direction");
		getColumnModel().getColumn(6).setCellRenderer(RendererFactory.createEdgeDirectionTableCellRenderer());
		JComboBox edgeDirectionCombo = new JComboBox(EdgeDirection.values());
		edgeDirectionCombo.setRenderer(RendererFactory.createEdgeDirectionListCellRenderer());
		getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(edgeDirectionCombo));
		getColumnModel().getColumn(7).setHeaderValue("X-Coordinates");
		getColumnModel().getColumn(8).setHeaderValue("Y-Coordinates");
		setPreferredScrollableViewportSize(new Dimension(800,400));
		mouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(! (e.getSource() instanceof RegionsTable)) {
					getSelectionModel().clearSelection();
				}
				if(e.getClickCount()==2 && ! (e.getSource() instanceof RegionsTable)) {
					addRegionCommand();
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
					createRegionPopupMenu(getSelectedRegions()).show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
		addMouseListener(mouseAdapter);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_DELETE) {
					deleteRegionsCommand(getSelectedRegions());
				}
			}
		});
	}
	
	/**
	 * Adds the region command.
	 */
	private void addRegionCommand() {
		System.out.println("Add Region Command");
		// TODO: add interlayerregion
		IntraLayerRegion region = new IntraLayerRegion();
		if(system.getLayers().size()>0) region.setLayer(system.getLayers().get(0));
		region.setNodeGenerationType(NodeGenerationType.POLYGON);
		if(system.getNodeTypes().size()>0) region.setNodeType(system.getNodeTypes().get(0));
		region.setEdgeGenerationType(EdgeGenerationType.NONE);
		if(system.getEdgeTypes().size()>0) region.setEdgeType(system.getEdgeTypes().get(0));
		region.setEdgeDirection(EdgeDirection.UNDIRECTED);
		region.setCoordinateList(new CoordinateList(new Coordinate[]{new Coordinate(0,0)}));
		system.addRegion(region);
		getModel().fireTableRowsInserted(system.getRegions().indexOf(region), 
				system.getRegions().indexOf(region));
	}
	
	/**
	 * Copy region command.
	 *
	 * @param region the region
	 */
	private void copyRegionCommand(Region region) {
		System.out.println("Copy Region Command");
		Region clone = region.clone();
		system.addRegion(clone);
		getModel().fireTableRowsInserted(system.getRegions().indexOf(clone), 
				system.getRegions().indexOf(clone));
		
	}
	
	/**
	 * Delete regions command.
	 *
	 * @param regions the regions
	 */
	private void deleteRegionsCommand(List<Region> regions) {
		System.out.println("Delete Regions Command");
		int value = JOptionPane.showConfirmDialog(this, 
				"Do you want to delete " + (regions.size()>1?"these":"this") 
				+ " region" + (regions.size()>1?"s":"") + "?", 
				"City.Net | Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			system.removeAllRegions(regions);
			getModel().fireTableDataChanged();
		}
	}
	
	/**
	 * Creates the region popup menu.
	 *
	 * @param regions the regions
	 * @return the j popup menu
	 */
	private JPopupMenu createRegionPopupMenu(final List<Region> regions) {
		JPopupMenu regionPopupMenu = new JPopupMenu();
		JMenuItem addRegionMenuItem = new JMenuItem("Add Region");
		addRegionMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addRegionCommand();
			}
		});
		regionPopupMenu.add(addRegionMenuItem);
		regionPopupMenu.add(new JSeparator());
		JMenuItem copyRegionMenuItem = new JMenuItem("Copy Region");
		copyRegionMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyRegionCommand(regions.get(0));
			}
		});
		copyRegionMenuItem.setEnabled(regions.size()==1);
		regionPopupMenu.add(copyRegionMenuItem);
		JMenuItem deleteRegionsMenuItem = new JMenuItem("Delete Region" + (regions.size()>1?"s":""));
		deleteRegionsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteRegionsCommand(regions);
			}
		});
		deleteRegionsMenuItem.setEnabled(regions.size()>0);
		regionPopupMenu.add(deleteRegionsMenuItem);
		regionPopupMenu.add(new JSeparator());
		return regionPopupMenu;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JTable#getModel()
	 */
	public RegionsTableModel getModel() {
		return (RegionsTableModel)super.getModel();
	}
	
	/**
	 * Gets the selected regions.
	 *
	 * @return the selected regions
	 */
	public List<Region> getSelectedRegions() {
		List<Region> regions = new ArrayList<Region>();
		for(int i : getSelectedRows()) regions.add(system.getRegions().get(i));
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
