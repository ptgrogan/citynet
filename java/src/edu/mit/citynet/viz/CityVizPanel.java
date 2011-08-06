package edu.mit.citynet.viz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.gui.CellRegionPanel;
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
	
	private CellRegionPanel cellRegionPanel;
	private CityPanel cityPanel;
	private VizLayeredPane layeredPane;
	private RegionTableModel<CellRegion> cellRegionTableModel;
	private RegionTable<CellRegion> cellRegionTable;
	private JButton editCellRegionButton, deleteCellRegionsButton;
	
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
		cellRegionPanel = new CellRegionPanel();
		initializePanel();
	}
	
	/**
	 * Creates the cell region popup menu.
	 *
	 * @param regions the regions
	 * @return the j popup menu
	 */
	private JPopupMenu createCellRegionPopupMenu(Set<CellRegion> regions) {
		JPopupMenu cellRegionPopupMenu = new JPopupMenu();
		if(regions.size()>0) {
			JMenuItem editCellRegionMenuItem = new JMenuItem("Edit Cell Region");
			editCellRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editCellRegionCommand();
				}
			});
			editCellRegionMenuItem.setEnabled(regions.size()==1);
			cellRegionPopupMenu.add(editCellRegionMenuItem);
			JMenuItem deleteCellRegionMenuItem = new JMenuItem("Delete Cell Region" + (regions.size()>1?"s":""));
			deleteCellRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteCellRegionsCommand();
				}
			});
			cellRegionPopupMenu.add(deleteCellRegionMenuItem);
		} else {
			JMenuItem addCellRegionMenuItem = new JMenuItem("Add Cell Region");
			addCellRegionMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addCellRegionCommand();
				}
			});
			cellRegionPopupMenu.add(addCellRegionMenuItem);
		}
		return cellRegionPopupMenu;
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
		MouseAdapter cellRegionMouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					if(cellRegionTable.getSelectionModel().isSelectionEmpty())
						addCellRegionCommand();
					else
						editCellRegionCommand();
				}
			}
			public void mousePressed(MouseEvent e) {
				if(e.getComponent()!=cellRegionTable)
					cellRegionTable.getSelectionModel().clearSelection();
				maybeShowPopup(e);
			}
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}
			private void maybeShowPopup(MouseEvent e) {
				if(e.isPopupTrigger()) {
					int row = cellRegionTable.rowAtPoint(e.getPoint());
					CellRegion region = (CellRegion)cellRegionTableModel.getRegionAt(row);
					if(!cellRegionTable.getSelectedRegions().contains(region)) {
						cellRegionTable.getSelectionModel().addSelectionInterval(row, row);
					}
					createCellRegionPopupMenu(cellRegionTable.getSelectedRegions()).show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
		cellRegionTable.addMouseListener(cellRegionMouseAdapter);
		cellRegionTable.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_DELETE) {
					deleteCellRegionsCommand();
				} else if(e.getKeyCode()==KeyEvent.VK_ENTER && cellRegionTable.getSelectedRowCount()==1){
					editCellRegionCommand();
				}
			}
		});
		cellRegionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				editCellRegionButton.setEnabled(cellRegionTable.getSelectedRowCount()==1);
				deleteCellRegionsButton.setEnabled(cellRegionTable.getSelectedRowCount()>0);
			}
		});
		JScrollPane cellRegionScroll = new JScrollPane(cellRegionTable);
		cellRegionScroll.addMouseListener(cellRegionMouseAdapter);
		leftPanel.add(cellRegionScroll,c);
		c.gridy++;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		JPanel cellRegionPanel = new JPanel();
		cellRegionPanel.setLayout(new BoxLayout(cellRegionPanel,BoxLayout.LINE_AXIS));
		JButton addCellRegionButton = new JButton("Add");
		addCellRegionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addCellRegionCommand();
			}
		});
		cellRegionPanel.add(addCellRegionButton);
		editCellRegionButton = new JButton("Edit");
		editCellRegionButton.setEnabled(false);
		editCellRegionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editCellRegionCommand();
			}
		});
		cellRegionPanel.add(editCellRegionButton);
		deleteCellRegionsButton = new JButton("Delete");
		deleteCellRegionsButton.setEnabled(false);
		deleteCellRegionsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteCellRegionsCommand();
			}
		});
		cellRegionPanel.add(deleteCellRegionsButton);
		leftPanel.add(cellRegionPanel, c);
		c.gridy++;
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
	 * Adds the cell region command.
	 */
	private void addCellRegionCommand() {
		System.out.println("Add Cell Region Command");
		CellRegion cellRegion = new CellRegion();
		cellRegionPanel.loadCellRegion(cellRegion);
		int value = JOptionPane.showConfirmDialog(this,cellRegionPanel,"City.Net | Cell Region", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			cellRegionPanel.saveCellRegionCommand();
			cityPanel.getCity().addCellRegion(cellRegion);
			// TODO: should only add new cell region to table model and update
			cellRegionTableModel.setRegions(cityPanel.getCity().getCellRegions());
			repaint();
		}
	}
	
	/**
	 * Edits the cell region command.
	 */
	private void editCellRegionCommand() {
		System.out.println("Edit Cell Region Command");
		cellRegionPanel.loadCellRegion(cellRegionTable.getSelectedRegion());
		int value = JOptionPane.showConfirmDialog(this,cellRegionPanel,"City.Net | Cell Region", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			cellRegionPanel.saveCellRegionCommand();
			repaint();
		}
	}
	
	/**
	 * Delete cell regions command.
	 */
	private void deleteCellRegionsCommand() {
		System.out.println("Delete Cell Regions Command");
		int value = JOptionPane.showConfirmDialog(this, "Do you want to delete these cell regions?", 
				"City.Net | Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			Set<CellRegion> regions = cityPanel.getCity().getCellRegions();
			regions.removeAll(cellRegionTable.getSelectedRegions());
			// TODO: should move removeAll method to the city
			cityPanel.getCity().setCellRegions(regions);
			// TODO: should remove cell region from table model and update
			cellRegionTableModel.setRegions(cityPanel.getCity().getCellRegions());
			repaint();
		}
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
