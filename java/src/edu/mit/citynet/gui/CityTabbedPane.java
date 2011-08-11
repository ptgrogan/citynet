package edu.mit.citynet.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.mit.citynet.core.Cell;
import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.util.CityNetIcon;

/**
 * The CityPanel class is a panel to display city information.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CityTabbedPane extends JTabbedPane {
	private static final long serialVersionUID = -9157294901016405959L;

	private CityNetFrame cityNetFrame;
	private City city;
	private SystemDetailsPanel systemDetailsPanel;
	private CityPanel cityVizPanel;
	
	/**
	 * Instantiates a new city panel.
	 *
	 * @param cityNetFrame the city net frame
	 * @param city the city
	 */
	public CityTabbedPane(CityNetFrame cityNetFrame, City city) {
		if (city==null) {
			throw new IllegalArgumentException("City cannot be null.");
		}
		this.cityNetFrame = cityNetFrame;
		this.city = city;
		systemDetailsPanel = new SystemDetailsPanel();
		initializePanel();
	}
	
	/**
	 * Initializes the panel.
	 */
	private void initializePanel() {
		cityVizPanel = new CityPanel(this);
		addTab("City", CityNetIcon.CITY.getIcon(), cityVizPanel);
		for(CitySystem system : city.getSystems()) {
			SystemPanel systemPanel = new SystemPanel(this, system);
			addTab(system.getName(), system.getType().getIcon(), 
					systemPanel, system.getDescription());
		}
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					Component c = getComponentAt(getSelectedIndex());
					if(c instanceof CityPanel)
						cityNetFrame.editCityDetailsCommand();
					else if(c instanceof SystemPanel)
						editSystemDetailsCommand(((SystemPanel)c).getSystem());
				}
			}
			public void mousePressed(MouseEvent e) {
				maybeShowPopup(e);
			}
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}
			private void maybeShowPopup(MouseEvent e) {
				if(e.isPopupTrigger()) {
					Component c = getSelectedComponent();
					if(c instanceof SystemPanel)
						createSystemTabsPopupMenu(((SystemPanel)c).getSystem()).show(e.getComponent(), e.getX(), e.getY());
					else
						createSystemTabsPopupMenu(null).show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		addTab("+", null, new JPanel(), "Add a new system");
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(getSelectedIndex()==getTabCount()-1) {
					setSelectedIndex(0);
					newSystemCommand();
				}
			}
		});
	}

	/**
	 * Creates the system tabs popup menu.
	 *
	 * @param system the system
	 * @return the j popup menu
	 */
	private JPopupMenu createSystemTabsPopupMenu(final CitySystem system) {
		JPopupMenu systemTabsPopupMenu = new JPopupMenu();
		if(system != null) {
			JMenuItem editSystemDetailsMenuItem = new JMenuItem("Edit System Details");
			editSystemDetailsMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					editSystemDetailsCommand(system);
				}
			});
			systemTabsPopupMenu.add(editSystemDetailsMenuItem);
			JMenuItem deleteSystemMenuItem = new JMenuItem("Delete System");
			deleteSystemMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteSystemCommand(system);
				}
			});
			systemTabsPopupMenu.add(deleteSystemMenuItem);
		} else {
			JMenuItem newSystemMenuItem = new JMenuItem("Add New System");
			newSystemMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					newSystemCommand();
				}
			});
			systemTabsPopupMenu.add(newSystemMenuItem);
		}
		return systemTabsPopupMenu;
	}
	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public City getCity() {
		return city;
	}
	
	/**
	 * Gets the city net frame.
	 *
	 * @return the city net frame
	 */
	public CityNetFrame getCityNetFrame() {
		return cityNetFrame;
	}

	/**
	 * Clear cells command.
	 */
	public void clearCellsCommand() {
		System.out.println("Clear Cells Command");
		for(Component c : getComponents()) {
			if(c instanceof SystemPanel) {
				SystemPanel p = (SystemPanel)c;
				if(!p.getSystem().getEdges().isEmpty())
					p.clearEdgesCommand();
				if(!p.getSystem().getNodes().isEmpty())
					p.clearNodesCommand();
			}
		}
		city.setCells(new HashSet<Cell>());
	}
	
	/**
	 * Generate cells command.
	 */
	public void generateCellsCommand(Set<CellRegion> cellRegions) {
		System.out.println("Generate Cells Command");
		if(!city.getCells().isEmpty())
			clearCellsCommand();
		for(CellRegion r : cellRegions) {
			r.generateCells();
		}
	}
	
	/**
	 * New system command.
	 */
	private void newSystemCommand() {
		CitySystem system = new CitySystem();
		addSystemCommand(system);
		editSystemDetailsCommand(system);
	}
	
	/**
	 * Adds the system command.
	 *
	 * @param system the system
	 */
	public void addSystemCommand(CitySystem system) {
		System.out.println("Add System Command");
		city.addSystem(system);
		SystemPanel systemPanel = new SystemPanel(this, system);
		insertTab(system.getName(), system.getType().getIcon(), 
				systemPanel, system.getDescription(), getTabCount()-1);
		setSelectedIndex(getTabCount()-2);
	}
	
	/**
	 * Command to edit the system details.
	 */
	public void editSystemDetailsCommand(CitySystem system) {
		System.out.println("Edit System Details Command");
		systemDetailsPanel.loadSystemDetails(system);
		int value = JOptionPane.showConfirmDialog(this,systemDetailsPanel,"City.Net | System Details", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			systemDetailsPanel.saveSystemDetailsCommand();
			setTitleAt(getSelectedIndex(), system.getName());
			setIconAt(getSelectedIndex(), system.getType().getIcon());
			setToolTipTextAt(getSelectedIndex(), system.getDescription());
		}
	}
	
	/**
	 * Delete system command.
	 *
	 * @param system the system
	 */
	public void deleteSystemCommand(CitySystem system) {
		System.out.println("Delete System Details Command");
		int value = JOptionPane.showConfirmDialog(this, "Do you want to delete the " + system.getName() + " system?", 
				"City.Net | Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			int index = getSelectedIndex();
			setSelectedIndex(index-1);
			removeTabAt(index);
		}
	}
}
