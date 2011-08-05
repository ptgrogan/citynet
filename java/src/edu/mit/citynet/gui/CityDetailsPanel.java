package edu.mit.citynet.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.vividsolutions.jts.geom.Coordinate;

import edu.mit.citynet.CityNet;
import edu.mit.citynet.core.City;

public class CityDetailsPanel extends JPanel {
	private static final long serialVersionUID = 4642496612942013193L;
	private static final String SOUTH = "South", NORTH = "North", 
		EAST = "East", WEST = "West";
	private City city;
	private JFileChooser fileChooser;
	private JTextField nameText, imagePathText;
	private JButton imageBrowseButton;
	private JSpinner latitudeSpinner, longitudeSpinner, rotationSpinner,
		x1Spinner, y1Spinner, x2Spinner, y2Spinner;
	private JComboBox latitudeCombo, longitudeCombo;
	private SpinnerNumberModel latitudeModel, longitudeModel, rotationModel,
		x1Model, y1Model, x2Model, y2Model;
	private JLabel imageLabel;
	
	public CityDetailsPanel(CityNetFrame owner) {
		fileChooser = new JFileChooser(System.getProperty("user.dir"));
		initializeDialog();
	}
	
	/**
	 * Initialize panel.
	 */
	private void initializeDialog() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("City Name: ", JLabel.RIGHT), c);
		c.gridx++;
		c.weightx = 1;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.LINE_START;
		nameText = new JTextField(20);
		add(nameText, c);
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 2;
		add(createBackgroundImagePanel(), c);
		c.gridy++;
		add(createCoordinateMappingPanel(), c);
		c.gridy++;
		add(new JPanel(), c);
		c.gridy = 1;
		c.gridx = 2;
		c.gridheight = 3;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		imageLabel = new JLabel(new ImageIcon(), JLabel.CENTER);
		add(imageLabel, c);
	}
	
	/**
	 * Creates the background image panel.
	 *
	 * @return the j panel
	 */
	private JPanel createBackgroundImagePanel() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Background Image"));
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(new JLabel("Image Path: ", JLabel.RIGHT), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		JPanel imagePathPanel = new JPanel(new BorderLayout());
		imagePathText = new JTextField(30);
		imagePathText.setEnabled(false);
		imagePathPanel.add(imagePathText, BorderLayout.CENTER);
		imageBrowseButton = new JButton("Browse");
		imageBrowseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveImagePathCommand();
			}
		});
		imagePathPanel.add(imageBrowseButton,BorderLayout.EAST);
		p.add(imagePathPanel, c);
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		p.add(new JLabel("Upper-left Corner:", JLabel.RIGHT),c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		JPanel vertex1Panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		vertex1Panel.add(new JLabel("X="));
		x1Model = new SpinnerNumberModel(0,-100,100,1d);
		x1Spinner = new JSpinner(x1Model);
		vertex1Panel.add(x1Spinner);
		vertex1Panel.add(new JLabel("Y="));
		y1Model = new SpinnerNumberModel(0,-100,100,1d);
		y1Spinner = new JSpinner(y1Model);
		vertex1Panel.add(y1Spinner);
		p.add(vertex1Panel, c);
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		p.add(new JLabel("Lower-right Corner:", JLabel.RIGHT),c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		JPanel vertex2Panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		vertex2Panel.add(new JLabel("X="));
		x2Model = new SpinnerNumberModel(0,-100,100,1d);
		x2Spinner = new JSpinner(x2Model);
		vertex2Panel.add(x2Spinner);
		vertex2Panel.add(new JLabel("Y="));
		y2Model = new SpinnerNumberModel(0,-100,100,1d);
		y2Spinner = new JSpinner(y2Model);
		vertex2Panel.add(y2Spinner);
		p.add(vertex2Panel, c);
		return p;
	}
	
	/**
	 * Creates the mapping panel.
	 *
	 * @return the j panel
	 */
	private JPanel createCoordinateMappingPanel() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Coordinate Mapping"));
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(new JLabel("Latitude: ", JLabel.RIGHT), c);
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
		p.add(latitudePanel, c);
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		p.add(new JLabel("Longitude: ", JLabel.RIGHT), c);
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
		p.add(longitudePanel, c);
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.LINE_END;
		p.add(new JLabel("Rotation: ", JLabel.RIGHT), c);
		c.gridx++;
		c.weightx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		JPanel rotationPanel = new JPanel(new FlowLayout(FlowLayout.LEADING,0,0));
		rotationModel = new SpinnerNumberModel(0,0,360,1d);
		rotationSpinner = new JSpinner(rotationModel);
		rotationSpinner.setPreferredSize(new Dimension(100,20));
		rotationPanel.add(rotationSpinner);
		rotationPanel.add(new JLabel(" degrees counterclockwise from Cardinal"));
		p.add(rotationPanel, c);
		return p;
	}
	
	/**
	 * Load city details.
	 */
	public void loadCityDetails(City city) {
		if (city==null) {
			throw new IllegalArgumentException("City cannot be null.");
		}
		this.city = city;
		nameText.setText(city.getName());
		imagePathText.setText(city.getImageFilePath());
		if(city.getImage()!=null) {
			imageLabel.setIcon(new ImageIcon(city.getImage()
					.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
		}
		if(city.getImagePolygon()!=null) {
			x1Model.setValue(city.getImagePolygon().getCoordinates()[0].x);
			y1Model.setValue(city.getImagePolygon().getCoordinates()[0].y);
			x2Model.setValue(city.getImagePolygon().getCoordinates()[2].x);
			y2Model.setValue(city.getImagePolygon().getCoordinates()[2].y);
		} else {
			x1Model.setValue(0);
			y1Model.setValue(0);
			x2Model.setValue(1);
			y2Model.setValue(1);
		}
		latitudeModel.setValue(Math.abs(city.getLatitude()));
		latitudeCombo.setSelectedItem(city.getLatitude()>0?NORTH:SOUTH);
		longitudeModel.setValue(Math.abs(city.getLongitude()));
		longitudeCombo.setSelectedItem(city.getLongitude()>0?EAST:WEST);
		rotationModel.setValue(city.getRotation());
		//getRootPane().setDefaultButton(okButton);
	}
	
	/**
	 * Save image path command.
	 */
	private void saveImagePathCommand() {
		int returnVal = fileChooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				imageLabel.setIcon(new ImageIcon(
						ImageIO.read(fileChooser.getSelectedFile())
						.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
				repaint();
				imagePathText.setText(fileChooser.getSelectedFile().getAbsolutePath());
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(this, "Could not read image file:\n" + 
						fileChooser.getSelectedFile().getAbsolutePath(), 
						"City.Net | Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Save city details command.
	 */
	public void saveCityDetailsCommand() {
		System.out.println("Save City Details Command");
		city.setName(nameText.getText());
		if(imagePathText.getText().length()>0)
			city.setImageFilePath(imagePathText.getText());
		else
			city.setImageFilePath(null);
		city.setImagePolygon(CityNet.getInstance().getGeometryFactory().createPolygon(
				CityNet.getInstance().getGeometryFactory().createLinearRing(
						new Coordinate[]{
								new Coordinate(x1Model.getNumber().doubleValue(),
										y1Model.getNumber().doubleValue()), 
								new Coordinate(x2Model.getNumber().doubleValue(),
										y1Model.getNumber().doubleValue()), 
								new Coordinate(x2Model.getNumber().doubleValue(),
										y2Model.getNumber().doubleValue()), 
								new Coordinate(x1Model.getNumber().doubleValue(),
										y2Model.getNumber().doubleValue()), 
								new Coordinate(x1Model.getNumber().doubleValue(),
										y1Model.getNumber().doubleValue())}
						), null));
		city.setLatitude(latitudeModel.getNumber().doubleValue()
				*(latitudeCombo.getSelectedItem()==NORTH?1:-1));
		city.setLongitude(longitudeModel.getNumber().doubleValue()
				*(longitudeCombo.getSelectedItem()==EAST?1:-1));
		city.setRotation(rotationModel.getNumber().doubleValue());
	}
}
