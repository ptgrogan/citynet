package edu.mit.citynet.gui;

import javax.swing.*;
import javax.swing.table.*;

import net.infonode.gui.laf.InfoNodeLookAndFeel;
import net.infonode.gui.laf.InfoNodeLookAndFeelThemes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.*;
import java.awt.event.*;

import edu.mit.citynet.core.*; 

public class BuildingLayersPanel extends BuildingPanel implements ActionListener{

	private static final long serialVersionUID = 4707459231955143752L;
	private JTable layerstable;
	private LayersTableModel layersmodel;
	private int systemid;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTable jTable1;
	
	public BuildingLayersPanel()
	{
		this.setName("Layers");
		City city = CityNet.getInstance().getCity();
		for (CitySystem system : city.getSystems())
		{
			String str = system.getName(); 
		 
			if (str.equals("Building"))
			{
				this.systemid = system.getId();
				break;  //Break the for loop
			}
			
		}
		
		String cols[] = {"Name", "Description","Display Height"};
		layersmodel = new LayersTableModel(cols);
		layersmodel.setLayers(city);
		initComponents();		
				
	}
	
	private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable(layersmodel);
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jScrollPane1.setViewportView(jTable1);

        jLabel1.setName("jLabel1"); // NOI18N

        jButton1.setText("Add"); // NOI18N
        jButton1.setActionCommand("Add");
        jButton2.setActionCommand("Edit");
        jButton3.setActionCommand("Delete");     
        jButton1.addActionListener((this));
        jButton2.addActionListener(this);
        jButton3.addActionListener(this);
        jButton2.setText("Edit"); // NOI18N

        jButton3.setText("Delete"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(134, 134, 134)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(242, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(83, Short.MAX_VALUE))
        );
    }// </editor-fold>

		
	private class LayersTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 5386287393549485163L;
		private List<Layer> nodeRegions;
		private Layer selected;
		private String headers[];
		
		/**
		 * Instantiates a new node region table model.
		 */
		public LayersTableModel(String cols[]) {
			super();
			nodeRegions = new ArrayList<Layer>();
			headers  = cols;
			
		}
		
		/**
		 * Sets the node regions.
		 *
		 * @param city the new node regions
		 */
		public void setLayers(City city) {
			nodeRegions.clear();
			selected = null;
			for(CitySystem system : city.getSystems()) {
				nodeRegions.addAll(system.getLayers());
			}
			fireTableDataChanged();
		}
		
		/**
		 * Gets the selected node region.
		 *
		 * @return the selected node region
		 */
		public Layer getSelectedNodeRegion() {
			return selected;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			return nodeRegions.size();
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return 3;
		}
		
		public String getColumnName(int col)
		{
			return headers[col];
		}
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(columnIndex==0)
			{
				return nodeRegions.get(rowIndex).getName();
			}
			else if (columnIndex == 1)
			{
				return nodeRegions.get(rowIndex).getDescription();
			}
			else
			{
				return nodeRegions.get(rowIndex).getDisplayHeight();
			}
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
		public boolean isCellEditable(int row, int col) {
	    	if(col==0) return true;
	    	else return false;
	    }
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
		public Class<?> getColumnClass(int col) {
			return Object.class;
	    }
		
		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
		 */
		public void setValueAt(Object value, int row, int col) {
	    	if(col==0) {
	    		if(selected != null && selected.equals(nodeRegions.get(row)))
	    			selected = null;
	    		else selected = nodeRegions.get(row);
	    	}
	    	fireTableDataChanged();
		}
	}

 
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		String str = e.getActionCommand().toString();
		if (str.equals("Add"))
		{
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	try {
						UIManager.setLookAndFeel(new InfoNodeLookAndFeel(
								InfoNodeLookAndFeelThemes.getSoftGrayTheme()));
						System.out.println("Launching City.Net...");
						LayersUpdate f = new LayersUpdate();
						f.pack();
						f.setLocationRelativeTo(null);
						f.setVisible(true);
						//f.openCityCommand(CityNet.getInstance().getCity());
	            	} catch(Exception e) {
	            		JOptionPane.showMessageDialog(null, 
	            				"A fatal exception of type " + 
	            				e.getClass().getSimpleName() + "occurred while " + 
	            				"launching City.Net.\nPlease consult the stack " + 
	            				"trace for more information.");
						e.printStackTrace();
					}
	            }
			});;
			
		}
		
		
		
	}
}

	

