/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.mit.citynet.CityNet;
import edu.mit.citynet.core.EdgeType;
import edu.mit.citynet.core.EdgeTypeAttribute;
import edu.mit.citynet.util.CityNetIcon;

/**
 * The EdgeTypePanel class.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class EdgeTypePanel extends JPanel {
	private static final long serialVersionUID = -6993531719921341469L;

	private EdgeType edgeType;
	private JTextField nameText;
	private JTextArea descriptionText;
	private JButton colorButton;
	private JLabel colorLabel;
	private AttributeTableModel<EdgeTypeAttribute> attributeTableModel;
	private JTable attributeTable;
	private JButton addAttributeButton, deleteAttributesButton;
	
	/**
	 * Instantiates a new node type panel.
	 *
	 */
	public EdgeTypePanel() {
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
		c.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Name: ", JLabel.RIGHT), c);
		c.gridx++;
		c.weightx = 1;
		nameText = new JTextField(30);
		add(nameText, c);
		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
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
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Color: ", JLabel.RIGHT), c);
		c.gridx++;
		JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		colorLabel = new JLabel();
		colorLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2)
					selectColorCommand();
			}
		});
		colorLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		colorLabel.setOpaque(true);
		colorLabel.setPreferredSize(new Dimension(35,20));
		colorPanel.add(colorLabel);
		colorButton = new JButton(CityNetIcon.COLOR_PALETTE.getIcon());
		colorButton.setToolTipText("Select the color");
		colorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectColorCommand();
			}
		});
		colorPanel.add(colorButton);
		add(colorPanel, c);
		c.gridy++;
		c.gridx = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Attributes: ", JLabel.RIGHT), c);
		c.gridx++;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		attributeTableModel = new AttributeTableModel<EdgeTypeAttribute>();
		attributeTable = new JTable(attributeTableModel);
		attributeTable.getTableHeader().setReorderingAllowed(false);
		attributeTable.getColumnModel().getColumn(0).setHeaderValue("Name");
		attributeTable.getColumnModel().getColumn(0).setPreferredWidth(75);
		attributeTable.getColumnModel().getColumn(1).setHeaderValue("Description");
		attributeTable.getColumnModel().getColumn(1).setPreferredWidth(125);
		attributeTable.getColumnModel().getColumn(2).setHeaderValue("Units");
		attributeTable.getColumnModel().getColumn(2).setPreferredWidth(75);
		attributeTable.getColumnModel().getColumn(3).setHeaderValue("Bounds");
		attributeTable.getColumnModel().getColumn(3).setPreferredWidth(75);
		attributeTable.getColumnModel().getColumn(4).setHeaderValue("Value");
		attributeTable.getColumnModel().getColumn(4).setPreferredWidth(50);
		attributeTable.setPreferredScrollableViewportSize(new Dimension(400,150));
		MouseAdapter attributeMouseAdapter = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2 && e.getComponent()!=attributeTable)
					addAttributeCommand();
			}
			public void mousePressed(MouseEvent e) {
				if(e.getComponent()!=attributeTable)
					attributeTable.getSelectionModel().clearSelection();
				maybeShowPopup(e);
			}
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}
			private void maybeShowPopup(MouseEvent e) {
				if(e.isPopupTrigger()) {
					int row = attributeTable.rowAtPoint(e.getPoint());
					attributeTable.getSelectionModel().addSelectionInterval(row, row);
					Set<EdgeTypeAttribute> attributes = new HashSet<EdgeTypeAttribute>();
					for(int i : attributeTable.getSelectedRows()) 
						attributes.add(attributeTableModel.getAttributes().get(i));
					createAttributePopupMenu(attributes).show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
		attributeTable.addMouseListener(attributeMouseAdapter);
		attributeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				deleteAttributesButton.setEnabled(attributeTable.getSelectedRowCount()>0);
			}
		});
		JScrollPane attributeScroll = new JScrollPane(attributeTable);
		attributeScroll.addMouseListener(attributeMouseAdapter);
		add(attributeScroll, c);
		c.gridy++;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_START;
		JPanel attributeButtonPanel = new JPanel();
		attributeButtonPanel.setLayout(new BoxLayout(attributeButtonPanel, BoxLayout.LINE_AXIS));
		addAttributeButton = new JButton(CityNetIcon.ADD_COORDINATE.getIcon());
		addAttributeButton.setToolTipText("Add new attribute");
		addAttributeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addAttributeCommand();
			}
		});
		attributeButtonPanel.add(addAttributeButton);
		deleteAttributesButton = new JButton(CityNetIcon.DELETE_COORDINATE.getIcon());
		deleteAttributesButton.setToolTipText("Delete selected attributes");
		deleteAttributesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteAttributesCommand();
			}
		});
		deleteAttributesButton.setEnabled(false);
		attributeButtonPanel.add(deleteAttributesButton);
		add(attributeButtonPanel, c);
	}
	
	/**
	 * Creates the attribute popup menu.
	 *
	 * @param attributes the attributes
	 * @return the j popup menu
	 */
	private JPopupMenu createAttributePopupMenu(final Set<EdgeTypeAttribute> attributes) {
		JPopupMenu attributePopupMenu = new JPopupMenu();
		if(attributes.size()>0) {
			JMenuItem deleteAttributesMenuItem = new JMenuItem("Delete Attribute" + (attributes.size()>1?"s":""));
			deleteAttributesMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					deleteAttributesCommand();
				}
			});
			attributePopupMenu.add(deleteAttributesMenuItem);
		} else {
			JMenuItem addAttributeMenuItem = new JMenuItem("Add Attribute");
			addAttributeMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addAttributeCommand();
				}
			});
			attributePopupMenu.add(addAttributeMenuItem);
		}
		return attributePopupMenu;
	}
	
	/**
	 * Adds the attribute command.
	 */
	private void addAttributeCommand() {
		System.out.println("Add Attribute Command");
		EdgeTypeAttribute attribute = new EdgeTypeAttribute();
		attribute.setId(CityNet.getInstance().getNextEdgeTypeAttributeId());
		attributeTableModel.getAttributes().add(attribute);
		attributeTableModel.fireTableRowsInserted(
				attributeTableModel.getRowCount(),
				attributeTableModel.getRowCount());
	}
	
	/**
	 * Delete attributes command.
	 */
	private void deleteAttributesCommand() {
		System.out.println("Delete Attributes Command");
		for(int i = attributeTable.getSelectedRows().length-1; i>=0; i--) {
			int rowDeleted = attributeTable.getSelectedRows()[i];
			attributeTableModel.getAttributes().remove(rowDeleted);
			attributeTableModel.fireTableRowsDeleted(rowDeleted, rowDeleted);
		}
	}
	
	/**
	 * Select color command.
	 */
	private void selectColorCommand() {
		Color color = JColorChooser.showDialog(
				this, "SpaceNet | Color", colorLabel.getBackground());
		if(color!=null) colorLabel.setBackground(color);
	}
	
	/**
	 * Load edge type.
	 *
	 * @param edgeType the edge type
	 */
	public void loadEdgeType(EdgeType edgeType) {
		this.edgeType = edgeType;
		nameText.setText(edgeType.getName());
		descriptionText.setText(edgeType.getDescription());
		colorLabel.setBackground(edgeType.getColor());
		attributeTableModel.setAttributes(edgeType.getAttributes());
	}
	
	/**
	 * Save edge type command.
	 */
	public void saveEdgeTypeCommand() {
		if(attributeTable.isEditing()) attributeTable.getCellEditor().stopCellEditing();
		edgeType.setName(nameText.getText());
		edgeType.setDescription(descriptionText.getText());
		edgeType.setColor(colorLabel.getBackground());
		Set<EdgeTypeAttribute> attributes = new HashSet<EdgeTypeAttribute>();
		attributes.addAll(attributeTableModel.getAttributes());
		edgeType.setAttributes(attributes);
	}
}
