package edu.mit.citynet.gui;

import javax.swing.JPanel;

import edu.mit.citynet.core.EdgeRegion;

public class EdgeRegionPanel extends JPanel {
	private static final long serialVersionUID = -1711030904730242337L;

	private EdgeRegion region;
	
	public EdgeRegionPanel() {
		
	}
	
	public void loadEdgeRegion(EdgeRegion region) {
		this.region = region;
	}
	
	public void saveEdgeRegionCommand() {
		
	}
}
