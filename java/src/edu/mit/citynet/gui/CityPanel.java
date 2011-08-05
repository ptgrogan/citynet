package edu.mit.citynet.gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.mit.citynet.core.Cell;
import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.City;

/**
 * The CityPanel class is a panel to display city information.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CityPanel extends JPanel {
	private static final long serialVersionUID = -9157294901016405959L;

	private CityNetFrame cityNetFrame;
	protected City city;
	protected Set<SystemPanel> systemPanels;
	private JLabel cityNameLabel;
	private JButton editCityButton;
	
	/**
	 * Instantiates a new city panel.
	 *
	 * @param cityNetFrame the city net frame
	 * @param city the city
	 */
	public CityPanel(CityNetFrame cityNetFrame, City city) {
		if (city==null) {
			throw new IllegalArgumentException("City cannot be null.");
		}
		this.cityNetFrame = cityNetFrame;
		this.city = city;
		initializePanel();
	}
	
	/**
	 * Initializes the panel.
	 */
	private void initializePanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		cityNameLabel = new JLabel(city.getName());
		add(cityNameLabel,c);
		c.gridx++;
		editCityButton = new JButton("Edit");
		editCityButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cityNetFrame.editCityDetailsCommand();
			}
		});
		add(editCityButton,c);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		cityNameLabel.setText(city.getName());
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
	 * Gets the city net frame.
	 *
	 * @return the city net frame
	 */
	public CityNetFrame getCityNetFrame() {
		return cityNetFrame;
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
	}
}
