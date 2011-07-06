package edu.mit.citynet.core;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

/**
 * The EdgeType class is used to define a classification of edges to be used in
 * the synthesis template. Each EdgeType is given a name, rgbColor (values for 
 * red, green, and blue between 0 and 1 to define the display color in the 
 * rendering), and a set of EdgeTypeAttribute objects.
 */
public class EdgeType {
	private int id;
	private String name, description;
	private Color color;
	private Set<EdgeTypeAttribute> attributes;
	
	/**
	 * Instantiates a new node type and initializes the attributes to an empty 
	 * set.
	 */
	public EdgeType() {
		attributes = new HashSet<EdgeTypeAttribute>();
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
	 * Gets the color.
	 *
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color.
	 *
	 * @param color the new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Gets the attributes.
	 *
	 * @return the attributes
	 */
	public Set<EdgeTypeAttribute> getAttributes() {
		return new HashSet<EdgeTypeAttribute>(attributes);
	}
	
	/**
	 * Sets the attributes.
	 *
	 * @param attributes the new attributes
	 */
	public void setAttributes(Set<EdgeTypeAttribute> attributes) {
		this.attributes = attributes;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if(object instanceof EdgeType) {
			return id==((EdgeType)object).getId();
		} else return false;
	}
}
