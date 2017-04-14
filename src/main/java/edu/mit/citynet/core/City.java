/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.core;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import edu.mit.citynet.CityNet;

/**
 * City class.
 * 
 * Stores the state of a synthesized city.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class City {
	private String name;
	private double latitude, longitude;
	private double rotation;
	private String imageFilePath;
	private transient Image image;
	private CoordinateList imageCoordinates;
	private List<Cell> cells;
	private List<CellRegion> cellRegions;
	private List<CitySystem> systems;
	
	/**
	 * Instantiates a new City.Net city.
	 */
	public City() {
		name = "New City";
		cells = new ArrayList<Cell>();
		cellRegions = new ArrayList<CellRegion>();
		systems = new ArrayList<CitySystem>();
		imageCoordinates = new CoordinateList(new Coordinate[]{		
			new Coordinate(0,0), new Coordinate(1,0), 
			new Coordinate(1,1), new Coordinate(0,1)});
	}
	
	/**
	 * Gets a copy of the cells.
	 *
	 * @return the cells
	 */
	public List<Cell> getCells() {
		return new ArrayList<Cell>(cells);
	}
	
	/**
	 * Sets the cells.
	 *
	 * @param cells the new cells
	 * @return true, if successful
	 */
	public boolean addAllCells(Collection<Cell> cells) {
		return this.cells.addAll(cells);
	}
	
	/**
	 * Removes the all cells.
	 *
	 * @param cells the cells
	 * @return true, if successful
	 */
	public boolean removeAllCells(Collection<Cell> cells) {
		return this.cells.removeAll(cells);
	}
	
	/**
	 * Adds the cell.
	 *
	 * @param cell the cell
	 * @return true, if successful
	 */
	public boolean addCell(Cell cell) {
		if(cell.getId()==0) 
			cell.setId(CityNet.getInstance().getNextCellId());
		return cells.add(cell);
	}

	/**
	 * Gets a copy of the systems.
	 *
	 * @return the systems
	 */
	public List<CitySystem> getSystems() {
		return new ArrayList<CitySystem>(systems);
	}

	/**
	 * Sets the systems.
	 *
	 * @param systems the new systems
	 * @return true, if successful
	 */
	public boolean addAllSystems(Collection<CitySystem> systems) {
		return this.systems.addAll(systems);
	}
	
	/**
	 * Removes the all systems.
	 *
	 * @param systems the systems
	 * @return true, if successful
	 */
	public boolean removeAllSystems(Collection<CitySystem> systems) {
		return this.systems.removeAll(systems);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() { 
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) { 
		this.name = name;
	}
	
	/**
	 * Gets the latitude, degrees (North > 0, South < 0).
	 *
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	
	/**
	 * Sets the city local coordinate frame origin latitude. Valid latitudes are
	 * between -90 (S) and 90 (N) degrees.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(double latitude) {
		if(latitude < -90 || latitude > 90) {
			throw new IllegalArgumentException("Latitude must be between " +
					"-90 and 90 degrees.");
		}
		this.latitude = latitude;
	}
	
	/**
	 * Gets the longitude, degrees (East > 0, West < 0).
	 *
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	
	/**
	 * Sets the city local coordinate frame origin longitude (degrees). 
	 * Valid longitude are between -180 (W) and 180 (E) degrees.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(double longitude) { 
		if(longitude < -180 || longitude > 180) {
			throw new IllegalArgumentException("Longitude must be between " +
					"-180 and 180 degrees.");
		}
		this.longitude = longitude;
	}
	
	/**
	 * Gets the rotation of city local coordinate frame from cardinal 
	 * (degrees counterclockwise).
	 *
	 * @return the rotation
	 */
	public double getRotation() {
		return rotation;
	}
	
	/**
	 * Sets the rotation of city local coordinate frame from cardinal 
	 * (degrees counterclockwise). Valid rotations are between 0 and 180 
	 * degrees.
	 *
	 * @param rotation the new rotation
	 */
	public void setRotation(double rotation) {
		if(rotation < 0 || rotation > 180) {
			throw new IllegalArgumentException("Rotation must be between " +
					"0 and 180 degrees.");
		}
		this.rotation = rotation;
	}
	
	/**
	 * Sets the image file path.
	 *
	 * @param imageFilePath the new image file path
	 */
	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
		if(imageFilePath!=null) {
			try {
				image = ImageIO.read(new FileInputStream(imageFilePath));
			} catch (IOException ex) {
				image = null;
			}
		}
	}
	
	/**
	 * Gets the image file path.
	 *
	 * @return the image file path
	 */
	public String getImageFilePath() {
		return imageFilePath;
	}
	
	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}
	
	/**
	 * Gets the image coordinates.
	 *
	 * @return the image coordinates
	 */
	public CoordinateList getImageCoordinates() {
		return new CoordinateList(imageCoordinates.toCoordinateArray());
	}
	
	/**
	 * Sets the image coordinates.
	 *
	 * @param coordinates the new image coordinates
	 */
	public void setImageCoordinates(CoordinateList coordinates) {
		this.imageCoordinates = coordinates;
	}
	
	/**
	 * Gets the image polygon.
	 *
	 * @return the image polygon
	 */
	public Polygon getImagePolygon() {
		if(imageCoordinates.size()==4) {
			GeometryFactory gf = CityNet.getInstance().getGeometryFactory();
			CoordinateList ringCoordinates = getImageCoordinates();
			ringCoordinates.closeRing();
			return gf.createPolygon(gf.createLinearRing(ringCoordinates.toCoordinateArray()), null);
		} else {
			throw new IllegalStateException("Cannot create an image polygon without 4 points.");
		}
	}

	/**
	 * Sets the cell regions.
	 *
	 * @param cellRegions the new cell regions
	 * @return true, if successful
	 */
	public boolean addAllCellRegions(Collection<CellRegion> cellRegions) {
		return this.cellRegions.addAll(cellRegions);
	}
	
	/**
	 * Removes the all cell regions.
	 *
	 * @param cellRegions the cell regions
	 * @return true, if successful
	 */
	public boolean removeAllCellRegions(Collection<CellRegion> cellRegions) {
		return this.cellRegions.removeAll(cellRegions);
	}

	/**
	 * Gets a copy of the cell regions.
	 *
	 * @return the cell regions
	 */
	public List<CellRegion> getCellRegions() {
		return new ArrayList<CellRegion>(cellRegions);
	}
	
	/**
	 * Adds the cell region.
	 *
	 * @param cellRegion the cell region
	 * @return true, if successful
	 */
	public boolean addCellRegion(CellRegion cellRegion) {
		if(cellRegion.getId()==0) 
			cellRegion.setId(CityNet.getInstance().getNextCellRegionId());
		return cellRegions.add(cellRegion);
	}
	
	/**
	 * Adds the system.
	 *
	 * @param system the system
	 * @return true, if successful
	 */
	public boolean addSystem(CitySystem system) {
		if(system.getId()==0)
			system.setId(CityNet.getInstance().getNextSystemId());
		return systems.add(system);
	}
	
	/**
	 * Removes the system.
	 *
	 * @param system the system
	 * @return true, if successful
	 */
	public boolean removeSystem(CitySystem system) {
		return systems.remove(system);
	}
}
