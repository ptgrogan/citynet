package edu.mit.citynet.util;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum CityNetCursor {
	OPEN_HAND("resources/open_hand.gif",new Point(14,14),"Open Hand"),
	CLOSED_HAND("resources/closed_hand.gif",new Point(14,14),"Closed Hand");
	
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
			try {
				cursor = Toolkit.getDefaultToolkit().createCustomCursor(
						ImageIO.read(getClass().getClassLoader().getResource(filePath)), 
						hotSpot,name);
			} catch (IOException e) {
				cursor = Cursor.getDefaultCursor();
				e.printStackTrace();
			}
		}
		return cursor;
	}
}
