/* SolutionPanel.java
 * Class which creates a panel with a representation of the genotype and fitness.
 * Coloured differently if the optimum/current solution.
 */

package p3.views;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.Border;

import p3.*;
import p3.fitness.FitnessFn;

public class SolutionPanel {
	private JPanel mainPanel;
	
	// Constructor for a specified solution
	public SolutionPanel(Solution sol, P3 p3) {
		FitnessFn ff = p3.getFF();
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(4,4,4,4);

		Color borderColor;
		int borderWeight;
		if (sol.getCreationStep()==p3.getStepCount()-1) {
			borderWeight = 3;
			borderColor = Color.RED;
			
		} else if(sol.equals(p3.getCurrentSolution())) {
			borderWeight = 3;
			borderColor = Color.ORANGE;
		} else {
			borderWeight = 3;
			borderColor = Color.LIGHT_GRAY;
		}
		Border border = BorderFactory.createLineBorder(borderColor, borderWeight);
		mainPanel.setBorder(border);
		
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(new Barcode(sol.toString()), c);
		
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(new JLabel(Double.toString(sol.getFitness(ff))), c);
		
		if (sol.getFitness(ff)==ff.getOptimalFitness()) {
			mainPanel.setBackground(Color.GREEN);
		} else if (sol.equals(p3.getFittestSolution())) {
			mainPanel.setBackground(Color.YELLOW);			
		}
		
		mainPanel.addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent e) {
				StringBuilder s = new StringBuilder();
				s.append("Genotype as Binary String:\n");
				s.append(sol.toString());
				
				//JOptionPane.showMessageDialog(mainPanel, s.toString());
				
				JTextArea genotypeBox = new JTextArea(s.toString());
				genotypeBox.setEditable(false);
				genotypeBox.setOpaque(false);
				JOptionPane.showMessageDialog(null, genotypeBox, "Genotype", JOptionPane.INFORMATION_MESSAGE);
            }
		});
	}
	
	// Constructor for a blank solution
	public SolutionPanel(int solSize) {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(4,4,4,4);
		
		Color borderColor = Color.LIGHT_GRAY;
		int borderWeight = 2;
		Border border = BorderFactory.createLineBorder(borderColor, borderWeight);
		mainPanel.setBorder(border);
		
		c.gridx = 0;
		c.gridy = 0;
		// Imitate an empty barcode panel
		JPanel emptyBarcode = new JPanel();
		emptyBarcode.setPreferredSize(new Dimension(2+solSize*2, 34));
		Border barcodeBorder = BorderFactory.createLineBorder(Color.BLUE, 1);
		emptyBarcode.setBorder(barcodeBorder);
		mainPanel.add(emptyBarcode, c);
		
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(new JLabel("--"), c);
		
	}
	
	public JPanel getPanel() {
		return mainPanel;
	}
	
}