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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import edu.mit.citynet.core.CellRegion;

/**
 * The CellRegionPanel contains user interface components to edit a cell region.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CellRegionPanel extends JPanel {
	private static final long serialVersionUID = -3420768492029999781L;

	private CellRegion cellRegion;
	private JTextArea descriptionText;
	private JTable coordinateTable;
	private CoordinateTableModel coordinateTableModel;
	private JSpinner rowsSpinner, columnsSpinner;
	private SpinnerNumberModel rowsModel, columnsModel;
	
	/**
	 * Instantiates a new cell region panel.
	 */
	public CellRegionPanel() {
		initializePanel();
	}
	
	/**
	 * Initializes the panel.
	 */
	private void initializePanel() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Description: ", JLabel.RIGHT), c);
		c.gridx++;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		descriptionText = new JTextArea(5,20);
		descriptionText.setLineWrap(true);
		descriptionText.setWrapStyleWord(true);
		descriptionText.setMargin(new Insets(2,2,2,2));
		add(new JScrollPane(descriptionText),c);
		c.gridx = 0;
		c.gridy++;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Rows: ", JLabel.RIGHT), c);
		c.gridx++;
		c.fill = GridBagConstraints.NONE;
		rowsModel = new SpinnerNumberModel(1,1,100,1);
		rowsSpinner = new JSpinner(rowsModel);
		add(rowsSpinner, c);
		c.gridy++;
		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Columns: ", JLabel.RIGHT), c);
		c.gridx++;
		c.fill = GridBagConstraints.NONE;
		columnsModel = new SpinnerNumberModel(1,1,100,1);
		columnsSpinner = new JSpinner(columnsModel);
		add(columnsSpinner, c);
		c.gridy++;
		c.gridx = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Vertices: ", JLabel.RIGHT), c);
		c.gridx++;
		c.weighty = 1;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.VERTICAL;
		coordinateTableModel = new CoordinateTableModel();
		coordinateTableModel.setSquare(true);
		coordinateTable = new JTable(coordinateTableModel);
		coordinateTable.getTableHeader().setReorderingAllowed(false);
		coordinateTable.getColumnModel().getColumn(0).setHeaderValue("X");
		coordinateTable.getColumnModel().getColumn(1).setHeaderValue("Y");
		coordinateTable.setPreferredScrollableViewportSize(new Dimension(150,75));
		add(new JScrollPane(coordinateTable), c);
	}
	
	/**
	 * Load cell region.
	 *
	 * @param cellRegion the cell region
	 */
	public void loadCellRegion(CellRegion cellRegion) {
		this.cellRegion = cellRegion;
		descriptionText.setText(cellRegion.getDescription());
		coordinateTableModel.setCoordinates(cellRegion.getCoordinateList());
		rowsSpinner.setValue(cellRegion.getNumberRows());
		columnsSpinner.setValue(cellRegion.getNumberColumns());
	}
	
	/**
	 * Save cell region command.
	 */
	public void saveCellRegionCommand() {
		if(coordinateTable.isEditing()) coordinateTable.getCellEditor().stopCellEditing();
		cellRegion.setDescription(descriptionText.getText());
		cellRegion.setCoordinateList(coordinateTableModel.getCoordinates());
		cellRegion.setNumberColumns(columnsModel.getNumber().intValue());
		cellRegion.setNumberRows(rowsModel.getNumber().intValue());
	}
}
