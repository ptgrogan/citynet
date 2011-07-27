package edu.mit.citynet.viz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.NodeRegion;

/**
 * The RegionTableModel class provides a generic table model for use in node,
 * edge, and cell regions. It contains a list of all the available regions
 * and a subset of the regions that are selected (checked) for display.
 *
 * @param <t> the generic type
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class RegionTableModel<t> extends AbstractTableModel {
	private static final long serialVersionUID = -1151237693598337559L;
	
	private List<t> regions;
	private Set<t> selectedRegions;
	
	/**
	 * Instantiates a new node region table model.
	 */
	public RegionTableModel() {
		super();
		regions = new ArrayList<t>();
		selectedRegions = new HashSet<t>();
	}
	
	/**
	 * Sets the regions.
	 *
	 * @param regions the new regions
	 */
	public void setRegions(Set<t> regions) {
		this.regions.clear();
		selectedRegions.clear();
		this.regions.addAll(regions);
		selectedRegions.addAll(regions);
		fireTableDataChanged();
	}
	
	/**
	 * Gets the selected regions.
	 *
	 * @return the selected regions
	 */
	public Set<t> getSelectedRegions() {
		return new HashSet<t>(selectedRegions);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return regions.size();
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
			return selectedRegions.contains(regions.get(rowIndex));
		} else {
			// TODO: this is a hack-y way to display the region description
			// ...should be a superclass property/method
			if(regions.get(rowIndex) instanceof NodeRegion)
				return ((NodeRegion)regions.get(rowIndex)).getDescription();
			else if(regions.get(rowIndex) instanceof EdgeRegion)
				return ((EdgeRegion)regions.get(rowIndex)).getDescription();
			else if(regions.get(rowIndex) instanceof CellRegion)
				return ((CellRegion)regions.get(rowIndex)).getDescription();
			else
				return regions.get(rowIndex);
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
    				selectedRegions.add(regions.get(row));
    			} else {
    				selectedRegions.remove(regions.get(row));
    			}
    		}
    	}
    	fireTableDataChanged();
	}
}
