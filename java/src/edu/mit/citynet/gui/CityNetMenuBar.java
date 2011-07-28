package edu.mit.citynet.gui;

import java.awt.Graphics;
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

	CityNetFrame frame;
	JMenu fileMenu;
	JMenuItem newCityItem, openCityItem, closeCityItem, saveCityAsItem, 
		saveCityItem, exitItem;
	
	/**
	 * Instantiates a new city net menu bar.
	 *
	 * @param frame the City.Net frame
	 */
	public CityNetMenuBar(final CityNetFrame frame) {
		this.frame = frame;
	    fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
	    newCityItem = new JMenuItem("New City", KeyEvent.VK_N);
	    newCityItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.CTRL_MASK));
	    newCityItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
    			frame.newCityCommand();
        	}
        });
        fileMenu.add(newCityItem);
        openCityItem = new JMenuItem("Open City", KeyEvent.VK_O);
        openCityItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openCityItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
    			frame.openCityCommand();
        	}
        });
        fileMenu.add(openCityItem);
        fileMenu.addSeparator();
        closeCityItem = new JMenuItem("Close City", KeyEvent.VK_C);
        closeCityItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        closeCityItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(getTopLevelAncestor() instanceof CityNetFrame) {
        			((CityNetFrame)getTopLevelAncestor()).closeCityCommand();
        		}
        	}
        });
        fileMenu.add(closeCityItem);
        fileMenu.addSeparator();
        saveCityItem = new JMenuItem("Save City", KeyEvent.VK_S);
        saveCityItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveCityItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
    			frame.saveCityCommand();
        	}
        });
        fileMenu.add(saveCityItem);
        saveCityAsItem = new JMenuItem("Save City as...", KeyEvent.VK_A);
        saveCityAsItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
    			frame.saveCityAsCommand();
        	}
        });
        fileMenu.add(saveCityAsItem);
        fileMenu.addSeparator();
        exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        exitItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
    			frame.exitCommand();
        	}
        });
        fileMenu.add(exitItem);
	    add(fileMenu);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		newCityItem.setEnabled(!frame.isCityOpen());
		openCityItem.setEnabled(!frame.isCityOpen());
		closeCityItem.setEnabled(frame.isCityOpen());
		saveCityItem.setEnabled(frame.isCityOpen());
		saveCityAsItem.setEnabled(frame.isCityOpen());
	}
}
