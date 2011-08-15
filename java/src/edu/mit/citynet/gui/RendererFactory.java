/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.Layer;
import edu.mit.citynet.core.NodeRegion.NodeRegionType;
import edu.mit.citynet.core.NodeType;

public abstract class RendererFactory {
	
	public static TableCellRenderer createCellRegionTableCellRenderer() {
		return new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 523244544309854408L;
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(value instanceof CellRegion) {
					setText(((CellRegion)value).getDescription());
				}
				return this;
			}
		};
	}
	
	public static TableCellRenderer createNodeTypeTableCellRenderer() {
		return new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 523244544309854408L;
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(value instanceof NodeType) {
					setText(((NodeType)value).getName());
					setIcon(((NodeType)value).getIcon());
				}
				return this;
			}
		};
	}
	
	public static ListCellRenderer createNodeTypeListCellRenderer() {
		return new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof NodeType) {
					setText(((NodeType)value).getName());
					setIcon(((NodeType)value).getIcon());
				}
				return this;
			}
		};
	}
	
	public static TableCellRenderer createNodeRegionTypeTableCellRenderer() {
		return new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 523244544309854408L;
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(value instanceof NodeRegionType) {
					setText(((NodeRegionType)value).getName());
					setIcon(((NodeRegionType)value).getIcon());
				}
				return this;
			}
		};
	}
	
	public static ListCellRenderer createNodeRegionTypeListCellRenderer() {
		return new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof NodeRegionType) {
					setText(((NodeRegionType)value).getName());
					setIcon(((NodeRegionType)value).getIcon());
				}
				return this;
			}
		};
	}
	
	public static TableCellRenderer createLayerTableCellRenderer() {
		return new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 523244544309854408L;
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(value instanceof Layer) {
					setText(((Layer)value).getName());
				}
				return this;
			}
		};
	}
	
	public static ListCellRenderer createLayerListCellRenderer() {
		return new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof Layer) {
					setText(((Layer)value).getName());
				}
				return this;
			}
		};
	}
}
