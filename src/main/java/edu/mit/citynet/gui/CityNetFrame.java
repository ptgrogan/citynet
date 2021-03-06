/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import edu.mit.citynet.CityNet;
import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.io.SpreadsheetTemplate;
import edu.mit.citynet.viz.DisplayOptionsPanel;

/**
 * CityNetFrame class.
 * 
 * The core graphical user interface (GUI) component.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CityNetFrame extends JFrame {
	private static final long serialVersionUID = -4707459231955143750L;
	private CityNetMenuBar menuBar;
	private JPanel backgroundPanel;
	private CityTabbedPane cityPanel;
	private JFileChooser fileChooser;
	private CityDetailsPanel cityDetailsPanel;
	private DisplayOptionsPanel displayOptionsPanel;
	private SystemImportPanel systemImportPanel;
	private SpreadsheetTemplate template;
	
	/**
	 * Instantiates a new city net frame.
	 */
	public CityNetFrame() {
		super("City.Net");
		template = new SpreadsheetTemplate();
		menuBar = new CityNetMenuBar(this);
		backgroundPanel = new BackgroundPanel();
		fileChooser = new JFileChooser(System.getProperty("user.dir")) {
			private static final long serialVersionUID = 5853237903722516861L;
			public void approveSelection() {
				if(getDialogType()==JFileChooser.SAVE_DIALOG) {
					File file = getSelectedFile();
					if (file != null && file.exists()) {
						int answer = JOptionPane.showOptionDialog(this, 
								"City '" + file.getAbsolutePath() + "' already exists. Overwrite?", 
								"City.Net | Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
					    if (answer == JOptionPane.NO_OPTION) {
							return;
					    }
					}
				}
				super.approveSelection();
		    }
		};
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if(f.isDirectory()) {
					return true;
				}
				String extension = null;
				
				String s = f.getName();
				int i = s.lastIndexOf('.');
				if (i > 0 &&  i < s.length() - 1) {
					extension = s.substring(i+1).toLowerCase();
		        }
				
				if(extension != null) {
					if(extension.equals("xls")) return true;
					else return false;
				} else return false;
			}

			@Override
			public String getDescription() {
				return "XLS Files";
			}
		});
		cityDetailsPanel = new CityDetailsPanel();
		displayOptionsPanel = new DisplayOptionsPanel();
		systemImportPanel = new SystemImportPanel();
		initializeFrame();
	}
	
	/**
	 * Initializes the frame.
	 */
	private void initializeFrame() {
		setJMenuBar(menuBar);
		setContentPane(backgroundPanel);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// try to specify multiple icon sizes (used for windows 7 task bar)
	    try {
	        java.lang.reflect.Method method = Class.forName("java.awt.Window").getDeclaredMethod("setIconImages", java.util.List.class);
			ArrayList<Image> images = new ArrayList<Image>();
			images.add(new ImageIcon(CityNetFrame.class.getResource("/images/citynet_16.png")).getImage());
			images.add(new ImageIcon(CityNetFrame.class.getResource("/images/citynet_32.png")).getImage());
	        method.invoke(this,images);
	    } catch( ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e ) {
	    	// otherwise assign small icon only
			setIconImage(new ImageIcon(CityNetFrame.class.getResource("/images/citynet_16.png")).getImage());
	    }
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitCommand();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		if(isCityOpen()) setTitle("City.Net | " + cityPanel.getCity().getName());
		else setTitle("City.Net");
	}
	
	/**
	 * Checks if is city open.
	 *
	 * @return true, if is city open
	 */
	public boolean isCityOpen() {
		return cityPanel!=null;
	}
	
	/**
	 * Creates a new city.
	 */
	public void newCityCommand() {
		System.out.println("New City Command");
		if(cityPanel != null) {
			closeCityCommand();
		}
		template.setFilePath(null);
		openCityCommand(new City());
		editCityDetailsCommand();
	}
	
	/**
	 * Opens a city from file.
	 */
	public void openCityCommand() {
		System.out.println("Open City Command");
		if(cityPanel != null) {
			closeCityCommand();
		}
		int returnVal = fileChooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			template.setFilePath(fileChooser.getSelectedFile().getAbsolutePath());
			try {
				openCityCommand(template.readTemplate());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Open city command.
	 *
	 * @param city the city
	 */
	private void openCityCommand(City city) {
		CityNet.getInstance().setCity(city);
		cityPanel = new CityTabbedPane(this,city);
		setContentPane(cityPanel);
		validate();
		repaint();
	}
	
	/**
	 * Closes the existing city (prompts to save changes).
	 *
	 * @return true, if successful
	 */
	public boolean closeCityCommand() {
		System.out.println("Close City Command");
		int answer = JOptionPane.showOptionDialog(this, 
				"Save changes to '" + cityPanel.getCity().getName() + "' before closing?", 
				"City.Net | Warning", JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.WARNING_MESSAGE, null, null, null);
	    if(answer == JOptionPane.CANCEL_OPTION) {
	    	return false;
	    } else if (answer == JOptionPane.YES_OPTION) {
			saveCityCommand();
	    }
		CityNet.getInstance().setCity(null);
		cityPanel = null;
		setContentPane(backgroundPanel);
		validate();
		repaint();
		return true;
	}
	
	/**
	 * Saves the city as a new file.
	 */
	public void saveCityAsCommand() {
		System.out.println("Save City As Command");
		if(cityPanel != null) {
			int returnVal = fileChooser.showSaveDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				String filePath = fileChooser.getSelectedFile().getAbsolutePath();
				if(!filePath.substring(filePath.length() - 4, filePath.length()).equals(".xls")) {
					filePath += ".xls";
				}
				template.setFilePath(filePath);
				saveCityCommand();
			}
		}
	}
	
	/**
	 * Saves the existing city (prompts to choose location if non-existent).
	 */
	public void saveCityCommand() {
		System.out.println("Save City Command");
		if(cityPanel != null) {
			if(template.getFilePath()==null) {
				saveCityAsCommand();
			} else {
				try {
					template.writeTemplate(cityPanel.getCity());
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this, "An error of type " + 
							e.getClass().getSimpleName() + " occurred while saving the city.",
							"City.Net | Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Exits the City.Net application (calls close command if city open).
	 */
	public void exitCommand() {
		System.out.println("Exit Command");
		if(cityPanel != null) {
			if(closeCityCommand()) dispose();
		} else {
			dispose();
		}
	}
	
	/**
	 * Command to edit the city details.
	 */
	public void editCityDetailsCommand() {
		System.out.println("Edit City Details Command");
		cityDetailsPanel.loadCityDetails(cityPanel.getCity());
		int value = JOptionPane.showConfirmDialog(this,cityDetailsPanel,"City.Net | City Details", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			cityDetailsPanel.saveCityDetailsCommand();
			repaint();
		}
	}

	/**
	 * Edits the display options command.
	 */
	public void editDisplayOptionsCommand() {
		System.out.println("Edit Display Options Command");
		displayOptionsPanel.loadDisplayOptions(CityNet.getInstance().getDisplayOptions());
		int value = JOptionPane.showConfirmDialog(this,displayOptionsPanel,"City.Net | Display Options", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			displayOptionsPanel.saveDisplayOptionsCommand();
			repaint();
		}
	}
	
	/**
	 * Command to import a system.
	 */
	public void importSystemCommand() {
		System.out.println("Import System Command");
		int value = JOptionPane.showConfirmDialog(this,systemImportPanel,"City.Net | Import System", 
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			for(CitySystem system : systemImportPanel.getSelectedSystems()) {
				cityPanel.addSystemCommand(system);
			}
		}
	}
}
