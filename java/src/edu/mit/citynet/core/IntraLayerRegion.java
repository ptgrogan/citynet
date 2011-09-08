package edu.mit.citynet.core;

import java.util.Vector;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

import edu.mit.citynet.CityNet;


/**
 * The Region class unifies previous concepts of node and edge regions
 * to generate nodes and edges within a system.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class IntraLayerRegion extends Region implements Cloneable {
	private int id;
	private Layer layer;
	private NodeType nodeType;
	private NodeGenerationType nodeGenerationType;
	private EdgeGenerationType edgeGenerationType;
	
	/**
	 * Instantiates a new intra-layer region.
	 */
	public IntraLayerRegion() {
		super();
		setDescription("New Intra-layer Region");
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
	
	/**
	 * Gets the node generation type.
	 *
	 * @return the node generation type
	 */
	public NodeGenerationType getNodeGenerationType() {
		return nodeGenerationType;
	}
	
	/**
	 * Sets the node generation type.
	 *
	 * @param nodeGenerationType the new node generation type
	 */
	public void setNodeGenerationType(NodeGenerationType nodeGenerationType) {
		this.nodeGenerationType = nodeGenerationType;
	}
	
	/**
	 * Gets the edge generation type.
	 *
	 * @return the edge generation type
	 */
	public EdgeGenerationType getEdgeGenerationType() {
		return edgeGenerationType;
	}
	
	/**
	 * Sets the edge generation type.
	 *
	 * @param edgeGenerationType the new edge generation type
	 */
	public void setEdgeGenerationType(EdgeGenerationType edgeGenerationType) {
		this.edgeGenerationType = edgeGenerationType;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public IntraLayerRegion clone() {
		IntraLayerRegion clone = new IntraLayerRegion();
		clone.setId(0);
		clone.setDescription(getDescription() + " (Copy)");
		clone.setLayer(getLayer());
		clone.setNodeGenerationType(getNodeGenerationType());
		clone.setNodeType(getNodeType());
		clone.setEdgeGenerationType(getEdgeGenerationType());
		clone.setEdgeType(getEdgeType());
		clone.setEdgeDirection(getEdgeDirection());
		clone.setCoordinateList(getCoordinateList());
		return clone;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.core.Region#generateRegion(edu.mit.citynet.core.CitySystem)
	 */
	public void generateRegion(CitySystem system) {
		GeometryFactory gf = CityNet.getInstance().getGeometryFactory();
		Vector<Node> newNodes = new Vector<Node>();
		switch(nodeGenerationType) {
		case POLYGON:
			for(Cell cell : CityNet.getInstance().getCity().getCells()) {
				if(containsPolygon(cell.getPolygon(), 0.5)) {
					newNodes.add(createNode(system,cell));
				}
			}
			break;
		case POLYLINE:
			LineString line = gf.createLineString(getCoordinateList().toCoordinateArray());
			for(Cell cell : CityNet.getInstance().getCity().getCells()) {
				if(cell.intersectsLine(line)) {
					newNodes.add(createNode(system,cell));
				}
			}
			break;
		case POLYPOINT:
			for(Cell cell : CityNet.getInstance().getCity().getCells()) {
				for(Coordinate coord : getCoordinateList().toCoordinateArray()) {
					if(cell.containsPoint(gf.createPoint(coord))) {
						newNodes.add(createNode(system,cell));
						break;
					}
				}
			}
			break;
		case NONE:
			// do nothing
		}

		Vector<Node> nodes;
		switch(edgeGenerationType) {
		case SEQUENTIAL:
			nodes = new Vector<Node>();
			for(int i=0;i<getCoordinateList().size();i++) {
				for(Node node : system.getNodes()) {
					if(node.getLayer().equals(layer)
							&& node.getCell().containsPoint(gf.createPoint(
									getCoordinateList().getCoordinate(i)))) {
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
		case ORTHOGONAL:
			nodes = getNodesForEdgeGeneration(nodeGenerationType,system);
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
		case ADJACENT:
			nodes = getNodesForEdgeGeneration(nodeGenerationType,system);
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
		case CONNECTED:
			nodes = getNodesForEdgeGeneration(nodeGenerationType,system);
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
		case NONE:
			// do nothing
		}
	}
	
	/**
	 * Gets the nodes for edge generation.
	 *
	 * @param nodeGenerationType the node generation type
	 * @param system the system
	 * @return the nodes for edge generation
	 */
	private Vector<Node> getNodesForEdgeGeneration(NodeGenerationType nodeGenerationType, CitySystem system) {
		GeometryFactory gf = CityNet.getInstance().getGeometryFactory();
		Vector<Node> nodes = new Vector<Node>();
		if(nodeGenerationType==NodeGenerationType.POLYGON 
				|| nodeGenerationType==NodeGenerationType.NONE) {
			for(Node node : system.getNodes()) {
				if(node.getLayer().equals(layer)
						&& this.containsPolygon(node.getCell().getPolygon(), 0.5)) {
					nodes.add(node);
				}
			}
		} else if(nodeGenerationType==NodeGenerationType.POLYLINE) {
			// TODO: special case if not polygon node generation but wants
			// orthogonal edge generation...
			LineString line = gf.createLineString(getCoordinateList().toCoordinateArray());
			for(Node node : system.getNodes()) {
				if(node.getLayer().equals(layer)
						&& node.getCell().intersectsLine(line)) {
					nodes.add(node);
				}
			}
		} else if(nodeGenerationType==NodeGenerationType.POLYPOINT) {
			// TODO: special case if not polygon node generation but wants
			// orthogonal edge generation...
			for(Node node : system.getNodes()) {
				if(node.getLayer().equals(layer)) {
					for(Coordinate coord : getCoordinateList().toCoordinateArray()) {
						if(node.getCell().containsPoint(gf.createPoint(coord))) {
							nodes.add(node);
							break;
						}
					}
				}
			}
		}
		return nodes;
	}
	
	/**
	 * Creates the node.
	 *
	 * @param system the system
	 * @param cell the cell
	 */
	private Node createNode(CitySystem system, Cell cell) {
		for(Node n : system.getNodes()) {
			if(n.getCell().equals(cell) && n.getLayer().equals(layer)) {
				return null; // node exists at same location
			}
		}
		Node node = new Node();
		node.setId(CityNet.getInstance().getNextNodeId());
		node.setCell(cell);
		node.setLayer(layer);
		node.setNodeType(nodeType);
		system.addNode(node);
		return node;
	}
	
	/**
	 * Creates the edge.
	 *
	 * @param system the system
	 * @param origin the origin
	 * @param destination the destination
	 */
	private Edge createEdge(CitySystem system, Node origin, Node destination) {
		Edge edge = new Edge();
		edge.setId(CityNet.getInstance().getNextEdgeId());
		edge.setOrigin(origin);
		edge.setDestination(destination);
		edge.setEdgeDirection(getEdgeDirection());
		edge.setEdgeType(getEdgeType());
		system.addEdge(edge);
		return edge;
	}
}