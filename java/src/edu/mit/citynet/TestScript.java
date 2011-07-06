package edu.mit.citynet;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.infonode.gui.laf.InfoNodeLookAndFeel;
import net.infonode.gui.laf.InfoNodeLookAndFeelThemes;
import edu.mit.citynet.core.Cell;
import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.Edge;
import edu.mit.citynet.core.EdgeType;
import edu.mit.citynet.core.EdgeTypeAttribute;
import edu.mit.citynet.core.Layer;
import edu.mit.citynet.core.Node;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.core.NodeType;
import edu.mit.citynet.core.NodeTypeAttribute;
import edu.mit.citynet.gui.CityNetFrame;
import edu.mit.citynet.io.SpreadsheetTemplate;

public class TestScript {
	public static void main(String[] args) {
		SpreadsheetTemplate t = new SpreadsheetTemplate();
		t.setFilePath("synthesisExample.xls");
		try {
			t.readTemplate();
		} catch (IOException e) {
			e.printStackTrace();
		}
		City city = CityNet.getInstance().getCity();
		System.out.println("City Name: " + city.getName());
		System.out.println("City Latitude/Longitude: " + 
				city.getLatitude() + ", " + city.getLongitude());
		System.out.println("City Image Path: " + city.getImageFilePath());
		System.out.println("City Image Polygon: " + city.getImagePolygon());
		System.out.println("City Cells...");
		for(Cell cell : city.getCells()) {
			System.out.println("Cell ID: " + cell.getId() + 
					", Polygon: " + cell.getPolygon());
		}
		System.out.println("City Cell Regions...");
		for(CellRegion cellRegion : city.getCellRegions()) {
			System.out.println("Cell Region ID: " + cellRegion.getId() + 
					", Polygon: " + cellRegion.getPolygon() + 
					", Rows: " + cellRegion.getNumberRows() + 
					", Columns: " + cellRegion.getNumberColumns() +
					", Description: " + cellRegion.getDescription());
		}
		System.out.println("Systems...");
		for(CitySystem system : city.getSystems()) {
			System.out.println("System ID: " + system.getId() + 
					", Name: " + system.getName() + 
					", Description: " + system.getDescription());
			System.out.println("Layers in " + system.getName() + " System...");
			for(Layer layer : system.getLayers()) {
				System.out.println("Layer ID: " + layer.getId() + 
						", Name: " + layer.getName() + 
						", Description: " + layer.getDescription() +
						", Display Height: " + layer.getDisplayHeight() );
			}
			System.out.println("Node Types in " + system.getName() + " System...");
			for(NodeType nodeType : system.getNodeTypes()) {
				System.out.println("Node Type ID: " + nodeType.getId() + 
						", Name: " + nodeType.getName() + 
						", Description: " + nodeType.getDescription() +
						", Color: " + nodeType.getColor() );
				System.out.println("Node Type Attributes in " + nodeType.getName() + " Node Type...");
				for(NodeTypeAttribute nodeTypeAttribute : nodeType.getAttributes()) {
					System.out.println("Node Type Attribute ID: " + nodeTypeAttribute.getId() + 
							", Name: " + nodeTypeAttribute.getName() +  
							", Units: " + nodeTypeAttribute.getUnits() +  
							", Bounds: " + nodeTypeAttribute.getBounds() +  
							", Value: " + nodeTypeAttribute.getValue() + 
							", Description: " + nodeTypeAttribute.getDescription());
				}
			}
			System.out.println("Edge Types in " + system.getName() + " System...");
			for(EdgeType edgeType : system.getEdgeTypes()) {
				System.out.println("Edge Type ID: " + edgeType.getId() + 
						", Name: " + edgeType.getName() + 
						", Description: " + edgeType.getDescription() +
						", Color: " + edgeType.getColor() );
				System.out.println("Edge Type Attributes in " + edgeType.getName() + " Edge Type...");
				for(EdgeTypeAttribute edgeTypeAttribute : edgeType.getAttributes()) {
					System.out.println("Edge Type Attribute ID: " + edgeTypeAttribute.getId() + 
							", Name: " + edgeTypeAttribute.getName() +  
							", Units: " + edgeTypeAttribute.getUnits() +  
							", Bounds: " + edgeTypeAttribute.getBounds() +  
							", Value: " + edgeTypeAttribute.getValue() + 
							", Description: " + edgeTypeAttribute.getDescription());
				}
			}
			System.out.println("Nodes in " + system.getName() + " System...");
			for(Node node : system.getNodes()) {
				System.out.println("Node ID: " + node.getId() + 
						", Cell ID: " + node.getCell().getId() + 
						", Layer ID: " + node.getLayer().getId() +
						", Node Type ID: " + node.getNodeType().getId() );
			}
			System.out.println("Edges in " + system.getName() + " System...");
			for(Edge edge : system.getEdges()) {
				System.out.println("Edge ID: " + edge.getId() + 
						", Origin ID: " + edge.getOrigin().getId() + 
						", Destination ID: " + edge.getDestination().getId() +
						", Edge Type ID: " + edge.getEdgeType().getId() + 
						", Directed: " + edge.isDirected());
			}
			System.out.println("Node Regions in " + system.getName() + " System...");
			for(NodeRegion nodeRegion : system.getNodeRegions()) {
				System.out.println("Node Region ID: " + nodeRegion.getId() + 
						", Layer ID: " + nodeRegion.getLayer().getId() +
						", Node Type ID: " + nodeRegion.getNodeType().getId() +
						", Polygon: " + nodeRegion.getPolygon());
			}
		}
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
					UIManager.setLookAndFeel(new InfoNodeLookAndFeel(
							InfoNodeLookAndFeelThemes.getSoftGrayTheme()));
					System.out.println("Launching City.Net...");
					CityNetFrame f = new CityNetFrame();
					f.pack();
					f.setLocationRelativeTo(null);
					f.setVisible(true);
					f.openCityCommand(CityNet.getInstance().getCity());
            	} catch(Exception e) {
            		JOptionPane.showMessageDialog(null, 
            				"A fatal exception of type " + 
            				e.getClass().getSimpleName() + "occurred while " + 
            				"launching City.Net.\nPlease consult the stack " + 
            				"trace for more information.");
					e.printStackTrace();
				}
            }
		});
	}
}
