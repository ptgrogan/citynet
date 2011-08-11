package edu.mit.citynet.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.mit.citynet.core.Layer;

/**
 * The LayerPanel class.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class LayerPanel extends JPanel {
	private static final long serialVersionUID = -1820720020787911656L;

	private SystemPanel systemPanel;
	private Layer layer;
	private JTextField nameText;
	private JTextArea descriptionText;
	private JSlider displayHeightSlider;
	
	/**
	 * Instantiates a new layer panel.
	 *
	 * @param systemPanel the system panel
	 */
	public LayerPanel(SystemPanel systemPanel) {
		this.systemPanel = systemPanel;
		initializePanel();
	}
	
	/**
	 * Initialize panel.
	 */
	private void initializePanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Name: ", JLabel.RIGHT), c);
		c.gridx++;
		c.weightx = 1;
		nameText = new JTextField(30);
		add(nameText, c);
		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Description: ", JLabel.RIGHT), c);
		c.gridx++;
		c.weighty = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		descriptionText = new JTextArea(5,20);
		descriptionText.setLineWrap(true);
		descriptionText.setWrapStyleWord(true);
		descriptionText.setMargin(new Insets(2,2,2,2));
		add(new JScrollPane(descriptionText),c);
	}
	
	/**
	 * Load layer.
	 *
	 * @param layer the layer
	 */
	public void loadLayer(Layer layer) {
		this.layer = layer;
		nameText.setText(layer.getName());
		descriptionText.setText(layer.getDescription());
		// TODO: display height
	}
	
	/**
	 * Save layer command.
	 */
	public void saveLayerCommand() {
		layer.setName(nameText.getText());
		layer.setDescription(descriptionText.getText());
		// TODO: display height
	}
}
