/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.core;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.GeometryFactory;

import edu.mit.citynet.CityNet;

/**
 * The CellRegion class specifies a spatial area over which cells should be 
 * generated (meshed). There are several options to define the type and 
 * density of cells generated within the region.
 * 
 * The CellRegion class was created to be able to specify cells without
 * relying on tedious individual cell definitions.
 */
public class CellRegion extends AbstractRegion implements Cloneable {
	private int id;
	private int numberRows;
	private int numberColumns;
	
	/**
	 * Instantiates a new cell region.
	 */
	public CellRegion() { 
		super();
		setDescription("New Cell Region");
		setCoordinateList(new CoordinateList(new Coordinate[]{
				new Coordinate(0,0), new Coordinate(1,0), 
				new Coordinate(1,1), new Coordinate(0,1)}));
		numberRows = 1;
		numberColumns = 1;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Generate cells.
	 */
	public void generateCells() {
		// force rectangular region
		double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE,
			minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
		for(Coordinate coord : getCoordinateList().toCoordinateArray()) {
			minX = Math.min(minX,coord.x);
			maxX = Math.max(maxX,coord.x);
			minY = Math.min(minY,coord.y);
			maxY = Math.max(maxY,coord.y);
		}
		double cellWidth = Math.abs((maxX-minX)/numberRows);
		double cellHeight = Math.abs((maxY-minY)/numberColumns);
		GeometryFactory gf = CityNet.getInstance().getGeometryFactory();
		for(int i=0; i<numberRows; i++) {
			for(int j=0; j<numberColumns; j++) {
				Coordinate c1 = new Coordinate(minX+cellWidth*i,
						minY+cellHeight*j);
				Coordinate c2 = new Coordinate(minX+cellWidth*(i+1),
						minY+cellHeight*j);
				Coordinate c3 = new Coordinate(minX+cellWidth*(i+1),
						minY+cellHeight*(j+1));
				Coordinate c4 = new Coordinate(minX+cellWidth*i,
						minY+cellHeight*(j+1));
				Cell cell = new Cell();
				cell.setId(CityNet.getInstance().getNextCellId());
				cell.setPolygon(gf.createPolygon(gf.createLinearRing(
						new Coordinate[]{c1,c2,c3,c4,c1}), null));
				CityNet.getInstance().getCity().addCell(cell);
			}
		}
	}

	/**
	 * Gets the number rows.
	 *
	 * @return the number rows
	 */
	public int getNumberRows() {
		return numberRows;
	}

	/**
	 * Sets the number rows.
	 *
	 * @param numberRows the new number rows
	 */
	public void setNumberRows(int numberRows) {
		this.numberRows = numberRows;
	}

	/**
	 * Gets the number columns.
	 *
	 * @return the number columns
	 */
	public int getNumberColumns() {
		return numberColumns;
	}

	/**
	 * Sets the number columns.
	 *
	 * @param numberColumns the new number columns
	 */
	public void setNumberColumns(int numberColumns) {
		this.numberColumns = numberColumns;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if(object instanceof CellRegion) {
			return id==((CellRegion)object).getId();
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "CellRegion" + id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public CellRegion clone() {
		CellRegion clone = new CellRegion();
		clone.setId(0);
		clone.setDescription(getDescription() + " (Copy)");
		clone.setCoordinateList(getCoordinateList());
		clone.setNumberColumns(getNumberColumns());
		clone.setNumberRows(getNumberRows());
		return clone;
	}
}
