/* visualisationPanel.java
 * Class with a JPanel which contains the panels with the pyramid, 
 * algorithm stage label, playback controls and additional visualisations.
 */

package p3.views;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import p3.P3;

public class VisualisationPanel {
	
	private JPanel mainPanel;
	private JLabel localStageLabel = new JLabel("\u2192 Local Search");
	private JLabel clusterStageLabel = new JLabel("\u2003 Cluster Model Building");
	private JLabel crossoverStageLabel = new JLabel("\u2003 Crossover");
	private CardLayout cardLayout = new CardLayout();
	private JPanel additionalVisPanel = new JPanel(cardLayout);
	private CurrentSolutionPanel currentSolPanel;
	
	// Constructor for VisualisationPanel, requires objects for each subpanel so that
	// their methods can be called without an egregious number of get methods etc
	public VisualisationPanel(PlaybackPanel playbackPanel, PyramidPanel pyramidPanel,
			HillClimbPanel hillClimbPanel, ClusteringPanel clusterPanel,
				CrossoverPanel crossoverPanel) {
		
		mainPanel = new JPanel();
		
		Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
				
		mainPanel.setLayout(new BorderLayout());
		
		JPanel sidePanel = new JPanel(new BorderLayout());
		JPanel stagePanel = new JPanel(new BorderLayout());
		stagePanel.setBorder(border);
		stagePanel.setLayout(new BoxLayout(stagePanel, BoxLayout.Y_AXIS));
		stagePanel.add(new JLabel("Current Algorithm Stage"));
		stagePanel.add(localStageLabel);
		stagePanel.add(clusterStageLabel);
		stagePanel.add(crossoverStageLabel);
		
		additionalVisPanel.setBorder(border);
		additionalVisPanel.add(hillClimbPanel.getPanel(), "Local Search");
		additionalVisPanel.add(clusterPanel.getPanel(), "Clustering");
		additionalVisPanel.add(crossoverPanel.getPanel(), "Crossover");
		
		sidePanel.add(stagePanel, BorderLayout.PAGE_START);
		sidePanel.add(additionalVisPanel, BorderLayout.CENTER);
		
		currentSolPanel = new CurrentSolutionPanel();
		sidePanel.add(currentSolPanel.getPanel(), BorderLayout.SOUTH);
		
		pyramidPanel.getPanel().setBorder(border);
		playbackPanel.getPanel().setBorder(border);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pyramidPanel.getPanel(), sidePanel);
		splitPane.setDividerLocation(700);
		splitPane.setResizeWeight(0.6);
		sidePanel.setMinimumSize(new Dimension(300,600));
		mainPanel.add(splitPane);
		
		mainPanel.add(playbackPanel.getPanel(), BorderLayout.PAGE_END);
	}
	
	public void setCurrentSol(P3 p3) {
		currentSolPanel.setCurrentSolution(p3);
	}
	
	public void setStage(String p3Stage) {
		//Change stage label text and Set card for additionalVisPanel
		if (p3Stage.equals("Local Search")||p3Stage.equals("New Random Solution")) {
			localStageLabel.setText("\u2192 Local Search");
			clusterStageLabel.setText("\u2003 Cluster Model Building");
			crossoverStageLabel.setText("\u2003 Crossover");
			cardLayout.show(additionalVisPanel, "Local Search");
		} 
		else if (p3Stage.equals("Clustering")) {
			localStageLabel.setText("\u2003 Local Search");
			clusterStageLabel.setText("\u2192 Cluster Model Building");
			crossoverStageLabel.setText("\u2003 Crossover");
			cardLayout.show(additionalVisPanel, "Clustering");			
		} 
		else if (p3Stage.equals("Crossover")) {
			localStageLabel.setText("\u2003 Local Search");
			clusterStageLabel.setText("\u2003 Cluster Model Building");
			crossoverStageLabel.setText("\u2192 Crossover");
			cardLayout.show(additionalVisPanel, "Crossover");
		} 
		else {
			System.out.println("Stage name received: "+p3Stage);
		}
	}
	
	public JPanel getPanel() {
		return mainPanel;
	}
	
}