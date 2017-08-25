/* MenuPanel.java
 * view with setup menu components, solution size and fitness function etc
 */

package p3.views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.BitSet;
import java.util.Random;

import javax.swing.*;

import p3.Solution;
import p3.fitness.*;

public class MenuPanel implements ActionListener {

	private JPanel mainPanel;
	private JButton startP3Button = new JButton("Run P3");
	private String[] fFList = { "Concatenated Traps", "HIFF", "JumpK", "Max Sat", "OneMax" };
	private JComboBox<String> fFComboBox = new JComboBox<String>(fFList);

	private SpinnerNumberModel defaultSpinnerModel = new SpinnerNumberModel(8, 2, 128, 1);
	// Create spinner model which doubles and halves value
	@SuppressWarnings("serial")
	private SpinnerNumberModel hiffSpinnerModel = new SpinnerNumberModel(8, 2, 128, 1) {
		@Override
		public Object getNextValue() {
			int currentValue = (int) super.getValue();
			Object nextValue = (currentValue * 2);
			Comparable maxVal = super.getMaximum();
			// Check if over maximum value
			if (maxVal.compareTo(nextValue) < 0) {
				nextValue = null;
			}
			return nextValue;
		}

		@Override
		public Object getPreviousValue() {
			int currentValue = (int) super.getValue();
			Object nextValue = (currentValue / 2);
			Comparable minVal = super.getMinimum();
			// Check if over maximum value
			if (minVal.compareTo(nextValue) > 0) {
				nextValue = null;
			}
			return nextValue;
		}
	};
	private JSpinner sSSpinner = new JSpinner(defaultSpinnerModel);

	private SpinnerNumberModel trapOrKSpinnerModel = new SpinnerNumberModel(7, 2, 32, 1);
	private JSpinner trapOrKSpinner = new JSpinner(trapOrKSpinnerModel);
	private JLabel trapOrKLabel = new JLabel("Number of Traps");

	private JTextArea infoBox = new JTextArea("This software runs Goldman and Punch's Parameterless Population Pyramid algorithm (P3). After selecting a fitness function and its associated settings, the program will show you the pyramid structure, and details about the current step of the algorithm.", 10, 30);
	private String setupErrorMessage;

	public MenuPanel() {
		layoutComponents();
	}

	// Construct a new fitnessFn object for the selected fitness function
	public FitnessFn getFF() {
		String selectedFF = (String) fFComboBox.getSelectedItem();
		int solSize = getSS();
		FitnessFn ff;
		switch (selectedFF) {
		case "Concatenated Traps":
			int trapCount = (int) trapOrKSpinner.getValue();
			ff = new ConcatenatedTraps(solSize, trapCount);
			break;
		case "HIFF":
			ff = new HIFF(solSize);
			break;
		case "JumpK":
			int kValue = (int) trapOrKSpinner.getValue();
			ff = new JumpK(solSize, kValue);
			break;
		case "Max Sat":
			BitSet bs = new BitSet(solSize);
			bs.flip(0, solSize);
			Solution targetSol = new Solution(bs, solSize);
			long seed = new Random().nextLong();
			ff = new MaxSat(solSize, 4.27, targetSol, seed);
			break;
		case "OneMax":
			ff = new OneMax(solSize);
			break;
		default:
			ff = new OneMax(solSize);
		}
		return ff;
	}

	// Check whether user input is appropriate, by checking values of menu components
	public boolean validSetup() {
		int solSize = getSS();
		int trapCount = getTrapCount();
		String selectedFF = (String) fFComboBox.getSelectedItem();

		Boolean isValid;
		switch (selectedFF) {
		case "Concatenated Traps":
			isValid = (solSize % trapCount) == 0;
			if (!isValid) {
				setupErrorMessage = "Number of traps must be a factor of solution size";
			}
			break;
		case "HIFF":
			double log2SolSize = Math.log(solSize) / Math.log(2);
			isValid = (log2SolSize % 1) == 0;
			if (!isValid) {
				setupErrorMessage = "Solution size must be a power of 2";
			}
			break;
		case "JumpK":
			isValid = trapCount < solSize;
			if (!isValid) {
				setupErrorMessage = "K cannot be greater than the solution size";
			}
			break;
		case "Max Sat":
			isValid = true;
			break;
		case "OneMax":
			isValid = true;
			break;
		default:
			isValid = false;
			setupErrorMessage = "Setup Error!";
		}
		return isValid;
	}

