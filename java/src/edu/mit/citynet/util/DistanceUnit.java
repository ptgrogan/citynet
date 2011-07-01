package edu.mit.citynet.util;

/**
 * Enumerates the allowable units of distance used. Provides methods to access
 * display names as well as conversion factors.
 * 
 * @author Paul, ptgrogan@mit.edu
 */
public enum DistanceUnit {
	
	/** English measuring system: foot. */
	FOOT("foot","feet","ft",3280.8399),
	
	/** English system: mile. */
	MILES("miles","miles","mi",0.621371192),
	
	/** Metric system: meter. */
	METERS("meter","meters","m",1000d),
	
	/** Metric system: kilometer. */
	KILOMETERS("kilometer","kilometers","km",1d);

	private String singularName, pluralName, abbreviation;
	private double numberPerKilometer;
	
	/**
	 * Instantiates a new distance units.
	 *
	 * @param singularName the singular name
	 * @param pluralName the plural name
	 * @param abbreviation the abbreviation
	 * @param numberPerKilometer the number of units per kilometer
	 */
	private DistanceUnit(String singularName, String pluralName, 
			String abbreviation, double numberPerKilometer) {
		this.singularName = singularName;
		this.pluralName = pluralName;
		this.abbreviation = abbreviation;
		this.numberPerKilometer = numberPerKilometer;
	}
	
	/**
	 * Gets the singlular name.
	 *
	 * @return the singlular name
	 */
	public String getSinglularName() { 
		return singularName;
	}
	
	/**
	 * Gets the plural name.
	 *
	 * @return the plural name
	 */
	public String getPluralName() { 
		return pluralName;
	}
	
	/**
	 * Gets the abbreviation.
	 *
	 * @return the abbreviation
	 */
	public String getAbbreviation() { 
		return abbreviation;
	}
	
	/**
	 * Gets the number of units per kilometer.
	 *
	 * @return the number of units per kilometer
	 */
	public double getNumberPerKilometer() { 
		return numberPerKilometer;
	}
	
	/**
	 * Gets the conversion factor to specified distance unit.
	 *
	 * @param distanceUnit the distance unit
	 * @return the conversion factor
	 */
	public double getConversionFactorTo(DistanceUnit distanceUnit) {
		return distanceUnit.getNumberPerKilometer()/getNumberPerKilometer();
	}
}
