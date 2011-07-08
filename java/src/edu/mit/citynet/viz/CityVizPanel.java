package edu.mit.citynet.viz;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import com.vividsolutions.jts.geom.Coordinate;

import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.NodeRegion;

/**
 * The CityVizPanel class produces a standard city-wide visualization of the 
 * different synthesis components (e.g. cell/node/edge regions).
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class CityVizPanel extends JPanel {
	private static final long serialVersionUID = 3994034732879260199L;
	
	private City city;
	private JLayeredPane layeredPane;
	private MapLayer mapLayer;
	private CellRegionLayer cellRegionLayer;
	private NodeRegionLayer nodeRegionLayer;
	private NodeRegionTableModel nodeRegionTableModel;
	
	/**
	 * Instantiates a new city viz panel.
	 *
	 * @param city the city
	 */
	public CityVizPanel(City city) {
		if (city==null) {
			throw new IllegalArgumentException("City cannot be null.");
		}
		this.city = city;
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		nodeRegionTableModel = new NodeRegionTableModel();
		nodeRegionTableModel.setNodeRegions(city);
		nodeRegionTableModel.addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				nodeRegionLayer.setNodeRegion(nodeRegionTableModel.getSelectedNodeRegion());
			}
		});
		JTable nodeRegionTable = new JTable(nodeRegionTableModel);
		nodeRegionTable.setTableHeader(null);
		nodeRegionTable.getColumnModel().getColumn(0).setMaxWidth(25);
		nodeRegionTable.setPreferredScrollableViewportSize(new Dimension(100,100));
		add(new JScrollPane(nodeRegionTable),c);
		c.gridx++;
		layeredPane = new JLayeredPane() {
			private static final long serialVersionUID = 8436113671918844606L;
			public void setBounds(int x, int y, int width, int height) {
				super.setBounds(x,y,width,height);
				mapLayer.setBounds(0,0,width,height);
				cellRegionLayer.setBounds(0,0,width,height);
				nodeRegionLayer.setBounds(0,0,width,height);
			}
		};
		layeredPane.setPreferredSize(new Dimension(250,250));
		add(layeredPane,c);
		mapLayer = new MapLayer(city.getImage());
		layeredPane.add(mapLayer, new Integer(1));
		cellRegionLayer = new CellRegionLayer(this,null);
		layeredPane.add(cellRegionLayer, new Integer(2));
		nodeRegionLayer = new NodeRegionLayer(this,null);
		layeredPane.add(nodeRegionLayer, new Integer(3));
	}
	
	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public City getCity() {
		return city;
	}
	
	/**
	 * Converts x-y coordinates to i-j coordinates.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the ij coordinates
	 */
	public int[] xy2ij(double x, double y) {
		int[] ij = new int[2];
		double scale = Math.min(((double)layeredPane.getWidth())/city.getImage().getWidth(null), 
				((double)layeredPane.getHeight())/city.getImage().getHeight(null));
		int pX = (int)(layeredPane.getWidth()-scale*city.getImage().getWidth(null))/2;
		int pY = (int)(layeredPane.getHeight()-scale*city.getImage().getHeight(null))/2;
		Coordinate[] imageCoords = city.getImagePolygon().getCoordinates();
		double bx1 = Math.min(imageCoords[0].x,Math.min(imageCoords[1].x,
				Math.min(imageCoords[2].x,imageCoords[3].x)));
		double by1 = Math.min(imageCoords[0].y,Math.min(imageCoords[1].y,
				Math.min(imageCoords[2].y,imageCoords[3].y)));
		double bx2 = Math.max(imageCoords[0].x,Math.max(imageCoords[1].x,
				Math.max(imageCoords[2].x,imageCoords[3].x)));
		double by2 = Math.max(imageCoords[0].y,Math.max(imageCoords[1].y,
				Math.max(imageCoords[2].y,imageCoords[3].y)));
		
		ij[0] = pX + (int)(scale*(x-bx1)/(bx2-bx1)*city.getImage().getWidth(null));
		ij[1] = pY + (int)(scale*(y-by1)/(by2-by1)*city.getImage().getHeight(null));
		return ij;
	}
	
	/**
	 * The NodeRegionTableModel class establishes a custom table model for use
	 * with the node regions table. The model allows the selection of up to one
	 * node region from the list of all regions.
	 */
	private class NodeRegionTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 5386287393549485163L;
		private List<NodeRegion> nodeRegions;
		private NodeRegion selected;
		
		/**
		 * Instantiates a new node region table model.
		 */
		public NodeRegionTableModel() {
			super();
			nodeRegions = new ArrayList<NodeRegion>();
		}
		
		/**
		 * Sets the node regions.
		 *
		 * @param city the new node regions
		 */
		public void setNodeRegions(City city) {
			nodeRegions.clear();
			selected = null;
			for(CitySystem system : city.getSystems()) {
				nodeRegions.addAll(system.getNodeRegions());
			}
			fireTableDataChanged();
		}
		
		/**
		 * Gets the selected node region.
		 *
		 * @return the selected node region
		 */
		public NodeRegion getSelectedNodeRegion() {
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
			return 2;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(columnIndex==0) {
				if(selected == null) return true;
				else if(nodeRegions.get(rowIndex).equals(selected)) return true;
				else return false;
			} else {
				return nodeRegions.get(rowIndex).getDescription();
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
			if(col==0) return Boolean.class;
			else return Object.class;
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
}
