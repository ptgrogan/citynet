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
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.mit.citynet.core.Layer;
import edu.mit.citynet.gui.DisplayHeightSlider;

/**
 * The DisplayOptionsPanel class.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class DisplayOptionsPanel extends JPanel {
	private static final long serialVersionUID = -4559321620341885337L;

	private DisplayOptions displayOptions;
	private JCheckBox gridDisplayCheck, mapDisplayCheck, layerFilterCheck;
	private JSpinner gridSpacingSpinner;
	private SpinnerNumberModel gridSpacingModel;
	private OpacitySlider cellRegionOpacitySlider, cellOpacitySlider,
		nodeOpacitySlider, edgeOpacitySlider, regionOpacitySlider;
	private DisplayHeightSlider displayHeightSlider;
	private JLabel displayHeightLabel;
	private final DecimalFormat format = new DecimalFormat("0.0");
	
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
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		add(buildOpacityPanel(),c);
		c.gridy++;
		add(buildLayerPanel(),c);
	}
	
	/**
	 * Builds the opacity panel.
	 *
	 * @return the j panel
	 */
	private JPanel buildOpacityPanel() {
		JPanel opacityPanel = new JPanel(new GridBagLayout());
		opacityPanel.setBorder(BorderFactory.createTitledBorder("Display Opacity"));
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		opacityPanel.add(new JLabel("Cell Regions: ", JLabel.RIGHT), c);
		c.weightx = 1;
		c.gridx++;
		cellRegionOpacitySlider = new OpacitySlider(false);
		opacityPanel.add(cellRegionOpacitySlider, c);
		c.weightx = 0;
		c.gridx = 0;
		c.gridy++;
		opacityPanel.add(new JLabel("Cells: ", JLabel.RIGHT), c);
		c.gridx++;
		cellOpacitySlider = new OpacitySlider(false);
		opacityPanel.add(cellOpacitySlider, c);
		c.gridx = 0;
		c.gridy++;
		opacityPanel.add(new JLabel("Regions: ", JLabel.RIGHT), c);
		c.gridx++;
		regionOpacitySlider = new OpacitySlider(false);
		opacityPanel.add(regionOpacitySlider, c);
		c.gridx = 0;
		c.gridy++;
		opacityPanel.add(new JLabel("Nodes: ", JLabel.RIGHT), c);
		c.gridx++;
		nodeOpacitySlider = new OpacitySlider(false);
		opacityPanel.add(nodeOpacitySlider, c);
		c.gridx = 0;
		c.gridy++;
		opacityPanel.add(new JLabel("Edges: ", JLabel.RIGHT), c);
		c.gridx++;
		edgeOpacitySlider = new OpacitySlider(true);
		opacityPanel.add(edgeOpacitySlider, c);
		return opacityPanel;
	}
	
	private JPanel buildLayerPanel() {
		JPanel layerPanel = new JPanel(new GridBagLayout());
		layerPanel.setBorder(BorderFactory.createTitledBorder("Display Height"));
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		layerFilterCheck = new JCheckBox("Filter Layer Display");
		layerFilterCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayHeightSlider.setEnabled(layerFilterCheck.isSelected());
			}
		});
		layerPanel.add(layerFilterCheck, c);
		c.gridwidth = 1;
		c.gridy++;
		layerPanel.add(new JLabel("Height: "), c);
		c.gridx++;
		c.weightx = 1;
		displayHeightSlider = new DisplayHeightSlider(DisplayHeightSlider.HORIZONTAL);
		displayHeightSlider.setEnabled(false);
		layerPanel.add(displayHeightSlider, c);
		c.gridy++;
		displayHeightLabel = new JLabel("Display Height: ");
		layerPanel.add(displayHeightLabel, c);
		return layerPanel;
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
		layerFilterCheck.setSelected(displayOptions.isLayersFiltered());
		displayHeightSlider.setDisplayHeight(displayOptions.getDisplayHeight());
		displayHeightSlider.loadLabels(new ArrayList<Layer>(), null);
		displayHeightSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				displayHeightLabel.setText("Display Height: " 
						+ format.format(displayHeightSlider.getDisplayHeight()));
			}
		});
		cellRegionOpacitySlider.setValue(
				(int)(displayOptions.getCellRegionOpacity()*OpacitySlider.NUM_TICKS));
		cellOpacitySlider.setValue(
				(int)(displayOptions.getCellOpacity()*OpacitySlider.NUM_TICKS));
		nodeOpacitySlider.setValue(
				(int)(displayOptions.getNodeOpacity()*OpacitySlider.NUM_TICKS));
		edgeOpacitySlider.setValue(
				(int)(displayOptions.getEdgeOpacity()*OpacitySlider.NUM_TICKS));
		regionOpacitySlider.setValue(
				(int)(displayOptions.getRegionOpacity()*OpacitySlider.NUM_TICKS));
	}
	
	/**
	 * Save display options command.
	 */
	public void saveDisplayOptionsCommand() {
		displayOptions.setGridDisplayed(gridDisplayCheck.isSelected());
		displayOptions.setGridSpacing(gridSpacingModel.getNumber().doubleValue());
		displayOptions.setMapDisplayed(mapDisplayCheck.isSelected());
		displayOptions.setLayersFiltered(layerFilterCheck.isSelected());
		displayOptions.setDisplayHeight(displayHeightSlider.getDisplayHeight());
		displayOptions.setCellRegionOpacity(
				cellRegionOpacitySlider.getValue()*OpacitySlider.TICK_SIZE);
		displayOptions.setCellOpacity(
				cellOpacitySlider.getValue()*OpacitySlider.TICK_SIZE);
		displayOptions.setNodeOpacity(
				nodeOpacitySlider.getValue()*OpacitySlider.TICK_SIZE);
		displayOptions.setEdgeOpacity(
				edgeOpacitySlider.getValue()*OpacitySlider.TICK_SIZE);
		displayOptions.setRegionOpacity(
				regionOpacitySlider.getValue()*OpacitySlider.TICK_SIZE);
	}
}
