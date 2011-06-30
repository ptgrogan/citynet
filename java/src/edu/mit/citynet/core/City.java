package edu.mit.citynet.core;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * City class.
 * 
 * Stores the state of a synthesized city.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class City {
	private String _name;
	private double _latitude, _longitude;
	private double _rotation;
	private URL _imageURL;
	private transient Image _image;
	private double[] _imageVertices;
	//private Set<Cell> _cells;
	//private Set<CellRegion> _cellRegions;
	//private Set<System> _systems;
	
	/**
	 * Instantiates a new City.Net city.
	 */
	public City() {
		_imageVertices = new double[2];
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() { 
		return _name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) { 
		_name = name;
	}
	
	/**
	 * Gets the latitude, degrees (North > 0, South < 0).
	 *
	 * @return the latitude
	 */
	public double getLatitude() {
		return _latitude;
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
		_latitude = latitude;
	}
	
	/**
	 * Gets the longitude, degrees (East > 0, West < 0).
	 *
	 * @return the longitude
	 */
	public double getLongitude() {
		return _longitude;
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
		_longitude = longitude;
	}
	
	/**
	 * Gets the rotation of city local coordinate frame from cardinal 
	 * (degrees counterclockwise).
	 *
	 * @return the rotation
	 */
	public double getRotation() {
		return _rotation;
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
		_rotation = rotation;
	}
	
	/**
	 * Sets the image URL.
	 *
	 * @param imageURL the new image URL
	 */
	public void setImageURL(URL imageURL) {
		_imageURL = imageURL;
		try {
			_image = ImageIO.read(_imageURL);
		} catch (IOException ex) {
			throw new IllegalArgumentException("Invalid URL");
		}
	}
	
	/**
	 * Gets the image URL.
	 *
	 * @return the image URL
	 */
	public URL getImageURL() {
		return _imageURL;
	}
	
	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public Image getImage() {
		return _image;
	}
	
	/**
	 * Gets the image vertices.
	 *
	 * @return the image vertices
	 */
	public double[] getImageVertices() {
		return _imageVertices;
	}
	
	/**
	 * Sets the image vertices.
	 *
	 * @param imageVertices the new image vertices
	 */
	public void setImageVertices(double[] imageVertices) {
		_imageVertices = imageVertices;
	}
}
