package edu.mit.citynet.util;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public enum CityNetIcon {
	CITY("resources/map.png"),
	BUILDING_SYSTEM("resources/building.png"),
	TRANSPORTATION_SYSTEM("resources/car.png"),
	ENERGY_SYSTEM("resources/lightbulb.png"),
	WASTE_SYSTEM("resources/bin.png"),
	WATER_SYSTEM("resources/paintcan.png"),
	UNDEFINED_SYSTEM("resources/brick.png"),
	EDGE_REGIONS("resources/chart_line.png"),
	NODE_REGIONS("resources/map.png"),
	POLYPOINT_REGION("resources/bullet_red.png"),
	POLYLINE_REGION("resources/chart_line.png"),
	POLYGON_REGION("resources/shape_handles.png"),
	UNDEFINED_REGION("resources/bullet_error.png"),
	CELL_REGIONS("resources/shape_handles.png"),
	DELETE("resources/delete.png");
	
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
