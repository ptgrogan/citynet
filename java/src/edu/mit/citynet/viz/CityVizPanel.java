package edu.mit.citynet.viz;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.mit.citynet.core.City;

public class CityVizPanel extends JPanel {
	private static final long serialVersionUID = 3994034732879260199L;
	private City city;
	private JLabel imageLabel;
	
	/**
	 * Instantiates a new city viz panel.
	 */
	public CityVizPanel() {
		imageLabel = new JLabel();
		imageLabel.setPreferredSize(new Dimension(250,250));
		add(imageLabel);
	}
	
	/**
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(City city) {
		this.city = city;
		if(city != null) {
			if(city.getImage() != null) {
				imageLabel.setIcon(new ImageIcon(city.getImage()));
			}
		}
	}
}
