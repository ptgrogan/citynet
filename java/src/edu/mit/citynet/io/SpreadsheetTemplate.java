package edu.mit.citynet.io;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import edu.mit.citynet.CityNet;
import edu.mit.citynet.core.City;

public class SpreadsheetTemplate {
	public static final String CITY_WORKSHEET = "city";
	public static final int CITY_NAME = 0, CITY_LATITUDE = 1, CITY_LONGITUDE = 2,
		CITY_ROTATION = 3, CITY_IMAGE_PATH = 4, CITY_IMAGE_VERTICES_X = 5,
		CITY_IMAGE_VERTICES_Y = 6;
	
	private String filePath;
	
	/**
	 * Instantiates a new spreadsheet template.
	 */
	public SpreadsheetTemplate() { }
	
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
	public void readTemplate() throws IOException {
		CityNet cityNet = CityNet.getInstance();
		FileInputStream fis = new FileInputStream(filePath);
		Workbook wb = new HSSFWorkbook(fis);
		wb.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
		fis.close();
		cityNet.setCity(readCity(wb));
		cityNet.updateIds();
	}
	
	/**
	 * Read city.
	 *
	 * @return the city
	 */
	private City readCity(Workbook wb) {
		City city = new City();
		String verticesX = "[]",verticesY = "[]";
		for(Row row : wb.getSheet(CITY_WORKSHEET)) {
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
		// strip brackets
		String[] vX = verticesX.substring(1,verticesX.length()-1).split(" ");
		String[] vY = verticesY.substring(1,verticesY.length()-1).split(" ");
		GeometryFactory gf = CityNet.getInstance().getGeometryFactory();
		Coordinate[] coordinates = new Coordinate[vX.length+1];
		for(int i=0; i<vX.length; i++) {
			coordinates[i] = new Coordinate(
					Double.parseDouble(vX[i]),Double.parseDouble(vY[i]));
		}
		coordinates[vX.length] = coordinates[0]; // close for linear ring
		city.setImagePolygon(gf.createPolygon(gf.createLinearRing(coordinates),null));
		return city;
	}
	
	/**
	 * Write template.
	 */
	public void writeTemplate() {
		// TODO
	}
}
