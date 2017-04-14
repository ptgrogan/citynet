/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import edu.mit.citynet.core.AbstractAttribute;

/**
 * The AttributeTableModel class.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 *
 * @param <t> the generic type
 */
public class AttributeTableModel<t extends AbstractAttribute> extends AbstractTableModel {
	private static final long serialVersionUID = -3945107108006904722L;

	private List<t> attributes;
	
	/**
	 * Instantiates a new attribute table model.
	 */
	public AttributeTableModel() {
		attributes = new ArrayList<t>();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return attributes.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 5;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		if(col==0) return attributes.get(row).getName();
		if(col==1) return attributes.get(row).getDescription();
		if(col==2) return attributes.get(row).getUnits();
		if(col==3) return attributes.get(row).getBounds();
		if(col==4) return attributes.get(row).getValue();
		else return null;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	public Class<?> getColumnClass(int col) {
		if(col==4) return Double.class;
		else return String.class;
    }
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int col) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object value, int row, int col) {
    	if(col==0 && value instanceof String)  {
    		attributes.get(row).setName((String)value);
    	} else if(col==1 && value instanceof String)  {
    		attributes.get(row).setDescription((String)value);
    	} else if(col==2 && value instanceof String)  {
    		attributes.get(row).setUnits((String)value);
    	} else if(col==3 && value instanceof String)  {
    		attributes.get(row).setBounds((String)value);
    	} else if(col==4 && value instanceof Double)  {
    		attributes.get(row).setValue((Double)value);
    	}
    	fireTableRowsUpdated(row, row);
	}
	
	/**
	 * Sets the attributes.
	 *
	 * @param attributes the new attributes
	 */
	public void setAttributes(Collection<t> attributes) {
		this.attributes.clear();
		this.attributes.addAll(attributes);
		fireTableDataChanged();
	}

	/**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
	public List<t> getAttributes() {
		return attributes;
	}
}
