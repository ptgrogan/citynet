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
	public CityVizPanel(City city) {
		if (city==null) {
			throw new IllegalArgumentException("City cannot be null.");
		}
		this.city = city;
		imageLabel = new JLabel();
		imageLabel.setPreferredSize(new Dimension(250,250));
		add(imageLabel);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#repaint()
	 */
	public void repaint() {
		super.repaint();
		if(imageLabel == null) return;
		if(city != null && city.getImage() != null) {
			imageLabel.setIcon(new ImageIcon(city.getImage()));
		} else {
			imageLabel.setIcon(null);
		}
	}
}
