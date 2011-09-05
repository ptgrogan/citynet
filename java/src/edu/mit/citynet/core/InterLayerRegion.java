package edu.mit.citynet.core;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.GeometryFactory;

import edu.mit.citynet.CityNet;

/**
 * The InterLayerRegion generates edges between functional layers.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class InterLayerRegion extends AbstractRegion implements Cloneable {
	private int id;
	private List<Layer> originLayers, destinationLayers;
	private EdgeType edgeType;
	private EdgeDirection edgeDirection;

	/**
	 * Instantiates a new inter layer region.
	 */
	public InterLayerRegion() {
		super();
		originLayers = new ArrayList<Layer>();
		destinationLayers = new ArrayList<Layer>();
		setDescription("New Inter-layer Region");
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
	 * Gets the origin layers.
	 *
	 * @return the origin layers
	 */
	public List<Layer> getOriginLayers() {
		return originLayers;
	}

	/**
	 * Sets the origin layers.
	 *
	 * @param originLayers the new origin layers
	 */
	public void setOriginLayers(List<Layer> originLayers) {
		this.originLayers = originLayers;
	}

	/**
	 * Gets the destination layers.
	 *
	 * @return the destination layers
	 */
	public List<Layer> getDestinationLayers() {
		return destinationLayers;
	}

	/**
	 * Sets the destination layers.
	 *
	 * @param destinationLayers the new destination layers
	 */
	public void setDestinationLayers(List<Layer> destinationLayers) {
		this.destinationLayers = destinationLayers;
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
	 * Gets the edge direction.
	 *
	 * @return the edge direction
	 */
	public EdgeDirection getEdgeDirection() {
		return edgeDirection;
	}

	/**
	 * Sets the edge direction.
	 *
	 * @param edgeDirection the new edge direction
	 */
	public void setEdgeDirection(EdgeDirection edgeDirection) {
		this.edgeDirection = edgeDirection;
	}
	
	/**
	 * Generates the nodes and edges within a region.
	 *
	 * @param system the system
	 */
	public void generateRegion(CitySystem system) {
		GeometryFactory gf = CityNet.getInstance().getGeometryFactory();
		
		Node[] originNodes = new Node[getCoordinateList().size()];
		Node[] destinationNodes = new Node[getCoordinateList().size()];
		for(int i=0;i<getCoordinateList().size();i++) {
			for(Node node : system.getNodes()) {
				if(node.getLayer().equals(originLayers.get(i))
						&& node.getCell().containsPoint(gf.createPoint(
								getCoordinateList().getCoordinate(i)))) {
					originNodes[i] = node;
				} else if(node.getLayer().equals(destinationLayers.get(i))
						&& node.getCell().containsPoint(gf.createPoint(
								getCoordinateList().getCoordinate(i)))) {
					destinationNodes[i] = node;
				}
				if(originNodes[i]!=null && destinationNodes[i]!=null) break;
			}
		}
		for(int i=0;i<getCoordinateList().size()-1;i++) {
			if(originNodes[i] != null 
					&& destinationNodes[i] != null 
					&& !originNodes[i].equals(destinationNodes[i])) {
				createEdge(system,originNodes[i],destinationNodes[i]);
			}
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
		edge.setEdgeDirection(edgeDirection);
		edge.setEdgeType(edgeType);
		system.addEdge(edge);
	}
}
