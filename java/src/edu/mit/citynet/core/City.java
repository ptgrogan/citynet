package edu.mit.citynet.core;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import com.vividsolutions.jts.geom.Polygon;

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
	private Polygon imagePolygon;

	private Set<Cell> cells;
	private Set<CellRegion> cellRegions;
	private Set<CitySystem> systems;
	
	/**
	 * Instantiates a new City.Net city.
	 */
	public City() {
		name = "New City";
		cells = new HashSet<Cell>();
		cellRegions = new HashSet<CellRegion>();
		systems = new HashSet<CitySystem>();
	}
	
	/**
	 * Gets a copy of the cells.
	 *
	 * @return the cells
	 */
	public Set<Cell> getCells() {
		return new HashSet<Cell>(cells);
	}
	
	/**
	 * Sets the cells.
	 *
	 * @param cells the new cells
	 */
	public void setCells(Set<Cell> cells) {
		this.cells = cells;
	}
	
	/**
	 * Adds the cell.
	 *
	 * @param cell the cell
	 * @return true, if successful
	 */
	public boolean addCell(Cell cell) {
		return cells.add(cell);
	}

	/**
	 * Gets a copy of the systems.
	 *
	 * @return the systems
	 */
	public Set<CitySystem> getSystems() {
		return new HashSet<CitySystem>(systems);
	}

	/**
	 * Sets the systems.
	 *
	 * @param systems the new systems
	 */
	public void setSystems(Set<CitySystem> systems) {
		this.systems = systems;
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
		try {
			image = ImageIO.read(new FileInputStream(imageFilePath));
		} catch (IOException ex) {
			throw new IllegalArgumentException("Invalid image file path.");
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
	 * Gets the image polygon.
	 *
	 * @return the image polygon
	 */
	public Polygon getImagePolygon() {
		return imagePolygon;
	}
	
	/**
	 * Sets the image polygon.
	 *
	 * @param imageVertices the new image polygon
	 */
	public void setImagePolygon(Polygon imagePolygon) {
		this.imagePolygon = imagePolygon;
	}

	/**
	 * Sets the cell regions.
	 *
	 * @param cellRegions the new cell regions
	 */
	public void setCellRegions(Set<CellRegion> cellRegions) {
		this.cellRegions = cellRegions;
	}

	/**
	 * Gets a copy of the cell regions.
	 *
	 * @return the cell regions
	 */
	public Set<CellRegion> getCellRegions() {
		return new HashSet<CellRegion>(cellRegions);
	}
}
