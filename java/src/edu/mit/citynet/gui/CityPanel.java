package edu.mit.citynet.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.mit.citynet.core.City;

public class CityPanel extends JPanel {
	private static final long serialVersionUID = -8626443292880784870L;
	private City _city;
	JTextField nameText;
	
	/**
	 * Instantiates a new city panel.
	 */
	public CityPanel(City city) {
		if (city==null) {
			throw new IllegalArgumentException("City cannot be null.");
		}
		_city = city;
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0;
		add(new JLabel("City Name: "), c);
		c.gridx++;
		nameText = new JTextField(20);
		nameText.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				saveCityNameCommand();
			}
		});
		add(nameText, c);
		c.gridx = 0;
		c.gridy++;
		c.weighty = 1;
		c.weightx = 1;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		add(new JPanel(), c);
	}
	
	private void saveCityNameCommand() {
		System.out.println("Save City Name Command");
		_city.setName(nameText.getText());
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#repaint()
	 */
	public void repaint() {
		super.repaint();
		if (nameText != null) {
			nameText.setText(_city.getName());
		}
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
