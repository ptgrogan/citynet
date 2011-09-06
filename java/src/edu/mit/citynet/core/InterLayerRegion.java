package edu.mit.citynet.core;

import com.vividsolutions.jts.geom.GeometryFactory;

import edu.mit.citynet.CityNet;

/**
 * The InterLayerRegion generates edges between functional layers.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class InterLayerRegion extends Region implements Cloneable {
	private Layer originLayer, destinationLayer;

	/**
	 * Instantiates a new inter-layer region.
	 */
	public InterLayerRegion() {
		super();
		setDescription("New Inter-layer Region");
	}

	/**
	 * Gets the origin layer.
	 *
	 * @return the origin layer
	 */
	public Layer getOriginLayer() {
		return originLayer;
	}

	/**
	 * Sets the origin layer.
	 *
	 * @param originLayer the new origin layer
	 */
	public void setOriginLayer(Layer originLayer) {
		this.originLayer = originLayer;
	}

	/**
	 * Gets the destination layer.
	 *
	 * @return the destination layer
	 */
	public Layer getDestinationLayer() {
		return destinationLayer;
	}

	/**
	 * Sets the destination layer.
	 *
	 * @param destinationLayer the new destination layer
	 */
	public void setDestinationLayer(Layer destinationLayer) {
		this.destinationLayer = destinationLayer;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.citynet.core.Region#generateRegion(edu.mit.citynet.core.CitySystem)
	 */
	public void generateRegion(CitySystem system) {
		GeometryFactory gf = CityNet.getInstance().getGeometryFactory();
		
		Node[] originNodes = new Node[getCoordinateList().size()];
		Node[] destinationNodes = new Node[getCoordinateList().size()];
		for(int i=0;i<getCoordinateList().size();i++) {
			for(Node node : system.getNodes()) {
				if(node.getLayer().equals(originLayer)
						&& node.getCell().containsPoint(gf.createPoint(
								getCoordinateList().getCoordinate(i)))) {
					originNodes[i] = node;
				} else if(node.getLayer().equals(destinationLayer)
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
		edge.setEdgeDirection(getEdgeDirection());
		edge.setEdgeType(getEdgeType());
		system.addEdge(edge);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public InterLayerRegion clone() {
		InterLayerRegion clone = new InterLayerRegion();
		clone.setId(0);
		clone.setDescription(getDescription() + " (Copy)");
		clone.setOriginLayer(getOriginLayer());
		clone.setDestinationLayer(getDestinationLayer());
		clone.setEdgeType(getEdgeType());
		clone.setEdgeDirection(getEdgeDirection());
		clone.setCoordinateList(getCoordinateList());
		return clone;
	}
}
