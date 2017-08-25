/* PyramidPanel.java
 * Panel which shows the main part of the visualisation; a representation of the pyramid
 * and its solutions.
 */

package p3.views;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

import p3.*;

public class PyramidPanel {
	private JPanel mainPanel;
	private JPanel pyramidPanel, pyramidVBoxPanel;

	public PyramidPanel() {
		mainPanel = new JPanel();
		mainPanel.setMinimumSize(new Dimension(500,400));
		mainPanel.setLayout(new BorderLayout());
		
		pyramidPanel = new JPanel();
		pyramidPanel.setLayout(new GridBagLayout());
		pyramidVBoxPanel = new JPanel();
		pyramidVBoxPanel.setLayout(new BoxLayout(pyramidVBoxPanel, BoxLayout.Y_AXIS));
		pyramidPanel.add(pyramidVBoxPanel);

		JScrollPane scrollPane = new JScrollPane(pyramidPanel);
		mainPanel.add(scrollPane);
	}
	
	public JPanel getPanel() {
		return mainPanel;
	}
	
	public void visPyramid(P3 p3) {
		Pyramid pyramid = p3.getPyramid();
		pyramidVBoxPanel.removeAll();
		ArrayList<Population> popList = pyramid.getPyramid();
		for (int i=0; i<pyramid.getPopCount(); i++) {
			boolean isCurrent = i==p3.getCurrentPopIndex();
			PopulationPanel popPanel = new PopulationPanel(popList.get(i), i, isCurrent, p3);
			
			//Constraint 0 to add to top of BoxLayout, stacking pyramid up
			pyramidVBoxPanel.add(popPanel.getPanel(), 0);
		}
		mainPanel.revalidate();
	}
	
	public void reset() {
		pyramidVBoxPanel.removeAll();
	}

}