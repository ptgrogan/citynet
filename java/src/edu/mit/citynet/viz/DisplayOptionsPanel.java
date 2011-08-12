/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.viz;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;

import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.gui.RegionTable;

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
	private JSlider cellRegionOpacitySlider, cellOpacitySlider,
		nodeRegionOpacitySlider, edgeRegionOpacitySlider, 
		nodeOpacitySlider, edgeOpacitySlider;
	private JList displayOrderList;
	private JTabbedPane tabbedPane;
	private JTable layerDisplayTable;
	private RegionTable<CellRegion> cellRegionsDisplayTable;
	private RegionTable<NodeRegion> nodeRegionsDisplayTable;
	private RegionTable<EdgeRegion> edgeRegionsDisplayTable;
	
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
		add(gridDisplayCheck, c);
		c.gridy++;
		c.gridwidth = 1;
		add(new JLabel("Grid Spacing: ", JLabel.RIGHT), c);
		c.gridx++;
		gridSpacingModel = new SpinnerNumberModel(1,0.1,10,1);
		gridSpacingSpinner = new JSpinner(gridSpacingModel);
		add(gridSpacingSpinner, c);
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 2;
		mapDisplayCheck = new JCheckBox("Display Map");
		add(mapDisplayCheck, c);
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
	}
	
	/**
	 * Save display options command.
	 */
	public void saveDisplayOptionsCommand() {
		displayOptions.setGridDisplayed(gridDisplayCheck.isSelected());
		displayOptions.setGridSpacing(gridSpacingModel.getNumber().doubleValue());
		displayOptions.setMapDisplayed(mapDisplayCheck.isSelected());
	}
}
