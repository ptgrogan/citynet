/*
 * CityNet: Integrated Urban Development Decision Support Tool
 * 
 * Copyright (c) 2011 MIT Strategic Engineering Research Group
 */
package edu.mit.citynet.gui;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;

import edu.mit.citynet.core.Layer;

/**
 * The Class DisplayHeightSlider.
 * 
 * @author Paul Grogan, ptgrogan@mit.edu
 */
public class DisplayHeightSlider extends JSlider {
	private static final long serialVersionUID = 1723043061074059089L;
	private static final int MIN_HEIGHT = 0, MAX_HEIGHT = 50;
	private final DecimalFormat format = new DecimalFormat("0.0");
	
	/**
	 * Instantiates a new display height slider.
	 *
	 * @param orientation the orientation
	 */
	public DisplayHeightSlider(int orientation) {
		super(orientation, MIN_HEIGHT, MAX_HEIGHT, 0);
		setMajorTickSpacing(5);
		setMinorTickSpacing(1);
		setSnapToTicks(true);
		setPaintTicks(true);
		setPaintLabels(true);
		setToolTipText("Height at which to display layer");
	}
	
	/**
	 * Gets the display height.
	 *
	 * @return the display height
	 */
	public double getDisplayHeight() {
		return getValue()/5d;
	}
	
	/**
	 * Sets the display height.
	 *
	 * @param displayHeight the new display height
	 */
	public void setDisplayHeight(double displayHeight) {
		setValue(Math.max(MIN_HEIGHT, Math.min((int)(displayHeight*5),MAX_HEIGHT)));
	}
	
	/**
	 * Load labels.
	 *
	 * @param layers the layers
	 * @param layer the layer
	 */
	public void loadLabels(Collection<Layer> layers, Layer layer) {
		Hashtable<Integer, JLabel> labelDictionary = new Hashtable<Integer, JLabel>();
		for(Layer l : layers) {
			if(l.equals(layer)) continue;
			JLabel label = labelDictionary.get((int)(l.getDisplayHeight()*5));
			if(label!=null) {
				label.setText(label.getText() + ", " + l.getName());
			} else {
				labelDictionary.put((int)(l.getDisplayHeight()*5), 
						new JLabel(format.format(l.getDisplayHeight()) + ": " + l.getName()));
			}
		}
		for(int i=MIN_HEIGHT; i<=MAX_HEIGHT; i+=5) {
			if(labelDictionary.get(i)==null
					&& labelDictionary.get(i-1)==null
					&& labelDictionary.get(i-2)==null
					&& labelDictionary.get(i-3)==null
					&& labelDictionary.get(i-4)==null
					&& labelDictionary.get(i+1)==null
					&& labelDictionary.get(i+2)==null
					&& labelDictionary.get(i+3)==null
					&& labelDictionary.get(i+4)==null)
				labelDictionary.put(i,new JLabel(format.format(i/5d)));
		}
		setLabelTable(labelDictionary);
	}
}
