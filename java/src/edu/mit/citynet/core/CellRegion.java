package edu.mit.citynet.core;

/**
 * The CellRegion class specifies a spatial area over which cells should be 
 * generated (meshed). There are several options to define the type and 
 * density of cells generated within the region.
 * 
 * The CellRegion class was created to be able to specify cells without
 * relying on tedious individual cell definitions.
 */
public class CellRegion extends AbstractRegion {
	private int id;
	private double[] gridSize;
	private String description;
	
	/**
	 * Instantiates a new cell region.
	 */
	public CellRegion() {
		gridSize = new double[2];
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
	 * Gets the grid size.
	 *
	 * @return the grid size
	 */
	public double[] getGridSize() {
		return gridSize;
	}

	/**
	 * Sets the grid size.
	 *
	 * @param gridSize the new grid size
	 */
	public void setGridSize(double[] gridSize) {
		this.gridSize = gridSize;
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
}
