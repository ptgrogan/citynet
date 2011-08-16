/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.EdgeDirection;
import edu.mit.citynet.core.EdgeRegion.EdgeRegionType;
import edu.mit.citynet.core.EdgeType;
import edu.mit.citynet.core.Layer;
import edu.mit.citynet.io.CoordinateFormat;

/**
 * The EdgeRegionsTableModel class defines a table model suitable for editing
 * multiple edge regions at the same time.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class EdgeRegionsTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -1005331566698742699L;
	private CitySystem system;
	
	/**
	 * Instantiates a new edge regions table model.
	 *
	 * @param system the system
	 */
	public EdgeRegionsTableModel(CitySystem system) {
		this.system = system;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return system.getEdgeRegions().size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 7;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		switch(col) {
		case 0: return system.getEdgeRegions().get(row).getDescription();
		case 1: return system.getEdgeRegions().get(row).getEdgeType();
		case 2: return system.getEdgeRegions().get(row).getEdgeRegionType();
		case 3: return system.getEdgeRegions().get(row).getEdgeDirection();
		case 4: return CoordinateFormat.createForMatlabSyntax(system.getEdgeRegions().get(row).getCoordinateList())[0];
		case 5: return CoordinateFormat.createForMatlabSyntax(system.getEdgeRegions().get(row).getCoordinateList())[1];
		case 6: return toLayersSyntax(system.getEdgeRegions().get(row).getLayers());
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
		case 1: return EdgeType.class;
		case 2: return EdgeRegionType.class;
		case 4: return String.class;
		case 5: return String.class;
		case 6: return String.class;
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
				system.getEdgeRegions().get(row).setDescription((String)value); 
			break;
		case 1:
			if(value instanceof EdgeType) 
				system.getEdgeRegions().get(row).setEdgeType((EdgeType)value); 
			break;
		case 2:
			if(value instanceof EdgeRegionType) 
				system.getEdgeRegions().get(row).setEdgeRegionType((EdgeRegionType)value); 
			break;
		case 3: 
			if(value instanceof EdgeDirection) 
				system.getEdgeRegions().get(row).setEdgeDirection((EdgeDirection)value); 
			break;
		case 4: 
			if(value instanceof String && CoordinateFormat.isValidMatlabSyntax((String)value)) {
				String yCoords = (String)getValueAt(row,5);
				List<Layer> layers = system.getEdgeRegions().get(row).getLayers();
				while(CoordinateFormat.getDoubleCoordinates((String)value).length 
						> CoordinateFormat.getDoubleCoordinates(yCoords).length) {
					// add extra y-coordinate... default to 0.0
					yCoords = yCoords.substring(0, yCoords.length()-2) + " 0.0]";
					// add extra layer... default to last layer used, or first layer
					if(layers.size()>0)
						layers.add(layers.get(layers.size()-1));
					else if(system.getLayers().size()>0)
						layers.add(system.getLayers().get(0));
					else
						layers.add(null);
				}
				while(CoordinateFormat.getDoubleCoordinates((String)value).length 
						< CoordinateFormat.getDoubleCoordinates(yCoords).length) {
					// remove extra y-coordinate... default to last
					yCoords = yCoords.substring(0, yCoords.lastIndexOf(" ")) + "]";
					// remove extra layer... default to last
					layers.remove(layers.get(layers.size()-1));
				}
				system.getEdgeRegions().get(row).setCoordinateList(
						CoordinateFormat.createFromMatlabSyntax((String)value, yCoords)); 
				system.getEdgeRegions().get(row).setLayers(layers);
			}
			break;
		case 5: 
			if(value instanceof String && CoordinateFormat.isValidMatlabSyntax((String)value)) {
				String xCoords = (String)getValueAt(row,4);
				List<Layer> layers = system.getEdgeRegions().get(row).getLayers();
				while(CoordinateFormat.getDoubleCoordinates((String)value).length 
						> CoordinateFormat.getDoubleCoordinates(xCoords).length) {
					// add extra x-coordinate... default to 0.0
					xCoords = xCoords.substring(0, xCoords.length()-2) + " 0.0]";
					// add extra layer... default to last layer used, or first layer
					if(layers.size()>0)
						layers.add(layers.get(layers.size()-1));
					else if(system.getLayers().size()>0)
						layers.add(system.getLayers().get(0));
					else
						layers.add(null);
				}
				while(CoordinateFormat.getDoubleCoordinates((String)value).length 
						< CoordinateFormat.getDoubleCoordinates(xCoords).length) {
					// remove extra x-coordinate... default to last
					xCoords = xCoords.substring(0, xCoords.lastIndexOf(" ")) + "]";
					// remove extra layer... default to last
					layers.remove(layers.get(layers.size()-1));
				}
				system.getEdgeRegions().get(row).setCoordinateList(
						CoordinateFormat.createFromMatlabSyntax(xCoords, (String)value));
				system.getEdgeRegions().get(row).setLayers(layers);
			}
			break;
		case 6:
			if(value instanceof String && isValidLayersSyntax((String)value)) {
				String xCoords = (String)getValueAt(row,4);
				String yCoords = (String)getValueAt(row,5);
				while(fromLayersSyntax((String)value).size() 
						> CoordinateFormat.getDoubleCoordinates(xCoords).length) {
					// add extra x-coordinate... default to 0.0
					xCoords = xCoords.substring(0, xCoords.length()-2) + " 0.0]";
					// add extra y-coordinate... default to 0.0
					yCoords = yCoords.substring(0, yCoords.length()-2) + " 0.0]";
				}
				while(fromLayersSyntax((String)value).size() 
						< CoordinateFormat.getDoubleCoordinates(xCoords).length) {
					// remove extra x-coordinate... default to last
					xCoords = xCoords.substring(0, xCoords.lastIndexOf(" ")) + "]";
					// remove extra y-coordinate... default to last
					yCoords = yCoords.substring(0, yCoords.lastIndexOf(" ")) + "]";
				}
				system.getEdgeRegions().get(row).setLayers(fromLayersSyntax((String)value));
				system.getEdgeRegions().get(row).setCoordinateList(
						CoordinateFormat.createFromMatlabSyntax(xCoords, yCoords));
			}
			break;
		}
    	fireTableRowsUpdated(row, row);
	}
	
	/**
	 * Checks if is valid layers syntax.
	 *
	 * @param syntax the syntax
	 * @return true, if is valid layers syntax
	 */
	private boolean isValidLayersSyntax(String syntax) {
		String pattern = "(";
		for(Layer layer : system.getLayers())
			pattern += layer.getId() + "|";
		pattern = pattern.substring(0,pattern.length()-1) + ")";
		return syntax.matches("^\\[(" + pattern + "\\s)*" + pattern + "\\]$");
	}
	
	/**
	 * To layers syntax.
	 *
	 * @param layers the layers
	 * @return the string
	 */
	private String toLayersSyntax(List<Layer> layers) {
		String syntax = "[";
		for(Layer layer : layers)
			syntax += layer.getId() + " ";
		return syntax.trim() + "]";
	}
	
	/**
	 * From layers syntax.
	 *
	 * @param syntax the syntax
	 * @return the list
	 */
	private List<Layer> fromLayersSyntax(String syntax) {
		List<Layer> layers = new ArrayList<Layer>();
		String[] strLayers = syntax.substring(1,syntax.length()-1).split(" ");
		for(int i=0; i<strLayers.length; i++) {
			for(Layer layer : system.getLayers()) {
				if(layer.getId()==Integer.parseInt(strLayers[i]))
					layers.add(layer);
			}
		}
		return layers;
	}
}
