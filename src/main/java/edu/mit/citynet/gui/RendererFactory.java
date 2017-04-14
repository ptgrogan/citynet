/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.EdgeDirection;
import edu.mit.citynet.core.EdgeGenerationType;
import edu.mit.citynet.core.EdgeRegion.EdgeRegionType;
import edu.mit.citynet.core.EdgeType;
import edu.mit.citynet.core.Layer;
import edu.mit.citynet.core.NodeGenerationType;
import edu.mit.citynet.core.NodeRegion.NodeRegionType;
import edu.mit.citynet.core.NodeType;
import edu.mit.citynet.io.HexColorFormat;

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
	
	public static TableCellRenderer createEdgeTypeTableCellRenderer() {
		return new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 523244544309854408L;
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(value instanceof EdgeType) {
					setText(((EdgeType)value).getName());
					setIcon(((EdgeType)value).getIcon());
				}
				return this;
			}
		};
	}
	
	public static ListCellRenderer createEdgeTypeListCellRenderer() {
		return new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof EdgeType) {
					setText(((EdgeType)value).getName());
					setIcon(((EdgeType)value).getIcon());
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
	
	public static TableCellRenderer createEdgeRegionTypeTableCellRenderer() {
		return new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 523244544309854408L;
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(value instanceof EdgeRegionType) {
					setText(((EdgeRegionType)value).getName());
					setIcon(((EdgeRegionType)value).getIcon());
				}
				return this;
			}
		};
	}
	
	public static ListCellRenderer createEdgeRegionTypeListCellRenderer() {
		return new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof EdgeRegionType) {
					setText(((EdgeRegionType)value).getName());
					setIcon(((EdgeRegionType)value).getIcon());
				}
				return this;
			}
		};
	}
	
	public static TableCellRenderer createNodeGenerationTypeTableCellRenderer() {
		return new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 523244544309854408L;
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(value instanceof NodeGenerationType) {
					setText(((NodeGenerationType)value).getName());
					setIcon(((NodeGenerationType)value).getIcon());
				}
				return this;
			}
		};
	}
	
	public static ListCellRenderer createNodeGenerationTypeListCellRenderer() {
		return new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof NodeGenerationType) {
					setText(((NodeGenerationType)value).getName());
					setIcon(((NodeGenerationType)value).getIcon());
				}
				return this;
			}
		};
	}
	
	public static TableCellRenderer createEdgeGenerationTypeTableCellRenderer() {
		return new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 523244544309854408L;
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(value instanceof EdgeGenerationType) {
					setText(((EdgeGenerationType)value).getName());
					setIcon(((EdgeGenerationType)value).getIcon());
				}
				return this;
			}
		};
	}
	
	public static ListCellRenderer createEdgeGenerationTypeListCellRenderer() {
		return new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof EdgeGenerationType) {
					setText(((EdgeGenerationType)value).getName());
					setIcon(((EdgeGenerationType)value).getIcon());
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
	
	public static TableCellRenderer createEdgeDirectionTableCellRenderer() {
		return new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 523244544309854408L;
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(value instanceof EdgeDirection) {
					setIcon(((EdgeDirection)value).getIcon());
					setText(((EdgeDirection)value).getName());
				}
				return this;
			}
		};
	}
	
	public static ListCellRenderer createEdgeDirectionListCellRenderer() {
		return new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if(value instanceof EdgeDirection) {
					setIcon(((EdgeDirection)value).getIcon());
					setText(((EdgeDirection)value).getName());
				}
				return this;
			}
		};
	}
	
	public static TableCellRenderer createHexColorTableCellRenderer() {
		return new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 523244544309854408L;
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(value instanceof String && HexColorFormat.isValidHexString((String)value)) {
					Color c = HexColorFormat.getColorFromHexString((String)value);
					setBackground(c);
					if(c.getRed()<0x44 && c.getGreen()<0x44 && c.getBlue()<0x44)
						setForeground(Color.white);
					else
						setForeground(Color.black);
					setText((String)value);
				}
				return this;
			}
		};
	}
}
