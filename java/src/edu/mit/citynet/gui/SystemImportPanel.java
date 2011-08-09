package edu.mit.citynet.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import edu.mit.citynet.CityNet;
import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.Edge;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.EdgeType;
import edu.mit.citynet.core.EdgeTypeAttribute;
import edu.mit.citynet.core.Layer;
import edu.mit.citynet.core.Node;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.core.NodeType;
import edu.mit.citynet.core.NodeTypeAttribute;
import edu.mit.citynet.io.SpreadsheetTemplate;
import edu.mit.citynet.util.CityNetIcon;

/**
 * The SystemImportPanel provides support for importing systems from existing
 * City.Net files.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class SystemImportPanel extends JPanel {
	private static final long serialVersionUID = -6381545168359526457L;
	private JFileChooser fileChooser;
	private JTextField filePathText;
	private JList systemsList;
	private DefaultListModel systemsListModel;
	private JButton browseButton;
	
	/**
	 * Instantiates a new system import panel.
	 */
	public SystemImportPanel() {
		fileChooser = new JFileChooser(System.getProperty("user.dir"));
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if(f.isDirectory()) {
					return true;
				}
				String extension = null;
				
				String s = f.getName();
				int i = s.lastIndexOf('.');
				if (i > 0 &&  i < s.length() - 1) {
					extension = s.substring(i+1).toLowerCase();
		        }
				
				if(extension != null) {
					if(extension.equals("xls")) return true;
					else return false;
				} else return false;
			}

			@Override
			public String getDescription() {
				return "XLS Files";
			}
		});
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
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("City.Net File: ", JLabel.RIGHT), c);
		c.gridx++;
		c.weightx = 1;
		JPanel imagePathPanel = new JPanel(new FlowLayout(FlowLayout.LEADING,0, 0));
		filePathText = new JTextField(30);
		filePathText.setEnabled(false);
		imagePathPanel.add(filePathText, BorderLayout.CENTER);
		browseButton = new JButton(CityNetIcon.BROWSE.getIcon());
		browseButton.setToolTipText("Browse for City.Net file");
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFilePathCommand();
			}
		});
		imagePathPanel.add(browseButton,BorderLayout.EAST);
		add(imagePathPanel, c);
		c.gridy++;
		c.gridx = 0;
		c.weightx = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(new JLabel("Systems: ", JLabel.RIGHT), c);
		c.gridx++;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		systemsListModel = new DefaultListModel();
		systemsList = new JList(systemsListModel);
		systemsList.setEnabled(false);
		systemsList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 5518278721412823866L;
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, 
						isSelected, cellHasFocus);
				if(value instanceof CitySystem) {
					setText(((CitySystem)value).getName());
					setIcon(((CitySystem)value).getType().getIcon());
				}
				return this;
			}
		});
		add(new JScrollPane(systemsList), c);
	}
	
	/**
	 * Save file path command.
	 */
	private void saveFilePathCommand() {
		int returnVal = fileChooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				filePathText.setText(fileChooser.getSelectedFile().getAbsolutePath());
				SpreadsheetTemplate template = new SpreadsheetTemplate();
				template.setFilePath(fileChooser.getSelectedFile().getAbsolutePath());
				City city = template.readTemplate();
				systemsList.setEnabled(true);
				systemsListModel.clear();
				for(CitySystem system : city.getSystems()) {
					systemsListModel.addElement(system);
				}
			} catch(Exception ex) {
				systemsList.setEnabled(false);
				JOptionPane.showMessageDialog(this, "Could not read City.Net file:\n" + 
						fileChooser.getSelectedFile().getAbsolutePath(), 
						"City.Net | Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Gets the selected systems.
	 *
	 * @return the selected systems
	 */
	public Set<CitySystem> getSelectedSystems() {
		Set<CitySystem> systems = new HashSet<CitySystem>();
		for(Object o : systemsList.getSelectedValues()) {
			if(o instanceof CitySystem) {
				CitySystem system = (CitySystem)o;
				// reset all ids for new city
				system.setId(CityNet.getInstance().getNextSystemId());
				for(Layer layer : system.getLayers()) {
					layer.setId(CityNet.getInstance().getNextLayerId());
				}
				for(NodeRegion region : system.getNodeRegions()) {
					region.setId(CityNet.getInstance().getNextNodeRegionId());
				}
				for(EdgeRegion region : system.getEdgeRegions()) {
					region.setId(CityNet.getInstance().getNextEdgeRegionId());
				}
				for(NodeType type : system.getNodeTypes()) {
					type.setId(CityNet.getInstance().getNextNodeTypeId());
					for(NodeTypeAttribute attribute : type.getAttributes()) {
						attribute.setId(CityNet.getInstance().getNextNodeTypeAttributeId());
					}
				}
				for(EdgeType type : system.getEdgeTypes()) {
					type.setId(CityNet.getInstance().getNextEdgeTypeId());
					for(EdgeTypeAttribute attribute : type.getAttributes()) {
						attribute.setId(CityNet.getInstance().getNextEdgeTypeAttributeId());
					}
				}
				system.setNodes(new HashSet<Node>());
				system.setEdges(new HashSet<Edge>());
				systems.add(system);
			}
		}
		filePathText.setText(null);
		systemsListModel.clear();
		return systems;
	}
}
