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

import com.vividsolutions.jts.geom.Coordinate;

import edu.mit.citynet.core.Layer;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.core.NodeRegion.NodeRegionType;
import edu.mit.citynet.core.NodeType;
import edu.mit.citynet.util.CityNetIcon;
import edu.mit.citynet.viz.SystemVizPanel;

public class NodeRegionPanel extends JPanel {
	private static final long serialVersionUID = -3163576993982031134L;
	
	private SystemVizPanel vizPanel;
	private NodeRegion nodeRegion;
	private JTextArea descriptionText;
	private JComboBox nodeRegionTypeCombo, layerCombo, nodeTypeCombo;
	private JTable coordinateTable;
	private CoordinateTableModel coordinateTableModel;
	private JButton addCoordinateButton, deleteCoordinatesButton, moveUpButton, moveDownButton;
	
	/**
	 * Instantiates a new node region panel.
	 */
	public NodeRegionPanel(SystemVizPanel vizPanel) {
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
		add(new JLabel("Layer: ", JLabel.RIGHT), c);
		c.gridx++;
		layerCombo = new JComboBox();
		layerCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 2820843521395503530L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, 
						isSelected, cellHasFocus);
				if(value instanceof Layer) {
					setText(((Layer)value).getName());
				}
				return this;
			}
		});
		add(layerCombo, c);
		c.gridy++;
		c.gridx = 0;
		add(new JLabel("Node Type: ", JLabel.RIGHT), c);
		c.gridx++;
		nodeTypeCombo = new JComboBox();
		nodeTypeCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 2820843521395503530L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, 
						isSelected, cellHasFocus);
				if(value instanceof NodeType) {
					setText(((NodeType)value).getName());
					setIcon(((NodeType)value).getIcon());
				}
				return this;
			}
		});
		add(nodeTypeCombo, c);
		c.gridy++;
		c.gridx = 0;
		add(new JLabel("Region Type: ", JLabel.RIGHT), c);
		c.gridx++;
		nodeRegionTypeCombo = new JComboBox();
		for(NodeRegionType type : NodeRegionType.values()) {
			nodeRegionTypeCombo.addItem(type);
		}
		nodeRegionTypeCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 2820843521395503530L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, 
						isSelected, cellHasFocus);
				if(value instanceof NodeRegionType) {
					setText(((NodeRegionType)value).getName());
					setIcon(((NodeRegionType)value).getIcon());
				}
				return this;
			}
		});
		add(nodeRegionTypeCombo, c);
		c.gridy = 0;
		c.gridx = 2;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Coordinates: ", JLabel.RIGHT), c);
		c.gridx++;
		c.gridheight = 4;
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
		coordinateTableModel.getCoordinates().add(new Coordinate(),true);
		coordinateTableModel.fireTableRowsInserted(
				coordinateTableModel.getRowCount(),
				coordinateTableModel.getRowCount());
	}
	
	/**
	 * Delete coordinates command.
	 */
	private void deleteCoordinatesCommand() {
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
	 * Load node region.
	 *
	 * @param nodeRegion the node region
	 */
	public void loadNodeRegion(NodeRegion nodeRegion) {
		this.nodeRegion = nodeRegion;
		descriptionText.setText(nodeRegion.getDescription());
		layerCombo.removeAllItems();
		for(Layer layer : vizPanel.getSystemPanel().getSystem().getLayers())
			layerCombo.addItem(layer);
		if(nodeRegion.getLayer()==null && layerCombo.getItemCount()>0)
			layerCombo.setSelectedIndex(0);
		else
			layerCombo.setSelectedItem(nodeRegion.getLayer());
		nodeTypeCombo.removeAllItems();
		for(NodeType type : vizPanel.getSystemPanel().getSystem().getNodeTypes())
			nodeTypeCombo.addItem(type);
		if(nodeRegion.getNodeType()==null && nodeTypeCombo.getItemCount()>0)
			nodeTypeCombo.setSelectedIndex(0);
		else
			nodeTypeCombo.setSelectedItem(nodeRegion.getNodeType());
		if(nodeRegion.getNodeRegionType()==null)
			nodeRegionTypeCombo.setSelectedIndex(0);
		else
			nodeRegionTypeCombo.setSelectedItem(nodeRegion.getNodeRegionType());
		coordinateTableModel.setCoordinates(nodeRegion.getCoordinateList());
	}
	
	/**
	 * Save node region command.
	 */
	public void saveNodeRegionCommand() {
		if(coordinateTable.isEditing()) coordinateTable.getCellEditor().stopCellEditing();
		nodeRegion.setNodeRegionType((NodeRegionType)nodeRegionTypeCombo.getSelectedItem());
		nodeRegion.setDescription(descriptionText.getText());
		nodeRegion.setLayer((Layer)layerCombo.getSelectedItem());
		nodeRegion.setNodeType((NodeType)nodeTypeCombo.getSelectedItem());
		nodeRegion.setCoordinateList(coordinateTableModel.getCoordinates());
	}
}
