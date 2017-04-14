/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.core;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
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
	private CoordinateList coordinateList;
	private String description;
	
	/**
	 * Instantiates a new abstract region.
	 */
	public AbstractRegion() {
		coordinateList = new CoordinateList();
	}
	
	/**
	 * Gets the coordinate list.
	 *
	 * @return the coordinate list
	 */
	@SuppressWarnings("unchecked")
	public CoordinateList getCoordinateList() {
		CoordinateList list = new CoordinateList();
		for(Coordinate coordinate : coordinateList.toCoordinateArray()) {
			list.add(new Coordinate(coordinate.x,coordinate.y));
		}
		return list;
	}
	
	/**
	 * Sets the coordinate list.
	 *
	 * @param coordinateList the new coordinate list
	 */
	@SuppressWarnings("unchecked")
	public void setCoordinateList(CoordinateList coordinateList) {
		this.coordinateList.clear();
		this.coordinateList.addAll(coordinateList);
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
	 * Gets the area of the region.
	 *
	 * @return the area
	 */
	public double getArea() {
		if(coordinateList.size()>2) {
			return getPolygon().getArea();
		} else return 0;
	}
	
	/**
	 * Gets the polygon.
	 *
	 * @return the polygon
	 */
	public Polygon getPolygon() {
		if(coordinateList.size()>2) {
			GeometryFactory gf = CityNet.getInstance().getGeometryFactory();
			CoordinateList ringCoordinates = getCoordinateList();
			ringCoordinates.closeRing();
			return gf.createPolygon(gf.createLinearRing(ringCoordinates.toCoordinateArray()), null);
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
		if(coordinateList.size()>2) {
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
		if(coordinateList.size()>2) {
			double intersectionArea = getPolygon().intersection(polygon).getArea();
			if(intersectionArea/Math.min(getPolygon().getArea(),polygon.getArea()) > overlapFraction) return true;
			else return false;
		} else return false;
	}
}
