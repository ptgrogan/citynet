package edu.mit.citynet.gui.editor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import edu.mit.citynet.core.City;
import edu.mit.citynet.gui.CityPanel;

/**
 * The CityPanelEditor provides editing capabilities for a city.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 * @author Nauman Zafar, nzafar@masdar.ac.ae
 */
public class CityEditorPanel extends CityPanel {
	private static final String SOUTH = "South", NORTH = "North", EAST = "East", WEST = "West";
	private static final long serialVersionUID = -1568725419896973318L;
	private JFileChooser fileChooser;
	private JTextField nameText, imagePathText;
	private JButton imageBrowseButton;
	private JSpinner latitudeSpinner, longitudeSpinner, rotationSpinner;
	private JComboBox latitudeCombo, longitudeCombo;
	private SpinnerNumberModel latitudeModel, longitudeModel, rotationModel;

	/**
	 * Instantiates a new city panel editor.
	 *
	 * @param city the city
	 */
	public CityEditorPanel(City city) {
		super(city);
		fileChooser = new JFileChooser(System.getProperty("user.dir"));
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
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		add(new JLabel("City Name: "), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		nameText = new JTextField(20);
		nameText.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				saveCityNameCommand();
			}
		});
		add(nameText, c);
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Image Path: "), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		JPanel imagePathPanel = new JPanel(new FlowLayout(FlowLayout.LEADING,0,0));
		imagePathText = new JTextField(30);
		imagePathText.setEnabled(false);
		imagePathPanel.add(imagePathText);
		imagePathPanel.add(new JLabel(" "));
		imageBrowseButton = new JButton("Browse");
		imageBrowseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveImagePathCommand();
			}
		});
		imagePathPanel.add(imageBrowseButton);
		add(imagePathPanel, c);
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Latitude: "), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		JPanel latitudePanel = new JPanel(new FlowLayout(FlowLayout.LEADING,0,0));
		latitudeModel = new SpinnerNumberModel(0,0,90,1d);
		latitudeSpinner = new JSpinner(latitudeModel);
		latitudeSpinner.setPreferredSize(new Dimension(100,20));
		latitudePanel.add(latitudeSpinner);
		latitudePanel.add(new JLabel(" degrees "));
		latitudeCombo = new JComboBox(new String[]{NORTH,SOUTH});
		latitudePanel.add(latitudeCombo);
		add(latitudePanel, c);
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Longitude: "), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		JPanel longitudePanel = new JPanel(new FlowLayout(FlowLayout.LEADING,0,0));
		longitudeModel = new SpinnerNumberModel(0,0,90,1d);
		longitudeSpinner = new JSpinner(longitudeModel);
		longitudeSpinner.setPreferredSize(new Dimension(100,20));
		longitudePanel.add(longitudeSpinner);
		longitudePanel.add(new JLabel(" degrees "));
		longitudeCombo = new JComboBox(new String[]{EAST,WEST});
		longitudePanel.add(longitudeCombo);
		add(longitudePanel, c);
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		add(new JLabel("Rotation: "), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		JPanel rotationPanel = new JPanel(new FlowLayout(FlowLayout.LEADING,0,0));
		rotationModel = new SpinnerNumberModel(0,0,360,1d);
		rotationSpinner = new JSpinner(rotationModel);
		rotationSpinner.setPreferredSize(new Dimension(100,20));
		rotationPanel.add(rotationSpinner);
		rotationPanel.add(new JLabel(" degrees counterclockwise from Cardinal"));
		add(rotationPanel, c);
	}
	
	/**
	 * Save city name command.
	 */
	private void saveCityNameCommand() {
		System.out.println("Save City Name Command");
		city.setName(nameText.getText());
	}
	
	/**
	 * Save image path command.
	 */
	private void saveImagePathCommand() {
		int returnVal = fileChooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			imagePathText.setText(fileChooser.getSelectedFile().getAbsolutePath());
			city.setImageFilePath(fileChooser.getSelectedFile().getPath());
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#repaint()
	 */
	public void repaint() {
		super.repaint();
		if (nameText != null) {
			nameText.setText(city.getName());
		}
	}
}
