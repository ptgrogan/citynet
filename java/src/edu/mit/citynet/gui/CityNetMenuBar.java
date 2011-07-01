package edu.mit.citynet.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * CityNetMenuBar class.
 * 
 * The menu bar along the top of the City.Net frame.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CityNetMenuBar extends JMenuBar {
	private static final long serialVersionUID = -6018553717087352056L;
	JMenu fileMenu;
	JMenuItem newCityItem, closeCityItem, exitItem;
	
	public CityNetMenuBar() {
	    fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
	    newCityItem = new JMenuItem("New City", KeyEvent.VK_N);
	    newCityItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.CTRL_MASK));
	    newCityItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// new city command
        	}
        });
        fileMenu.add(newCityItem);
        closeCityItem = new JMenuItem("Close Scenario", KeyEvent.VK_C);
        closeCityItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        closeCityItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// close city command
        	}
        });
        fileMenu.add(closeCityItem);
        exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        exitItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// exit command
        	}
        });
        fileMenu.add(exitItem);
	    add(fileMenu);
	}
	
}
