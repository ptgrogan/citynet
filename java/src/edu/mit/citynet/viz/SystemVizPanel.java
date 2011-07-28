package edu.mit.citynet.viz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.gui.test.SystemTestPanel;
import edu.mit.citynet.util.CityNetIcon;

/**
 * The SystemVizPanel class provides a system-level visualization of cells, 
 * node regions and nodes, and edge regions and edges. The node region and
 * edge region visibility can be toggled using two tables.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class SystemVizPanel extends AbstractVizPanel {
	private static final long serialVersionUID = -3650203268180181634L;
	
	private SystemTestPanel systemPanel;
	private CitySystem system;
	private VizLayeredPane layeredPane;
	private RegionTableModel<NodeRegion> nodeRegionTableModel;
	private RegionTableModel<EdgeRegion> edgeRegionTableModel;
	
	/**
	 * Instantiates a new system viz panel.
	 *
	 * @param systemPanel the system panel
	 * @param system the system
	 */
	public SystemVizPanel(SystemTestPanel systemPanel, CitySystem system) {
		if (systemPanel==null) {
			throw new IllegalArgumentException("System Panel cannot be null.");
		}
		this.systemPanel = systemPanel;
		this.system = system;
		initializePanel();
	}
	
	/**
	 * Initializes the panel.
	 */
	private void initializePanel() {
		setResizeWeight(0);
		setDividerLocation(200);
		setOneTouchExpandable(true);
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		nodeRegionTableModel = new RegionTableModel<NodeRegion>();
		nodeRegionTableModel.setRegions(system.getNodeRegions());
		nodeRegionTableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				layeredPane.repaint();
			}
		});
		JTable nodeRegionTable = new JTable(nodeRegionTableModel);
		nodeRegionTable.getTableHeader().setReorderingAllowed(false);
		nodeRegionTable.getColumnModel().getColumn(0).setMaxWidth(25);
		nodeRegionTable.getColumnModel().getColumn(0).setHeaderValue(null);
		nodeRegionTable.getColumnModel().getColumn(1).setHeaderValue("Node Region");
		nodeRegionTable.setPreferredScrollableViewportSize(new Dimension(200,200));
		leftPanel.add(new JScrollPane(nodeRegionTable),c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel nodeButtonPanel = new JPanel();
		nodeButtonPanel.setLayout(new BoxLayout(nodeButtonPanel,BoxLayout.LINE_AXIS));
		JButton generateNodesButton = new JButton("Generate",CityNetIcon.NODE_REGIONS.getIcon());
		generateNodesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				systemPanel.generateNodesCommand(nodeRegionTableModel.getSelectedRegions());
				repaint();
			}
		});
		nodeButtonPanel.add(generateNodesButton);
		JButton clearNodesButton = new JButton("Clear",CityNetIcon.DELETE.getIcon());
		clearNodesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				systemPanel.clearNodesCommand();
				repaint();
			}
		});
		nodeButtonPanel.add(clearNodesButton);
		leftPanel.add(nodeButtonPanel, c);
		c.gridy++;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		edgeRegionTableModel = new RegionTableModel<EdgeRegion>();
		edgeRegionTableModel.setRegions(system.getEdgeRegions());
		edgeRegionTableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				layeredPane.repaint();
			}
		});
		JTable edgeRegionTable = new JTable(edgeRegionTableModel);
		edgeRegionTable.getTableHeader().setReorderingAllowed(false);
		edgeRegionTable.getColumnModel().getColumn(0).setMaxWidth(25);
		edgeRegionTable.getColumnModel().getColumn(0).setHeaderValue(null);
		edgeRegionTable.getColumnModel().getColumn(1).setHeaderValue("Edge Region");
		edgeRegionTable.setPreferredScrollableViewportSize(new Dimension(200,200));
		leftPanel.add(new JScrollPane(edgeRegionTable),c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel edgeButtonPanel = new JPanel();
		edgeButtonPanel.setLayout(new BoxLayout(edgeButtonPanel,BoxLayout.LINE_AXIS));
		JButton generateEdgesButton = new JButton("Generate",CityNetIcon.EDGE_REGIONS.getIcon());
		generateEdgesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				systemPanel.generateEdgesCommand(edgeRegionTableModel.getSelectedRegions());
				repaint();
			}
		});
		edgeButtonPanel.add(generateEdgesButton);
		JButton clearEdgesButton = new JButton("Clear",CityNetIcon.DELETE.getIcon());
		clearEdgesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				systemPanel.clearEdgesCommand();
				repaint();
			}
		});
		edgeButtonPanel.add(clearEdgesButton);
		leftPanel.add(edgeButtonPanel, c);
		setLeftComponent(leftPanel);
		JPanel rightPanel = new JPanel(new BorderLayout());
		layeredPane = new VizLayeredPane(this, systemPanel.getCityPanel().getCity(), system);
		rightPanel.add(layeredPane,BorderLayout.CENTER);
		setRightComponent(rightPanel);
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz.AbstractVizPanel#getSelectedCellRegions()
	 */
	public Set<CellRegion> getSelectedCellRegions() {
		return new HashSet<CellRegion>();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz2.AbstractVizPanel#getSelectedNodeRegions()
	 */
	public Set<NodeRegion> getSelectedNodeRegions() {
		return nodeRegionTableModel.getSelectedRegions();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz.AbstractVizPanel#getSelectedEdgeRegions()
	 */
	public Set<EdgeRegion> getSelectedEdgeRegions() {
		return edgeRegionTableModel.getSelectedRegions();
	}
}
