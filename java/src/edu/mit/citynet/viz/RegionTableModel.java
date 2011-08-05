package edu.mit.citynet.viz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	private Set<t> checkedRegions;
	
	/**
	 * Instantiates a new node region table model.
	 */
	public RegionTableModel() {
		super();
		regions = new ArrayList<t>();
		checkedRegions = new HashSet<t>();
	}
	
	/**
	 * Sets the regions.
	 *
	 * @param regions the new regions
	 */
	public void setRegions(Set<t> regions) {
		this.regions.clear();
		checkedRegions.clear();
		this.regions.addAll(regions);
		Collections.sort(this.regions, new Comparator<t>() {
			public int compare(t region1, t region2) {
				// TODO: this is a hack-y way to sort based on description
				// ...should be a superclass property/method
				if(region1 instanceof NodeRegion && region2 instanceof NodeRegion)
					return ((NodeRegion)region1).getDescription().compareTo(((NodeRegion)region2).getDescription());
				else if(region1 instanceof EdgeRegion && region2 instanceof EdgeRegion)
					return ((EdgeRegion)region1).getDescription().compareTo(((EdgeRegion)region2).getDescription());
				else if(region1 instanceof CellRegion && region2 instanceof CellRegion)
					return ((CellRegion)region1).getDescription().compareTo(((CellRegion)region2).getDescription());
				else return 0;
			}
		});
		checkedRegions.addAll(regions);
		fireTableDataChanged();
	}
	
	/**
	 * Gets the region at a row.
	 *
	 * @param row the row
	 * @return the region
	 */
	public t getRegionAt(int row) {
		if(row >= 0 && row < regions.size())
			return regions.get(row);
		else return null;
	}
	
	/**
	 * Gets the checked regions.
	 *
	 * @return the checked regions
	 */
	public Set<t> getCheckedRegions() {
		return new HashSet<t>(checkedRegions);
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
			return checkedRegions.contains(regions.get(rowIndex));
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
    				checkedRegions.add(regions.get(row));
    			} else {
    				checkedRegions.remove(regions.get(row));
    			}
    		}
    	}
    	fireTableRowsUpdated(row, row);
	}
}
