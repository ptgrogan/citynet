/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.mit.citynet.core.Layer;

/**
 * The LayerPanel class.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class LayerPanel extends JPanel {
	private static final long serialVersionUID = -1820720020787911656L;
	private static final int MIN_HEIGHT = 0, MAX_HEIGHT = 50;

	private SystemPanel systemPanel;
	private Layer layer;
	private JTextField nameText;
	private JTextArea descriptionText;
	private JLabel displayHeightLabel;
	private JSlider displayHeightSlider;
	private final DecimalFormat format = new DecimalFormat("0.0");
	
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
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		displayHeightLabel = new JLabel("Display Height: ", JLabel.LEFT);
		displayHeightLabel.setPreferredSize(new Dimension(100,20));
		add(displayHeightLabel, c);
		c.gridy++;
		displayHeightSlider = new JSlider(JSlider.VERTICAL, MIN_HEIGHT, MAX_HEIGHT, 0);
		displayHeightSlider.setMajorTickSpacing(5);
		displayHeightSlider.setMinorTickSpacing(1);
		displayHeightSlider.setSnapToTicks(true);
		displayHeightSlider.setPaintTicks(true);
		displayHeightSlider.setPaintLabels(true);
		displayHeightSlider.setToolTipText("Height at which to display layer");
		displayHeightSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				displayHeightLabel.setText("Display Height: " 
						+ format.format(displayHeightSlider.getValue()/5d));
			}
		});
		add(displayHeightSlider, c);
		
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
		displayHeightSlider.setValue(Math.max(MIN_HEIGHT,
				Math.min((int)(layer.getDisplayHeight()*5),MAX_HEIGHT)));
		Hashtable<Integer, JLabel> labelDictionary = new Hashtable<Integer, JLabel>();
		for(Layer l : systemPanel.getSystem().getLayers()) {
			if(l.equals(layer)) continue;
			JLabel label = labelDictionary.get((int)(l.getDisplayHeight()*5));
			if(label!=null) {
				label.setText(label.getText() + ", " + l.getName());
			} else {
				labelDictionary.put((int)(l.getDisplayHeight()*5), 
						new JLabel(format.format(l.getDisplayHeight()) + ": " + l.getName()));
			}
		}
		for(int i=MIN_HEIGHT; i<=MAX_HEIGHT; i+=5) {
			if(labelDictionary.get(i)==null
					&& labelDictionary.get(i-1)==null
					&& labelDictionary.get(i-2)==null
					&& labelDictionary.get(i-3)==null
					&& labelDictionary.get(i-4)==null
					&& labelDictionary.get(i+1)==null
					&& labelDictionary.get(i+2)==null
					&& labelDictionary.get(i+3)==null
					&& labelDictionary.get(i+4)==null)
				labelDictionary.put(i,new JLabel(format.format(i/5d)));
		}
		displayHeightSlider.setLabelTable(labelDictionary);
	}
	
	/**
	 * Save layer command.
	 */
	public void saveLayerCommand() {
		layer.setName(nameText.getText());
		layer.setDescription(descriptionText.getText());
		layer.setDisplayHeight(displayHeightSlider.getValue()/5d);
	}
}
