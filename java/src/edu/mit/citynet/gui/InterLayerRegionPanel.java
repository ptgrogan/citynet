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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.vividsolutions.jts.geom.Coordinate;

import edu.mit.citynet.core.EdgeDirection;
import edu.mit.citynet.core.EdgeType;
import edu.mit.citynet.core.InterLayerRegion;
import edu.mit.citynet.core.Layer;
import edu.mit.citynet.util.CityNetIcon;

/**
 * The RegionPanel class provides a view for editing a region.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class InterLayerRegionPanel extends JPanel {
	private static final long serialVersionUID = -3163576993982031134L;
	
	private SystemPanel systemPanel;
	private InterLayerRegion region;
	private JTextArea descriptionText;
	private JComboBox originLayerCombo, destinationLayerCombo, 
		edgeDirectionCombo, edgeTypeCombo;
	private JTable coordinateTable;
	private CoordinateTableModel coordinateTableModel;
	private CoordinateSelectionPanel coordinateSelectionPanel;
	private JButton addCoordinateButton, deleteCoordinatesButton, 
		moveUpButton, moveDownButton, selectCoordinatesButton;
	
	/**
	 * Instantiates a new node region panel.
	 *
	 * @param systemPanel the system panel
	 */
	public InterLayerRegionPanel(SystemPanel systemPanel) {
		this.systemPanel = systemPanel;
		coordinateSelectionPanel = new CoordinateSelectionPanel(
				systemPanel.getCityPanel().getCity(), systemPanel.getSystem());
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
		add(new JLabel("Origin Layer: ", JLabel.RIGHT), c);
		c.gridx++;
		originLayerCombo = new JComboBox();
		originLayerCombo.setRenderer(RendererFactory.createLayerListCellRenderer());
		add(originLayerCombo, c);
		c.gridy++;
		c.gridx = 0;
		add(new JLabel("Destination Layer: ", JLabel.RIGHT), c);
		c.gridx++;
		destinationLayerCombo = new JComboBox();
		destinationLayerCombo.setRenderer(RendererFactory.createLayerListCellRenderer());
		add(destinationLayerCombo, c);
		c.gridy++;
		c.gridx = 0;
		add(new JLabel("Edge Type: ", JLabel.RIGHT), c);
		c.gridx++;
		edgeTypeCombo = new JComboBox();
		edgeTypeCombo.setRenderer(RendererFactory.createEdgeTypeListCellRenderer());
		add(edgeTypeCombo, c);
		c.gridy++;
		c.gridx = 0;
		add(new JLabel("Edge Direction: ", JLabel.RIGHT), c);
		c.gridx++;
		edgeDirectionCombo = new JComboBox(EdgeDirection.values());
		edgeDirectionCombo.setRenderer(RendererFactory.createEdgeDirectionListCellRenderer());
		add(edgeDirectionCombo,c);
		c.gridy = 0;
		c.gridx = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Coordinates: ", JLabel.RIGHT), c);
		c.gridx++;
		c.gridheight = 5;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.VERTICAL;
		coordinateTableModel = new CoordinateTableModel();
		coordinateTable = new JTable(coordinateTableModel);
		coordinateTable.getTableHeader().setReorderingAllowed(false);
		coordinateTable.getColumnModel().getColumn(0).setHeaderValue("X");
		coordinateTable.getColumnModel().getColumn(1).setHeaderValue("Y");
		coordinateTable.setPreferredScrollableViewportSize(new Dimension(150,200));
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
		selectCoordinatesButton = new JButton(CityNetIcon.SELECT_COORDINATES.getIcon());
		selectCoordinatesButton.setToolTipText("Select coordinates from map");
		selectCoordinatesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectCoordinatesCommand();
			}
		});
		coordinateButtonPanel.add(selectCoordinatesButton);
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
			coordinateTableModel.fireTableRowsUpdated(selectedRow, selectedRow+1);
			coordinateTable.getSelectionModel().setSelectionInterval(selectedRow+1,selectedRow+1);
		}
	}
	
	/**
	 * Load region.
	 *
	 * @param region the region
	 */
	public void loadRegion(InterLayerRegion region) {
		this.region = region;
		descriptionText.setText(region.getDescription());
		originLayerCombo.removeAllItems();
		for(Layer layer : systemPanel.getSystem().getLayers())
			originLayerCombo.addItem(layer);
		if(region.getOriginLayer()==null && originLayerCombo.getItemCount()>0)
			originLayerCombo.setSelectedIndex(0);
		else
			originLayerCombo.setSelectedItem(region.getOriginLayer());
		destinationLayerCombo.removeAllItems();
		for(Layer layer : systemPanel.getSystem().getLayers())
			destinationLayerCombo.addItem(layer);
		if(region.getDestinationLayer()==null && destinationLayerCombo.getItemCount()>0)
			destinationLayerCombo.setSelectedIndex(0);
		else
			destinationLayerCombo.setSelectedItem(region.getDestinationLayer());
		edgeTypeCombo.removeAllItems();
		for(EdgeType type : systemPanel.getSystem().getEdgeTypes())
			edgeTypeCombo.addItem(type);
		if(region.getEdgeType()==null && edgeTypeCombo.getItemCount()>0)
			edgeTypeCombo.setSelectedIndex(0);
		edgeDirectionCombo.setSelectedItem(region.getEdgeDirection());
		coordinateTableModel.setCoordinates(region.getCoordinateList());
	}
	
	/**
	 * Select coordinates command.
	 */
	public void selectCoordinatesCommand() {
		System.out.println("Select Coordinates Command");
		coordinateSelectionPanel.setCoordinates(coordinateTableModel.getCoordinates());
		int value = JOptionPane.showConfirmDialog(this, coordinateSelectionPanel, 
				"City.Net | Select Coordinates", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE);
		if(value == JOptionPane.OK_OPTION) {
			coordinateTableModel.setCoordinates(coordinateSelectionPanel.getCoordinates());
		}
	}
	
	/**
	 * Save node region command.
	 */
	public void saveRegionCommand() {
		if(coordinateTable.isEditing()) coordinateTable.getCellEditor().stopCellEditing();
		region.setDescription(descriptionText.getText());
		region.setOriginLayer((Layer)originLayerCombo.getSelectedItem());
		region.setDestinationLayer((Layer)destinationLayerCombo.getSelectedItem());
		region.setEdgeType((EdgeType)edgeTypeCombo.getSelectedItem());
		region.setEdgeDirection((EdgeDirection)edgeDirectionCombo.getSelectedItem());
		region.setCoordinateList(coordinateTableModel.getCoordinates());
	}
}
