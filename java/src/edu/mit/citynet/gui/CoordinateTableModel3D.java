package edu.mit.citynet.gui;

import java.util.ArrayList;
import java.util.List;

import edu.mit.citynet.core.Layer;

/**
 * The VertexModel3D class is used for edge regions which can cross layers.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CoordinateTableModel3D extends CoordinateTableModel {
	private static final long serialVersionUID = 3598861671329323732L;
	private List<Layer> layers;
	
	/**
	 * Instantiates a new vertex table model.
	 */
	public CoordinateTableModel3D() {
		super();
		layers = new ArrayList<Layer>();
	}
	
	/**
	 * Sets the layers.
	 *
	 * @param layers the new layers
	 */
	public void setLayers(List<Layer> layers) {
		this.layers = layers;
		fireTableDataChanged();
	}
	
	/**
	 * Gets the layers.
	 *
	 * @return the layers
	 */
	public List<Layer> getLayers() {
		return layers;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.gui.VertexTableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 3;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		if(col==2) return layers.get(row);
		else return super.getValueAt(row, col);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	public Class<?> getColumnClass(int col) {
		if(col==2) return Layer.class;
		else return super.getColumnClass(col);
    }
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object value, int row, int col) {
    	if(col==3 && value instanceof Layer) {
    		layers.set(row,(Layer)value);
    	} else super.setValueAt(value, row, col);
    	fireTableRowsUpdated(row, row);
	}
}