	//Display or hide the appropriate menu componenets for the specified fitness function
	// along with a description of it. 
	@Override
	public void actionPerformed(ActionEvent e) {
		String fFString = (String) fFComboBox.getSelectedItem();

		switch (fFString) {
		case "Concatenated Traps":
			infoBox.setText("Concatenated traps scores fitness based on the sum of a number of trap functions (determined by number of traps parameter). Each trap function of length n gets the cardinality u of the trap, and scores n if u =  n, and n-1-u for other values of u.");
			trapOrKSpinner.setVisible(true);
			trapOrKLabel.setText("Number of Traps");
			sSSpinner.setModel(defaultSpinnerModel);
			break;
		case "HIFF":
			infoBox.setText("HIFF calculates the score by creating a binary tree of each neighbouring pair of variables, and their subsequent pairs. A pair scores its length if all bits have the same value. Thus, the optimum will be all 0 or all 1.");
			trapOrKSpinner.setVisible(false);
			// remove text to prevent gridBagLayout moving components
			trapOrKLabel.setText("   ");
			sSSpinner.setModel(hiffSpinnerModel);
			break;
		case "JumpK":
			infoBox.setText("JumpK scores K + cardinality for when cardinality is between 0 and solution size - k, and when all bits are 1. It scores solution size - cardinality when cardinality is greater than solution size - k, and less than solution size");
			trapOrKSpinner.setVisible(true);
			trapOrKLabel.setText("K Value");
			sSSpinner.setModel(defaultSpinnerModel);
			break;
		case "Max Sat":
			infoBox.setText("Max Sat is made up of a number of clauses, each with three unique variables. These variables map to genes in the solution, and in each clause can be negated. Each clause is the disjunction of the three variables, and a score is determined based on how many clauses are true. In this case, the optimum value is designed to be when all bits equal 1, but there are likely other bitstrings with the same fitness. The number of clauses used = 4.27 x solution size, to ensure an appropriate challenge.");
			trapOrKSpinner.setVisible(false);
			trapOrKLabel.setText("   ");
			sSSpinner.setModel(defaultSpinnerModel);
			break;
		case "OneMax":
			infoBox.setText("OneMax simply scores fitness based on cardinality. As such, the solution will be found by the hill climber.");
			trapOrKSpinner.setVisible(false);
			// remove text to prevent gridBagLayout moving components
			trapOrKLabel.setText("   ");
			sSSpinner.setModel(defaultSpinnerModel);
			break;
		default:
			infoBox.setText("Select a fitness function from the drop down box to the left to configure the P3 visualisation.");
		}

	}
	
	public JPanel getPanel() {
		return mainPanel;
	}

	public int getSS() {
		return (int) sSSpinner.getValue();
	}

	public int getTrapCount() {
		return (int) trapOrKSpinner.getValue();
	}
	
	public void showSetupError() {
		if (setupErrorMessage != null) {
			JOptionPane.showMessageDialog(mainPanel, setupErrorMessage);
		}
	}

	public void addStartP3Listener(ActionListener startP3ButtonListener) {
		startP3Button.addActionListener(startP3ButtonListener);
		
	}

	private void layoutComponents() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JLabel header = new JLabel("<html><font size = +1>P3 Visualisaton Setup</html>", JLabel.CENTER);
		c.gridwidth = 3;
		c.insets = new Insets(8, 12, 24, 12);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(header, c);

		c.insets = new Insets(8, 12, 8, 12);
		c.gridwidth = 1;
		JLabel fFLabel = new JLabel("Fitness Function");
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(fFLabel, c);

		// add fitness function combo box
		fFComboBox.addActionListener(this);
		c.gridx = 1;
		c.gridy = 1;
		mainPanel.add(fFComboBox, c);

		JLabel sSLabel = new JLabel("Solution Size");
		c.gridx = 0;
		c.gridy = 2;
		mainPanel.add(sSLabel, c);

		// add solution size spinner
		c.gridx = 1;
		c.gridy = 2;
		mainPanel.add(sSSpinner, c);

		// add trap count/k value label
		c.gridx = 0;
		c.gridy = 3;
		mainPanel.add(trapOrKLabel, c);

		// add trap size spinner
		c.gridx = 1;
		c.gridy = 3;
		mainPanel.add(trapOrKSpinner, c);

		infoBox.setEditable(false);
		infoBox.setLineWrap(true);
		infoBox.setWrapStyleWord(true);
		infoBox.setOpaque(false);
		c.gridheight = 4;
		c.gridx = 2;
		c.gridy = 1;
		mainPanel.add(infoBox, c);

		// Add start vis button
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		mainPanel.add(startP3Button, c);
	}
}