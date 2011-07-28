package edu.mit.citynet.gui;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import edu.mit.citynet.core.Cell;
import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.City;

/**
 * The CityPanel class is an abstract panel to display city information. It is used
 * as a common base class for the CityPanelViz and the editor.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public abstract class CityPanel extends JPanel {
	private static final long serialVersionUID = -9157294901016405959L;

	protected City city;
	protected Set<SystemPanel> systemPanels;
	
	/**
	 * Instantiates a new city panel.
	 *
	 * @param city the city
	 */
	public CityPanel(City city) {
		if (city==null) {
			throw new IllegalArgumentException("City cannot be null.");
		}
		this.city = city;
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
