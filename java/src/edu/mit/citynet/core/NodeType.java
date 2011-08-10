package edu.mit.citynet.core;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;

import edu.mit.citynet.util.CityNetIcon;

/**
 * The NodeType class is used to define a classification of nodes to be used in
 * the synthesis template. Each NodeType is given a name, rgbColor (values for 
 * red, green, and blue between 0 and 1 to define the display color in the 
 * rendering), and a set of NodeTypeAttribute objects.
 */
public class NodeType {
	private int id;
	private String name, description;
	private Color color;
	private Set<NodeTypeAttribute> attributes;
	
	/**
	 * Instantiates a new node type and initializes the attributes to an empty 
	 * set.
	 */
	public NodeType() {
		attributes = new HashSet<NodeTypeAttribute>();
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
	public Set<NodeTypeAttribute> getAttributes() {
		return new HashSet<NodeTypeAttribute>(attributes);
	}
	
	/**
	 * Sets the attributes.
	 *
	 * @param attributes the new attributes
	 */
	public void setAttributes(Set<NodeTypeAttribute> attributes) {
		this.attributes = attributes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if(object instanceof NodeType) {
			return id==((NodeType)object).getId();
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "NodeType" + id;
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
				CityNetIcon.NODE_TYPE_BLANK.getIcon().getImage().getSource(), filter);
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(producer));
	}
}
