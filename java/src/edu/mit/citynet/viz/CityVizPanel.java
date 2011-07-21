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

import edu.mit.citynet.core.Cell;
import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.EdgeRegion;
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
	private RegionTableModel<CellRegion> cellRegionTableModel;
	
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
		cellRegionTableModel = new RegionTableModel<CellRegion>();
		cellRegionTableModel.setRegions(cityPanel.getCity().getCellRegions());
		cellRegionTableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				layeredPane.repaint();
			}
		});
		JTable cellRegionTable = new JTable(cellRegionTableModel);
		cellRegionTable.getTableHeader().setReorderingAllowed(false);
		cellRegionTable.getColumnModel().getColumn(0).setMaxWidth(25);
		cellRegionTable.getColumnModel().getColumn(0).setHeaderValue(null);
		cellRegionTable.getColumnModel().getColumn(1).setHeaderValue("Cell Region");
		cellRegionTable.setPreferredScrollableViewportSize(new Dimension(100,100));
		add(new JScrollPane(cellRegionTable),c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.LINE_AXIS));
		JButton generateCellsButton = new JButton("Generate");
		generateCellsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateCellsCommand();
			}
		});
		buttonPanel.add(generateCellsButton);
		JButton clearCellsButton = new JButton("Clear");
		clearCellsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearCellsCommand();
			}
		});
		buttonPanel.add(clearCellsButton);
		add(buttonPanel, c);
		c.gridy = 0;
		c.gridheight = 2;
		c.gridx++;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		layeredPane = new VizLayeredPane(this, cityPanel.getCity(), system);
		layeredPane.setPreferredSize(new Dimension(250,250));
		add(layeredPane,c);
	}
	
	/**
	 * Clear cells command.
	 */
	private void clearCellsCommand() {
		System.out.println("Clear Cells Command");
		cityPanel.getCity().setCells(new HashSet<Cell>());
		layeredPane.repaint();
	}
	
	/**
	 * Generate cells command.
	 */
	private void generateCellsCommand() {
		System.out.println("Generate Cells Command");
		clearCellsCommand();
		for(CellRegion r : cellRegionTableModel.getSelectedRegions()) {
			r.generateCells();
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
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz.AbstractVizPanel#getSelectedCellRegions()
	 */
	public Set<CellRegion> getSelectedCellRegions() {
		return new HashSet<CellRegion>(cellRegionTableModel.getSelectedRegions());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz2.AbstractVizPanel#getSelectedNodeRegions()
	 */
	public Set<NodeRegion> getSelectedNodeRegions() {
		return new HashSet<NodeRegion>();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz.AbstractVizPanel#getSelectedEdgeRegions()
	 */
	public Set<EdgeRegion> getSelectedEdgeRegions() {
		return new HashSet<EdgeRegion>();
	}
}
