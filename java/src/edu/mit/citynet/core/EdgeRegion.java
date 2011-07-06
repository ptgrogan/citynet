package edu.mit.citynet.core;

import java.util.Vector;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import edu.mit.citynet.CityNet;

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
		UNDEFINED,POLYGON_ORTHOGONAL,POLYGON_ADJACENT,POLYGON_CONNECTED,
		POLYLINE,POLYPOINT;
	}
	private int id;
	private EdgeRegionType edgeRegionType;
	private Vector<Layer> layers;
	private EdgeType edgeType;
	private boolean directed;
	private String description;
	
	/**
	 * Instantiates a new edge region.
	 */
	public EdgeRegion() {
		layers = new Vector<Layer>();
	}
	
	/**
	 * Generate edges.
	 *
	 * @param system the system
	 */
	public void generateEdges(CitySystem system) {
		GeometryFactory gf = CityNet.getInstance().getGeometryFactory();
		Coordinate[] coords = getPolygon().getCoordinates();
		Vector<Node> nodes = null;
		switch(edgeRegionType) {
		case POLYLINE:
			nodes = new Vector<Node>();
			// TODO: processes nodes in uncertain order
			for(int i=0;i<layers.size()-1;i++) {
				for(Node node : system.getNodes()) {
					LineString line = gf.createLineString(
							new Coordinate[]{coords[i],coords[i+1]});
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
							&& node.getCell().containsPoint(gf.createPoint(coords[i]))) {
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
					int numCommonCoords = 0;
					for(Coordinate c1 : origin.getCell().getPolygon().getCoordinates()) {
						for(Coordinate c2 : destination.getCell().getPolygon().getCoordinates()) {
							if(c1.equals(c2)) numCommonCoords++;
						}
					}
					if(!origin.equals(destination) && numCommonCoords==2) {
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
					int numCommonCoords = 0;
					for(Coordinate c1 : origin.getCell().getPolygon().getCoordinates()) {
						for(Coordinate c2 : destination.getCell().getPolygon().getCoordinates()) {
							if(c1.equals(c2)) numCommonCoords++;
						}
					}
					if(!origin.equals(destination) && numCommonCoords>=2) {
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
			throw new RuntimeException("Unknown or undefined edge region type.");
		}
	}
	
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
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the layers.
	 *
	 * @return the layers
	 */
	public Vector<Layer> getLayers() {
		return layers;
	}

	/**
	 * Sets the layers.
	 *
	 * @param layers the new layers
	 */
	public void setLayers(Vector<Layer> layers) {
		this.layers = layers;
	}
}
