package edu.mit.citynet.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import edu.mit.citynet.CityNet;
import edu.mit.citynet.core.City;
import edu.mit.citynet.io.SpreadsheetTemplate;

/**
 * CityNetFrame class.
 * 
 * The core graphical user interface (GUI) component.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CityNetFrame extends JFrame {
	private static final long serialVersionUID = -4707459231955143750L;
	CityNetMenuBar menuBar;
	JPanel backgroundPanel;
	CityPanel cityPanel;
	JFileChooser fileChooser;
	
	/**
	 * Instantiates a new city net frame.
	 */
	public CityNetFrame() {
		super("City.Net");
		menuBar = new CityNetMenuBar();
		setJMenuBar(menuBar);
		backgroundPanel = new BackgroundPanel();
		setContentPane(backgroundPanel);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitCommand();
			}
		});
		fileChooser = new JFileChooser(System.getProperty("user.dir")) {
			private static final long serialVersionUID = 5853237903722516861L;
			public void approveSelection() {
				if(getDialogType()==JFileChooser.SAVE_DIALOG) {
					File file = getSelectedFile();
					if (file != null && file.exists()) {
						int answer = JOptionPane.showOptionDialog(this, 
								"City '" + file.getAbsolutePath() + "' already exists. Overwrite?", 
								"Save Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
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
	}
	
	public void newCityCommand() {
		System.out.println("New City Command");
		if(cityPanel != null) {
			closeCityCommand();
		}
		City city = new City();
		CityNet.getInstance().setCity(city);
		city.setName("New City");
		cityPanel = new CityPanel(city);
		setContentPane(cityPanel);
		validate();
	}
	
	public void openCityCommand() {
		System.out.println("Open City Command");
		if(cityPanel != null) {
			closeCityCommand();
		}
		int returnVal = fileChooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			SpreadsheetTemplate t = new SpreadsheetTemplate();
			t.setFilePath(fileChooser.getSelectedFile().getAbsolutePath());
			try {
				City city = t.readTemplate();
				CityNet.getInstance().setCity(city);
				cityPanel = new CityPanel(city);
				setContentPane(cityPanel);
				validate();
				pack();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closeCityCommand() {
		System.out.println("Close City Command");
		cityPanel = null;
		setContentPane(backgroundPanel);
		validate();
	}
	
	public void exitCommand() {
		System.out.println("Exit Command");
		if(cityPanel != null) {
			closeCityCommand();
		}
		dispose();
	}
}
