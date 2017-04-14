/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import javax.swing.table.AbstractTableModel;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.EdgeDirection;
import edu.mit.citynet.core.EdgeGenerationType;
import edu.mit.citynet.core.EdgeType;
import edu.mit.citynet.core.InterLayerRegion;
import edu.mit.citynet.core.IntraLayerRegion;
import edu.mit.citynet.core.Layer;
import edu.mit.citynet.core.NodeGenerationType;
import edu.mit.citynet.core.NodeType;
import edu.mit.citynet.io.CoordinateFormat;

/**
 * The RegionsTableModel class defines a table model suitable for editing
 * multiple regions at the same time.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class RegionsTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -1005331566698742699L;
	private CitySystem system;
	
	/**
	 * Instantiates a new node regions table model.
	 *
	 * @param system the system
	 */
	public RegionsTableModel(CitySystem system) {
		this.system = system;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return system.getRegions().size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 11;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		switch(col) {
		case 0: return system.getRegions().get(row).getDescription();
		case 1: 
			if(system.getRegions().get(row) instanceof IntraLayerRegion) {
				return ((IntraLayerRegion)system.getRegions().get(row)).getLayer();
			} else return null;
		case 2: 
			if(system.getRegions().get(row) instanceof IntraLayerRegion) {
				return ((IntraLayerRegion)system.getRegions().get(row)).getNodeGenerationType();
			} else return null;
		case 3: 
			if(system.getRegions().get(row) instanceof IntraLayerRegion) {
				return ((IntraLayerRegion)system.getRegions().get(row)).getNodeType();
			} else return null;
		case 4: 
			if(system.getRegions().get(row) instanceof IntraLayerRegion) {
				return ((IntraLayerRegion)system.getRegions().get(row)).getEdgeGenerationType();
			} else return null;
		case 5: return system.getRegions().get(row).getEdgeType();
		case 6: return system.getRegions().get(row).getEdgeDirection();
		case 7: return CoordinateFormat.createForMatlabSyntax(system.getRegions().get(row).getCoordinateList())[0];
		case 8: return CoordinateFormat.createForMatlabSyntax(system.getRegions().get(row).getCoordinateList())[1];
		case 9:
			if(system.getRegions().get(row) instanceof InterLayerRegion) {
				return ((InterLayerRegion)system.getRegions().get(row)).getOriginLayer();
			} else return null;
		case 10:
			if(system.getRegions().get(row) instanceof InterLayerRegion) {
				return ((InterLayerRegion)system.getRegions().get(row)).getDestinationLayer();
			} else return null;
		default: return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int col) {
		if(system.getRegions().get(row) instanceof IntraLayerRegion) {
			if(col <= 7) return true;
			else return false;
		} else if(system.getRegions().get(row) instanceof InterLayerRegion) {
			if(col <= 0 || col >= 5) return true;
			else return false;
		}
    	return false;
    }
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	public Class<?> getColumnClass(int col) {
		switch(col) {
		case 0: return String.class;
		case 1: return Layer.class;
		case 2: return NodeGenerationType.class;
		case 3: return NodeType.class;
		case 4: return EdgeGenerationType.class;
		case 5: return EdgeType.class;
		case 6: return EdgeDirection.class;
		case 7: return String.class;
		case 8: return String.class;
		case 9: return Layer.class;
		case 10: return Layer.class;
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
				system.getRegions().get(row).setDescription((String)value); 
			break;
		case 1: 
			if(system.getRegions().get(row) instanceof IntraLayerRegion 
					&& value instanceof Layer) 
				((IntraLayerRegion)system.getRegions().get(row)).setLayer((Layer)value); 
			break;
		case 2:
			if(system.getRegions().get(row) instanceof IntraLayerRegion 
					&& value instanceof NodeGenerationType) 
				((IntraLayerRegion)system.getRegions().get(row)).setNodeGenerationType((NodeGenerationType)value); 
			break;
		case 3:
			if(system.getRegions().get(row) instanceof IntraLayerRegion 
					&& value instanceof NodeType) 
				((IntraLayerRegion)system.getRegions().get(row)).setNodeType((NodeType)value); 
			break;
		case 4: 
			if(system.getRegions().get(row) instanceof IntraLayerRegion 
					&& value instanceof EdgeGenerationType) 
				((IntraLayerRegion)system.getRegions().get(row)).setEdgeGenerationType((EdgeGenerationType)value); 
			break;
		case 5:
			if(system.getRegions().get(row) instanceof IntraLayerRegion 
					&& value instanceof EdgeType) 
				((IntraLayerRegion)system.getRegions().get(row)).setEdgeType((EdgeType)value); 
			break;
		case 6:
			if(system.getRegions().get(row) instanceof IntraLayerRegion 
					&& value instanceof EdgeDirection) 
				((IntraLayerRegion)system.getRegions().get(row)).setEdgeDirection((EdgeDirection)value); 
			break;
		case 7: 
			if(value instanceof String && CoordinateFormat.isValidMatlabSyntax((String)value)) {
				String yCoords = (String)getValueAt(row,8);
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
				system.getRegions().get(row).setCoordinateList(
						CoordinateFormat.createFromMatlabSyntax((String)value, yCoords)); 
			}
			break;
		case 8: 
			if(value instanceof String && CoordinateFormat.isValidMatlabSyntax((String)value)) {
				String xCoords = (String)getValueAt(row,7);
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
				system.getRegions().get(row).setCoordinateList(
						CoordinateFormat.createFromMatlabSyntax(xCoords, (String)value));
			}
			break;
		case 9: 
			if(system.getRegions().get(row) instanceof InterLayerRegion 
					&& value instanceof Layer) 
				((InterLayerRegion)system.getRegions().get(row)).setOriginLayer((Layer)value); 
			break;
		case 10: 
			if(system.getRegions().get(row) instanceof InterLayerRegion 
					&& value instanceof Layer) 
				((InterLayerRegion)system.getRegions().get(row)).setDestinationLayer((Layer)value); 
			break;
		}
    	fireTableRowsUpdated(row, row);
	}
}
