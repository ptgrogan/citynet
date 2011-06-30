package edu.mit.citynet.gui;

import java.awt.Color;

import javax.swing.JPanel;

import edu.mit.citynet.core.City;

public class CityPanel extends JPanel {
	private static final long serialVersionUID = -8626443292880784870L;
	private City _city;
	
	/**
	 * Instantiates a new city panel.
	 */
	public CityPanel(City city) {
		_city = city;
		setBackground(Color.LIGHT_GRAY);
	}
	
	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public City getCity() {
		return _city;
	}
}
