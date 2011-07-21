package edu.mit.citynet.io;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.GeometryFactory;

import edu.mit.citynet.CityNet;
import edu.mit.citynet.core.Cell;
import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.City;
import edu.mit.citynet.core.CitySystem;
import edu.mit.citynet.core.Edge;
import edu.mit.citynet.core.EdgeRegion;
import edu.mit.citynet.core.EdgeType;
import edu.mit.citynet.core.EdgeTypeAttribute;
import edu.mit.citynet.core.Layer;
import edu.mit.citynet.core.Node;
import edu.mit.citynet.core.NodeRegion;
import edu.mit.citynet.core.NodeType;
import edu.mit.citynet.core.NodeTypeAttribute;

public class SpreadsheetTemplate {
	public static final String CITY = "city";
	public static final int CITY_NAME = 0, CITY_LATITUDE = 1, 
		CITY_LONGITUDE = 2, CITY_ROTATION = 3, CITY_IMAGE_PATH = 4, 
		CITY_IMAGE_VERTICES_X = 5, CITY_IMAGE_VERTICES_Y = 6;
	public static final String SYSTEMS = "systems";
	public static final int SYSTEM_ID = 0, SYSTEM_NAME = 1,
		SYSTEM_DESCRIPTION = 2;
	public static final String NODE_TYPES = "node_types";
	public static final int NODE_TYPE_ID = 0, NODE_TYPE_SYSTEM_ID = 1,
		NODE_TYPE_NAME = 2, NODE_TYPE_DESCRIPTION = 3, NODE_TYPE_COLOR = 4;
	public static final String NODE_TYPE_ATTRIBUTES = "node_type_attributes";
	public static final int NODE_TYPE_ATTRIBUTE_ID = 0, 
		NODE_TYPE_ATTRIBUTE_TYPE_ID = 1, NODE_TYPE_ATTRIBUTE_NAME = 2,
		NODE_TYPE_ATTRIBUTE_DESCRIPTION = 3, NODE_TYPE_ATTRIBUTE_UNITS = 4,
		NODE_TYPE_ATTRIBUTE_BOUNDS = 5, NODE_TYPE_ATTRIBUTE_VALUE = 6;
	public static final String EDGE_TYPES = "edge_types";
	public static final int EDGE_TYPE_ID = 0, EDGE_TYPE_SYSTEM_ID = 1,
		EDGE_TYPE_NAME = 2, EDGE_TYPE_DESCRIPTION = 3, EDGE_TYPE_COLOR = 4;
	public static final String EDGE_TYPE_ATTRIBUTES = "edge_type_attributes";
	public static final int EDGE_TYPE_ATTRIBUTE_ID = 0, 
		EDGE_TYPE_ATTRIBUTE_TYPE_ID = 1, EDGE_TYPE_ATTRIBUTE_NAME = 2,
		EDGE_TYPE_ATTRIBUTE_DESCRIPTION = 3, EDGE_TYPE_ATTRIBUTE_UNITS = 4,
		EDGE_TYPE_ATTRIBUTE_BOUNDS = 5, EDGE_TYPE_ATTRIBUTE_VALUE = 6;
	public static final String CELLS = "cells";
	public static final int CELL_ID = 0, CELL_LOCATION_X = 1, 
		CELL_LOCATION_Y = 2, CELL_DIMENSION_X = 3, CELL_DIMENSION_Y = 4;
	public static final String LAYERS = "layers";
	public static final int LAYER_ID = 0, LAYER_SYSTEM_ID = 1, 
		LAYER_NAME = 2, LAYER_DESCRIPTION = 3, LAYER_DISPLAY_HEIGHT = 4;
	public static final String NODES = "nodes";
	public static final int NODE_ID = 0, NODE_SYSTEM_ID = 1, 
		NODE_NODE_TYPE_ID = 2, NODE_CELL_ID = 3, NODE_LAYER_ID = 4;
	public static final String EDGES = "edges";
	public static final int EDGE_ID = 0, EDGE_SYSTEM_ID = 1, 
		EDGE_EDGE_TYPE_ID = 2, EDGE_ORIGIN_ID = 3, EDGE_DESTINATION_ID = 4,
		EDGE_DIRECTED = 5;
	public static final String NODE_REGIONS = "node_regions";
	public static final int NODE_REGION_ID = 0, NODE_REGION_SYSTEM_ID = 1,
		NODE_REGION_NODE_TYPE_ID = 2, NODE_REGION_LAYER_ID = 3, 
		NODE_REGION_VERTICES_X = 4, NODE_REGION_VERTICES_Y = 5,
		NODE_REGION_TYPE = 6, NODE_REGION_DESCRIPTION = 7;
	public static final String EDGE_REGIONS = "edge_regions";
	public static final int EDGE_REGION_ID = 0, EDGE_REGION_SYSTEM_ID = 1,
		EDGE_REGION_EDGE_TYPE_ID = 2, EDGE_REGION_LAYER_ID = 3, 
		EDGE_REGION_VERTICES_X = 4, EDGE_REGION_VERTICES_Y = 5,
		EDGE_REGION_TYPE = 6, EDGE_REGION_DIRECTED = 7, 
		EDGE_REGION_DESCRIPTION = 8;
	public static final String CELL_REGIONS = "cell_regions";
	public static final int CELL_REGION_ID = 0, CELL_REGION_VERTICES_X = 1, 
		CELL_REGION_VERTICES_Y = 2, CELL_REGION_NUM_ROWS = 3, 
		CELL_REGION_NUM_COLS = 4, CELL_REGION_DESCRIPTION = 5;
	
