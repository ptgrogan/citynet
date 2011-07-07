package edu.mit.citynet.core;

/**
 * The Layer class represents a logical differentiation between objects that 
 * may exist at the same spatial location.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class Layer {
	private int id;
	private String name, description;
	private double displayHeight;
	
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
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Gets the display height.
	 *
	 * @return the display height
	 */
	public double getDisplayHeight() {
		return displayHeight;
	}
	
	/**
	 * Sets the display height.
	 *
	 * @param displayHeight the new display height
	 */
	public void setDisplayHeight(double displayHeight) {
		this.displayHeight = displayHeight;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if(object instanceof Layer) {
			return id==((Layer)object).getId();
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Layer" + id;
	}
	
}
