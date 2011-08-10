package edu.mit.citynet.gui;

import javax.swing.JPanel;

import edu.mit.citynet.core.NodeRegion;

public class NodeRegionPanel extends JPanel {
	private static final long serialVersionUID = -3163576993982031134L;
	
	private NodeRegion region;
	
	public void loadNodeRegion(NodeRegion region) {
		this.region = region;
	}
	
	public void saveNodeRegionCommand() {
		
	}

}
