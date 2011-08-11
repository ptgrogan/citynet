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

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.mit.citynet.core.EdgeType;
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
	 * Save edge type command.
	 */
	public void saveEdgeTypeCommand() {
		edgeType.setName(nameText.getText());
		edgeType.setDescription(descriptionText.getText());
		edgeType.setColor(colorLabel.getBackground());
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
	}
}
