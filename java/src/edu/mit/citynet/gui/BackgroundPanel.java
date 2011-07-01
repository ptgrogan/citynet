package edu.mit.citynet.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
	private static final long serialVersionUID = 4630026509701808585L;
	private Image logo;
	
	public BackgroundPanel() {
		try {
			logo = ImageIO.read(getClass().getClassLoader().getResource(
					"resources" + System.getProperty("file.separator") + 
					"citynetLogo.png"));
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(this, 
					"An unhandled exception of type " + 
					e1.getClass().getSimpleName() + 
					" occurred while opening the City.Net logo.\nPlease " +
					"check the stack trace for more information.");
			e1.printStackTrace();
		}
		setBackground(Color.WHITE);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		add(new JLabel(new ImageIcon(logo)), c);
	}
}