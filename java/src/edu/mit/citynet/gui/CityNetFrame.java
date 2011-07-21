package edu.mit.citynet.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CityNet;

/**
 * CityNetFrame class.
 * 
 * The core graphical user interface (GUI) component.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CityNetFrame extends JFrame {
	private static final long serialVersionUID = -4707459231955143750L;
	CityNetMenuBar menuBar;
	JPanel backgroundPanel;
	CityPanel cityPanel;
	private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTabbedPane jTabbedPane6;
    private javax.swing.JTabbedPane jTabbedPane7;
    private javax.swing.JTabbedPane jTabbedPane8;
    private javax.swing.JTabbedPane jTabbedPane9;
    private javax.swing.JTextField jTextField1;
	
	/**
	 * Instantiates a new city net frame.
	 */
	public CityNetFrame() {
		super("City.Net");
		menuBar = new CityNetMenuBar();
		setJMenuBar(menuBar);
		//backgroundPanel = new BackgroundPanel();
		//setContentPane(backgroundPanel);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exitCommand();
			}
		});		
		
		initComponents(CityNet.getInstance().getCity());
	}
	
	public void newCityCommand() {
		System.out.println("New City Command");
		if(cityPanel != null) {
			closeCityCommand();
		}
		City city = new City();
		city.setName("New City");
		cityPanel = new CityPanel(city);
		setContentPane(cityPanel);
		validate();
		JTable t1 = new JTable();
		
	}
	
	public void openCityCommand(City city) {
		System.out.println("Open City Command");
		if(cityPanel != null) {
			closeCityCommand();
		}
		cityPanel = new CityPanel(city);
		setContentPane(cityPanel);
		validate();
		pack();
	}
	
	public void closeCityCommand() {
		System.out.println("Close City Command");
		cityPanel = null;
		setContentPane(backgroundPanel);
		validate();
	}
	
	public void exitCommand() {
		System.out.println("Exit Command");
		if(cityPanel != null) {
			closeCityCommand();
		}
		dispose();
	}

	 private void initComponents(City city) {

	        jTextField1 = new javax.swing.JTextField();
	        jTabbedPane1 = new javax.swing.JTabbedPane();
	        jTabbedPane2 = new javax.swing.JTabbedPane();
	        jTabbedPane5 = new javax.swing.JTabbedPane();
	        jPanel1 = new javax.swing.JPanel();
	        jPanel2 = new javax.swing.JPanel();
	        jPanel3 = new javax.swing.JPanel();
	        jPanel4 = new javax.swing.JPanel();
	        jPanel5 = new javax.swing.JPanel();
	        jTabbedPane6 = new javax.swing.JTabbedPane();
	        jTabbedPane7 = new javax.swing.JTabbedPane();
	        jTabbedPane8 = new javax.swing.JTabbedPane();
	        jTabbedPane9 = new javax.swing.JTabbedPane();
	        jTabbedPane3 = new javax.swing.JTabbedPane();
	        jTabbedPane4 = new javax.swing.JTabbedPane();
	        jLabel1 = new javax.swing.JLabel();
	        jButton1 = new javax.swing.JButton();
	        jMenuBar1 = new javax.swing.JMenuBar();
	        jMenu1 = new javax.swing.JMenu();
	        jMenu2 = new javax.swing.JMenu();

	        
	        jTextField1.setText(city.getName()); // NOI18N
	        jTextField1.setName("jTextField1"); // NOI18N

	        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	        setTitle("City.NET"); // NOI18N
	        setName("Form"); // NOI18N

	        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

	        jTabbedPane2.setName("jTabbedPane2"); // NOI18N

	        jTabbedPane5.setName("jTabbedPane5"); // NOI18N

	        //jPanel1.setName("jPanel1"); // NOI18N
	        BuildingLayersPanel blbpanel = new BuildingLayersPanel();
	        
	        jTabbedPane5.addTab("Layers",blbpanel); // NOI18N

	        //jPanel2.setName("jPanel2"); // NOI18N
	        BuildingNodeTypePanel bntpanel = new BuildingNodeTypePanel();
	        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(bntpanel);
	        jPanel2.setLayout(jPanel2Layout);
	        jPanel2Layout.setHorizontalGroup(
	            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGap(0, 544, Short.MAX_VALUE)
	        );
	        jPanel2Layout.setVerticalGroup(
	            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGap(0, 193, Short.MAX_VALUE)
	        );

	        jTabbedPane5.addTab("Node Types", bntpanel); // NOI18N

	        //jPanel3.setName("jPanel3"); // NOI18N

	        BuildingNodeRegionsPanel bnrPanel = new BuildingNodeRegionsPanel();
	        
	        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(bnrPanel);
	        jPanel3.setLayout(jPanel3Layout);
	        jPanel3Layout.setHorizontalGroup(
	            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGap(0, 544, Short.MAX_VALUE)
	        );
	        jPanel3Layout.setVerticalGroup(
	            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGap(0, 193, Short.MAX_VALUE)
	        );

	        jTabbedPane5.addTab("Node Regions", bnrPanel); // NOI18N

	        //jPanel4.setName("jPanel4"); // NOI18N
	        BuildingEdgeTypePanel betpanel = new BuildingEdgeTypePanel();
	        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(betpanel);
	        jPanel4.setLayout(jPanel4Layout);
	        jPanel4Layout.setHorizontalGroup(
	            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGap(0, 544, Short.MAX_VALUE)
	        );
	        jPanel4Layout.setVerticalGroup(
	            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGap(0, 193, Short.MAX_VALUE)
	        );

	        jTabbedPane5.addTab("Edge Types", betpanel); // NOI18N

	        jPanel5.setName("jPanel5"); // NOI18N

	        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
	        jPanel5.setLayout(jPanel5Layout);
	        jPanel5Layout.setHorizontalGroup(
	            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGap(0, 544, Short.MAX_VALUE)
	        );
	        jPanel5Layout.setVerticalGroup(
	            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGap(0, 193, Short.MAX_VALUE)
	        );

	        jTabbedPane5.addTab("Edge Regions", jPanel5); // NOI18N

	        jTabbedPane2.addTab("Building", jTabbedPane5); // NOI18N

	        jTabbedPane6.setName("jTabbedPane6"); // NOI18N
	        jTabbedPane2.addTab("Transportation", jTabbedPane6); // NOI18N

	        jTabbedPane7.setName("jTabbedPane7"); // NOI18N
	        jTabbedPane2.addTab("Energy", jTabbedPane7); // NOI18N

	        jTabbedPane8.setName("jTabbedPane8"); // NOI18N
	        jTabbedPane2.addTab("Water", jTabbedPane8); // NOI18N

	        jTabbedPane9.setName("jTabbedPane9"); // NOI18N
	        jTabbedPane2.addTab("Waste", jTabbedPane9); // NOI18N

	        jTabbedPane1.addTab("Synthesis", jTabbedPane2); // NOI18N

	        jTabbedPane3.setName("jTabbedPane3"); // NOI18N
	        jTabbedPane1.addTab("Analysis", jTabbedPane3); // NOI18N

	        jTabbedPane4.setName("jTabbedPane4"); // NOI18N
	        jTabbedPane1.addTab("Evaluation", jTabbedPane4); // NOI18N

	        jLabel1.setText("City:"); // NOI18N
	        jLabel1.setName("jLabel1"); // NOI18N

	        jButton1.setText("Retrieve"); // NOI18N
	        jButton1.setName("CityRetrieve"); // NOI18N

	        jMenuBar1.setName("jMenuBar1"); // NOI18N

	        jMenu1.setText("File"); // NOI18N
	        jMenu1.setName("jMenu1"); // NOI18N
	        jMenuBar1.add(jMenu1);

	        jMenu2.setText("Edit"); // NOI18N
	        jMenu2.setName("jMenu2"); // NOI18N
	        jMenuBar1.add(jMenu2);

	        setJMenuBar(jMenuBar1);

	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	      	        
	        layout.setHorizontalGroup(
	                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                .addGroup(layout.createSequentialGroup()
	                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                        .addGroup(layout.createSequentialGroup()
	                            .addGap(122, 122, 122)
	                            .addComponent(jLabel1)
	                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
	                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                            .addComponent(jButton1))
	                        .addGroup(layout.createSequentialGroup()
	                            .addContainerGap()
	                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)))
	                    .addContainerGap())
	            );
	            layout.setVerticalGroup(
	                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                .addGroup(layout.createSequentialGroup()
	                    .addContainerGap()
	                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                        .addComponent(jLabel1)
	                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addComponent(jButton1))
	                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
	                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addContainerGap())
	            );

	        pack();
	    }// </editor-fold>        

}


