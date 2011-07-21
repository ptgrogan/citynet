package edu.mit.citynet.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import edu.mit.citynet.core.Cell;
import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.viz.CityVizPanel;

public class CityPanel extends JPanel {
	private static final long serialVersionUID = -8626443292880784870L;
	private City city;
	private JTextField nameText;
	private CityVizPanel cityVizPanel;
	private Set<SystemPanel> systemPanels;
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
		cityVizPanel = new CityVizPanel(this);
		tabbedPane.add(cityVizPanel, "City");
		systemPanels = new HashSet<SystemPanel>();
		for(CitySystem system : city.getSystems()) {
			SystemPanel systemPanel = new SystemPanel(this, system);
			systemPanels.add(systemPanel);
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
	
	/**
	 * Clear cells command.
	 */
	public void clearCellsCommand() {
		System.out.println("Clear Cells Command");
		for(SystemPanel p : systemPanels) {
			if(!p.getSystem().getEdges().isEmpty())
				p.clearEdgesCommand();
			if(!p.getSystem().getNodes().isEmpty())
				p.clearNodesCommand();
		}
		city.setCells(new HashSet<Cell>());
		cityVizPanel.repaint();
	}
	
	/**
	 * Generate cells command.
	 */
	public void generateCellsCommand(Set<CellRegion> cellRegions) {
		System.out.println("Generate Cells Command");
		if(!city.getCells().isEmpty())
			clearCellsCommand();
		for(CellRegion r : cellRegions) {
			r.generateCells();
		}
		cityVizPanel.repaint();
	}
}
