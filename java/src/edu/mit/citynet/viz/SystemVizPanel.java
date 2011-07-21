package edu.mit.citynet.viz;

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
import edu.mit.citynet.core.Edge;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.Node;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.gui.CityPanel;

public class SystemVizPanel extends AbstractVizPanel {
	private static final long serialVersionUID = -3650203268180181634L;
	
	private CityPanel cityPanel;
	private CitySystem system;
	private VizLayeredPane layeredPane;
	private RegionTableModel<NodeRegion> nodeRegionTableModel;
	private RegionTableModel<EdgeRegion> edgeRegionTableModel;
	
	public SystemVizPanel(CityPanel cityPanel, CitySystem system) {
		if (cityPanel==null) {
			throw new IllegalArgumentException("City Panel cannot be null.");
		}
		if (system==null) {
			throw new IllegalArgumentException("System cannot be null.");
		}
		this.cityPanel = cityPanel;
		this.system = system;
		setLayout(new GridBagLayout());
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
		nodeRegionTable.setPreferredScrollableViewportSize(new Dimension(100,100));
		add(new JScrollPane(nodeRegionTable),c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel nodeButtonPanel = new JPanel();
		nodeButtonPanel.setLayout(new BoxLayout(nodeButtonPanel,BoxLayout.LINE_AXIS));
		JButton generateNodesButton = new JButton("Generate");
		generateNodesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateNodesCommand();
			}
		});
		nodeButtonPanel.add(generateNodesButton);
		JButton clearNodesButton = new JButton("Clear");
		clearNodesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearNodesCommand();
			}
		});
		nodeButtonPanel.add(clearNodesButton);
		add(nodeButtonPanel, c);
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
		edgeRegionTable.setPreferredScrollableViewportSize(new Dimension(100,100));
		add(new JScrollPane(edgeRegionTable),c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel edgeButtonPanel = new JPanel();
		edgeButtonPanel.setLayout(new BoxLayout(edgeButtonPanel,BoxLayout.LINE_AXIS));
		JButton generateEdgesButton = new JButton("Generate");
		generateEdgesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateEdgesCommand();
			}
		});
		edgeButtonPanel.add(generateEdgesButton);
		JButton clearEdgesButton = new JButton("Clear");
		clearEdgesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearEdgesCommand();
			}
		});
		edgeButtonPanel.add(clearEdgesButton);
		add(edgeButtonPanel, c);
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.gridx++;
		c.gridy = 0;
		c.gridheight = 4;
		layeredPane = new VizLayeredPane(this, cityPanel.getCity(), system);
		add(layeredPane,c);
	}
	
	private void clearNodesCommand() {
		System.out.println("Clear Nodes Command");
		system.setNodes(new HashSet<Node>());
		layeredPane.repaint();
	}
	
	private void generateNodesCommand() {
		System.out.println("Generate Nodes Command");
		if(!system.getNodes().isEmpty())
			clearNodesCommand();
		for(NodeRegion r : nodeRegionTableModel.getSelectedRegions()) {
			r.generateNodes(system);
		}
		layeredPane.repaint();
	}
	
	private void clearEdgesCommand() {
		System.out.println("Clear Edges Command");
		system.setEdges(new HashSet<Edge>());
		layeredPane.repaint();
	}
	
	private void generateEdgesCommand() {
		System.out.println("Generate Edges Command");
		if(!system.getEdges().isEmpty())
			clearEdgesCommand();
		for(EdgeRegion r : edgeRegionTableModel.getSelectedRegions()) {
			r.generateEdges(system);
		}
		layeredPane.repaint();
	}
	
	/**
	 * Gets the city panel.
	 *
	 * @return the city panel
	 */
	public CityPanel getCityPanel() {
		return cityPanel;
	}
	
	/**
	 * Gets the system.
	 *
	 * @return the system
	 */
	public CitySystem getSystem() {
		return system;
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
