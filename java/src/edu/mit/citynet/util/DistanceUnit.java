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

	private String _singularName, _pluralName, _abbreviation;
	private double _numberPerKilometer;
	
	/**
	 * Instantiates a new distance units.
	 *
	 * @param singularName the singular name
	 * @param pluralName the plural name
	 * @param abbreviation the abbreviation
	 * @param numberPerKilometer the number of units per kilometer
	 */
	private DistanceUnit(String singularName, String pluralName, String abbreviation, double numberPerKilometer) {
		_singularName = singularName;
		_pluralName = pluralName;
		_abbreviation = abbreviation;
		_numberPerKilometer = numberPerKilometer;
	}
	
	/**
	 * Gets the singlular name.
	 *
	 * @return the singlular name
	 */
	public String getSinglularName() { return _singularName; }
	
	/**
	 * Gets the plural name.
	 *
	 * @return the plural name
	 */
	public String getPluralName() { return _pluralName; }
	
	/**
	 * Gets the abbreviation.
	 *
	 * @return the abbreviation
	 */
	public String getAbbreviation() { return _abbreviation; }
	
	/**
	 * Gets the number of units per kilometer.
	 *
	 * @return the number of units per kilometer
	 */
	public double getNumberPerKilometer() { return _numberPerKilometer; }
}
