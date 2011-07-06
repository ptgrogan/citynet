package edu.mit.citynet;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.infonode.gui.laf.InfoNodeLookAndFeel;
import net.infonode.gui.laf.InfoNodeLookAndFeelThemes;
import edu.mit.citynet.gui.CityNetFrame;
import edu.mit.citynet.io.SpreadsheetTemplate;

public class TestScript {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
					UIManager.setLookAndFeel(new InfoNodeLookAndFeel(
							InfoNodeLookAndFeelThemes.getSoftGrayTheme()));
					System.out.println("Launching City.Net...");
					CityNetFrame f = new CityNetFrame();
					f.pack();
					f.setLocationRelativeTo(null);
					f.setVisible(true);
					SpreadsheetTemplate t = new SpreadsheetTemplate();
					t.setFilePath("synthesisExample.xls");
					try {
						t.readTemplate();
					} catch (IOException e) {
						e.printStackTrace();
					}
					f.openCityCommand(CityNet.getInstance().getCity());
            	} catch(Exception e) {
            		JOptionPane.showMessageDialog(null, 
            				"A fatal exception of type " + 
            				e.getClass().getSimpleName() + "occurred while " + 
            				"launching City.Net.\nPlease consult the stack " + 
            				"trace for more information.");
					e.printStackTrace();
				}
            }
		});
	}
}
