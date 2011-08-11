/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JTable;

import edu.mit.citynet.core.AbstractRegion;

/**
 * The Class RegionTable.
 *
 * @param <t> the generic type
 */
public class RegionTable<t extends AbstractRegion> extends JTable {
	private static final long serialVersionUID = -8984001928254508996L;
	
	/**
	 * Instantiates a new region table.
	 *
	 * @param model the model
	 */
	public RegionTable(RegionTableModel<t> model) {
		super(model);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JTable#getModel()
	 */
	@SuppressWarnings("unchecked")
	public RegionTableModel<t> getModel() {
		return (RegionTableModel<t>)super.getModel();
	}
	
	/**
	 * Gets the selected regions.
	 *
	 * @return the selected regions
	 */
	public Set<t> getSelectedRegions() {
		Set<t> regions = new HashSet<t>();
		for(int row : getSelectedRows()) {
			regions.add(getModel().getRegionAt(row));
		}
		return regions;
	}
	
	/**
	 * Gets the selected region.
	 *
	 * @return the selected region
	 */
	public t getSelectedRegion() {
		return getModel().getRegionAt(getSelectedRow());
	}
}
