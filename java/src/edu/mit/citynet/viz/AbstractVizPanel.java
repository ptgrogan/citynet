package edu.mit.citynet.viz;

import java.util.Set;

import javax.swing.JPanel;

import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.NodeRegion;

public abstract class AbstractVizPanel extends JPanel {
	private static final long serialVersionUID = 3664632230067588258L;

	public abstract Set<CellRegion> getSelectedCellRegions();
	public abstract Set<NodeRegion> getSelectedNodeRegions();
	public abstract Set<EdgeRegion> getSelectedEdgeRegions();
}
