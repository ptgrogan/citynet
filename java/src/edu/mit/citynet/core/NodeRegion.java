/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.core;

import javax.swing.ImageIcon;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import edu.mit.citynet.CityNet;
import edu.mit.citynet.util.CityNetIcon;

/**
 * The NodeRegion class describes nodes that will be generated at cells over a 
 * region of space. It was created to be able to specify nodes without relying
 * on cellular definitions.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class NodeRegion extends AbstractRegion implements Cloneable {
	
	/**
	 * The NodeRegionType enumeration. Defines the set of possible regions that
	 * can be specified, each with differing node generation behaviors.
	 */
	public enum NodeRegionType {
		UNDEFINED("Undefined"),
		POLYGON("Polygon"),
		POLYLINE("Polyline"),
		POLYPOINT("Polypoint");

		private String name;
		
		/**
		 * Instantiates a new node region type.
		 *
		 * @param name the name
		 */
		private NodeRegionType(String name) {
			this.name = name;
		}
		
		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		public String toString() {
			return getName();
		}
		
		/**
		 * Gets the icon.
		 *
		 * @return the icon
		 */
		public ImageIcon getIcon() {
			switch(this) {
			case POLYGON: return CityNetIcon.POLYGON_REGION.getIcon();
			case POLYLINE: return CityNetIcon.POLYLINE_REGION.getIcon();
			case POLYPOINT: return CityNetIcon.POLYPOINT_REGION.getIcon();
			default: return CityNetIcon.UNDEFINED_REGION.getIcon();
			}
		}
		
		/**
		 * Gets the node region type.
		 *
		 * @param name the name
		 * @return the node region type
		 */
		public static NodeRegionType getNodeRegionType(String name) {
			for(NodeRegionType t : NodeRegionType.values()) {
				if(t.getName().toLowerCase().equals(name.toLowerCase())) return t;
			}
			return UNDEFINED;
		}
	}
	private int id;
	private NodeRegionType nodeRegionType;
	private Layer layer;
	private NodeType nodeType;
	
	/**
	 * Instantiates a new node region.
	 */
	public NodeRegion() {
		super();
		setDescription("New Node Region");
	}
	
	/**
	 * Generate nodes.
	 *
	 * @param system the system
	 * @return the set of generated nodes
	 */
	public void generateNodes(CitySystem system) {
		GeometryFactory gf = CityNet.getInstance().getGeometryFactory();
		switch(nodeRegionType) {
		case POLYGON:
			for(Cell cell : CityNet.getInstance().getCity().getCells()) {
				if(containsPolygon(cell.getPolygon(), 0.5)) {
					createNode(system,cell);
				}
			}
			break;
		case POLYLINE:
			LineString line = gf.createLineString(getCoordinateList().toCoordinateArray());
			for(Cell cell : CityNet.getInstance().getCity().getCells()) {
				if(cell.intersectsLine(line)) {
					createNode(system,cell);
				}
			}
			break;
		case POLYPOINT:
			for(Cell cell : CityNet.getInstance().getCity().getCells()) {
				for(Coordinate coord : getCoordinateList().toCoordinateArray()) {
					if(cell.containsPoint(gf.createPoint(coord))) {
						createNode(system,cell);
						break;
					}
				}
			}
			break;
		case UNDEFINED:
			throw new IllegalStateException("Unknown or undefined node region type.");
		}
	}
	
	private void createNode(CitySystem system, Cell cell) {
		for(Node n : system.getNodes()) {
			if(n.getCell().equals(cell) && n.getLayer().equals(layer)) {
				return; // node exists at same location
			}
		}
		Node node = new Node();
		node.setId(CityNet.getInstance().getNextNodeId());
		node.setCell(cell);
		node.setLayer(layer);
		node.setNodeType(nodeType);
		system.addNode(node);
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the node region type.
	 *
	 * @return the node region type
	 */
	public NodeRegionType getNodeRegionType() {
		return nodeRegionType;
	}

	/**
	 * Sets the node region type.
	 *
	 * @param nodeRegiontype the new node region type
	 */
	public void setNodeRegionType(NodeRegionType nodeRegionType) {
		this.nodeRegionType = nodeRegionType;
	}

	/**
	 * Gets the layer.
	 *
	 * @return the layer
	 */
	public Layer getLayer() {
		return layer;
	}

	/**
	 * Sets the layer.
	 *
	 * @param layer the new layer
	 */
	public void setLayer(Layer layer) {
		this.layer = layer;
	}

	/**
	 * Gets the node type.
	 *
	 * @return the node type
	 */
	public NodeType getNodeType() {
		return nodeType;
	}

	/**
	 * Sets the node type.
	 *
	 * @param nodeType the new node type
	 */
	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if(object instanceof NodeRegion) {
			return id==((NodeRegion)object).getId();
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "NodeRegion" + id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public NodeRegion clone() {
		NodeRegion clone = new NodeRegion();
		clone.setId(0);
		clone.setDescription(getDescription() + " (Copy)");
		clone.setLayer(getLayer());
		clone.setNodeRegionType(getNodeRegionType());
		clone.setNodeType(getNodeType());
		clone.setCoordinateList(getCoordinateList());
		return clone;
	}
}