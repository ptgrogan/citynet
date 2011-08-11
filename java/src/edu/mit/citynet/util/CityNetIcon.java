/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.util;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public enum CityNetIcon {
	CITY("resources/map.png"),
	BROWSE("resources/folder_explore.png"),
	BUILDING_SYSTEM("resources/building.png"),
	TRANSPORTATION_SYSTEM("resources/car.png"),
	ENERGY_SYSTEM("resources/lightbulb.png"),
	WASTE_SYSTEM("resources/bin.png"),
	WATER_SYSTEM("resources/paintcan.png"),
	UNDEFINED_SYSTEM("resources/brick.png"),
	NODE_TYPE_BLANK("resources/bullet_white.png"),
	EDGE_TYPE_BLANK("resources/edge_blank.png"),
	POLYPOINT_REGION("resources/bullet_red.png"),
	POLYLINE_REGION("resources/chart_line.png"),
	POLYGON_REGION("resources/shape_handles.png"),
	UNDEFINED_REGION("resources/bullet_error.png"),
	ADD_COORDINATE("resources/bullet_add.png"),
	DELETE_COORDINATE("resources/bullet_delete.png"),
	MOVE_UP("resources/bullet_arrow_up.png"),
	MOVE_DOWN("resources/bullet_arrow_down.png"),
	UNDIRECTED("resources/arrow_both.png"),
	DIRECTED("resources/arrow_right.png"),
	GENERATE("resources/lightning.png"),
	DELETE("resources/delete.png"),
	COLOR_PALETTE("resources/color_swatch.png");
	
	private String filePath;
	private transient ImageIcon icon;
	
	/**
	 * Instantiates a new icon library.
	 *
	 * @param filePath the file path
	 */
	private CityNetIcon(String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * Gets the file path.
	 *
	 * @return the file path
	 */
	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public ImageIcon getIcon() {
		if(icon==null) {
			try {
				icon = new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource(filePath)));
			} catch (IOException e) {
				icon = new ImageIcon();
				e.printStackTrace();
			}
		}
		return icon;
	}
}
