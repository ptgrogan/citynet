/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.viz;

import java.text.DecimalFormat;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;

/**
 * The OpacitySlider class.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class OpacitySlider extends JSlider {
	private static final long serialVersionUID = 7809848979149011663L;
	public static final int NUM_TICKS = 20;
	public static final float TICK_SIZE = 1f/NUM_TICKS;
	private static DecimalFormat format = new DecimalFormat("0.0");
	
	/**
	 * Instantiates a new opacity slider.
	 */
	public OpacitySlider() {
		super(JSlider.HORIZONTAL, 0, NUM_TICKS, 0);
		setMajorTickSpacing(5);
		setMinorTickSpacing(1);
		setSnapToTicks(true);
		setPaintTicks(true);
		setPaintLabels(true);
		Hashtable<Integer, JLabel> labelDictionary = new Hashtable<Integer, JLabel>();
		for(int i=0; i<=20; i+=5) {
			labelDictionary.put(i,new JLabel(format.format((i*TICK_SIZE))));
		}
		setLabelTable(labelDictionary);
	}
}
