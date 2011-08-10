package edu.mit.citynet.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.mit.citynet.core.Cell;
import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.util.CityNetIcon;
import edu.mit.citynet.viz.CityVizPanel;

/**
 * The CityPanel class is a panel to display city information.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CityPanel extends JPanel {
	private static final long serialVersionUID = -9157294901016405959L;

	private CityNetFrame cityNetFrame;
	private City city;
	private SystemDetailsPanel systemDetailsPanel;
	private JTabbedPane tabbedPane;
	private CityVizPanel cityVizPanel;
	
	/**
	 * Instantiates a new city panel.
	 *
	 * @param cityNetFrame the city net frame
	 * @param city the city
	 */
	public CityPanel(CityNetFrame cityNetFrame, City city) {
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
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		tabbedPane = new JTabbedPane();
		cityVizPanel = new CityVizPanel(this);
		tabbedPane.addTab("City", CityNetIcon.CITY.getIcon(), cityVizPanel);
		for(CitySystem system : city.getSystems()) {
			SystemPanel systemPanel = new SystemPanel(this, system);
			tabbedPane.addTab(system.getName(), system.getType().getIcon(), 
					systemPanel, system.getDescription());
		}
		tabbedPane.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					Component c = tabbedPane.getComponentAt(tabbedPane.getSelectedIndex());
					if(c instanceof CityVizPanel)
						cityNetFrame.editCityDetailsCommand();
					else if(c instanceof SystemPanel)
						editSystemDetailsCommand(((SystemPanel)c).getSystem());
				}
			}
		});
		tabbedPane.addTab("+", new JPanel());
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(tabbedPane.getSelectedIndex()==tabbedPane.getTabCount()-1) {
					tabbedPane.setSelectedIndex(0);
					CitySystem system = new CitySystem();
					addSystemCommand(system);
					editSystemDetailsCommand(system);
				}
			}
		});
		add(tabbedPane, BorderLayout.CENTER);
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
		for(Component c : tabbedPane.getComponents()) {
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
	 * Adds the system command.
	 *
	 * @param system the system
	 */
	public void addSystemCommand(CitySystem system) {
		System.out.println("Add System Command");
		city.addSystem(system);
		SystemPanel systemPanel = new SystemPanel(this, system);
		tabbedPane.insertTab(system.getName(), system.getType().getIcon(), 
				systemPanel, system.getDescription(), tabbedPane.getTabCount()-1);
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-2);
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
			tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), system.getName());
			tabbedPane.setIconAt(tabbedPane.getSelectedIndex(), system.getType().getIcon());
			tabbedPane.setToolTipTextAt(tabbedPane.getSelectedIndex(), system.getDescription());
		}
	}
}
