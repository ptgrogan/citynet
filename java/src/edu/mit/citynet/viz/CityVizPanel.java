package edu.mit.citynet.viz;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashSet;
import java.util.Set;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.gui.CityPanel;

/**
 * The CityVizPanel class produces a standard city-wide visualization of the 
 * different synthesis components (e.g. cell/node/edge regions).
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CityVizPanel extends AbstractVizPanel {
	private static final long serialVersionUID = 3994034732879260199L;
	
	private CityPanel cityPanel;
	private CitySystem system;
	private VizLayeredPane layeredPane;
	
	/**
	 * Instantiates a new city viz panel.
	 *
	 * @param city the city
	 */
	public CityVizPanel(CityPanel cityPanel) {
		if (cityPanel==null) {
			throw new IllegalArgumentException("City Panel cannot be null.");
		}
		this.cityPanel = cityPanel;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		layeredPane = new VizLayeredPane(this, cityPanel.getCity(), system);
		layeredPane.setPreferredSize(new Dimension(250,250));
		add(layeredPane,c);
	}
	
	/**
	 * Gets the city panel.
	 *
	 * @return the city panel
	 */
	public CityPanel getCityPanel() {
		return cityPanel;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz2.AbstractVizPanel#getSelectedNodeRegions()
	 */
	public Set<NodeRegion> getSelectedNodeRegions() {
		return new HashSet<NodeRegion>();
	}
}
