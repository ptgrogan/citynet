package edu.mit.citynet.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.viz.CityVizPanel;

public class CityPanel extends JPanel {
	private static final long serialVersionUID = -8626443292880784870L;
	private City city;
	JTextField nameText;
	CityVizPanel cityVizPanel;
	
	/**
	 * Instantiates a new city panel.
	 */
	public CityPanel(City city) {
		if (city==null) {
			throw new IllegalArgumentException("City cannot be null.");
		}
		this.city = city;
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2,2,2,2);
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
		JTabbedPane tabbedPane = new JTabbedPane();
		cityVizPanel = new CityVizPanel(city);
		tabbedPane.add(cityVizPanel, "City");
		for(CitySystem system : city.getSystems()) {
			SystemPanel systemPanel = new SystemPanel(system);
			tabbedPane.add(systemPanel, system.getName());
		}
		add(tabbedPane, c);
	}
	
	private void saveCityNameCommand() {
		System.out.println("Save City Name Command");
		city.setName(nameText.getText());
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#repaint()
	 */
	public void repaint() {
		super.repaint();
		if (nameText != null) {
			nameText.setText(city.getName());
		}
	}
	
	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public City getCity() {
		return city;
	}
}
