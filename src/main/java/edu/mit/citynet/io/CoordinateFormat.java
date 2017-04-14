/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.io;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;

/**
 * The CoordinateFormat class helps convert between Matlab syntax
 * and Coordiante objects.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CoordinateFormat {
	/**
	 * Creates the polygon from MATLAB syntax. Assumes vertices are formatted
	 * with square brackets and space delimiters such as: [0 0.4 0.5 0] and
	 * there are equal number of vertices in both x- and y-coordinates.
	 *
	 * @param verticesX the vertices x
	 * @param verticesY the vertices y
	 * @return the polygon
	 */
	public static CoordinateList createFromMatlabSyntax(String verticesX, String verticesY) {
		String[] vX = verticesX.substring(1,verticesX.length()-1).split(" ");
		String[] vY = verticesY.substring(1,verticesY.length()-1).split(" ");
		CoordinateList coordinates = new CoordinateList();
		for(int i=0; i<Math.min(vX.length,vY.length); i++) {
			coordinates.add(new Coordinate(
					Double.parseDouble(vX[i]),Double.parseDouble(vY[i])),true);
		}
		return coordinates;
	}
	
	/**
	 * Creates the coordinates for Matlab syntax.
	 *
	 * @param coordinates the coordinates
	 * @return the string
	 */
	public static String[] createForMatlabSyntax(CoordinateList coordinates) {
		String verticesX = "[", verticesY = "[";
		for(Coordinate coordinate : coordinates.toCoordinateArray()) {
			verticesX += coordinate.x + " ";
			verticesY += coordinate.y + " ";
		}
		verticesX = verticesX.trim() + "]";
		verticesY = verticesY.trim() + "]";
		return new String[]{verticesX, verticesY};
	}
	
	/**
	 * Checks if is valid matlab syntax.
	 *
	 * @param coordinates the coordinates
	 * @return true, if is valid matlab syntax
	 */
	public static boolean isValidMatlabSyntax(String coordinates) {
		// regex expression to match patterns similar to [0.0 3 4]
		// ^\[(\d\.?\d\[0-9]*\s)*\d\.?\d*[0-9]*\]$
		return coordinates.matches("^\\[(\\d*\\.?\\d*[0-9]*\\s)*\\d*\\.?\\d*[0-9]+\\]$");
	}
	
	/**
	 * Gets the double coordinates.
	 *
	 * @param coordinates the coordinates
	 * @return the double coordinates
	 */
	public static double[] getDoubleCoordinates(String coordinates) {
		String[] strCoords = coordinates.substring(1,coordinates.length()-1).split(" ");
		double[] dblCoords = new double[strCoords.length];
		for(int i=0; i<strCoords.length; i++) {
			dblCoords[i] = Double.parseDouble(strCoords[i]);
		}
		return dblCoords;
	}
}
