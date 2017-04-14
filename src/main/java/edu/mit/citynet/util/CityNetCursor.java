/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.util;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public enum CityNetCursor {
	OPEN_HAND("/images/open_hand.gif",new Point(14,14),"Open Hand"),
	CLOSED_HAND("/images/closed_hand.gif",new Point(14,14),"Closed Hand");
	
	private String filePath;
	private Point hotSpot;
	private String name;
	private Cursor cursor;
	
	/**
	 * Instantiates a new city net cursor.
	 *
	 * @param filePath the file path
	 * @param hotSpot the hot spot
	 * @param name the name
	 */
	private CityNetCursor(String filePath, Point hotSpot, String name) {
		this.filePath = filePath;
		this.hotSpot = hotSpot;
		this.name = name;
	}
	
	/**
	 * Gets the cursor.
	 *
	 * @return the cursor
	 */
	public Cursor getCursor() {
		if(cursor==null) {
			cursor = Toolkit.getDefaultToolkit().createCustomCursor(
					new ImageIcon(CityNetCursor.class.getResource(filePath)).getImage(), 
					hotSpot, name);
		}
		return cursor;
	}
}
