package edu.mit.citynet.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.CitySystem.CitySystemType;

/**
 * The Class SystemDetailsPanel.
 */
public class SystemDetailsPanel extends JPanel {
	private static final long serialVersionUID = 5770848822470595923L;
	private CitySystem system;
	private JTextField nameText;
	private JComboBox typeCombo;
	private JTextArea descriptionText;
	
	/**
	 * Instantiates a new system details panel.
	 */
	public SystemDetailsPanel() {
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
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Type: ", JLabel.RIGHT), c);
		c.gridx++;
		typeCombo = new JComboBox();
		typeCombo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, 
						isSelected, cellHasFocus);
				if(value instanceof CitySystemType) {
					setIcon(((CitySystemType)value).getIcon());
					setText(((CitySystemType)value).getName());
				}
				return this;
			}
		});
		for(CitySystemType type : CitySystemType.values()) {
			typeCombo.addItem(type);
		}
		typeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(system == null || system.getName().equals("New System")) {
					nameText.setText(typeCombo.getSelectedItem().toString());
				}
			}
		});
		add(typeCombo, c);
		c.gridy++;
		c.gridx = 0;
		add(new JLabel("System Name: ", JLabel.RIGHT), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		nameText = new JTextField(20);
		add(nameText, c);
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		add(new JLabel("Description: ", JLabel.RIGHT), c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		descriptionText = new JTextArea(3,20);
		descriptionText.setMargin(new Insets(2,2,2,2));
		add(new JScrollPane(descriptionText),c);
	}
	
	/**
	 * Load system details.
	 */
	public void loadSystemDetails(CitySystem system) {
		if (system==null) {
			throw new IllegalArgumentException("System cannot be null.");
		}
		this.system = system;
		nameText.setText(system.getName());
		typeCombo.setSelectedItem(system.getType());
		descriptionText.setText(system.getDescription());
	}
	
	/**
	 * Save system details.
	 */
	public void saveSystemDetailsCommand() {
		System.out.println("Save System Details Command");
		if (system==null) {
			throw new IllegalArgumentException("System cannot be null.");
		}
		system.setName(nameText.getText());
		system.setType((CitySystemType)typeCombo.getSelectedItem());
		system.setDescription(descriptionText.getText());
	}
}
