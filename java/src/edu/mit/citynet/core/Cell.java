package edu.mit.citynet.core;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class Cell {
	private int id;
	private Polygon polygon;
	
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
	 * Gets the polygon.
	 *
	 * @return the polygon
	 */
	public Polygon getPolygon() {
		return polygon;
	}
	
	/**
	 * Sets the polygon.
	 *
	 * @param polygon the new polygon
	 */
	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}
	
	/**
	 * Determines whether the cell contains a point.
	 *
	 * @param point the point
	 * @return true, if the cell contains the point
	 */
	public boolean containsPoint(Point point) {
		if (polygon != null) return polygon.contains(point);
		else return false;
	}
	
	/**
	 * Determines whether the cell intersects a line.
	 *
	 * @param line the line
	 * @return true, if the cell intersects the line
	 */
	public boolean intersectsLine(LineString line) {
		if (polygon != null) return polygon.intersects(line);
		else return false;
	}
	
	/**
	 * Gets the area.
	 *
	 * @return the area
	 */
	public double getArea() {
		return polygon.getArea();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if(object instanceof Cell) {
			return id==((Cell)object).getId();
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Cell" + id;
	}
}
