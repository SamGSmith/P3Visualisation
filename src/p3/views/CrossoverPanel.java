/* CrossoverPanel.java
 * Shows the current solution, the donor solution and current cluster, and their result 
 * after crossover.
 */

package p3.views;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.Border;

import p3.*;
import p3.fitness.FitnessFn;

public class CrossoverPanel {
	private JPanel mainPanel = new JPanel();
	private JLabel currentBitstring, donorBitstring, crossBitstring;
	private JLabel currentFitLabel, donorFitLabel, crossFitLabel;
	private JLabel crossoverFinishedLabel;
	private JPanel cListPanel = new JPanel(new BorderLayout());
	private JButton skipButton = new JButton("Skip to end of current population Crossover");
	
	private JPanel currentBarPanel = new JPanel(new BorderLayout());
	private JPanel donorBarPanel = new JPanel(new BorderLayout());
	private JPanel crossBarPanel = new JPanel(new BorderLayout());

	// Constructor, adds components to mainPanel
	public CrossoverPanel() {
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(4, 4, 4, 4);
		c.weightx = 0.1;
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(new JLabel("Cluster Model List", SwingConstants.CENTER), c);
		
		c.insets = new Insets(4, 4, 16, 4);
		c.gridy = 1;
		cListPanel.setMinimumSize(new Dimension(46,46));
		mainPanel.add(cListPanel, c);

		c.gridy = 2;
		JPanel tableContainer = new JPanel(new BorderLayout());
		tableContainer.add(makeTable());
		tableContainer.setMinimumSize(new Dimension(160,200));
		mainPanel.add(tableContainer, c);
		
		c.insets = new Insets(2, 4, 2, 4);
		c.fill = GridBagConstraints.NONE;
		c.gridy = 3;
		crossoverFinishedLabel = new JLabel("", SwingConstants.CENTER);
		mainPanel.add(crossoverFinishedLabel, c);
		
		c.gridy = 4;
		mainPanel.add(skipButton, c);
		
	}
	
	// Lays out crossover table components
	private JScrollPane makeTable() {
		JPanel tablePanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(6, 6, 6, 6);
		c.gridx = 0;
		c.gridy = 1;
		tablePanel.add(new JLabel("Current Solution", SwingConstants.CENTER), c);
		
		c.gridy = 2;
		tablePanel.add(new JLabel("Donor Solution", SwingConstants.CENTER), c);
		
		c.gridy = 3;
		tablePanel.add(new JLabel("Crossover Solution", SwingConstants.CENTER), c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 4;
		tablePanel.add(new JSeparator(SwingConstants.VERTICAL), c);
		c.gridx = 3;
		tablePanel.add(new JSeparator(SwingConstants.VERTICAL), c);
		c.gridx = 5;
		tablePanel.add(new JSeparator(SwingConstants.VERTICAL), c);
		
		c.gridheight = 1;
		c.gridx = 2;
		c.gridy = 0;
		tablePanel.add(new JLabel("Solution Bitstring"), c);
		c.gridy = 1;
		currentBitstring = new JLabel();
		tablePanel.add(currentBitstring, c);
		c.gridy = 2;
		donorBitstring = new JLabel();
		tablePanel.add(donorBitstring, c);
		c.gridy = 3;
		crossBitstring = new JLabel();
		tablePanel.add(crossBitstring, c);
		
		c.gridx = 4;
		c.gridy = 0;
		tablePanel.add(new JLabel("Fitness"), c);
		c.gridy = 1;
		currentFitLabel = new JLabel();
		tablePanel.add(currentFitLabel, c);
		c.gridy = 2;
		donorFitLabel = new JLabel();
		tablePanel.add(donorFitLabel, c);
		c.gridy = 3;
		crossFitLabel = new JLabel();
		tablePanel.add(crossFitLabel, c);
		
		c.gridx = 6;
		c.gridy = 0;
		tablePanel.add(new JLabel("Barcode Representation"), c);
		c.gridy = 1;
		tablePanel.add(currentBarPanel, c);
		c.gridy = 2;
		tablePanel.add(donorBarPanel, c);
		c.gridy = 3;
		tablePanel.add(crossBarPanel, c);
		
		
		JScrollPane tableScrollPane = new JScrollPane(tablePanel);
		return tableScrollPane;
	}
	
	// Fill the table with the relevant values for this crossover based on CrossoverTracker
	public void displayCrossover(CrossoverTracker crossoverTracker, FitnessFn ff) {
		cListPanel.removeAll();
		
		currentBarPanel.removeAll();
		donorBarPanel.removeAll();
		crossBarPanel.removeAll();
		
		//Only update if crossoverTracker has been defined
		if(crossoverTracker!=null) {
			Solution currentSol = crossoverTracker.getCurrentSolution();
			Solution donorSol = crossoverTracker.getCurrentDonor();
			Solution crossSol = crossoverTracker.getCurrentCross();
			StringBuilder currentSB = new StringBuilder("<html><nobr>");
			StringBuilder donorSB = new StringBuilder("<html><nobr>");
			StringBuilder crossSB = new StringBuilder("<html><nobr>");
			
			int[] currentCluster = crossoverTracker.getCurrentCluster();
			if (currentCluster!=null) {
				setClusterListLabel(crossoverTracker);
				
				setBarcodes(currentSol, donorSol, crossSol);
				
				Set<Integer> clusterSet = new HashSet<Integer>();
				for (int i : currentCluster) {
					clusterSet.add(new Integer(i));
				}
				
				for(int i=0; i<currentSol.size(); i++) {
					char currentChar = (currentSol.get(i)) ? '1' : '0';
					char donorChar = (donorSol.get(i)) ? '1' : '0';
					char crossChar = (crossSol.get(i)) ? '1' : '0';
					
					// Check current index i in cluster. Highlight bits from donor in red
					// and from current solution in blue, to show crossover.
					if (clusterSet.contains(i)) {
						currentSB.append(currentChar);
						donorSB.append("<font color='red'>"+donorChar+"</font>");
						crossSB.append("<font color='red'>"+crossChar+"</font>");				
					} else {
						currentSB.append("<font color='blue'>"+currentChar+"</font>");
						donorSB.append(donorChar);
						crossSB.append("<font color='blue'>"+crossChar+"</font>");				
					}
					
					
				}
				
				Double currentFit = currentSol.getFitness(ff);
				currentFitLabel.setText(Double.toString(currentFit));
				
				Double donorFit = donorSol.getFitness(ff);
				donorFitLabel.setText(Double.toString(donorFit));
				
				// Show crossover fitness, along with arrow indicating improvement
				// or otherwise.
				Double crossFit = crossSol.getFitness(ff);
				String fitArrow;
				if (crossFit>currentFit) {
					fitArrow = " \u2191";
				} else if (crossFit<currentFit) {
					fitArrow = " \u2193";					
				} else {
					fitArrow = " -";										
				}
				crossFitLabel.setText(Double.toString(crossFit)+fitArrow);
				
			} else {
				currentFitLabel.setText("");
				donorFitLabel.setText("");
				crossFitLabel.setText("");
				setCListPanel("[]");
			}
			
			currentSB.append("</nobr></html>");
			donorSB.append("</nobr></html>");
			crossSB.append("</nobr></html>");
			
			currentBitstring.setText(currentSB.toString());
			donorBitstring.setText(donorSB.toString());
			crossBitstring.setText(crossSB.toString());
			
			if(crossoverTracker.crossoverFinished()) {
				Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 1);
				crossoverFinishedLabel.setBorder(greenBorder);
				crossoverFinishedLabel.setText("<html><nobr><b>Crossover Completed for Population</b></html>");
			} else {
				Border redBorder = BorderFactory.createLineBorder(Color.RED, 1);
				crossoverFinishedLabel.setBorder(redBorder);
				crossoverFinishedLabel.setText("<html><nobr>Crossover Incomplete for Population</html>");
			}
		}
	}
	
