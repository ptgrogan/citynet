package edu.mit.citynet.gui;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
	JPanel defaultPanel;
	CityPanel cityPanel;
	
	/**
	 * Instantiates a new city net frame.
	 */
	public CityNetFrame() {
		super("City.Net");
		defaultPanel = new JPanel();
		defaultPanel.setBackground(new Color(0x9999aa));
		setContentPane(defaultPanel);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_N && e.isControlDown()) {
					newCityCommand();
				} else if(e.getKeyCode()==KeyEvent.VK_W && e.isControlDown()) {
					closeCityCommand();
				} else if(e.getKeyCode()==KeyEvent.VK_Q && e.isControlDown()) {
					quitCommand();
				}
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				quitCommand();
			}
		});
	}
	
	private void newCityCommand() {
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
	
	private void closeCityCommand() {
		System.out.println("Close City Command");
		cityPanel = null;
		setContentPane(defaultPanel);
		validate();
	}
	
	private void quitCommand() {
		System.out.println("Quit Command");
		if(cityPanel != null) {
			closeCityCommand();
		}
		dispose();
	}
}
