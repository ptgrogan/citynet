package edu.mit.citynet.io;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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

/**
 * The SpreadsheetTemplate provides file input/operation methods for
 * the synthesis template in spreadsheet format.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
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
		// maps store quick look-up values by id
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
	 * Reads a spreadsheet template and returns the City object.
	 *
	 * @return the city
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public City readTemplate() throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		Workbook wb = new HSSFWorkbook(fis);
		wb.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
		fis.close();
		return readCity(wb);
	}
	
	/**
	 * Reads the city from the spreadsheet template.
	 *
	 * @param wb the workbook
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
		city.setCellRegions(readCellRegions(wb));
		city.setCells(readCells(wb));
		city.setSystems(readSystems(wb));
		return city;
	}
	
	/**
	 * Read the systems from the spreadsheet template.
	 *
	 * @param wb the workbook
	 * @return the sets of systems
	 */
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
			system.setNodeRegions(readNodeRegions(wb,system.getId()));
			system.setEdgeRegions(readEdgeRegions(wb,system.getId()));
			system.setNodes(readNodes(wb,system.getId()));
			system.setEdges(readEdges(wb,system.getId()));
			systems.add(system);
		}
		return systems;
	}
	
	/**
	 * Reads the cells from a spreadsheet template.
	 *
	 * @param wb the workbook
	 * @return the set of cells
	 */
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
	
	/**
	 * Reads the cell regions from the spreadsheet template.
	 *
	 * @param wb the workbook
	 * @return the set of cell regions
	 */
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
	
	/**
	 * Reads the layers for a system from a spreadsheet template.
	 *
	 * @param wb the workbook
	 * @param systemId the system id from which to load layers
	 * @return the set of layers
	 */
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
	
	/**
	 * Read the node types for a system from the spreadsheet template.
	 *
	 * @param wb the workbook
	 * @param systemId the system id from which to load node types
	 * @return the set of node types
	 */
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
	
	/**
	 * Read the node type attributes for a node type from the spreadsheet 
	 * template.
	 *
	 * @param wb the workbook
	 * @param nodeTypeId the node type id from which to load the attributes
	 * @return the set of node type attributes
	 */
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
	
	/**
	 * Reads the edge types for a system from the spreadsheet template.
	 *
	 * @param wb the workbook
	 * @param systemId the system id from which to read the edge types
	 * @return the set of edge types
	 */
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
	
	/**
	 * Read the edge type attributes for a edge type from the spreadsheet 
	 * template.
	 *
	 * @param wb the workbook
	 * @param edgeTypeId the edge type id from which to load the attributes
	 * @return the set of edge type attributes
	 */
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
	
	/**
	 * Read the nodes for a system from the spreadsheet template.
	 *
	 * @param wb the workbook
	 * @param systemId the system id from which to load nodes
	 * @return the set of nodes
	 */
	private Set<Node> readNodes(Workbook wb, int systemId) {
		Set<Node> nodes = new HashSet<Node>();
		for(Row row : wb.getSheet(NODES)) {
			if(row.getRowNum()==0 || 
					(int)row.getCell(NODE_SYSTEM_ID).getNumericCellValue()!=systemId) 
				continue; // skip header row and layers in other systems
			Node node = new Node();
			node.setId((int)row.getCell(NODE_ID).getNumericCellValue());
			node.setCell(cellMap.get((int)row.getCell(NODE_CELL_ID).getNumericCellValue()));
			node.setLayer(layerMap.get((int)row.getCell(NODE_LAYER_ID).getNumericCellValue()));
			node.setNodeType(nodeTypeMap.get((int)row.getCell(NODE_NODE_TYPE_ID).getNumericCellValue()));
			nodes.add(node);
			nodeMap.put(node.getId(), node);
		}
		return nodes;
	}
	
	/**
	 * Read the edges for a system from the spreadsheet template.
	 *
	 * @param wb the workbook
	 * @param systemId the system id from which to load edges
	 * @return the set of edges
	 */
	private Set<Edge> readEdges(Workbook wb, int systemId) {
		Set<Edge> edges = new HashSet<Edge>();
		for(Row row : wb.getSheet(EDGES)) {
			if(row.getRowNum()==0 || 
					(int)row.getCell(EDGE_SYSTEM_ID).getNumericCellValue()!=systemId) 
				continue; // skip header row and layers in other systems
			Edge edge = new Edge();
			edge.setId((int)row.getCell(EDGE_ID).getNumericCellValue());
			edge.setOrigin(nodeMap.get((int)row.getCell(EDGE_ORIGIN_ID).getNumericCellValue()));
			edge.setDestination(nodeMap.get((int)row.getCell(EDGE_DESTINATION_ID).getNumericCellValue()));
			edge.setEdgeType(edgeTypeMap.get((int)row.getCell(EDGE_EDGE_TYPE_ID).getNumericCellValue()));
			edge.setDirected(row.getCell(EDGE_DIRECTED).getNumericCellValue()==1);
			edges.add(edge);
		}
		return edges;
	}
	
	/**
	 * Read the node regions for a system from the spreadsheet template.
	 *
	 * @param wb the workbook
	 * @param systemId the system id from which to load node regions
	 * @return the set of node regions
	 */
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
			nodeRegion.setNodeRegionType(NodeRegion.NodeRegionType.getNodeRegionType(row.getCell(NODE_REGION_TYPE).getStringCellValue()));
			nodeRegion.setDescription(row.getCell(NODE_REGION_DESCRIPTION).getStringCellValue());
			nodeRegions.add(nodeRegion);
		}
		return nodeRegions;
	}
	
	/**
	 * Read the edge regions for a system from the spreadsheet template.
	 *
	 * @param wb the workbook
	 * @param systemId the system id from which to load edge regions
	 * @return the set of edge regions
	 */
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
			edgeRegion.setEdgeRegionType(EdgeRegion.EdgeRegionType.getEdgeRegionType(row.getCell(EDGE_REGION_TYPE).getStringCellValue()));
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
	private static CoordinateList createCoordinatesFromMatlabSyntax(String verticesX, String verticesY) {
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
	 * Creates the coordinates for Matlab syntax.
	 *
	 * @param coordinates the coordinates
	 * @return the string
	 */
	private static String[] createCoordinatesForMatlabSyntax(Coordinate[] coordinates) {
		String verticesX = "[", verticesY = "[";
		for(Coordinate coordinate : coordinates) {
			verticesX += coordinate.x + " ";
			verticesY += coordinate.y + " ";
		}
		verticesX = verticesX.trim() + "]";
		verticesY = verticesY.trim() + "]";
		return new String[]{verticesX, verticesY};
	}
	
	/**
	 * Writes a spreadsheet template template.
	 *
	 * @param city the city
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeTemplate(City city) throws IOException {
		File file = new File(filePath);
		Workbook wb;
		if(file.exists()) {
			FileInputStream fis = new FileInputStream(file);
			wb = new HSSFWorkbook(fis);
			fis.close();
		} else {
			wb = new HSSFWorkbook();
		}
		wb.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
		formatTemplate(wb);
		clearTemplate(wb);
		writeCity(city, wb);
		FileOutputStream fos = new FileOutputStream(file);
		wb.write(fos);
		fos.close();
	}
	
	/**
	 * Formats the template.
	 *
	 * @param wb the workbook
	 */
	private void formatTemplate(Workbook wb) {
		if(wb.getSheet(CITY)==null) {
			Sheet s = wb.createSheet(CITY);
			s.createRow(CITY_NAME).getCell(0).setCellValue("Name:");
			s.createRow(CITY_LATITUDE).getCell(0).setCellValue("Latitude:");
			s.createRow(CITY_LONGITUDE).getCell(0).setCellValue("Longitude:");
			s.createRow(CITY_ROTATION).getCell(0).setCellValue("Rotation:");
			s.createRow(CITY_IMAGE_PATH).getCell(0).setCellValue("Image File Path:");
			s.createRow(CITY_IMAGE_VERTICES_X).getCell(0).setCellValue("X-Vertices:");
			s.createRow(CITY_IMAGE_VERTICES_Y).getCell(0).setCellValue("Y-Vertices:");
		}
		if(wb.getSheet(CELL_REGIONS)==null) {
			Sheet s = wb.createSheet(CELL_REGIONS);
			s.createRow(0);
			s.getRow(0).getCell(CELL_REGION_ID).setCellValue("ID");
			s.getRow(0).getCell(CELL_REGION_VERTICES_X).setCellValue("X-Vertices");
			s.getRow(0).getCell(CELL_REGION_VERTICES_Y).setCellValue("Y-Vertices");
			s.getRow(0).getCell(CELL_REGION_NUM_ROWS).setCellValue("# Rows");
			s.getRow(0).getCell(CELL_REGION_NUM_COLS).setCellValue("# Columns");
			s.getRow(0).getCell(CELL_REGION_DESCRIPTION).setCellValue("Description");
		}
		if(wb.getSheet(CELLS)==null) {
			Sheet s = wb.createSheet(CELLS);
			s.createRow(0);
			s.getRow(0).getCell(CELL_ID).setCellValue("ID");
			s.getRow(0).getCell(CELL_LOCATION_X).setCellValue("X-Location");
			s.getRow(0).getCell(CELL_LOCATION_Y).setCellValue("Y-Location");
			s.getRow(0).getCell(CELL_DIMENSION_X).setCellValue("X-Dimension");
			s.getRow(0).getCell(CELL_DIMENSION_Y).setCellValue("Y-Dimension");
		}
		if(wb.getSheet(SYSTEMS)==null) {
			Sheet s = wb.createSheet(SYSTEMS);
			s.createRow(0);
			s.getRow(0).getCell(SYSTEM_ID).setCellValue("ID");
			s.getRow(0).getCell(SYSTEM_NAME).setCellValue("Name");
			s.getRow(0).getCell(SYSTEM_DESCRIPTION).setCellValue("Description");
		}
		if(wb.getSheet(LAYERS)==null) {
			Sheet s = wb.createSheet(LAYERS);
			s.createRow(0);
			s.getRow(0).getCell(LAYER_ID).setCellValue("ID");
			s.getRow(0).getCell(LAYER_SYSTEM_ID).setCellValue("System ID");
			s.getRow(0).getCell(LAYER_NAME).setCellValue("Name");
			s.getRow(0).getCell(LAYER_DESCRIPTION).setCellValue("Description");
			s.getRow(0).getCell(LAYER_DISPLAY_HEIGHT).setCellValue("Display Height");
		}
		if(wb.getSheet(NODE_TYPES)==null) {
			Sheet s = wb.createSheet(NODE_TYPES);
			s.createRow(0);
			s.getRow(0).getCell(NODE_TYPE_ID).setCellValue("ID");
			s.getRow(0).getCell(NODE_TYPE_SYSTEM_ID).setCellValue("System ID");
			s.getRow(0).getCell(NODE_TYPE_NAME).setCellValue("Name");
			s.getRow(0).getCell(NODE_TYPE_DESCRIPTION).setCellValue("Description");
			s.getRow(0).getCell(NODE_TYPE_COLOR).setCellValue("Color");
		}
		if(wb.getSheet(NODE_TYPE_ATTRIBUTES)==null) {
			Sheet s = wb.createSheet(NODE_TYPE_ATTRIBUTES);
			s.createRow(0);
			s.getRow(0).getCell(NODE_TYPE_ATTRIBUTE_ID).setCellValue("ID");
			s.getRow(0).getCell(NODE_TYPE_ATTRIBUTE_TYPE_ID).setCellValue("Node Type ID");
			s.getRow(0).getCell(NODE_TYPE_ATTRIBUTE_NAME).setCellValue("Name");
			s.getRow(0).getCell(NODE_TYPE_ATTRIBUTE_DESCRIPTION).setCellValue("Description");
			s.getRow(0).getCell(NODE_TYPE_ATTRIBUTE_UNITS).setCellValue("Units");
			s.getRow(0).getCell(NODE_TYPE_ATTRIBUTE_BOUNDS).setCellValue("Bounds");
			s.getRow(0).getCell(NODE_TYPE_ATTRIBUTE_VALUE).setCellValue("Value");
		}
		if(wb.getSheet(EDGE_TYPES)==null) {
			Sheet s = wb.createSheet(EDGE_TYPES);
			s.createRow(0);
			s.getRow(0).getCell(EDGE_TYPE_ID).setCellValue("ID");
			s.getRow(0).getCell(EDGE_TYPE_SYSTEM_ID).setCellValue("System ID");
			s.getRow(0).getCell(EDGE_TYPE_NAME).setCellValue("Name");
			s.getRow(0).getCell(EDGE_TYPE_DESCRIPTION).setCellValue("Description");
			s.getRow(0).getCell(EDGE_TYPE_COLOR).setCellValue("Color");
		}
		if(wb.getSheet(EDGE_TYPE_ATTRIBUTES)==null) {
			Sheet s = wb.createSheet(EDGE_TYPE_ATTRIBUTES);
			s.createRow(0);
			s.getRow(0).getCell(EDGE_TYPE_ATTRIBUTE_ID).setCellValue("ID");
			s.getRow(0).getCell(EDGE_TYPE_ATTRIBUTE_TYPE_ID).setCellValue("Edge Type ID");
			s.getRow(0).getCell(EDGE_TYPE_ATTRIBUTE_NAME).setCellValue("Name");
			s.getRow(0).getCell(EDGE_TYPE_ATTRIBUTE_DESCRIPTION).setCellValue("Description");
			s.getRow(0).getCell(EDGE_TYPE_ATTRIBUTE_UNITS).setCellValue("Units");
			s.getRow(0).getCell(EDGE_TYPE_ATTRIBUTE_BOUNDS).setCellValue("Bounds");
			s.getRow(0).getCell(EDGE_TYPE_ATTRIBUTE_VALUE).setCellValue("Value");
		}
		if(wb.getSheet(NODE_REGIONS)==null) {
			Sheet s = wb.createSheet(NODE_REGIONS);
			s.createRow(0);
			s.getRow(0).getCell(NODE_REGION_ID).setCellValue("ID");
			s.getRow(0).getCell(NODE_REGION_SYSTEM_ID).setCellValue("System ID");
			s.getRow(0).getCell(NODE_REGION_NODE_TYPE_ID).setCellValue("Node Type ID");
			s.getRow(0).getCell(NODE_REGION_LAYER_ID).setCellValue("Layer ID");
			s.getRow(0).getCell(NODE_REGION_VERTICES_X).setCellValue("X-Vertices");
			s.getRow(0).getCell(NODE_REGION_VERTICES_Y).setCellValue("Y-Vertices");
			s.getRow(0).getCell(NODE_REGION_TYPE).setCellValue("Generation Type");
			s.getRow(0).getCell(NODE_REGION_DESCRIPTION).setCellValue("Description");
		}
		if(wb.getSheet(EDGE_REGIONS)==null) {
			Sheet s = wb.createSheet(EDGE_REGIONS);
			s.createRow(0);
			s.getRow(0).getCell(EDGE_REGION_ID).setCellValue("ID");
			s.getRow(0).getCell(EDGE_REGION_SYSTEM_ID).setCellValue("System ID");
			s.getRow(0).getCell(EDGE_REGION_EDGE_TYPE_ID).setCellValue("Edge Type ID");
			s.getRow(0).getCell(EDGE_REGION_LAYER_ID).setCellValue("Layer IDs");
			s.getRow(0).getCell(EDGE_REGION_VERTICES_X).setCellValue("X-Vertices");
			s.getRow(0).getCell(EDGE_REGION_VERTICES_Y).setCellValue("Y-Vertices");
			s.getRow(0).getCell(EDGE_REGION_TYPE).setCellValue("Generation Type");
			s.getRow(0).getCell(EDGE_REGION_DIRECTED).setCellValue("Directed");
			s.getRow(0).getCell(EDGE_REGION_DESCRIPTION).setCellValue("Description");
		}
		if(wb.getSheet(NODES)==null) {
			Sheet s = wb.createSheet(NODES);
			s.createRow(0);
			s.getRow(0).getCell(NODE_ID).setCellValue("ID");
			s.getRow(0).getCell(NODE_SYSTEM_ID).setCellValue("System ID");
			s.getRow(0).getCell(NODE_NODE_TYPE_ID).setCellValue("Node Type ID");
			s.getRow(0).getCell(NODE_CELL_ID).setCellValue("Cell ID");
			s.getRow(0).getCell(NODE_LAYER_ID).setCellValue("Layer ID");
		}
		if(wb.getSheet(EDGES)==null) {
			Sheet s = wb.createSheet(EDGES);
			s.createRow(0);
			s.getRow(0).getCell(EDGE_ID).setCellValue("ID");
			s.getRow(0).getCell(EDGE_SYSTEM_ID).setCellValue("System ID");
			s.getRow(0).getCell(EDGE_EDGE_TYPE_ID).setCellValue("Edge Type ID");
			s.getRow(0).getCell(EDGE_ORIGIN_ID).setCellValue("Origin Node ID");
			s.getRow(0).getCell(EDGE_DESTINATION_ID).setCellValue("Destination Node ID");
			s.getRow(0).getCell(EDGE_DIRECTED).setCellValue("Directed");
		}
	}
	
	/**
	 * Clears a template.
	 *
	 * @param wb the workbook
	 */
	private void clearTemplate(Workbook wb) {
		clearSheet(wb,CELL_REGIONS);
		clearSheet(wb,CELLS);
		clearSheet(wb,SYSTEMS);
		clearSheet(wb,LAYERS);
		clearSheet(wb,NODE_TYPES);
		clearSheet(wb,NODE_TYPE_ATTRIBUTES);
		clearSheet(wb,EDGE_TYPES);
		clearSheet(wb,EDGE_TYPE_ATTRIBUTES);
		clearSheet(wb,NODE_REGIONS);
		clearSheet(wb,EDGE_REGIONS);
		clearSheet(wb,NODES);
		clearSheet(wb,EDGES);
	}
	
	/**
	 * Clears a sheet of entries.
	 *
	 * @param wb the workbook
	 * @param sheetName the sheet name
	 */
	private static void clearSheet(Workbook wb, String sheetName) {
		Sheet sheet = wb.getSheet(sheetName);
		while(sheet.getLastRowNum()>0)
			sheet.removeRow(sheet.getRow(sheet.getLastRowNum()));
	}
	
	/**
	 * Writes a city to the spreadsheet template.
	 *
	 * @param city the city
	 * @param wb the workbook
	 */
	private void writeCity(City city, Workbook wb) {
		Sheet s = wb.getSheet(CITY);
		s.getRow(CITY_NAME).getCell(1).setCellValue(city.getName());
		s.getRow(CITY_LATITUDE).getCell(1).setCellValue(city.getLatitude());
		s.getRow(CITY_LONGITUDE).getCell(1).setCellValue(city.getLongitude());
		s.getRow(CITY_ROTATION).getCell(1).setCellValue(city.getRotation());
		s.getRow(CITY_IMAGE_PATH).getCell(1).setCellValue(city.getImageFilePath());
		String[] vertices = createCoordinatesForMatlabSyntax(city.getImagePolygon().getCoordinates());
		s.getRow(CITY_IMAGE_VERTICES_X).getCell(1).setCellValue(vertices[0]);
		s.getRow(CITY_IMAGE_VERTICES_Y).getCell(1).setCellValue(vertices[1]);
		
		for(CellRegion cellRegion : city.getCellRegions()) {
			writeCellRegion(cellRegion, wb);
		}
		for(Cell cell : city.getCells()) {
			writeCell(cell, wb);
		}
		for(CitySystem system : city.getSystems()) {
			writeSystem(system, wb);
		}
	}
	
	/**
	 * Gets the row for object, creating a new row if it does not exist.
	 *
	 * @param wb the workbook
	 * @param sheetName the sheet name
	 * @param idColumn the id column
	 * @param objectId the object id
	 * @return the row for object
	 */
	private static Row getRowForObject(Workbook wb, String sheetName, int idColumn, int objectId) {
		Sheet sheet = wb.getSheet(sheetName);
		for(Row r : sheet) {
			if(r.getRowNum() > 0 && r.getCell(idColumn).getNumericCellValue()==objectId) {
				return r;
			}
		}
		return sheet.createRow(sheet.getLastRowNum()+1);
	}
	
	/**
	 * Writes a cell region to the spreadsheet template.
	 *
	 * @param cellRegion the cell region
	 * @param wb the workbook
	 */
	private void writeCellRegion(CellRegion cellRegion, Workbook wb) {
		Row row = getRowForObject(wb, CELL_REGIONS, CELL_REGION_ID, cellRegion.getId());
		row.getCell(CELL_REGION_ID).setCellValue(cellRegion.getId());
		String[] vertices = createCoordinatesForMatlabSyntax(cellRegion.getCoordinateList().toCoordinateArray());
		row.getCell(CELL_REGION_VERTICES_X).setCellValue(vertices[0]);
		row.getCell(CELL_REGION_VERTICES_Y).setCellValue(vertices[1]);
		row.getCell(CELL_REGION_NUM_ROWS).setCellValue(cellRegion.getNumberRows());
		row.getCell(CELL_REGION_NUM_COLS).setCellValue(cellRegion.getNumberColumns());
		row.getCell(CELL_REGION_DESCRIPTION).setCellValue(cellRegion.getDescription());
	}
	
	/**
	 * Writes a cell to the spreadsheet template.
	 *
	 * @param cell the cell
	 * @param wb the workbook
	 */
	private void writeCell(Cell cell, Workbook wb) {
		Row row = getRowForObject(wb, CELLS, CELL_ID, cell.getId());
		row.getCell(CELL_ID).setCellValue(cell.getId());
		double locX = Double.MAX_VALUE, locY = Double.MAX_VALUE, dimX = 0, dimY = 0;
		for(Coordinate c : cell.getPolygon().getCoordinates()) {
			locX = Math.min(locX, c.x);
			locY = Math.min(locY, c.y);
		}
		for(Coordinate c : cell.getPolygon().getCoordinates()) {
			dimX = Math.max(dimX, c.x-locX);
			dimY = Math.max(dimY, c.y-locY);
		}
		row.getCell(CELL_LOCATION_X).setCellValue(locX);
		row.getCell(CELL_LOCATION_Y).setCellValue(locY);
		row.getCell(CELL_DIMENSION_X).setCellValue(dimX);
		row.getCell(CELL_DIMENSION_Y).setCellValue(dimY);
	}
	
	/**
	 * Writes a system to the spreadsheet template.
	 *
	 * @param system the system
	 * @param wb the workbook
	 */
	private void writeSystem(CitySystem system, Workbook wb) {
		Row row = getRowForObject(wb, SYSTEMS, SYSTEM_ID, system.getId());
		row.getCell(SYSTEM_ID).setCellValue(system.getId());
		row.getCell(SYSTEM_NAME).setCellValue(system.getName());
		row.getCell(SYSTEM_DESCRIPTION).setCellValue(system.getDescription());
		
		for(Layer layer : system.getLayers()) {
			writeLayer(layer, system.getId(), wb);
		}
		for(NodeType nodeType : system.getNodeTypes()) {
			writeNodeType(nodeType, system.getId(), wb);
		}
		for(EdgeType edgeType : system.getEdgeTypes()) {
			writeEdgeType(edgeType, system.getId(), wb);
		}
		for(NodeRegion nodeRegion : system.getNodeRegions()) {
			writeNodeRegion(nodeRegion, system.getId(), wb);
		}
		for(EdgeRegion edgeRegion: system.getEdgeRegions()) {
			writeEdgeRegion(edgeRegion, system.getId(), wb);
		}
		for(Node node : system.getNodes()) {
			writeNode(node, system.getId(), wb);
		}
		for(Edge edge: system.getEdges()) {
			writeEdge(edge, system.getId(), wb);
		}
	}
	
	/**
	 * Writes a layer to the spreadsheet template.
	 *
	 * @param layer the layer
	 * @param systemId the system id
	 * @param wb the workbook
	 */
	private void writeLayer(Layer layer, int systemId, Workbook wb) {
		Row row = getRowForObject(wb, LAYERS, LAYER_ID, layer.getId());
		row.getCell(LAYER_ID).setCellValue(layer.getId());
		row.getCell(LAYER_SYSTEM_ID).setCellValue(systemId);
		row.getCell(LAYER_NAME).setCellValue(layer.getName());
		row.getCell(LAYER_DESCRIPTION).setCellValue(layer.getDescription());
		row.getCell(LAYER_DISPLAY_HEIGHT).setCellValue(layer.getDisplayHeight());
	}
	
	/**
	 * Write a node type to the spreadsheet template.
	 *
	 * @param nodeType the node type
	 * @param systemId the system id
	 * @param wb the workbook
	 */
	private void writeNodeType(NodeType nodeType, int systemId, Workbook wb) {
		Row row = getRowForObject(wb, NODE_TYPES, NODE_TYPE_ID, nodeType.getId());
		row.getCell(NODE_TYPE_ID).setCellValue(nodeType.getId());
		row.getCell(NODE_TYPE_SYSTEM_ID).setCellValue(systemId);
		row.getCell(NODE_TYPE_NAME).setCellValue(nodeType.getName());
		row.getCell(NODE_TYPE_DESCRIPTION).setCellValue(nodeType.getDescription());
		// remove the alpha component of color, encode as hex string
		row.getCell(NODE_TYPE_COLOR).setCellValue("0x" + Integer.toHexString(0xff000000^nodeType.getColor().getRGB()));
		
		for(NodeTypeAttribute attribute : nodeType.getAttributes()) {
			writeNodeTypeAttribute(attribute, nodeType.getId(), wb);
		}
	}
	
	/**
	 * Writes a node type attribute to the spreadsheet template.
	 *
	 * @param attribute the attribute
	 * @param nodeTypeId the node type id
	 * @param wb the workbook
	 */
	private void writeNodeTypeAttribute(NodeTypeAttribute attribute, int nodeTypeId, Workbook wb) {
		Row row = getRowForObject(wb, NODE_TYPE_ATTRIBUTES, NODE_TYPE_ATTRIBUTE_ID, attribute.getId());
		row.getCell(NODE_TYPE_ATTRIBUTE_ID).setCellValue(attribute.getId());
		row.getCell(NODE_TYPE_ATTRIBUTE_TYPE_ID).setCellValue(nodeTypeId);
		row.getCell(NODE_TYPE_ATTRIBUTE_NAME).setCellValue(attribute.getName());
		row.getCell(NODE_TYPE_ATTRIBUTE_DESCRIPTION).setCellValue(attribute.getDescription());
		row.getCell(NODE_TYPE_ATTRIBUTE_UNITS).setCellValue(attribute.getUnits());
		row.getCell(NODE_TYPE_ATTRIBUTE_BOUNDS).setCellValue(attribute.getBounds());
		row.getCell(NODE_TYPE_ATTRIBUTE_VALUE).setCellValue(attribute.getValue());
	}
	
	/**
	 * Writes an edge type to the spreadsheet template.
	 *
	 * @param edgeType the edge type
	 * @param systemId the system id
	 * @param wb the workbook
	 */
	private void writeEdgeType(EdgeType edgeType, int systemId, Workbook wb) {
		Row row = getRowForObject(wb, EDGE_TYPES, EDGE_TYPE_ID, edgeType.getId());
		row.getCell(EDGE_TYPE_ID).setCellValue(edgeType.getId());
		row.getCell(EDGE_TYPE_SYSTEM_ID).setCellValue(systemId);
		row.getCell(EDGE_TYPE_NAME).setCellValue(edgeType.getName());
		row.getCell(EDGE_TYPE_DESCRIPTION).setCellValue(edgeType.getDescription());
		// remove the alpha component of color, encode as hex string
		row.getCell(EDGE_TYPE_COLOR).setCellValue("0x" + Integer.toHexString(0xff000000^edgeType.getColor().getRGB()));

		for(EdgeTypeAttribute attribute : edgeType.getAttributes()) {
			writeEdgeTypeAttribute(attribute, edgeType.getId(), wb);
		}
	}
	
	/**
	 * Writes an edge type attribute to the spreadsheet template.
	 *
	 * @param attribute the attribute
	 * @param edgeTypeId the edge type id
	 * @param wb the workbook
	 */
	private void writeEdgeTypeAttribute(EdgeTypeAttribute attribute, int edgeTypeId, Workbook wb) {
		Row row = getRowForObject(wb, EDGE_TYPE_ATTRIBUTES, EDGE_TYPE_ATTRIBUTE_ID, attribute.getId());
		row.getCell(EDGE_TYPE_ATTRIBUTE_ID).setCellValue(attribute.getId());
		row.getCell(EDGE_TYPE_ATTRIBUTE_TYPE_ID).setCellValue(edgeTypeId);
		row.getCell(EDGE_TYPE_ATTRIBUTE_NAME).setCellValue(attribute.getName());
		row.getCell(EDGE_TYPE_ATTRIBUTE_DESCRIPTION).setCellValue(attribute.getDescription());
		row.getCell(EDGE_TYPE_ATTRIBUTE_UNITS).setCellValue(attribute.getUnits());
		row.getCell(EDGE_TYPE_ATTRIBUTE_BOUNDS).setCellValue(attribute.getBounds());
		row.getCell(EDGE_TYPE_ATTRIBUTE_VALUE).setCellValue(attribute.getValue());
	}

	/**
	 * Writes a node region to the spreadsheet template.
	 *
	 * @param nodeRegion the node region
	 * @param systemId the system id
	 * @param wb the workbook
	 */
	private void writeNodeRegion(NodeRegion nodeRegion, int systemId, Workbook wb) {
		Row row = getRowForObject(wb, NODE_REGIONS, NODE_REGION_ID, nodeRegion.getId());
		row.getCell(NODE_REGION_ID).setCellValue(nodeRegion.getId());
		row.getCell(NODE_REGION_SYSTEM_ID).setCellValue(systemId);
		row.getCell(NODE_REGION_NODE_TYPE_ID).setCellValue(nodeRegion.getNodeType().getId());
		row.getCell(NODE_REGION_LAYER_ID).setCellValue(nodeRegion.getLayer().getId());
		String[] vertices = createCoordinatesForMatlabSyntax(nodeRegion.getCoordinateList().toCoordinateArray());
		row.getCell(NODE_REGION_VERTICES_X).setCellValue(vertices[0]);
		row.getCell(NODE_REGION_VERTICES_Y).setCellValue(vertices[1]);
		row.getCell(NODE_REGION_TYPE).setCellValue(nodeRegion.getNodeRegionType().getName());
		row.getCell(NODE_REGION_DESCRIPTION).setCellValue(nodeRegion.getDescription());
	}
	
	/**
	 * Writes an edge region to the spreadsheet template.
	 *
	 * @param edgeRegion the edge region
	 * @param systemId the system id
	 * @param wb the workbook
	 */
	private void writeEdgeRegion(EdgeRegion edgeRegion, int systemId, Workbook wb) {
		Row row = getRowForObject(wb, EDGE_REGIONS, EDGE_REGION_ID, edgeRegion.getId());
		row.getCell(EDGE_REGION_ID).setCellValue(edgeRegion.getId());
		row.getCell(EDGE_REGION_SYSTEM_ID).setCellValue(systemId);
		row.getCell(EDGE_REGION_EDGE_TYPE_ID).setCellValue(edgeRegion.getEdgeType().getId());
		String layerIds = "[";
		for(Layer layer : edgeRegion.getLayers()) {
			layerIds += layer.getId() + " ";
		}
		layerIds = layerIds.trim() + "]";
		row.getCell(EDGE_REGION_LAYER_ID).setCellValue(layerIds);
		String[] vertices = createCoordinatesForMatlabSyntax(edgeRegion.getCoordinateList().toCoordinateArray());
		row.getCell(EDGE_REGION_VERTICES_X).setCellValue(vertices[0]);
		row.getCell(EDGE_REGION_VERTICES_Y).setCellValue(vertices[1]);
		row.getCell(EDGE_REGION_TYPE).setCellValue(edgeRegion.getEdgeRegionType().getName());
		row.getCell(EDGE_REGION_DIRECTED).setCellValue(edgeRegion.isDirected()?1:0);
		row.getCell(EDGE_REGION_DESCRIPTION).setCellValue(edgeRegion.getDescription());
	}

	/**
	 * Writes a node to the spreadsheet template.
	 *
	 * @param node the node
	 * @param systemId the system id
	 * @param wb the workbook
	 */
	private void writeNode(Node node, int systemId, Workbook wb) {
		Row row = getRowForObject(wb, NODES, NODE_ID, node.getId());
		row.getCell(NODE_ID).setCellValue(node.getId());
		row.getCell(NODE_SYSTEM_ID).setCellValue(systemId);
		row.getCell(NODE_NODE_TYPE_ID).setCellValue(node.getNodeType().getId());
		row.getCell(NODE_CELL_ID).setCellValue(node.getCell().getId());
		row.getCell(NODE_LAYER_ID).setCellValue(node.getLayer().getId());
	}
	
	/**
	 * Writes an edge to the spreadsheet template.
	 *
	 * @param edge the edge
	 * @param systemId the system id
	 * @param wb the workbook
	 */
	private void writeEdge(Edge edge, int systemId, Workbook wb) {
		Row row = getRowForObject(wb, EDGES, EDGE_ID, edge.getId());
		row.getCell(EDGE_ID).setCellValue(edge.getId());
		row.getCell(EDGE_SYSTEM_ID).setCellValue(systemId);
		row.getCell(EDGE_EDGE_TYPE_ID).setCellValue(edge.getEdgeType().getId());
		row.getCell(EDGE_ORIGIN_ID).setCellValue(edge.getOrigin().getId());
		row.getCell(EDGE_DESTINATION_ID).setCellValue(edge.getDestination().getId());
		row.getCell(EDGE_DIRECTED).setCellValue(edge.isDirected()?1:0);
	}
}
