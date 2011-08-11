package edu.mit.citynet.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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

public class EdgeRegionPanel extends JPanel {
	private static final long serialVersionUID = -3163576993982031134L;
	private static final String DIRECTED = "Directed", UNDIRECTED = "Undirected";
	
	private SystemPanel systemPanel;
	private EdgeRegion edgeRegion;
	private JTextArea descriptionText;
	private JComboBox edgeRegionTypeCombo, directedCombo, edgeTypeCombo;
	private JTable coordinateTable;
	private CoordinateTableModel3D coordinateTableModel;
	private JButton addCoordinateButton, deleteCoordinatesButton, 
		moveUpButton, moveDownButton;
	
	/**
	 * Instantiates a new edge region panel.
	 *
	 * @param systemPanel the system panel
	 */
	public EdgeRegionPanel(SystemPanel systemPanel) {
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
		directedCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 2820843521395503530L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, 
						isSelected, cellHasFocus);
				if(value == DIRECTED) {
					setIcon(CityNetIcon.DIRECTED.getIcon());
				} else if(value==UNDIRECTED) {
					setIcon(CityNetIcon.UNDIRECTED.getIcon());
				}
				return this;
			}
		});
		add(directedCombo,c);
		c.gridx = 0;
		c.gridy++;
		add(new JLabel("Edge Type: ", JLabel.RIGHT), c);
		c.gridx++;
		edgeTypeCombo = new JComboBox();
		edgeTypeCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 2820843521395503530L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, 
						isSelected, cellHasFocus);
				if(value instanceof EdgeType) {
					setText(((EdgeType)value).getName());
					setIcon(((EdgeType)value).getIcon());
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
		add(new JLabel("Coordinates: ", JLabel.RIGHT), c);
		c.gridx++;
		c.gridheight = 4;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.VERTICAL;
		coordinateTableModel = new CoordinateTableModel3D();
		coordinateTable = new JTable(coordinateTableModel) {
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
		    		for(Layer layer : systemPanel.getSystem().getLayers()) {
		    			comboBox.addItem(layer);
		    		}
		    		return new DefaultCellEditor(comboBox);
		    	} else return super.getCellEditor(row,col);
			}
		};
		coordinateTable.getTableHeader().setReorderingAllowed(false);
		coordinateTable.getColumnModel().getColumn(0).setHeaderValue("X");
		coordinateTable.getColumnModel().getColumn(1).setHeaderValue("Y");
		coordinateTable.getColumnModel().getColumn(2).setHeaderValue("Layer");
		coordinateTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 2092491034324672219L;
			
			/* (non-Javadoc)
			 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
			 */
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(value instanceof Layer) {
					setText(((Layer)value).getName());
				} else if(value==null) {
					setText(null);
				}
				return this;
			}
		});
		coordinateTable.setPreferredScrollableViewportSize(new Dimension(200,200));
		MouseAdapter coordinateMouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2 && e.getComponent()!=coordinateTable)
					addCoordinateCommand();
			}
			public void mousePressed(MouseEvent e) {
				if(e.getComponent()!=coordinateTable)
					coordinateTable.getSelectionModel().clearSelection();
				maybeShowPopup(e);
			}
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}
			private void maybeShowPopup(MouseEvent e) {
				if(e.isPopupTrigger()) {
					int row = coordinateTable.rowAtPoint(e.getPoint());
					coordinateTable.getSelectionModel().addSelectionInterval(row, row);
					Set<Coordinate> coordinates = new HashSet<Coordinate>();
					for(int i : coordinateTable.getSelectedRows()) 
						coordinates.add(coordinateTableModel.getCoordinates().getCoordinate(i));
					createCoordinatePopupMenu(coordinates).show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
		coordinateTable.addMouseListener(coordinateMouseAdapter);
		coordinateTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				deleteCoordinatesButton.setEnabled(coordinateTable.getSelectedRowCount()>0);
				moveUpButton.setEnabled(coordinateTable.getSelectedRowCount()==1
						&& coordinateTable.getSelectedRow() > 0);
				moveDownButton.setEnabled(coordinateTable.getSelectedRowCount()==1
						&& coordinateTable.getSelectedRow() < coordinateTableModel.getRowCount()-1);
			}
		});
		JScrollPane coordinateScroll = new JScrollPane(coordinateTable);
		coordinateScroll.addMouseListener(coordinateMouseAdapter);
		add(coordinateScroll,c);
		c.gridx++;
		c.weightx = 0;
		JPanel coordinateButtonPanel = new JPanel();
		coordinateButtonPanel.setLayout(new BoxLayout(coordinateButtonPanel, BoxLayout.PAGE_AXIS));
		moveUpButton = new JButton(CityNetIcon.MOVE_UP.getIcon());
		moveUpButton.setToolTipText("Move coordinate up one row");
		moveUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveCoordinateUpCommand();
			}
		});
		moveUpButton.setEnabled(false);
		coordinateButtonPanel.add(moveUpButton);
		moveDownButton = new JButton(CityNetIcon.MOVE_DOWN.getIcon());
		moveDownButton.setToolTipText("Move coordinate down one row");
		moveDownButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				moveCoordinateDownCommand();
			}
		});
		moveDownButton.setEnabled(false);
		coordinateButtonPanel.add(moveDownButton);
		addCoordinateButton = new JButton(CityNetIcon.ADD_COORDINATE.getIcon());
		addCoordinateButton.setToolTipText("Add new coordinate");
		addCoordinateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addCoordinateCommand();
			}
		});
		coordinateButtonPanel.add(addCoordinateButton);
		deleteCoordinatesButton = new JButton(CityNetIcon.DELETE_COORDINATE.getIcon());
		deleteCoordinatesButton.setToolTipText("Delete selected coordinates");
		deleteCoordinatesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteCoordinatesCommand();
			}
		});
		deleteCoordinatesButton.setEnabled(false);
		coordinateButtonPanel.add(deleteCoordinatesButton);
		add(coordinateButtonPanel, c);
	}
	
	/**
	 * Creates the coordinate popup menu.
	 *
	 * @param coordinates the coordinates
	 * @return the j popup menu
	 */
	private JPopupMenu createCoordinatePopupMenu(final Set<Coordinate> coordinates) {
		JPopupMenu coordinatePopupMenu = new JPopupMenu();
		if(coordinates.size()>0) {
			JMenuItem moveCoordinatesUpItem = new JMenuItem("Move Coordinate Up");
			moveCoordinatesUpItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					moveCoordinateUpCommand();
				}
			});
			moveCoordinatesUpItem.setEnabled(coordinateTable.getSelectedRowCount()==1
						&& coordinateTable.getSelectedRow() > 0);
			coordinatePopupMenu.add(moveCoordinatesUpItem);
			JMenuItem moveCoordinatesDownItem = new JMenuItem("Move Coordinate Down");
			moveCoordinatesDownItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					moveCoordinateDownCommand();
				}
			});
			moveCoordinatesDownItem.setEnabled(coordinateTable.getSelectedRowCount()==1
						&& coordinateTable.getSelectedRow() < coordinateTableModel.getRowCount()-1);
			coordinatePopupMenu.add(moveCoordinatesDownItem);
			JMenuItem deleteCoordinatesMenuItem = new JMenuItem("Delete Coordinate" + (coordinates.size()>1?"s":""));
			deleteCoordinatesMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteCoordinatesCommand();
				}
			});
			coordinatePopupMenu.add(deleteCoordinatesMenuItem);
		} else {
			JMenuItem addCoordinateMenuItem = new JMenuItem("Add Coordinate");
			addCoordinateMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addCoordinateCommand();
				}
			});
			coordinatePopupMenu.add(addCoordinateMenuItem);
		}
		return coordinatePopupMenu;
	}
	
	/**
	 * Adds the coordinate command.
	 */
	private void addCoordinateCommand() {
		System.out.println("Add Coordinate Command");
		coordinateTableModel.getCoordinates().add(new Coordinate(),true);
		if(systemPanel.getSystem().getLayers().size()>0)
			coordinateTableModel.getLayers().add(
					(Layer)systemPanel.getSystem().getLayers().toArray()[0]);
		else
			coordinateTableModel.getLayers().add(null);
		coordinateTableModel.fireTableRowsInserted(
				coordinateTableModel.getRowCount(),
				coordinateTableModel.getRowCount());
	}
	
	/**
	 * Delete coordinates command.
	 */
	private void deleteCoordinatesCommand() {
		System.out.println("Delete Coordinates Command");
		for(int i = coordinateTable.getSelectedRows().length-1; i>=0; i--) {
			int rowDeleted = coordinateTable.getSelectedRows()[i];
			coordinateTableModel.getCoordinates().remove(rowDeleted);
			coordinateTableModel.getLayers().remove(rowDeleted);
			coordinateTableModel.fireTableRowsDeleted(rowDeleted, rowDeleted);
		}
	}
	
	/**
	 * Move coordinate up command.
	 */
	@SuppressWarnings("unchecked")
	private void moveCoordinateUpCommand() {
		int selectedRow = coordinateTable.getSelectedRow();
		if(selectedRow > 0) {
			Coordinate coord = coordinateTableModel.getCoordinates().getCoordinate(selectedRow);
			coordinateTableModel.getCoordinates().set(selectedRow,
					coordinateTableModel.getCoordinates().getCoordinate(selectedRow-1));
			coordinateTableModel.getCoordinates().set(selectedRow-1, coord);
			Layer layer = coordinateTableModel.getLayers().get(selectedRow);
			coordinateTableModel.getLayers().set(selectedRow,
					coordinateTableModel.getLayers().get(selectedRow-1));
			coordinateTableModel.getLayers().set(selectedRow-1, layer);
			coordinateTableModel.fireTableRowsUpdated(selectedRow-1, selectedRow);
			coordinateTable.getSelectionModel().setSelectionInterval(selectedRow-1,selectedRow-1);
		}
	}
	
	/**
	 * Move coordinate down command.
	 */
	@SuppressWarnings("unchecked")
	private void moveCoordinateDownCommand() {
		int selectedRow = coordinateTable.getSelectedRow();
		if(selectedRow < coordinateTableModel.getRowCount()-1) {
			Coordinate coord = coordinateTableModel.getCoordinates().getCoordinate(selectedRow);
			coordinateTableModel.getCoordinates().set(selectedRow,
					coordinateTableModel.getCoordinates().getCoordinate(selectedRow+1));
			coordinateTableModel.getCoordinates().set(selectedRow+1, coord);
			Layer layer = coordinateTableModel.getLayers().get(selectedRow);
			coordinateTableModel.getLayers().set(selectedRow,
					coordinateTableModel.getLayers().get(selectedRow+1));
			coordinateTableModel.getLayers().set(selectedRow+1, layer);
			coordinateTableModel.fireTableRowsUpdated(selectedRow, selectedRow+1);
			coordinateTable.getSelectionModel().setSelectionInterval(selectedRow+1,selectedRow+1);
		}
	}
	
	/**
	 * Load edge region.
	 *
	 * @param edgeRegion the edge region
	 */
	public void loadEdgeRegion(EdgeRegion edgeRegion) {
		this.edgeRegion = edgeRegion;
		descriptionText.setText(edgeRegion.getDescription());
		edgeTypeCombo.removeAllItems();
		for(EdgeType type : systemPanel.getSystem().getEdgeTypes())
			edgeTypeCombo.addItem(type);
		if(edgeRegion.getEdgeType()==null && edgeTypeCombo.getItemCount()>0)
			edgeTypeCombo.setSelectedIndex(0);
		else
			edgeTypeCombo.setSelectedItem(edgeRegion.getEdgeType());
		directedCombo.setSelectedItem(edgeRegion.isDirected()?DIRECTED:UNDIRECTED);
		if(edgeRegion.getEdgeRegionType()==null)
			edgeRegionTypeCombo.setSelectedIndex(0);
		else
			edgeRegionTypeCombo.setSelectedItem(edgeRegion.getEdgeRegionType());
		coordinateTableModel.setCoordinates(edgeRegion.getCoordinateList());
		coordinateTableModel.setLayers(edgeRegion.getLayers());
	}
	
	/**
	 * Save edge region command.
	 */
	public void saveEdgeRegionCommand() {
		if(coordinateTable.isEditing()) coordinateTable.getCellEditor().stopCellEditing();
		edgeRegion.setEdgeRegionType((EdgeRegionType)edgeRegionTypeCombo.getSelectedItem());
		edgeRegion.setDescription(descriptionText.getText());
		edgeRegion.setEdgeType((EdgeType)edgeTypeCombo.getSelectedItem());
		edgeRegion.setDirected(directedCombo.getSelectedItem()==DIRECTED);
		for(int i = coordinateTableModel.getLayers().size()-1; i >=0; i--) {
			// remove any null layer coordinates, cannot initialize layers to
			// non-null value because not guaranteed to exist
			if(coordinateTableModel.getLayers().get(i)==null) {
				coordinateTableModel.getLayers().remove(i);
				coordinateTableModel.getCoordinates().remove(i);
			}
		}
		edgeRegion.setCoordinateList(coordinateTableModel.getCoordinates());
		edgeRegion.setLayers(coordinateTableModel.getLayers());
	}
}
