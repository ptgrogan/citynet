package edu.mit.citynet.core;

/**
 * The NodeTypeAttribute class is used to describe an aspect of a particular 
 * NodeType. Each NodeTypeAttribute is given a name and numeric value.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class NodeTypeAttribute extends AbstractAttribute {
	private int id;
	
	/**
	 * Instantiates a new node type attribute.
	 */
	public NodeTypeAttribute() {
		setName("New Node Type Attribute");
		setUnits("-");
		setBounds("[0,inf)");
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if(object instanceof NodeTypeAttribute) {
			return id==((NodeTypeAttribute)object).getId();
		} else return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "NodeTypeAttribute" + id;
	}
}