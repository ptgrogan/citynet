package edu.mit.citynet.core;

import java.util.Arrays;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import edu.mit.citynet.CityNet;

/**
 * The AbstractRegion class provides the abstracted methods for geometry-type 
 * operations within regions of space. It uses the JTS library for geometry-
 * related operations.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public abstract class AbstractRegion {
	private Coordinate[] coordinates;
	
	public Coordinate[] getCoordinates() {
		return coordinates;
	}
	
	public void setCoordinates(Coordinate[] coordinates) {
		this.coordinates = coordinates;
	}
	
	/**
	 * Gets the area of the region.
	 *
	 * @return the area
	 */
	public double getArea() {
		if(coordinates.length>2) {
			return getPolygon().getArea();
		} else return 0;
	}
	
	/**
	 * Gets the polygon.
	 *
	 * @return the polygon
	 */
	public Polygon getPolygon() {
		if(coordinates.length>2) {
			GeometryFactory gf = CityNet.getInstance().getGeometryFactory();
			Coordinate[] coordinates = Arrays.copyOf(this.coordinates, this.coordinates.length+1);
			coordinates[this.coordinates.length] = (Coordinate)coordinates[0].clone();
			return gf.createPolygon(gf.createLinearRing(coordinates), null);
		} else {
			throw new IllegalStateException("Cannot create a polygon with fewer than 3 points.");
		}
	}
	
	/**
	 * Determines whether the region contains a point.
	 *
	 * @param point the point
	 * @return true, if the region contains the point
	 */
	public boolean containsPoint(Point point) {
		if(coordinates.length>2) {
			return getPolygon().contains(point);
		} else return false;
	}
	
	/**
	 * Determines whether the region contains a polygon, measured by if 
	 * minimum overlap fraction between the two is achieved.
	 *
	 * @param polygon the polygon
	 * @param overlapFraction the minimum overlap fraction
	 * @return true, if this region contains the minimum overlap fraction of
	 * the specified polygon
	 */
	public boolean containsPolygon(Polygon polygon, double overlapFraction) {
		if(coordinates.length>2) {
			double intersectionArea = getPolygon().intersection(polygon).getArea();
			if(intersectionArea/getArea() > overlapFraction) return true;
			else return false;
		} else return false;
	}
}
