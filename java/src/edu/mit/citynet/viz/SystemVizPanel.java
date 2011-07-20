package edu.mit.citynet.viz;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.gui.CityPanel;

public class SystemVizPanel extends AbstractVizPanel {
	private static final long serialVersionUID = -3650203268180181634L;
	
	private CityPanel cityPanel;
	private CitySystem system;
	private VizLayeredPane layeredPane;
	private NodeRegionTableModel nodeRegionTableModel;
	
	public SystemVizPanel(CityPanel cityPanel, CitySystem system) {
		if (cityPanel==null) {
			throw new IllegalArgumentException("City Panel cannot be null.");
		}
		if (system==null) {
			throw new IllegalArgumentException("System cannot be null.");
		}
		this.cityPanel = cityPanel;
		this.system = system;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		nodeRegionTableModel = new NodeRegionTableModel();
		nodeRegionTableModel.setNodeRegions(system);
		nodeRegionTableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				layeredPane.repaint();
			}
		});
		JTable nodeRegionTable = new JTable(nodeRegionTableModel);
		nodeRegionTable.setTableHeader(null);
		nodeRegionTable.getColumnModel().getColumn(0).setMaxWidth(25);
		nodeRegionTable.setPreferredScrollableViewportSize(new Dimension(100,100));
		add(new JScrollPane(nodeRegionTable),c);
		c.gridx++;
		layeredPane = new VizLayeredPane(this, cityPanel.getCity(), system);
		layeredPane.setPreferredSize(new Dimension(250,250));
		add(layeredPane,c);
	}
	
	/**
	 * Gets the city panel.
	 *
	 * @return the city panel
	 */
	public CityPanel getCityPanel() {
		return cityPanel;
	}
	
	/**
	 * Gets the system.
	 *
	 * @return the system
	 */
	public CitySystem getSystem() {
		return system;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz2.AbstractVizPanel#getSelectedNodeRegions()
	 */
	public Set<NodeRegion> getSelectedNodeRegions() {
		return nodeRegionTableModel.getSelectedRegions();
	}
	
	/**
	 * The NodeRegionTableModel class establishes a custom table model for use
	 * with the node regions table. The model allows the selection of up to one
	 * node region from the list of all regions.
	 */
	private class NodeRegionTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 5386287393549485163L;
		private List<NodeRegion> nodeRegions;
		private Set<NodeRegion> selectedRegions;
		
		/**
		 * Instantiates a new node region table model.
		 */
		public NodeRegionTableModel() {
			super();
			nodeRegions = new ArrayList<NodeRegion>();
			selectedRegions = new HashSet<NodeRegion>();
		}
		
		/**
		 * Sets the node regions.
		 *
		 * @param city the new node regions
		 */
		public void setNodeRegions(CitySystem system) {
			nodeRegions.clear();
			selectedRegions.clear();
			nodeRegions.addAll(system.getNodeRegions());
			selectedRegions.addAll(system.getNodeRegions());
			fireTableDataChanged();
		}
		
		/**
		 * Gets the selected regions.
		 *
		 * @return the selected regions
		 */
		public Set<NodeRegion> getSelectedRegions() {
			return new HashSet<NodeRegion>(selectedRegions);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			return nodeRegions.size();
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return 2;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(columnIndex==0) {
				return selectedRegions.contains(nodeRegions.get(rowIndex));
			} else {
				return nodeRegions.get(rowIndex).getDescription();
			}
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
		public boolean isCellEditable(int row, int col) {
	    	if(col==0) return true;
	    	else return false;
	    }
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
		public Class<?> getColumnClass(int col) {
			if(col==0) return Boolean.class;
			else return Object.class;
	    }
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
		 */
		public void setValueAt(Object value, int row, int col) {
	    	if(col==0) {
	    		if(value instanceof Boolean) {
	    			if((Boolean)value) {
	    				selectedRegions.add(nodeRegions.get(row));
	    			} else {
	    				selectedRegions.remove(nodeRegions.get(row));
	    			}
	    		}
	    	}
	    	fireTableDataChanged();
		}
	}
}
