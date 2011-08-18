/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import javax.swing.table.AbstractTableModel;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.io.HexColorFormat;

/**
 * The EdgeTypesTableModel class defines a table model suitable for editing
 * multiple edge types at the same time.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class EdgeTypesTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -1005331566698742699L;
	private CitySystem system;
	
	/**
	 * Instantiates a new edge regions table model.
	 *
	 * @param system the system
	 */
	public EdgeTypesTableModel(CitySystem system) {
		this.system = system;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return system.getEdgeTypes().size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 4;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		switch(col) {
		case 0: return system.getEdgeTypes().get(row).getName();
		case 1: return system.getEdgeTypes().get(row).getDescription();
		case 2: return HexColorFormat.getStringFromColor(system.getEdgeTypes().get(row).getColor());
		case 3: return system.getEdgeTypes().get(row).getAttributes();
		default: return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int col) {
    	return true;
    }
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	public Class<?> getColumnClass(int col) {
		switch(col) {
		case 0: return String.class;
		case 1: return String.class;
		case 2: return String.class;
		case 3: return Object.class;
		default: return Object.class;
		}
    }
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object value, int row, int col) {
		switch(col) {
		case 0: 
			if(value instanceof String) 
				system.getEdgeTypes().get(row).setName((String)value); 
			break;
		case 1:
			if(value instanceof String) 
				system.getEdgeTypes().get(row).setDescription((String)value); 
			break;
		case 2:
			if(value instanceof String && HexColorFormat.isValidHexString((String)value)) 
				system.getEdgeTypes().get(row).setColor(HexColorFormat.getColorFromHexString((String)value)); 
			break;
		case 3: 
			// TODO: edit attributes
			break;
		}
    	fireTableRowsUpdated(row, row);
	}
}
