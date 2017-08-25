/* p3GUI.java
 * Class extending JFrame which contains the setup and main visualisation
 * panel, and switches between them. Contains instances of the various
 * sub panels, and has get methods for each.
 */

package p3.views;

import java.awt.*;
import javax.swing.*;

public class P3GUI {
	
	private JFrame frame = new JFrame();
	private Container contentPane = frame.getContentPane();
	private MenuPanel menuPanel = new MenuPanel();
	private PlaybackPanel playbackPanel = new PlaybackPanel();
	private PyramidPanel pyramidPanel = new PyramidPanel();
	private HillClimbPanel hillClimbPanel = new HillClimbPanel();
	private ClusteringPanel clusterPanel = new ClusteringPanel();
	private CrossoverPanel crossoverPanel = new CrossoverPanel();
	private VisualisationPanel visPanel = new VisualisationPanel(playbackPanel,
			pyramidPanel, hillClimbPanel, clusterPanel, crossoverPanel);
	
	public P3GUI() {
		frame.setTitle("P3 Visualisation");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		contentPane.setLayout(new CardLayout());

		contentPane.add(menuPanel.getPanel(), "P3 Setup");
		
		contentPane.add(visPanel.getPanel(), "P3 Visualisation");
		
        frame.pack();
        frame.setMinimumSize(new Dimension(1200, 700));
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
		
	}

	public void switchToVis() {
		CardLayout cl = (CardLayout)(contentPane.getLayout());
		cl.show(contentPane, "P3 Visualisation");
	}
	
	public void reset() {
		CardLayout cl = (CardLayout)(contentPane.getLayout());
		cl.show(contentPane, "P3 Setup");
	}
	
	public MenuPanel getMenuPanel() {
		return menuPanel;
	}
	
	public VisualisationPanel getVisPanel() {
		return visPanel;
	}

	public PlaybackPanel getPlaybackPanel() {
		return playbackPanel;
	}
	
	public PyramidPanel getPyramidPanel() {
		return pyramidPanel;
	}
	
	public HillClimbPanel getHillClimbPanel() {
		return hillClimbPanel;
	}
	
	public ClusteringPanel getClusterPanel() {
		return clusterPanel;
	}
	
	public CrossoverPanel getCrossoverPanel() {
		return crossoverPanel;
	}
}