	private String filePath;
	private transient Map<Integer,Layer> layerMap;
	private transient Map<Integer,NodeType> nodeTypeMap;
	private transient Map<Integer,EdgeType> edgeTypeMap;
	private transient Map<Integer,Cell> cellMap;
	private transient Map<Integer,Node> nodeMap;
	
	/**
	 * Instantiates a new spreadsheet template.
	 */
	public SpreadsheetTemplate() {
		layerMap = new HashMap<Integer,Layer>();
		nodeTypeMap = new HashMap<Integer,NodeType>();
		edgeTypeMap = new HashMap<Integer,EdgeType>();
		cellMap = new HashMap<Integer,Cell>();
		nodeMap = new HashMap<Integer,Node>();
	}
	
	/**
	 * Gets the file path.
	 *
	 * @return the file path
	 */
	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * Sets the file path.
	 *
	 * @param filePath the new file path
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * Read template.
	 */
	public City readTemplate() throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		Workbook wb = new HSSFWorkbook(fis);
		wb.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
		fis.close();
		return readCity(wb);
	}
	
	/**
	 * Read city.
	 *
	 * @return the city
	 */
	private City readCity(Workbook wb) {
		City city = new City();
		String verticesX = "[]",verticesY = "[]";
		for(Row row : wb.getSheet(CITY)) {
			if(row.getRowNum()==CITY_NAME)
				city.setName(row.getCell(1).getStringCellValue());
			if(row.getRowNum()==CITY_LATITUDE)
				city.setLatitude(row.getCell(1).getNumericCellValue());
			if(row.getRowNum()==CITY_LONGITUDE)
				city.setLongitude(row.getCell(1).getNumericCellValue());
			if(row.getRowNum()==CITY_ROTATION)
				city.setRotation(row.getCell(1).getNumericCellValue());
			if(row.getRowNum()==CITY_IMAGE_PATH)
				city.setImageFilePath(row.getCell(1).getStringCellValue());
			if(row.getRowNum()==CITY_IMAGE_VERTICES_X)
				verticesX = row.getCell(1).getStringCellValue();
			if(row.getRowNum()==CITY_IMAGE_VERTICES_Y)
				verticesY = row.getCell(1).getStringCellValue();
		}
		GeometryFactory gf = CityNet.getInstance().getGeometryFactory();
		CoordinateList coords = createCoordinatesFromMatlabSyntax(verticesX,verticesY);
		coords.closeRing(); 
		city.setImagePolygon(gf.createPolygon(
				gf.createLinearRing(coords.toCoordinateArray()), null));
		city.setSystems(readSystems(wb));
		city.setCells(readCells(wb));
		city.setCellRegions(readCellRegions(wb));
		return city;
	}
	private Set<CitySystem> readSystems(Workbook wb) {
		Set<CitySystem> systems = new HashSet<CitySystem>();
		for(Row row : wb.getSheet(SYSTEMS)) {
			if(row.getRowNum()==0) continue; // skip header row
			CitySystem system = new CitySystem();
			system.setId((int)row.getCell(SYSTEM_ID).getNumericCellValue());
			system.setName(row.getCell(SYSTEM_NAME).getStringCellValue());
			system.setDescription(row.getCell(SYSTEM_DESCRIPTION).getStringCellValue());
			system.setLayers(readLayers(wb,system.getId()));
			system.setNodeTypes(readNodeTypes(wb,system.getId()));
			system.setEdgeTypes(readEdgeTypes(wb,system.getId()));
			system.setNodes(readNodes(wb,system.getId()));
			system.setEdges(readEdges(wb,system.getId()));
			system.setNodeRegions(readNodeRegions(wb,system.getId()));
			system.setEdgeRegions(readEdgeRegions(wb,system.getId()));
			systems.add(system);
		}
		return systems;
	}
	private Set<Cell> readCells(Workbook wb) {
		Set<Cell> cells = new HashSet<Cell>();
		GeometryFactory gf = CityNet.getInstance().getGeometryFactory();
		for(Row row : wb.getSheet(CELLS)) {
			if(row.getRowNum()==0) continue; // skip header row
			Cell cell = new Cell();
			cell.setId((int)row.getCell(CELL_ID).getNumericCellValue());
			double locX = row.getCell(CELL_LOCATION_X).getNumericCellValue();
			double locY = row.getCell(CELL_LOCATION_Y).getNumericCellValue();
			double dimX = row.getCell(CELL_DIMENSION_X).getNumericCellValue();
			double dimY = row.getCell(CELL_DIMENSION_Y).getNumericCellValue();
			Coordinate c1 = new Coordinate(locX,locY);
			Coordinate c2 = new Coordinate(locX+dimX,locY);
			Coordinate c3 = new Coordinate(locX+dimX,locY+dimY);
			Coordinate c4 = new Coordinate(locX,locY+dimY);
			cell.setPolygon(gf.createPolygon(gf.createLinearRing(
					new Coordinate[]{c1,c2,c3,c4,c1}), null));
			cellMap.put(cell.getId(), cell);
			cells.add(cell);
		}
		return cells;
	}
	private Set<CellRegion> readCellRegions(Workbook wb) {
		Set<CellRegion> cellRegions = new HashSet<CellRegion>();
		for(Row row : wb.getSheet(CELL_REGIONS)) {
			if(row.getRowNum()==0) continue; // skip header row
			CellRegion cellRegion = new CellRegion();
			cellRegion.setId((int)row.getCell(CELL_REGION_ID).getNumericCellValue());
			cellRegion.setNumberRows((int)row.getCell(CELL_REGION_NUM_ROWS).getNumericCellValue());
			cellRegion.setNumberColumns((int)row.getCell(CELL_REGION_NUM_COLS).getNumericCellValue());
			cellRegion.setDescription(row.getCell(CELL_REGION_DESCRIPTION).getStringCellValue());
			String verticesX = row.getCell(CELL_REGION_VERTICES_X).getStringCellValue();
			String verticesY = row.getCell(CELL_REGION_VERTICES_Y).getStringCellValue();
			cellRegion.setCoordinateList(createCoordinatesFromMatlabSyntax(verticesX,verticesY));
			cellRegions.add(cellRegion);
		}
		return cellRegions;
	}
	private Set<Layer> readLayers(Workbook wb, int systemId) {
		Set<Layer> layers = new HashSet<Layer>();
		for(Row row : wb.getSheet(LAYERS)) {
			if(row.getRowNum()==0 || 
					(int)row.getCell(LAYER_SYSTEM_ID).getNumericCellValue()!=systemId) 
				continue; // skip header row and layers in other systems
			Layer layer = new Layer();
			layer.setId((int)row.getCell(LAYER_ID).getNumericCellValue());
			layer.setName(row.getCell(LAYER_NAME).getStringCellValue());
			layer.setDescription(row.getCell(LAYER_DESCRIPTION).getStringCellValue());
			layer.setDisplayHeight(row.getCell(LAYER_DISPLAY_HEIGHT).getNumericCellValue());
			layerMap.put(layer.getId(), layer);
			layers.add(layer);
		}
		return layers;
	}
	private Set<NodeType> readNodeTypes(Workbook wb, int systemId) {
		Set<NodeType> nodeTypes = new HashSet<NodeType>();
		for(Row row : wb.getSheet(NODE_TYPES)) {
			if(row.getRowNum()==0 || 
					(int)row.getCell(NODE_TYPE_SYSTEM_ID).getNumericCellValue()!=systemId) 
				continue; // skip header row and layers in other systems
			NodeType nodeType = new NodeType();
			nodeType.setId((int)row.getCell(NODE_TYPE_ID).getNumericCellValue());
			nodeType.setName(row.getCell(NODE_TYPE_NAME).getStringCellValue());
			nodeType.setDescription(row.getCell(NODE_TYPE_DESCRIPTION).getStringCellValue());
			nodeType.setColor(new Color(Integer.decode(row.getCell(NODE_TYPE_COLOR).getStringCellValue())));
			nodeType.setAttributes(readNodeTypeAttributes(wb,nodeType.getId()));
			nodeTypeMap.put(nodeType.getId(), nodeType);
			nodeTypes.add(nodeType);
		}
		return nodeTypes;
	}
	private Set<NodeTypeAttribute> readNodeTypeAttributes(Workbook wb, int nodeTypeId) {
		Set<NodeTypeAttribute> nodeTypeAttributes = new HashSet<NodeTypeAttribute>();
		for(Row row : wb.getSheet(NODE_TYPE_ATTRIBUTES)) {
			if(row.getRowNum()==0 || 
					(int)row.getCell(NODE_TYPE_ATTRIBUTE_TYPE_ID).getNumericCellValue()!=nodeTypeId) 
				continue; // skip header row and layers in other systems
			NodeTypeAttribute nodeTypeAttribute = new NodeTypeAttribute();
			nodeTypeAttribute.setId((int)row.getCell(NODE_TYPE_ATTRIBUTE_ID).getNumericCellValue());
			nodeTypeAttribute.setName(row.getCell(NODE_TYPE_ATTRIBUTE_NAME).getStringCellValue());
			nodeTypeAttribute.setDescription(row.getCell(NODE_TYPE_ATTRIBUTE_DESCRIPTION).getStringCellValue());
			nodeTypeAttribute.setUnits(row.getCell(NODE_TYPE_ATTRIBUTE_UNITS).getStringCellValue());
			nodeTypeAttribute.setBounds(row.getCell(NODE_TYPE_ATTRIBUTE_BOUNDS).getStringCellValue());
			nodeTypeAttribute.setValue(row.getCell(NODE_TYPE_ATTRIBUTE_VALUE).getNumericCellValue());
			nodeTypeAttributes.add(nodeTypeAttribute);
		}
		return nodeTypeAttributes;
	}
	private Set<EdgeType> readEdgeTypes(Workbook wb, int systemId) {
		Set<EdgeType> edgeTypes = new HashSet<EdgeType>();
		for(Row row : wb.getSheet(EDGE_TYPES)) {
			if(row.getRowNum()==0 || 
					(int)row.getCell(EDGE_TYPE_SYSTEM_ID).getNumericCellValue()!=systemId) 
				continue; // skip header row and layers in other systems
			EdgeType edgeType = new EdgeType();
			edgeType.setId((int)row.getCell(EDGE_TYPE_ID).getNumericCellValue());
			edgeType.setName(row.getCell(EDGE_TYPE_NAME).getStringCellValue());
			edgeType.setDescription(row.getCell(EDGE_TYPE_DESCRIPTION).getStringCellValue());
			edgeType.setColor(new Color(Integer.decode(row.getCell(EDGE_TYPE_COLOR).getStringCellValue())));
			edgeType.setAttributes(readEdgeTypeAttributes(wb,edgeType.getId()));
			edgeTypeMap.put(edgeType.getId(), edgeType);
			edgeTypes.add(edgeType);
		}
		return edgeTypes;
	}
	private Set<EdgeTypeAttribute> readEdgeTypeAttributes(Workbook wb, int edgeTypeId) {
		Set<EdgeTypeAttribute> edgeTypeAttributes = new HashSet<EdgeTypeAttribute>();
		for(Row row : wb.getSheet(EDGE_TYPE_ATTRIBUTES)) {
			if(row.getRowNum()==0 || 
					(int)row.getCell(EDGE_TYPE_ATTRIBUTE_TYPE_ID).getNumericCellValue()!=edgeTypeId) 
				continue; // skip header row and layers in other systems
			EdgeTypeAttribute edgeTypeAttribute = new EdgeTypeAttribute();
			edgeTypeAttribute.setId((int)row.getCell(EDGE_TYPE_ATTRIBUTE_ID).getNumericCellValue());
			edgeTypeAttribute.setName(row.getCell(EDGE_TYPE_ATTRIBUTE_NAME).getStringCellValue());
			edgeTypeAttribute.setDescription(row.getCell(EDGE_TYPE_ATTRIBUTE_DESCRIPTION).getStringCellValue());
			edgeTypeAttribute.setUnits(row.getCell(EDGE_TYPE_ATTRIBUTE_UNITS).getStringCellValue());
			edgeTypeAttribute.setBounds(row.getCell(EDGE_TYPE_ATTRIBUTE_BOUNDS).getStringCellValue());
			edgeTypeAttribute.setValue(row.getCell(EDGE_TYPE_ATTRIBUTE_VALUE).getNumericCellValue());
			edgeTypeAttributes.add(edgeTypeAttribute);
		}
		return edgeTypeAttributes;
	}
	private Set<Node> readNodes(Workbook wb, int systemId) {
		Set<Node> nodes = new HashSet<Node>();
		for(Row row : wb.getSheet(NODES)) {
			if(row.getRowNum()==0 || 
					(int)row.getCell(NODE_SYSTEM_ID).getNumericCellValue()!=systemId) 
				continue; // skip header row and layers in other systems
			Node node = new Node();
			node.setId((int)row.getCell(NODE_TYPE_ID).getNumericCellValue());
			node.setCell(cellMap.get((int)row.getCell(NODE_CELL_ID).getNumericCellValue()));
			node.setLayer(layerMap.get((int)row.getCell(NODE_LAYER_ID).getNumericCellValue()));
			node.setNodeType(nodeTypeMap.get((int)row.getCell(NODE_NODE_TYPE_ID).getNumericCellValue()));
			nodes.add(node);
		}
		return nodes;
	}
	private Set<Edge> readEdges(Workbook wb, int systemId) {
		Set<Edge> edges = new HashSet<Edge>();
		for(Row row : wb.getSheet(EDGES)) {
			if(row.getRowNum()==0 || 
					(int)row.getCell(EDGE_SYSTEM_ID).getNumericCellValue()!=systemId) 
				continue; // skip header row and layers in other systems
			Edge edge = new Edge();
			edge.setId((int)row.getCell(EDGE_TYPE_ID).getNumericCellValue());
			edge.setOrigin(nodeMap.get((int)row.getCell(EDGE_ORIGIN_ID).getNumericCellValue()));
			edge.setDestination(nodeMap.get((int)row.getCell(EDGE_DESTINATION_ID).getNumericCellValue()));
			edge.setEdgeType(edgeTypeMap.get((int)row.getCell(EDGE_EDGE_TYPE_ID).getNumericCellValue()));
			edge.setDirected(row.getCell(EDGE_DIRECTED).getNumericCellValue()==1);
			edges.add(edge);
		}
		return edges;
	}
	private Set<NodeRegion> readNodeRegions(Workbook wb, int systemId) {
		Set<NodeRegion> nodeRegions = new HashSet<NodeRegion>();
		for(Row row : wb.getSheet(NODE_REGIONS)) {
			if(row.getRowNum()==0 || 
					(int)row.getCell(NODE_REGION_SYSTEM_ID).getNumericCellValue()!=systemId) 
				continue; // skip header row and layers in other systems
			NodeRegion nodeRegion = new NodeRegion();
			nodeRegion.setId((int)row.getCell(NODE_REGION_ID).getNumericCellValue());
			nodeRegion.setLayer(layerMap.get((int)row.getCell(NODE_REGION_LAYER_ID).getNumericCellValue()));
			nodeRegion.setNodeType(nodeTypeMap.get((int)row.getCell(NODE_REGION_NODE_TYPE_ID).getNumericCellValue()));
			String verticesX = row.getCell(NODE_REGION_VERTICES_X).getStringCellValue();
			String verticesY = row.getCell(NODE_REGION_VERTICES_Y).getStringCellValue();
			nodeRegion.setCoordinateList(createCoordinatesFromMatlabSyntax(verticesX, verticesY));
			String regionType = row.getCell(NODE_REGION_TYPE).getStringCellValue();
			if(regionType.toLowerCase().equals("polygon")) {
				nodeRegion.setNodeRegionType(NodeRegion.NodeRegionType.POLYGON);
			} else if(regionType.toLowerCase().equals("polyline")) {
				nodeRegion.setNodeRegionType(NodeRegion.NodeRegionType.POLYLINE);
			} else if(regionType.toLowerCase().equals("polypoint")) {
				nodeRegion.setNodeRegionType(NodeRegion.NodeRegionType.POLYPOINT);
			} else {
				nodeRegion.setNodeRegionType(NodeRegion.NodeRegionType.UNDEFINED);
			}
			nodeRegion.setDescription(row.getCell(NODE_REGION_DESCRIPTION).getStringCellValue());
			nodeRegions.add(nodeRegion);
		}
		return nodeRegions;
	}
	private Set<EdgeRegion> readEdgeRegions(Workbook wb, int systemId) {
		Set<EdgeRegion> edgeRegions = new HashSet<EdgeRegion>();
		for(Row row : wb.getSheet(EDGE_REGIONS)) {
			if(row.getRowNum()==0 || 
					(int)row.getCell(EDGE_REGION_SYSTEM_ID).getNumericCellValue()!=systemId) 
				continue; // skip header row and layers in other systems
			EdgeRegion edgeRegion = new EdgeRegion();
			edgeRegion.setId((int)row.getCell(EDGE_REGION_ID).getNumericCellValue());
			String layersText = row.getCell(EDGE_REGION_LAYER_ID).getStringCellValue();
			Vector<Layer> layers = new Vector<Layer>();
			for(String s : layersText.substring(1,layersText.length()-1).split(" ")) {
				layers.add(layerMap.get(Integer.parseInt(s)));
			}
			edgeRegion.setLayers(layers);
			edgeRegion.setEdgeType(edgeTypeMap.get((int)row.getCell(EDGE_REGION_EDGE_TYPE_ID).getNumericCellValue()));
			String verticesX = row.getCell(EDGE_REGION_VERTICES_X).getStringCellValue();
			String verticesY = row.getCell(EDGE_REGION_VERTICES_Y).getStringCellValue();
			edgeRegion.setCoordinateList(createCoordinatesFromMatlabSyntax(verticesX, verticesY));
			String regionType = row.getCell(EDGE_REGION_TYPE).getStringCellValue();
			if(regionType.toLowerCase().equals("orthogonal")) {
				edgeRegion.setEdgeRegionType(EdgeRegion.EdgeRegionType.POLYGON_ORTHOGONAL);
			} else if(regionType.toLowerCase().equals("polyline")) {
				edgeRegion.setEdgeRegionType(EdgeRegion.EdgeRegionType.POLYLINE);
			} else if(regionType.toLowerCase().equals("neighbors") 
					|| regionType.toLowerCase().equals("adjacent")) {
				edgeRegion.setEdgeRegionType(EdgeRegion.EdgeRegionType.POLYGON_ADJACENT);
			} else if(regionType.toLowerCase().equals("connected")) {
				edgeRegion.setEdgeRegionType(EdgeRegion.EdgeRegionType.POLYGON_CONNECTED);
			} else if(regionType.toLowerCase().equals("polypoint")) {
				edgeRegion.setEdgeRegionType(EdgeRegion.EdgeRegionType.POLYPOINT);
			} else {
				edgeRegion.setEdgeRegionType(EdgeRegion.EdgeRegionType.UNDEFINED);
			}
			edgeRegion.setDirected(row.getCell(EDGE_REGION_DIRECTED).getNumericCellValue()==1);
			edgeRegion.setDescription(row.getCell(EDGE_REGION_DESCRIPTION).getStringCellValue());
			edgeRegions.add(edgeRegion);
		}
		return edgeRegions;
	}
	
	/**
	 * Creates the polygon from MATLAB syntax. Assumes vertices are formatted
	 * with square brackets and space delimiters such as: [0 0.4 0.5 0] and
	 * there are equal number of vertices in both x- and y-coordinates.
	 *
	 * @param verticesX the vertices x
	 * @param verticesY the vertices y
	 * @return the polygon
	 */
	private CoordinateList createCoordinatesFromMatlabSyntax(String verticesX, String verticesY) {
		String[] vX = verticesX.substring(1,verticesX.length()-1).split(" ");
		String[] vY = verticesY.substring(1,verticesY.length()-1).split(" ");
		CoordinateList coordinates = new CoordinateList();
		for(int i=0; i<vX.length; i++) {
			coordinates.add(new Coordinate(
					Double.parseDouble(vX[i]),Double.parseDouble(vY[i])),true);
		}
		return coordinates;
	}
	
	/**
	 * Write template.
	 */
	public void writeTemplate() {
		// TODO
	}
}
