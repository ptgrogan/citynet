/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.io;

import java.awt.Color;

public abstract class HexColorFormat {
	/**
	 * Checks if is valid hex string.
	 *
	 * @param hexString the hex string
	 * @return true, if is valid color syntax
	 */
	public static boolean isValidHexString(String hexString) {
		return hexString.matches("^0x[0-9a-fA-F]{6}$");
	}
	
	/**
	 * Gets the color from hex string.
	 *
	 * @param hexString the hex string
	 * @return the color from hex string
	 */
	public static Color getColorFromHexString(String hexString) {
		if(isValidHexString(hexString))
				return new Color(Integer.decode(hexString));
		else return Color.WHITE;
	}
	
	/**
	 * Gets the string from color.
	 *
	 * @param color the color
	 * @return the string from color
	 */
	public static String getStringFromColor(Color color) {
		return "0x" + String.format("%06x", 0xff000000^color.getRGB());
	}
}
