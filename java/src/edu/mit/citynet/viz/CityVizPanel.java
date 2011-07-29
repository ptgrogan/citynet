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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.gui.CityPanel;
import edu.mit.citynet.util.CityNetIcon;

/**
 * The CityVizPanel class provides a city-level visualization of cells and
 * cell regions. The cell region visibility can be toggled using a tables.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CityVizPanel extends AbstractVizPanel {
	private static final long serialVersionUID = 3994034732879260199L;
	
	private CityPanel cityPanel;
	private VizLayeredPane layeredPane;
	private RegionTableModel<CellRegion> cellRegionTableModel;
	private RegionTable<CellRegion> cellRegionTable;
	
	/**
	 * Instantiates a new city viz panel.
	 *
	 * @param cityPanel the city panel
	 */
	public CityVizPanel(final CityPanel cityPanel) {
		if (cityPanel==null) {
			throw new IllegalArgumentException("City Panel cannot be null.");
		}
		this.cityPanel = cityPanel;
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
		c.weightx = 0.5;
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
		cellRegionTable = new RegionTable<CellRegion>(cellRegionTableModel);
		cellRegionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				repaint();
			}
		});
		cellRegionTable.getTableHeader().setReorderingAllowed(false);
		cellRegionTable.getColumnModel().getColumn(0).setMaxWidth(25);
		cellRegionTable.getColumnModel().getColumn(0).setHeaderValue(null);
		cellRegionTable.getColumnModel().getColumn(1).setHeaderValue("Cell Region");
		cellRegionTable.setPreferredScrollableViewportSize(new Dimension(200,400));
		leftPanel.add(new JScrollPane(cellRegionTable),c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.LINE_AXIS));
		JButton generateCellsButton = new JButton("Generate",CityNetIcon.CELL_REGIONS.getIcon());
		generateCellsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cityPanel.generateCellsCommand(cellRegionTableModel.getCheckedRegions());
				repaint();
			}
		});
		buttonPanel.add(generateCellsButton);
		JButton clearCellsButton = new JButton("Clear",CityNetIcon.DELETE.getIcon());
		clearCellsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cityPanel.clearCellsCommand();
				repaint();
			}
		});
		buttonPanel.add(clearCellsButton);
		leftPanel.add(buttonPanel, c);
		setLeftComponent(leftPanel);
		JPanel rightPanel = new JPanel(new BorderLayout());
		layeredPane = new VizLayeredPane(this, cityPanel.getCity(), null);
		rightPanel.add(layeredPane,BorderLayout.CENTER);
		setRightComponent(rightPanel);
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
	public Set<CellRegion> getCheckedCellRegions() {
		// display all cell regions selected in table
		return new HashSet<CellRegion>(cellRegionTableModel.getCheckedRegions());
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz2.AbstractVizPanel#getSelectedNodeRegions()
	 */
	public Set<NodeRegion> getCheckedNodeRegions() {
		// presently do not display any node regions
		return new HashSet<NodeRegion>();
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz.AbstractVizPanel#getSelectedEdgeRegions()
	 */
	public Set<EdgeRegion> getCheckedEdgeRegions() {
		// presently do not display any edge regions
		return new HashSet<EdgeRegion>();
	}

	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz.AbstractVizPanel#getSelectedCellRegions()
	 */
	public Set<CellRegion> getSelectedCellRegions() {
		return cellRegionTable.getSelectedRegions();
	}

	/* (non-Javadoc)
	 * @see edu.mit.citynet.viz.AbstractVizPanel#getSelectedNodeRegions()
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
