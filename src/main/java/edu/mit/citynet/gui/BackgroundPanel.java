/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * BackgroundPanel class.
 * 
 * A simple background to display when no city is being edited in the GUI.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class BackgroundPanel extends JPanel {
	private static final long serialVersionUID = 4630026509701808585L;
	private Image logo;
	
	/**
	 * Instantiates a new background panel.
	 * 
	 * Reads the City.Net logo and filters it to appear slightly transparent.
	 */
	public BackgroundPanel() {
		Image rawLogo = new ImageIcon(BackgroundPanel.class.getResource("/images/citynetLogo.png")).getImage();
		ImageFilter filter = new RGBImageFilter() {
			public int filterRGB(int x, int y, int rgb) {
				return 0x66ffffff & rgb; // make slightly transparent
			}
		};
		ImageProducer ip = new FilteredImageSource(rawLogo.getSource(), filter);
		logo = Toolkit.getDefaultToolkit().createImage(ip);
		setLayout(new BorderLayout());
		add(new JLabel(new ImageIcon(logo)), BorderLayout.CENTER);
	}
}