/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import javax.swing.table.AbstractTableModel;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.Layer;
import edu.mit.citynet.core.NodeRegion.NodeRegionType;
import edu.mit.citynet.core.NodeType;
import edu.mit.citynet.io.CoordinateFormat;

/**
 * The NodeRegionsTableModel class defines a table model suitable for editing
 * multiple node regions at the same time.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class NodeRegionsTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -1005331566698742699L;
	private CitySystem system;
	
	/**
	 * Instantiates a new node regions table model.
	 *
	 * @param system the system
	 */
	public NodeRegionsTableModel(CitySystem system) {
		this.system = system;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return system.getNodeRegions().size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 6;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		switch(col) {
		case 0: return system.getNodeRegions().get(row).getDescription();
		case 1: return system.getNodeRegions().get(row).getNodeType();
		case 2: return system.getNodeRegions().get(row).getNodeRegionType();
		case 3: return system.getNodeRegions().get(row).getLayer();
		case 4: return CoordinateFormat.createForMatlabSyntax(system.getNodeRegions().get(row).getCoordinateList())[0];
		case 5: return CoordinateFormat.createForMatlabSyntax(system.getNodeRegions().get(row).getCoordinateList())[1];
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
		case 1: return NodeType.class;
		case 2: return NodeRegionType.class;
		case 3: return Layer.class;
		case 4: return String.class;
		case 5: return String.class;
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
				system.getNodeRegions().get(row).setDescription((String)value); 
			break;
		case 1:
			if(value instanceof NodeType) 
				system.getNodeRegions().get(row).setNodeType((NodeType)value); 
			break;
		case 2:
			if(value instanceof NodeRegionType) 
				system.getNodeRegions().get(row).setNodeRegionType((NodeRegionType)value); 
			break;
		case 3: 
			if(value instanceof Layer) 
				system.getNodeRegions().get(row).setLayer((Layer)value); 
			break;
		case 4: 
			if(value instanceof String && CoordinateFormat.isValidMatlabSyntax((String)value)) {
				String yCoords = (String)getValueAt(row,5);
				while(CoordinateFormat.getDoubleCoordinates((String)value).length 
						> CoordinateFormat.getDoubleCoordinates(yCoords).length) {
					// add extra y-coordinate... default to 0.0
					yCoords = yCoords.substring(0, yCoords.length()-2) + " 0.0]";
				}
				while(CoordinateFormat.getDoubleCoordinates((String)value).length 
						< CoordinateFormat.getDoubleCoordinates(yCoords).length) {
					// remove extra y-coordinate... default to last
					yCoords = yCoords.substring(0, yCoords.lastIndexOf(" ")) + "]";
				}
				system.getNodeRegions().get(row).setCoordinateList(
						CoordinateFormat.createFromMatlabSyntax((String)value, yCoords)); 
			}
			break;
		case 5: 
			String xCoords = (String)getValueAt(row,4);
			while(CoordinateFormat.getDoubleCoordinates((String)value).length 
					> CoordinateFormat.getDoubleCoordinates(xCoords).length) {
				// add extra x-coordinate... default to 0.0
				xCoords = xCoords.substring(0, xCoords.length()-2) + " 0.0]";
			}
			while(CoordinateFormat.getDoubleCoordinates((String)value).length 
					< CoordinateFormat.getDoubleCoordinates(xCoords).length) {
				// remove extra x-coordinate... default to last
				xCoords = xCoords.substring(0, xCoords.lastIndexOf(" ")) + "]";
			}
			system.getNodeRegions().get(row).setCoordinateList(
					CoordinateFormat.createFromMatlabSyntax(xCoords, (String)value));
			break;
		}
    	fireTableRowsUpdated(row, row);
	}
}
