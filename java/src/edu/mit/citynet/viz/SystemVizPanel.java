package edu.mit.citynet.viz;

import javax.swing.JPanel;

import edu.mit.citynet.core.CitySystem;

public class SystemVizPanel extends JPanel {
	private static final long serialVersionUID = -3650203268180181634L;
	
	private CitySystem system;
	
	public SystemVizPanel(CitySystem system) {
		this.system = system;
	}
}
