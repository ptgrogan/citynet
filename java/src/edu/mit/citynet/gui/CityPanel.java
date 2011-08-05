package edu.mit.citynet.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.mit.citynet.core.Cell;
import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.viz.CityVizPanel;

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
	private CityVizPanel cityVizPanel;
	
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
		setLayout(new BorderLayout());
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
			/*
			SystemTestPanel systemPanel = new SystemTestPanel(this, system);
			systemPanels.add(systemPanel);
			tabbedPane.addTab(system.getName(), systemPanel);
			*/
		}
		add(tabbedPane, BorderLayout.CENTER);
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
