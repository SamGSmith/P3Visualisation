/* HillClimbPanel
 * Panel showing progression from random solution to local optimum, and the fitness
 * transition over time.
 */

package p3.views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import p3.*;
import p3.fitness.*;

public class HillClimbPanel implements ActionListener {
	private int solSize;
	private JPanel mainPanel = new JPanel();
	private JPanel graphContainer = new JPanel(new BorderLayout());
	private JPanel inputSolContainer = new JPanel();
	private JPanel outputSolContainer = new JPanel();

	// Constructor, lays out components appropriately
	public HillClimbPanel() {

		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(2, 2, 2, 2);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		//c.weightx = 0.05;
		c.weighty = 0.05;
		c.gridx = 0;
		c.gridy = 0;
		String graphTitle = "Solution Fitness by Number of Hill Climb Steps";
		
		JPanel graphTitleContainer = new JPanel(new BorderLayout());
		graphTitleContainer.add(new JLabel(graphTitle, SwingConstants.CENTER));
		JButton helpButton = new JButton("?");
		helpButton.setMargin(new Insets(1,1,1,1));
		helpButton.addActionListener(this);
		graphTitleContainer.add(helpButton, BorderLayout.EAST);
		mainPanel.add(graphTitleContainer, c);
		//mainPanel.add(new JLabel(graphTitle, SwingConstants.CENTER), c);
				
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(8, 8, 8, 8);
		c.weightx = 0.9;
		c.weighty = 0.9;
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(graphContainer, c);
		
		c.insets = new Insets(2, 2, 2, 2);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		c.weightx = 1;
		c.weighty = 0.05;
		
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		JPanel randSolContainer = new JPanel(new BorderLayout());
		randSolContainer.add(new JLabel("Random Solution", SwingConstants.CENTER), BorderLayout.NORTH);
		randSolContainer.add(inputSolContainer, BorderLayout.CENTER);
		mainPanel.add(randSolContainer, c);
		
		//mainPanel.add(new JLabel("Random Solution", SwingConstants.CENTER), c);
		c.gridx = 0;
		c.gridy = 3;
		//mainPanel.add(inputSolContainer, c);

		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.1;
		//c.gridheight = 2;
		c.gridx = 1;
		c.gridy = 2;
		mainPanel.add(new JLabel("\u2192", SwingConstants.CENTER), c);

		c.anchor = GridBagConstraints.WEST;
		c.gridheight = 1;
		c.weightx = 1;
		c.gridx = 2;
		c.gridy = 2;

		c.gridx = 2;
		c.gridy = 2;
		JPanel climbSolContainer = new JPanel(new BorderLayout());
		climbSolContainer.add(new JLabel("After Local Search", SwingConstants.CENTER), BorderLayout.NORTH);
		climbSolContainer.add(outputSolContainer, BorderLayout.CENTER);
		mainPanel.add(climbSolContainer, c);
		
		//mainPanel.add(new JLabel("After Local Search", SwingConstants.CENTER), c);

		c.gridx = 2;
		c.gridy = 3;
		//mainPanel.add(outputSolContainer, c);

	}
	
	// Used to show a blank graph and blank solutions
	public void showBlank(int solSize, FitnessFn ff) {
		graphContainer.removeAll();
		graphContainer.add(new FitnessGraph(new ArrayList<Double>(), ff));
		
		this.solSize = solSize;	
		inputSolContainer.removeAll();
		inputSolContainer.add(new SolutionPanel(solSize).getPanel());

		outputSolContainer.removeAll();
		outputSolContainer.add(new SolutionPanel(solSize).getPanel());
	}

	// Used to show after an initial random solution has been defined, but before search
	public void showInitialRandom(Solution inputSol, P3 p3) {
		graphContainer.removeAll();
		ArrayList<Double> initProgressionList = new ArrayList<Double>();
		initProgressionList.add(inputSol.getFitness(p3.getFF()));
		graphContainer.add(new FitnessGraph(initProgressionList, p3.getFF()));
		
		inputSolContainer.removeAll();
		inputSolContainer.add(new SolutionPanel(inputSol, p3).getPanel());

		outputSolContainer.removeAll();
		outputSolContainer.add(new SolutionPanel(solSize).getPanel());
		
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	// Show result of hill climb and how hill climb progressed in the graph
	public void showImprovement(Solution outputSol, ArrayList<Double> fitnessProgression, P3 p3) {
		graphContainer.removeAll();
		graphContainer.add(new FitnessGraph(fitnessProgression, p3.getFF()));
		
		outputSolContainer.removeAll();
		outputSolContainer.add(new SolutionPanel(outputSol, p3).getPanel());
	}

	public JPanel getPanel() {
		return mainPanel;
	}

	// Show help text to explain meaning of graph
	@Override
	public void actionPerformed(ActionEvent arg0) {
		JTextArea helpPane = new JTextArea();
		helpPane.setText("This graph shows the fitness values for each solution that hill climber tried "
				+ "while performing local search. Each point is a tried solution, the x axis shows how"
				+ " many steps were taken, and the y axis shows fitness, with optimum fitness for the "
				+ "problem at the top, and minimum at the bottom. The red trend line shows the fittest "
				+ "solution found at that point in the hill search.");
		helpPane.setEditable(false);
		helpPane.setOpaque(false);
		helpPane.setColumns(40);
		helpPane.setLineWrap(true);
		helpPane.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(helpPane);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setPreferredSize(new Dimension(400,100));
		JOptionPane.showMessageDialog(null, scrollPane, "Cluster List", JOptionPane.INFORMATION_MESSAGE);
		
	}
}
