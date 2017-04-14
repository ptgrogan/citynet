/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.util;

import javax.swing.ImageIcon;

public enum CityNetIcon {
	CITY("/images/map.png"),
	BROWSE("/images/folder_explore.png"),
	BUILDING_SYSTEM("/images/building.png"),
	TRANSPORTATION_SYSTEM("/images/car.png"),
	ENERGY_SYSTEM("/images/lightbulb.png"),
	WASTE_SYSTEM("/images/bin.png"),
	WATER_SYSTEM("/images/paintcan.png"),
	UNDEFINED_SYSTEM("/images/brick.png"),
	NODE_TYPE_BLANK("/images/bullet_white.png"),
	EDGE_TYPE_BLANK("/images/edge_blank.png"),
	POLYPOINT_REGION("/images/bullet_red.png"),
	POLYLINE_REGION("/images/chart_line.png"),
	POLYGON_REGION("/images/shape_handles.png"),
	UNDEFINED_REGION("/images/bullet_error.png"),
	ADD_COORDINATE("/images/bullet_add.png"),
	DELETE_COORDINATE("/images/bullet_delete.png"),
	MOVE_UP("/images/bullet_arrow_up.png"),
	MOVE_DOWN("/images/bullet_arrow_down.png"),
	UNDIRECTED("/images/arrow_both.png"),
	DIRECTED("/images/arrow_right.png"),
	GENERATE("/images/lightning.png"),
	DELETE("/images/delete.png"),
	COLOR_PALETTE("/images/color_swatch.png"),
	SELECT_COORDINATES("/images/map.png");
	
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
			icon = new ImageIcon(CityNetIcon.class.getResource(filePath));
		}
		return icon;
	}
}
