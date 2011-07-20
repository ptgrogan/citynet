package edu.mit.citynet.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.viz.SystemVizPanel;

public class SystemPanel extends JPanel {
	private static final long serialVersionUID = 7969091470964127984L;
	
	private CityPanel cityPanel;
	private CitySystem system;
	private JTextArea descriptionText;
	private SystemVizPanel systemVizPanel;
	
	/**
	 * Instantiates a new system panel.
	 *
	 * @param system the system
	 */
	public SystemPanel(CityPanel cityPanel, CitySystem system) {
		this.cityPanel = cityPanel;
		this.system = system;
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0;
		c.insets = new Insets(2,2,2,2);
		add(new JLabel("Description:"),c);
		c.gridx++;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		descriptionText = new JTextArea(system.getDescription());
		descriptionText.setLineWrap(true);
		descriptionText.setWrapStyleWord(true);
		descriptionText.setFont(new Font("Sans-Serif", Font.PLAIN, 11));
		descriptionText.setMargin(new Insets(1,2,1,2));
		descriptionText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				saveSystemDescriptionCommand();
			}
		});
		add(new JScrollPane(descriptionText), c);
		systemVizPanel = new SystemVizPanel(cityPanel, system);
		c.gridx = 0;
		c.gridy++;
		c.weighty = 1;
		c.gridwidth = 2;
		add(systemVizPanel,c);
	}
	
	/**
	 * Gets the city panel.
	 *
	 * @return the city panel
	 */
	public CityPanel getCityPanel() {
		return cityPanel;
	}

	private void saveSystemDescriptionCommand() {
		System.out.println("Save System Description Command");
		system.setDescription(descriptionText.getText());
	}
	
}
