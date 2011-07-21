package edu.mit.citynet.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.Edge;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.Node;
import edu.mit.citynet.core.NodeRegion;
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
		systemVizPanel = new SystemVizPanel(this, system);
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
	
	/**
	 * Gets the system.
	 *
	 * @return the system
	 */
	public CitySystem getSystem() {
		return system;
	}

	/**
	 * Save system description command.
	 */
	private void saveSystemDescriptionCommand() {
		System.out.println("Save System Description Command");
		system.setDescription(descriptionText.getText());
	}

	public void clearNodesCommand() {
		System.out.println("Clear Nodes Command");
		system.setNodes(new HashSet<Node>());
		systemVizPanel.repaint();
	}
	
	public void generateNodesCommand(Set<NodeRegion> nodeRegions) {
		System.out.println("Generate Nodes Command");
		if(!system.getNodes().isEmpty())
			clearNodesCommand();
		if(!system.getEdges().isEmpty())
			clearEdgesCommand();
		for(NodeRegion r : nodeRegions) {
			r.generateNodes(system);
		}
		systemVizPanel.repaint();
	}
	
	public void clearEdgesCommand() {
		System.out.println("Clear Edges Command");
		system.setEdges(new HashSet<Edge>());
		systemVizPanel.repaint();
	}
	
	public void generateEdgesCommand(Set<EdgeRegion> edgeRegions) {
		System.out.println("Generate Edges Command");
		if(!system.getEdges().isEmpty())
			clearEdgesCommand();
		for(EdgeRegion r : edgeRegions) {
			r.generateEdges(system);
		}
		systemVizPanel.repaint();
	}
}
