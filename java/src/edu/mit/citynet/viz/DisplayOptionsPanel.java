/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.viz;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * The DisplayOptionsPanel class.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class DisplayOptionsPanel extends JPanel {
	private static final long serialVersionUID = -4559321620341885337L;

	private DisplayOptions displayOptions;
	private JCheckBox gridDisplayCheck, mapDisplayCheck;
	private JSpinner gridSpacingSpinner;
	private SpinnerNumberModel gridSpacingModel;
	private OpacitySlider cellRegionOpacitySlider, cellOpacitySlider,
		nodeRegionOpacitySlider, edgeRegionOpacitySlider, 
		nodeOpacitySlider, edgeOpacitySlider;
	
	/**
	 * Instantiates a new display options panel.
	 */
	public DisplayOptionsPanel() {
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
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		gridDisplayCheck = new JCheckBox("Display Grid");
		gridDisplayCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gridSpacingSpinner.setEnabled(gridDisplayCheck.isSelected());
			}
		});
		add(gridDisplayCheck, c);
		c.gridx+=2;
		c.gridwidth = 1;
		add(new JLabel("Grid Spacing: ", JLabel.RIGHT), c);
		c.gridx++;
		gridSpacingModel = new SpinnerNumberModel(1,0.1,10,0.25);
		gridSpacingSpinner = new JSpinner(gridSpacingModel);
		add(gridSpacingSpinner, c);
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 4;
		mapDisplayCheck = new JCheckBox("Display Map");
		add(mapDisplayCheck, c);
		c.gridy++;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(new JLabel("Cell Regions: ", JLabel.RIGHT), c);
		c.gridx++;
		c.gridwidth = 3;
		cellRegionOpacitySlider = new OpacitySlider();
		add(cellRegionOpacitySlider, c);
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 1;
		add(new JLabel("Cells: ", JLabel.RIGHT), c);
		c.gridx++;
		c.gridwidth = 3;
		cellOpacitySlider = new OpacitySlider();
		add(cellOpacitySlider, c);
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 1;
		add(new JLabel("Node Regions: ", JLabel.RIGHT), c);
		c.gridx++;
		c.gridwidth = 3;
		nodeRegionOpacitySlider = new OpacitySlider();
		add(nodeRegionOpacitySlider, c);
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 1;
		add(new JLabel("Nodes: ", JLabel.RIGHT), c);
		c.gridx++;
		c.gridwidth = 3;
		nodeOpacitySlider = new OpacitySlider();
		add(nodeOpacitySlider, c);
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 1;
		add(new JLabel("Edge Regions: ", JLabel.RIGHT), c);
		c.gridx++;
		c.gridwidth = 3;
		edgeRegionOpacitySlider = new OpacitySlider();
		add(edgeRegionOpacitySlider, c);
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 1;
		add(new JLabel("Edges: ", JLabel.RIGHT), c);
		c.gridx++;
		c.gridwidth = 3;
		edgeOpacitySlider = new OpacitySlider();
		add(edgeOpacitySlider, c);
	}
	
	/**
	 * Load display options.
	 *
	 * @param displayOptions the display options
	 */
	public void loadDisplayOptions(DisplayOptions displayOptions) {
		this.displayOptions = displayOptions;
		gridDisplayCheck.setSelected(displayOptions.isGridDisplayed());
		gridSpacingSpinner.setValue(displayOptions.getGridSpacing());
		mapDisplayCheck.setSelected(displayOptions.isMapDisplayed());
		cellRegionOpacitySlider.setValue(
				(int)(displayOptions.getCellRegionOpacity()*OpacitySlider.NUM_TICKS));
		cellOpacitySlider.setValue(
				(int)(displayOptions.getCellOpacity()*OpacitySlider.NUM_TICKS));
		nodeRegionOpacitySlider.setValue(
				(int)(displayOptions.getNodeRegionOpacity()*OpacitySlider.NUM_TICKS));
		nodeOpacitySlider.setValue(
				(int)(displayOptions.getNodeOpacity()*OpacitySlider.NUM_TICKS));
		edgeRegionOpacitySlider.setValue(
				(int)(displayOptions.getEdgeRegionOpacity()*OpacitySlider.NUM_TICKS));
		edgeOpacitySlider.setValue(
				(int)(displayOptions.getEdgeOpacity()*OpacitySlider.NUM_TICKS));
	}
	
	/**
	 * Save display options command.
	 */
	public void saveDisplayOptionsCommand() {
		displayOptions.setGridDisplayed(gridDisplayCheck.isSelected());
		displayOptions.setGridSpacing(gridSpacingModel.getNumber().doubleValue());
		displayOptions.setMapDisplayed(mapDisplayCheck.isSelected());
		displayOptions.setCellRegionOpacity(
				cellRegionOpacitySlider.getValue()*OpacitySlider.TICK_SIZE);
		displayOptions.setCellOpacity(
				cellOpacitySlider.getValue()*OpacitySlider.TICK_SIZE);
		displayOptions.setNodeRegionOpacity(
				nodeRegionOpacitySlider.getValue()*OpacitySlider.TICK_SIZE);
		displayOptions.setNodeOpacity(
				nodeOpacitySlider.getValue()*OpacitySlider.TICK_SIZE);
		displayOptions.setEdgeRegionOpacity(
				edgeRegionOpacitySlider.getValue()*OpacitySlider.TICK_SIZE);
		displayOptions.setEdgeOpacity(
				edgeOpacitySlider.getValue()*OpacitySlider.TICK_SIZE);
	}
}
