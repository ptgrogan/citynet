package edu.mit.citynet.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.mit.citynet.core.*;

public class BuildingEdgeTypePanel extends BuildingPanel implements ListSelectionListener
{
	private static final long serialVersionUID = 4707459231955143756L;
	
	private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private NodeTypeTableModel typesmodel;
	private AttributeTableModel attributesmodel;
    
    public BuildingEdgeTypePanel ()
    {
    	City city = CityNet.getInstance().getCity();
    	String headers[] = {"Name","Description","Color"};
    	String headers1[] = {"Name", "Description","Units","Bounds","Value"};
    	typesmodel = new NodeTypeTableModel(headers);
    	typesmodel.setTypes(city);
    	attributesmodel = new AttributeTableModel(headers1);
    	EdgeType nt;
    	
    	if (typesmodel.getRowCount() > 0)
    	{
    		nt = typesmodel.getElement(0);
    		attributesmodel.setAttributes(nt);
    		
    	}
    	initComponents();
    	
    }    
    
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable(typesmodel);
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();       
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        
        jScrollPane1.setViewportView(jTable1);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.getSelectionModel().addListSelectionListener(this);

        
        jButton1.setText("Add"); // NOI18N
        jButton2.setText("Edit"); // NOI18N
        jButton3.setText("Delete"); // NOI18N
        
        if (typesmodel.getRowCount() > 0)
        {
        	 jTable2 = new javax.swing.JTable(attributesmodel);
        }
        else{
        	jTable2 = new javax.swing.JTable();
        	jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        }
        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable2.setName("jTable2"); // NOI18N
        jScrollPane2.setViewportView(jTable2);

        jLabel1.setText("Node Types:"); // NOI18N

        jLabel2.setText("Node Type Attributes:"); // NOI18N

        jButton4.setText("Add"); // NOI18N
        
        jButton5.setText("Edit"); // NOI18N
        
        jButton6.setText("Delete"); // NOI18N
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(114, 114, 114)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3))
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(229, 229, 229)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(9, 9, 9)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jButton3))
                        .addGap(6, 6, 6))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton4)
                        .addComponent(jButton5))
                    .addComponent(jButton6))
                .addContainerGap(19, Short.MAX_VALUE))
        );
    }
    
    private class NodeTypeTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 5386287393549485163L;
		private List<EdgeType> nodeTypes;
		private EdgeType selected;
		private String headers[];
		
		/**
		 * Instantiates a new node region table model.
		 */
		public NodeTypeTableModel(String cols[]) {
			super();
			nodeTypes = new ArrayList<EdgeType>();
			headers  = cols;			
		}
		
		/**
		 * Sets the node regions.
		 *
		 * @param city the new node regions
		 */
		public void setTypes(City city) {
			nodeTypes.clear();
			selected = null;
			for(CitySystem system : city.getSystems()) {
				nodeTypes.addAll(system.getEdgeTypes());
			}
			fireTableDataChanged();
		}
		
		/**
		 * Gets the selected node region.
		 *
		 * @return the selected node region
		 */
		public EdgeType getSelectedNodeRegion() {
			return selected;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			return nodeTypes.size();
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return headers.length;
		}
		
		public String getColumnName(int col)
		{
			return headers[col];
		}
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		
		public EdgeType getElement(int index)
		{
			return nodeTypes.get(index);
		}
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(columnIndex==0)
			{
				return nodeTypes.get(rowIndex).getName();
			}
			else if (columnIndex == 1)
			{
				return nodeTypes.get(rowIndex).getDescription();
			}
			else
			{
				return nodeTypes.get(rowIndex).getColor();
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
	    		if(selected != null && selected.equals(nodeTypes.get(row)))
	    			selected = null;
	    		else selected = nodeTypes.get(row);
	    	}
	    	fireTableDataChanged();
		}
	}
    
    private class AttributeTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 5386287393549485163L;
		private List<EdgeTypeAttribute> nodeAttributes;
		private EdgeTypeAttribute selected;
		private String headers[];
		
		/**
		 * Instantiates a new node region table model.
		 */
		public AttributeTableModel(String cols[]) {
			super();
			nodeAttributes = new ArrayList<EdgeTypeAttribute>();
			headers  = cols;			
		}
		
		/**
		 * Sets the node regions.
		 *
		 * @param city the new node regions
		 */
		public void setAttributes(EdgeType nt) {
			nodeAttributes.clear();
			selected = null;
			nodeAttributes.addAll(nt.getAttributes());
			fireTableDataChanged();
		}
		
		/**
		 * Gets the selected node region.
		 *
		 * @return the selected node region
		 */
		public EdgeTypeAttribute getSelectedNodeRegion() {
			return selected;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			return nodeAttributes.size();
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return headers.length;
		}
		
		public String getColumnName(int col)
		{
			return headers[col];
		}
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		
		public EdgeTypeAttribute getElement(int index)
		{
			return nodeAttributes.get(index);
		}
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(columnIndex==0)
			{
				return nodeAttributes.get(rowIndex).getName();
			}
			else if (columnIndex == 1)
			{
				return nodeAttributes.get(rowIndex).getDescription();
			}
			else if (columnIndex == 2)
			{
				return nodeAttributes.get(rowIndex).getUnits();
			}
			else if (columnIndex == 3)
			{
				return nodeAttributes.get(rowIndex).getBounds();
			}
			else
			{
				return nodeAttributes.get(rowIndex).getValue();
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
	    		if(selected != null && selected.equals(nodeAttributes.get(row)))
	    			selected = null;
	    		else selected = nodeAttributes.get(row);
	    	}
	    	fireTableDataChanged();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
	
		int index = e.getFirstIndex();
		EdgeType nt = typesmodel.getElement(index);
		attributesmodel.setAttributes(nt);
			
	}
}
