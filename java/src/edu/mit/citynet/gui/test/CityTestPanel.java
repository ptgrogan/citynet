package edu.mit.citynet.gui.test;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.gui.CityNetFrame;
import edu.mit.citynet.gui.CityPanel;
import edu.mit.citynet.gui.SystemPanel;
import edu.mit.citynet.viz.CityVizPanel;

public class CityTestPanel extends CityPanel {
	private static final long serialVersionUID = -8626443292880784870L;
	private JTextField nameText;
	private CityVizPanel cityVizPanel;
	/**
	 * Instantiates a new city panel.
	 */
	public CityTestPanel(CityNetFrame owner, City city) {
		super(owner,city);
		initializePanel();
	}
	
	/**
	 * Initializes the panel.
	 */
	private void initializePanel() {
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
		cityVizPanel = new CityVizPanel(this);
		tabbedPane.addTab("City", cityVizPanel);
		systemPanels = new HashSet<SystemPanel>();
		List<CitySystem> systems = new ArrayList<CitySystem>(city.getSystems());
		Collections.sort(systems, new Comparator<CitySystem>() {
			public int compare(CitySystem system1, CitySystem system2) {
				return system1.getName().compareTo(system2.getName());
			}
		});
		for(CitySystem system : systems) {
			SystemTestPanel systemPanel = new SystemTestPanel(this, system);
			systemPanels.add(systemPanel);
			tabbedPane.addTab(system.getName(), systemPanel);
		}
		add(tabbedPane, c);
	}
	
	/**
	 * Save city name command.
	 */
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
}
