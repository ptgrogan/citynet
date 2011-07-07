package edu.mit.citynet.viz;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import edu.mit.citynet.core.CellRegion;
import edu.mit.citynet.core.NodeRegion;

public class CellRegionLayer extends JPanel {
	private static final long serialVersionUID = -5721250566037759894L;
	private CityVizPanel cityVizPanel;
	private CellRegion cellRegion;
	
	/**
	 * Instantiates a new cell region layer.
	 *
	 * @param city the city
	 */
	public CellRegionLayer(CityVizPanel cityVizPanel, CellRegion cellRegion) {
		this.cityVizPanel = cityVizPanel;
		this.cellRegion = cellRegion;
		setOpaque(false);
	}
	
	/**
	 * Sets the cell region.
	 *
	 * @param cellRegion the new node region
	 */
	public void setCellRegion(CellRegion cellRegion) {
		this.cellRegion = cellRegion;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(cityVizPanel.getCity().getImage() == null 
				|| cityVizPanel.getCity().getImagePolygon() == null) return;
		
		Set<CellRegion> cellRegions = new HashSet<CellRegion>();
		if(cellRegion!=null) {
			cellRegions.add(cellRegion);
		} else {
			cellRegions.addAll(cityVizPanel.getCity().getCellRegions());
		}
		
		for(CellRegion cellRegion : cellRegions) {
			int[] xPoints = new int[cellRegion.getCoordinateList().size()];
			int[] yPoints = new int[cellRegion.getCoordinateList().size()];
			for(int i=0; i<cellRegion.getCoordinateList().size(); i++) {
				double x = cellRegion.getCoordinateList().getCoordinate(i).x;
				double y = cellRegion.getCoordinateList().getCoordinate(i).y;
				int[] ij = cityVizPanel.xy2ij(x,y);
				xPoints[i] = ij[0];
				yPoints[i] = ij[1];
			}
			if(g instanceof Graphics2D) {
				Graphics2D g2d = (Graphics2D)g;
				g2d.setStroke(new BasicStroke(2f));
				g2d.setColor(Color.BLACK);
				g2d.drawPolygon(xPoints, yPoints, xPoints.length);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				g2d.fillPolygon(xPoints, yPoints, xPoints.length);
			}
		}
	}
}
