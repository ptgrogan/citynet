package edu.mit.citynet.core;

import com.vividsolutions.jts.geom.Coordinate;
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
public class CellRegion extends AbstractRegion {
	private int id;
	private int numberRows;
	private int numberColumns;
	private String description;
	
	/**
	 * Instantiates a new cell region.
	 */
	public CellRegion() { }

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
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Generate cells.
	 */
	public void generateCells() {
		// force rectangular region
		double minX = 0,maxX = 0,minY = 0,maxY = 0;
		for(Coordinate coord : getPolygon().getCoordinates()) {
			minX = Math.min(minX,coord.x);
			maxX = Math.max(maxX,coord.x);
			minY = Math.min(minY,coord.y);
			maxY = Math.max(maxY,coord.y);
		}
		double cellWidth = Math.abs((maxX-minX)/numberRows);
		double cellHeight = Math.abs((maxY-minY)/numberRows);
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
}
