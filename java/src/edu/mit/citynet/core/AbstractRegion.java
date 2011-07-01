package edu.mit.citynet.core;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * The AbstractRegion class provides the abstracted methods for geometry-type 
 * operations within regions of space. It uses the JTS library for geometry-
 * related operations.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public abstract class AbstractRegion {
	private Polygon polygon;
	
	/**
	 * Gets the area of the region.
	 *
	 * @return the area
	 */
	public double getArea() {
		if (polygon != null) return polygon.getArea();
		else return 0;
	}
	
	/**
	 * Determines whether the region contains a point.
	 *
	 * @param point the point
	 * @return true, if the region contains the point
	 */
	public boolean containsPoint(Point point) {
		if (polygon != null) return polygon.contains(point);
		else return false;
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
		if (this.polygon != null) {
			double intersectionArea = this.polygon.intersection(polygon).getArea();
			if(intersectionArea/getArea() > overlapFraction) return true;
			else return false;
		} else return false;
	}
}
