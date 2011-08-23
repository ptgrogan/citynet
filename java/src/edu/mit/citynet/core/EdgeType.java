/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.core;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ImageIcon;

import edu.mit.citynet.CityNet;
import edu.mit.citynet.util.CityNetIcon;

/**
 * The EdgeType class is used to define a classification of edges to be used in
 * the synthesis template. Each EdgeType is given a name, rgbColor (values for 
 * red, green, and blue between 0 and 1 to define the display color in the 
 * rendering), and a set of EdgeTypeAttribute objects.
 */
public class EdgeType implements Cloneable {
	private int id;
	private String name, description;
	private Color color;
	private List<EdgeTypeAttribute> attributes;
	
	/**
	 * Instantiates a new node type and initializes the attributes to an empty 
	 * set.
	 */
	public EdgeType() {
		name = "New Edge Type";
		color = Color.red;
		attributes = new ArrayList<EdgeTypeAttribute>();
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
	public List<EdgeTypeAttribute> getAttributes() {
		return new ArrayList<EdgeTypeAttribute>(attributes);
	}
	
	/**
	 * Adds the attribute.
	 *
	 * @param attribute the attribute
	 * @return true, if successful
	 */
	public boolean addAttribute(EdgeTypeAttribute attribute) {
		if(attribute.getId()==0)
			attribute.setId(CityNet.getInstance().getNextEdgeTypeAttributeId());
		return attributes.add(attribute);
	}
	
	/**
	 * Sets the attributes.
	 *
	 * @param attributes the new attributes
	 * @return true, if successful
	 */
	public boolean addAllAttributes(Collection<EdgeTypeAttribute> attributes) {
		return this.attributes.addAll(attributes);
	}
	
	/**
	 * Removes the all attributes.
	 *
	 * @param attributes the attributes
	 * @return true, if successful
	 */
	public boolean removeAllAttributes(Collection<EdgeTypeAttribute> attributes) {
		return this.attributes.removeAll(attributes);
	}
	
	/**
	 * Sets the attributes.
	 *
	 * @param attributes the new attributes
	 */
	public void setAttributes(Collection<EdgeTypeAttribute> attributes) {
		this.attributes.clear();
		this.attributes.addAll(attributes);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if(object instanceof EdgeType) {
			return id==((EdgeType)object).getId();
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "EdgeType" + id;
	}
	
	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public ImageIcon getIcon() {
		ImageFilter filter = new RGBImageFilter() {
			public int filterRGB(int x, int y, int rgb) {
				return rgb & color.getRGB();
			}
		};
		ImageProducer producer = new FilteredImageSource(
				CityNetIcon.EDGE_TYPE_BLANK.getIcon().getImage().getSource(), filter);
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(producer));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public EdgeType clone() {
		EdgeType clone = new EdgeType();
		clone.setId(0);
		clone.setName(getName() + " (Copy)");
		clone.setDescription(getDescription());
		for(EdgeTypeAttribute attribute : getAttributes())
			clone.addAttribute(attribute.clone());
		clone.setColor(getColor());
		return clone;
	}
}
