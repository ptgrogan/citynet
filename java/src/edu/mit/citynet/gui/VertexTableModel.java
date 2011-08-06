/*
 * 
 */
package edu.mit.citynet.gui;

import javax.swing.table.AbstractTableModel;

import com.vividsolutions.jts.geom.CoordinateList;

/**
 * The Class VertexTableModel.
 */
public class VertexTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -3408504299722389072L;
	
	private CoordinateList coordinates;
	private boolean isSquare;
	private static final int[] SQUARE_PAIR_X = new int[]{3,2,1,0};
	private static final int[] SQUARE_PAIR_Y = new int[]{1,0,3,2};
	
	/**
	 * Instantiates a new vertex table model.
	 */
	public VertexTableModel() {
		coordinates = new CoordinateList();
	}
	
	/**
	 * Sets if the vertices are required to be square.
	 *
	 * @param isSquare the new square
	 */
	public void setSquare(boolean isSquare) {
		this.isSquare = isSquare;
	}
	
	/**
	 * Checks if the vertices are required to be square.
	 *
	 * @return true, if is square
	 */
	public boolean isSquare() {
		return isSquare;
	}
	
	/**
	 * Sets the coordinates.
	 *
	 * @param coordinates the new coordinates
	 */
	public void setCoordinates(CoordinateList coordinates) {
		this.coordinates = coordinates;
		fireTableDataChanged();
	}
	
	/**
	 * Gets the coordinates.
	 *
	 * @return the coordinates
	 */
	public CoordinateList getCoordinates() {
		return coordinates;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return coordinates.size();
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
	public Object getValueAt(int row, int col) {
		if(col==0) return coordinates.getCoordinate(row).x;
		else return coordinates.getCoordinate(row).y;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	public Class<?> getColumnClass(int col) {
		return Double.class;
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
    	if(col==0 && value instanceof Double) {
    		coordinates.getCoordinate(row).x = (Double)value;
    		if(isSquare) {
        		coordinates.getCoordinate(SQUARE_PAIR_X[row]).x = (Double)value;
            	fireTableRowsUpdated(SQUARE_PAIR_X[row], SQUARE_PAIR_X[row]);
    		}
    	} else if(col==1 && value instanceof Double) {
    		coordinates.getCoordinate(row).y = (Double)value;
    		if(isSquare) {
        		coordinates.getCoordinate(SQUARE_PAIR_Y[row]).y = (Double)value;
            	fireTableRowsUpdated(SQUARE_PAIR_Y[row], SQUARE_PAIR_Y[row]);
    		}
    	}
    	fireTableRowsUpdated(row, row);
	}
}
