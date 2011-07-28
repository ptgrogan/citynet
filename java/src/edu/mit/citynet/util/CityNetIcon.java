package edu.mit.citynet.util;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public enum CityNetIcon {
	EDGE_REGIONS("resources/chart_line.png"),
	NODE_REGIONS("resources/map.png"),
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
