package edu.mit.citynet.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.mit.citynet.core.City;

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
	}
	
	public void newCityCommand() {
		System.out.println("New City Command");
		if(cityPanel != null) {
			closeCityCommand();
		}
		City city = new City();
		city.setName("New City");
		cityPanel = new CityPanel(city);
		setContentPane(cityPanel);
		validate();
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
