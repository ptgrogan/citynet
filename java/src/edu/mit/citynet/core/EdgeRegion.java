/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

import edu.mit.citynet.CityNet;
import edu.mit.citynet.util.CityNetIcon;

/**
 * The EdgeRegion class specifies a spatial area over which edges should be
 * generated. There are several types of regions, including:
 * POLYLINE_PERIMETER (link edges between adjacent vertices),
 * ORTHOGONAL_NEIGHBORS (link edges between all orthogonal nodes inside
 * region, assuming square grid spacing), ALL_NEIGHBORS (link edges between
 * all nodes, orthogonal and diagonal, inside region, assuming square grid
 * spacing), FULLY_CONNECTED (link edges between all nodes contained within
 * region.
 * 
 * The EdgeRegion class was created to be able to specify edges without
 * relying on cellular definitions.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class EdgeRegion extends AbstractRegion {
	/**
	 * The NodeRegionType enumeration. Defines the set of possible regions that
	 * can be specified, each with differing node generation behaviors.
	 */
	public enum EdgeRegionType {
		
		UNDEFINED("Undefined"),
		POLYGON_ORTHOGONAL("Orthogonal"),
		POLYGON_ADJACENT("Adjacent"),
		POLYGON_CONNECTED("Connected"),
		POLYLINE("Polyline"),
		POLYPOINT("Polypoint");
		
		private String name;
		
		/**
		 * Instantiates a new edge region type.
		 *
		 * @param name the name
		 */
		private EdgeRegionType(String name) {
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
			// TODO: differentiate icons for three types of polygon regions
			case POLYGON_ORTHOGONAL: return CityNetIcon.POLYGON_REGION.getIcon();
			case POLYGON_ADJACENT: return CityNetIcon.POLYGON_REGION.getIcon();
			case POLYGON_CONNECTED: return CityNetIcon.POLYGON_REGION.getIcon();
			case POLYLINE: return CityNetIcon.POLYLINE_REGION.getIcon();
			case POLYPOINT: return CityNetIcon.POLYPOINT_REGION.getIcon();
			default: return CityNetIcon.UNDEFINED_REGION.getIcon();
			}
		}
		
		/**
		 * Gets the edge region type.
		 *
		 * @param name the name
		 * @return the edge region type
		 */
		public static EdgeRegionType getEdgeRegionType(String name) {
			for(EdgeRegionType t : EdgeRegionType.values()) {
				if(t.getName().toLowerCase().equals(name.toLowerCase())) return t;
			}
			return UNDEFINED;
		}
	}
	
	private int id;
	private EdgeRegionType edgeRegionType;
	private List<Layer> layers;
	private EdgeType edgeType;
	private boolean directed;
	
	/**
	 * Instantiates a new edge region.
	 */
	public EdgeRegion() {
		super();
		setDescription("New Edge Region");
		layers = new ArrayList<Layer>();
	}
	
	/**
	 * Generate edges.
	 *
	 * @param system the system
	 */
	public void generateEdges(CitySystem system) {
		GeometryFactory gf = CityNet.getInstance().getGeometryFactory();
		Vector<Node> nodes = null;
		CoordinateList coords = getCoordinateList();
		switch(edgeRegionType) {
		case POLYLINE:
			nodes = new Vector<Node>();
			// TODO: processes nodes in uncertain order, may impact 
			// connectivity of resulting edges
			for(int i=0;i<layers.size()-1;i++) {
				for(Node node : system.getNodes()) {
					LineString line = gf.createLineString(
							new Coordinate[]{coords.getCoordinate(i),
									coords.getCoordinate(i+1)});
					if(node.getLayer().equals(layers.get(i)) 
							&& node.getCell().intersectsLine(line)) {
						nodes.addElement(node);
					}
				}
			}
			for(int i=0;i<nodes.size()-1;i++) {
				if(!nodes.get(i).equals(nodes.get(i+1))) {
					createEdge(system,nodes.get(i),nodes.get(i+1));
				}
			}
			break;
		case POLYPOINT:
			nodes = new Vector<Node>();
			for(int i=0;i<layers.size();i++) {
				for(Node node : system.getNodes()) {
					if(node.getLayer().equals(layers.get(i))
							&& node.getCell().containsPoint(gf.createPoint(
									coords.getCoordinate(i)))) {
						nodes.addElement(node);
					}
				}
			}
			for(int i=0;i<nodes.size()-1;i++) {
				if(!nodes.get(i).equals(nodes.get(i+1))) {
					createEdge(system,nodes.get(i),nodes.get(i+1));
				}
			}
			break;
		case POLYGON_ORTHOGONAL:
			nodes = new Vector<Node>();
			for(Node node : system.getNodes()) {
				if(node.getLayer().equals(layers.get(0))
						&& this.containsPolygon(node.getCell().getPolygon(), 0.5)) {
					nodes.add(node);
				}
			}
			for(int i=0;i<nodes.size();i++) {
				for(int j=i+1;j<nodes.size();j++) {
					Node origin = nodes.get(i);
					Node destination = nodes.get(j);
					if(origin.getCell().getPolygon().intersection(
							destination.getCell().getPolygon()) 
							instanceof LineString) {
						createEdge(system,origin,destination);
					}
				}
			}
			break;
		case POLYGON_ADJACENT:
			nodes = new Vector<Node>();
			for(Node node : system.getNodes()) {
				if(node.getLayer().equals(layers.get(0))
						&& this.containsPolygon(node.getCell().getPolygon(), 0.5)) {
					nodes.add(node);
				}
			}
			for(int i=0;i<nodes.size();i++) {
				for(int j=i+1;j<nodes.size();j++) {
					Node origin = nodes.get(i);
					Node destination = nodes.get(j);
					if(origin.getCell().getPolygon().intersection(
							destination.getCell().getPolygon()) 
							instanceof LineString || 
							origin.getCell().getPolygon().intersection(
									destination.getCell().getPolygon()) 
									instanceof Point) {
						createEdge(system,origin,destination);
					}
				}
			}
			break;
		case POLYGON_CONNECTED:
			nodes = new Vector<Node>();
			for(Node node : system.getNodes()) {
				if(node.getLayer().equals(layers.get(0))
						&& this.containsPolygon(node.getCell().getPolygon(), 0.5)) {
					nodes.add(node);
				}
			}
			for(int i=0;i<nodes.size();i++) {
				for(int j=i+1;j<nodes.size();j++) {
					Node origin = nodes.get(i);
					Node destination = nodes.get(j);
					if(!origin.equals(destination)) {
						createEdge(system,origin,destination);
					}
				}
			}
			break;
		case UNDEFINED:
			throw new IllegalStateException("Unknown or undefined edge region type.");
		}
	}
	
	/**
	 * Creates the edge.
	 *
	 * @param system the system
	 * @param origin the origin
	 * @param destination the destination
	 */
	private void createEdge(CitySystem system, Node origin, Node destination) {
		Edge edge = new Edge();
		edge.setId(CityNet.getInstance().getNextEdgeId());
		edge.setOrigin(origin);
		edge.setDestination(destination);
		edge.setDirected(directed);
		edge.setEdgeType(edgeType);
		system.addEdge(edge);
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
	 * Gets the edge region type.
	 *
	 * @return the edge region type
	 */
	public EdgeRegionType getEdgeRegionType() {
		return edgeRegionType;
	}

	/**
	 * Sets the edge region type.
	 *
	 * @param edgeRegionType the new edge region type
	 */
	public void setEdgeRegionType(EdgeRegionType edgeRegionType) {
		this.edgeRegionType = edgeRegionType;
	}

	/**
	 * Gets the edge type.
	 *
	 * @return the edge type
	 */
	public EdgeType getEdgeType() {
		return edgeType;
	}

	/**
	 * Sets the edge type.
	 *
	 * @param edgeType the new edge type
	 */
	public void setEdgeType(EdgeType edgeType) {
		this.edgeType = edgeType;
	}

	/**
	 * Checks if is directed.
	 *
	 * @return true, if is directed
	 */
	public boolean isDirected() {
		return directed;
	}

	/**
	 * Sets the directed.
	 *
	 * @param directed the new directed
	 */
	public void setDirected(boolean directed) {
		this.directed = directed;
	}

	/**
	 * Gets the layers.
	 *
	 * @return the layers
	 */
	public List<Layer> getLayers() {
		return layers;
	}

	/**
	 * Sets the layers.
	 *
	 * @param layers the new layers
	 */
	public void setLayers(List<Layer> layers) {
		this.layers = layers;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if(object instanceof EdgeRegion) {
			return id==((EdgeRegion)object).getId();
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "EdgeRegion" + id;
	}
}