	private void setBarcodes(Solution currentSol, Solution donorSol, Solution crossSol) {
		currentBarPanel.add(new Barcode(currentSol.toString()));
		donorBarPanel.add(new Barcode(donorSol.toString()));
		crossBarPanel.add(new Barcode(crossSol.toString()));
	}
	
	// Essentially same cluster list shown in clusteringpanel, but with current cluster
	// highlighted in red.
	private void setClusterListLabel(CrossoverTracker crossoverTracker) {
		ArrayList<int[]> clusterList = crossoverTracker.getClusterList();
		StringBuilder modelSB = new StringBuilder();
		modelSB.append("<html>");
		for (int i=0; i<clusterList.size(); i++) {
			String clusterString = Arrays.toString(clusterList.get(i));
			if(i==crossoverTracker.getClusterIndex()) {
				//modelSB.append("<font color='red'>"+clusterString+"</font>"+", ");
				modelSB.append("<u>"+clusterString+"</u>"+", ");
				//modelSB.append("<b>"+clusterString+"</b>"+", ");
				//modelSB.append(" \u2192 "+clusterString+" \u2190 "+", ");
			} else {
				modelSB.append(clusterString+", ");
			}
		}
		if(clusterList.size()>0) {
			modelSB.delete(modelSB.length()-2, modelSB.length());
		}
		modelSB.append("</html>");
		
		setCListPanel(modelSB.toString());
	}
	
	private void setCListPanel(String clusterString) {
		JPanel listPanel = new JPanel();
		
		//Put cluster list in JEditorPane so that bold text works.
		//JEditorPane modelListPane = new JEditorPane();
		//modelListPane.setContentType("text/html");
		//modelListPane.setText(clusterString);
		//modelListPane.setEditable(false);
		//modelListPane.setOpaque(false);
		//modelListPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
		//listPanel.add(modelListPane);
		
		listPanel.add(new JLabel(clusterString));
		JScrollPane listScrollPane = new JScrollPane(listPanel);
		cListPanel.add(listScrollPane);	
	}
	
	public JPanel getPanel() {
		return mainPanel;
	}
	
	public void skipButtonEnabled(boolean enabled) {
		skipButton.setEnabled(enabled);
	}
	
	public void addSkipListener(ActionListener skipListener) {
		skipButton.addActionListener(skipListener);
	}
}
