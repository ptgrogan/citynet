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
	private JTable verticesTable;
	private VertexTableModel verticesTableModel;
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
		for(Layer layer : vizPanel.getSystemPanel().getSystem().getLayers()) {
			layerCombo.addItem(layer);
		}
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
		for(NodeType type : vizPanel.getSystemPanel().getSystem().getNodeTypes()) {
			nodeTypeCombo.addItem(type);
		}
		nodeTypeCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 2820843521395503530L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, 
						isSelected, cellHasFocus);
				if(value instanceof NodeType) {
					setText(((NodeType)value).getName());
					final Color color = ((NodeType)value).getColor();
					ImageFilter filter = new RGBImageFilter() {
						public int filterRGB(int x, int y, int rgb) {
							return rgb & color.getRGB();
						}
					};
					ImageProducer producer = new FilteredImageSource(
							CityNetIcon.NODE_TYPE_BLANK.getIcon().getImage().getSource(), filter);
					setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(producer)));
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
		add(new JLabel("Vertices: ", JLabel.RIGHT), c);
		c.gridx++;
		c.gridheight = 4;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.VERTICAL;
		verticesTableModel = new VertexTableModel();
		verticesTable = new JTable(verticesTableModel);
		verticesTable.getTableHeader().setReorderingAllowed(false);
		verticesTable.getColumnModel().getColumn(0).setHeaderValue("X");
		verticesTable.getColumnModel().getColumn(1).setHeaderValue("Y");
		verticesTable.setPreferredScrollableViewportSize(new Dimension(150,200));
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
	 * Load node region.
	 *
	 * @param nodeRegion the node region
	 */
	public void loadNodeRegion(NodeRegion nodeRegion) {
		this.nodeRegion = nodeRegion;
		nodeRegionTypeCombo.setSelectedItem(nodeRegion.getNodeRegionType());
		descriptionText.setText(nodeRegion.getDescription());
		layerCombo.setSelectedItem(nodeRegion.getLayer());
		nodeTypeCombo.setSelectedItem(nodeRegion.getNodeType());
		verticesTableModel.setCoordinates(nodeRegion.getCoordinateList());
	}
	
	/**
	 * Save node region command.
	 */
	public void saveNodeRegionCommand() {
		if(verticesTable.isEditing()) verticesTable.getCellEditor().stopCellEditing();
		nodeRegion.setNodeRegionType((NodeRegionType)nodeRegionTypeCombo.getSelectedItem());
		nodeRegion.setDescription(descriptionText.getText());
		nodeRegion.setLayer((Layer)layerCombo.getSelectedItem());
		nodeRegion.setNodeType((NodeType)nodeTypeCombo.getSelectedItem());
		nodeRegion.setCoordinateList(verticesTableModel.getCoordinates());
	}
}
