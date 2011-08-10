package edu.mit.citynet.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import com.vividsolutions.jts.geom.Coordinate;

import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.EdgeRegion.EdgeRegionType;
import edu.mit.citynet.core.EdgeType;
import edu.mit.citynet.core.Layer;
import edu.mit.citynet.util.CityNetIcon;
import edu.mit.citynet.viz.SystemVizPanel;

public class EdgeRegionPanel extends JPanel {
	private static final long serialVersionUID = -3163576993982031134L;
	private static final String DIRECTED = "Directed", UNDIRECTED = "Undirected";
	
	private SystemVizPanel vizPanel;
	private EdgeRegion edgeRegion;
	private JTextArea descriptionText;
	private JComboBox edgeRegionTypeCombo, directedCombo, edgeTypeCombo;
	private JTable verticesTable;
	private VertexTableModel3D verticesTableModel;
	private JButton addCoordinateButton, deleteCoordinatesButton, moveUpButton, moveDownButton;
	
	/**
	 * Instantiates a new edge region panel.
	 */
	public EdgeRegionPanel(SystemVizPanel vizPanel) {
		this.vizPanel = vizPanel;
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
		c.gridy++;
		c.gridx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Directed: ", JLabel.RIGHT), c);
		c.gridx++;
		directedCombo = new JComboBox(new String[]{DIRECTED,UNDIRECTED});
		add(directedCombo,c);
		c.gridx = 0;
		c.gridy++;
		add(new JLabel("Edge Type: ", JLabel.RIGHT), c);
		c.gridx++;
		edgeTypeCombo = new JComboBox();
		for(EdgeType type : vizPanel.getSystemPanel().getSystem().getEdgeTypes()) {
			edgeTypeCombo.addItem(type);
		}
		edgeTypeCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 2820843521395503530L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, 
						isSelected, cellHasFocus);
				if(value instanceof EdgeType) {
					setText(((EdgeType)value).getName());
					final Color color = ((EdgeType)value).getColor();
					ImageFilter filter = new RGBImageFilter() {
						public int filterRGB(int x, int y, int rgb) {
							return rgb & color.getRGB();
						}
					};
					ImageProducer producer = new FilteredImageSource(
							CityNetIcon.EDGE_TYPE_BLANK.getIcon().getImage().getSource(), filter);
					setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(producer)));
				}
				return this;
			}
		});
		add(edgeTypeCombo, c);
		c.gridy++;
		c.gridx = 0;
		add(new JLabel("Region Type: ", JLabel.RIGHT), c);
		c.gridx++;
		edgeRegionTypeCombo = new JComboBox();
		for(EdgeRegionType type : EdgeRegionType.values()) {
			edgeRegionTypeCombo.addItem(type);
		}
		edgeRegionTypeCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 2820843521395503530L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, 
						isSelected, cellHasFocus);
				if(value instanceof EdgeRegionType) {
					setText(((EdgeRegionType)value).getName());
					setIcon(((EdgeRegionType)value).getIcon());
				}
				return this;
			}
		});
		add(edgeRegionTypeCombo, c);
		c.gridy = 0;
		c.gridx = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Vertices: ", JLabel.RIGHT), c);
		c.gridx++;
		c.gridheight = 4;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.VERTICAL;
		verticesTableModel = new VertexTableModel3D();
		verticesTable = new JTable(verticesTableModel) {
			private static final long serialVersionUID = 1L;
			
			/* (non-Javadoc)
			 * @see javax.swing.JTable#getCellEditor(int, int)
			 */
			public TableCellEditor getCellEditor(int row, int col) {
		    	if(col==2) {
		    		JComboBox comboBox = new JComboBox();
		    		comboBox.setRenderer(new DefaultListCellRenderer() {
		    			private static final long serialVersionUID = 2820843521395503530L;
		    			public Component getListCellRendererComponent(JList list,
		    					Object value, int index, boolean isSelected,
		    					boolean cellHasFocus) {
		    				super.getListCellRendererComponent(list, value, 
		    						index, isSelected, cellHasFocus);
		    				if(value instanceof Layer) {
		    					setText(((Layer)value).getName());
		    				}
		    				return this;
		    			}
		    		});
		    		for(Layer layer : vizPanel.getSystemPanel().getSystem().getLayers()) {
		    			comboBox.addItem(layer);
		    		}
		    		return new DefaultCellEditor(comboBox);
		    	} else return super.getCellEditor(row,col);
			}
		};
		verticesTable.getTableHeader().setReorderingAllowed(false);
		verticesTable.getColumnModel().getColumn(0).setHeaderValue("X");
		verticesTable.getColumnModel().getColumn(1).setHeaderValue("Y");
		verticesTable.getColumnModel().getColumn(2).setHeaderValue("Layer");
		verticesTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 2092491034324672219L;
			
			/* (non-Javadoc)
			 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
			 */
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(value instanceof Layer) {
					setText(((Layer)value).getName());
				}
				return this;
			}
		});
		verticesTable.setPreferredScrollableViewportSize(new Dimension(200,200));
		verticesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				deleteCoordinatesButton.setEnabled(verticesTable.getSelectedRowCount()>0);
				moveUpButton.setEnabled(verticesTable.getSelectedRowCount()==1
						&& verticesTable.getSelectedRow() > 0);
				moveDownButton.setEnabled(verticesTable.getSelectedRowCount()==1
						&& verticesTable.getSelectedRow() < verticesTableModel.getRowCount()-1);
			}
		});
		add(new JScrollPane(verticesTable), c);
		c.gridx++;
		c.weightx = 0;
		JPanel coordinateButtonPanel = new JPanel();
		coordinateButtonPanel.setLayout(new BoxLayout(coordinateButtonPanel, BoxLayout.PAGE_AXIS));
		moveUpButton = new JButton(CityNetIcon.MOVE_UP.getIcon());
		moveUpButton.setToolTipText("Move coordinate up one row");
		moveUpButton.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				int selectedRow = verticesTable.getSelectedRow();
				if(selectedRow > 0) {
					Coordinate coord = verticesTableModel.getCoordinates().getCoordinate(selectedRow);
					verticesTableModel.getCoordinates().set(selectedRow,
							verticesTableModel.getCoordinates().getCoordinate(selectedRow-1));
					verticesTableModel.getCoordinates().set(selectedRow-1, coord);
					verticesTableModel.fireTableRowsUpdated(selectedRow-1, selectedRow);
					verticesTable.getSelectionModel().setSelectionInterval(selectedRow-1,selectedRow-1);
				}
			}
		});
		coordinateButtonPanel.add(moveUpButton);
		moveDownButton = new JButton(CityNetIcon.MOVE_DOWN.getIcon());
		moveDownButton.setToolTipText("Move coordinate down one row");
		moveDownButton.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				int selectedRow = verticesTable.getSelectedRow();
				if(selectedRow < verticesTableModel.getRowCount()-1) {
					Coordinate coord = verticesTableModel.getCoordinates().getCoordinate(selectedRow);
					verticesTableModel.getCoordinates().set(selectedRow,
							verticesTableModel.getCoordinates().getCoordinate(selectedRow+1));
					verticesTableModel.getCoordinates().set(selectedRow+1, coord);
					verticesTableModel.fireTableRowsUpdated(selectedRow, selectedRow+1);
					verticesTable.getSelectionModel().setSelectionInterval(selectedRow+1,selectedRow+1);
				}
			}
		});
		coordinateButtonPanel.add(moveDownButton);
		addCoordinateButton = new JButton(CityNetIcon.ADD_COORDINATE.getIcon());
		addCoordinateButton.setToolTipText("Add new coordinate");
		addCoordinateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verticesTableModel.getCoordinates().add(new Coordinate(),true);
				verticesTableModel.fireTableRowsInserted(
						verticesTableModel.getRowCount(),
						verticesTableModel.getRowCount());
			}
		});
		coordinateButtonPanel.add(addCoordinateButton);
		deleteCoordinatesButton = new JButton(CityNetIcon.DELETE_COORDINATE.getIcon());
		deleteCoordinatesButton.setToolTipText("Delete selected coordinates");
		deleteCoordinatesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i = verticesTable.getSelectedRows().length-1; i>=0; i--) {
					int rowDeleted = verticesTable.getSelectedRows()[i];
					verticesTableModel.getCoordinates().remove(rowDeleted);
					verticesTableModel.fireTableRowsDeleted(rowDeleted, rowDeleted);
				}
			}
		});
		coordinateButtonPanel.add(deleteCoordinatesButton);
		add(coordinateButtonPanel, c);
	}
	
	/**
	 * Load edge region.
	 *
	 * @param edgeRegion the edge region
	 */
	public void loadEdgeRegion(EdgeRegion edgeRegion) {
		this.edgeRegion = edgeRegion;
		edgeRegionTypeCombo.setSelectedItem(edgeRegion.getEdgeRegionType());
		descriptionText.setText(edgeRegion.getDescription());
		edgeTypeCombo.setSelectedItem(edgeRegion.getEdgeType());
		verticesTableModel.setCoordinates(edgeRegion.getCoordinateList());
		verticesTableModel.setLayers(edgeRegion.getLayers());
	}
	
	/**
	 * Save edge region command.
	 */
	public void saveEdgeRegionCommand() {
		if(verticesTable.isEditing()) verticesTable.getCellEditor().stopCellEditing();
		edgeRegion.setEdgeRegionType((EdgeRegionType)edgeRegionTypeCombo.getSelectedItem());
		edgeRegion.setDescription(descriptionText.getText());
		edgeRegion.setEdgeType((EdgeType)edgeTypeCombo.getSelectedItem());
		edgeRegion.setCoordinateList(verticesTableModel.getCoordinates());
		edgeRegion.setLayers(verticesTableModel.getLayers());
	}
}
