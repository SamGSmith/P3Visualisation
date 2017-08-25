/* CurrentSolutionPanel.java
 * Class which creates a panel which shows the algorithm's current Solution.
 * and the corresponding bitstring.
 */

package p3.views;

import java.awt.*;
import javax.swing.*;

import p3.*;

public class CurrentSolutionPanel {
	private JPanel mainPanel = new JPanel();
	private JPanel solutionPanelContainer  = new JPanel();
	private JLabel fullStringLabel = new JLabel();
	private JTextArea ffLabel = new JTextArea();
	
	// Layout components in main panel
	public CurrentSolutionPanel() {

		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2,2,2,2);
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.2;
		c.weighty = 0.15;
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(new JLabel("Current Solution", SwingConstants.CENTER), c);
		c.anchor = GridBagConstraints.NORTH;
		c.weighty = 0.8;
		c.gridx = 0;
		c.gridy = 1;
		mainPanel.add(solutionPanelContainer, c);
		
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.3;
		c.weighty = 0.15;
		c.gridx = 1;
		c.gridy = 0;
		mainPanel.add(new JLabel("Full Binary String", SwingConstants.CENTER), c);
		c.gridx = 2;
		mainPanel.add(new JLabel("Fitness Function", SwingConstants.CENTER), c);
		
		c.anchor = GridBagConstraints.NORTH;
		c.weighty = 0.9;
		c.gridx = 1;
		c.gridy = 1;
		JPanel labelContainer = new JPanel(new GridBagLayout());
		labelContainer.add(fullStringLabel);
		JScrollPane fullStringScrollPanel = new JScrollPane(labelContainer);
		//fullStringScrollPanel.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.add(fullStringScrollPanel ,c);
		
		c.gridx = 2;
		c.weightx = 0.1;
		ffLabel.setEditable(false);
		ffLabel.setOpaque(false);
		JPanel ffContainer = new JPanel(new BorderLayout());
		ffContainer.add(ffLabel);
		JScrollPane ffScrollPanel = new JScrollPane(ffContainer);
		ffScrollPanel.setMaximumSize(new Dimension(300,100));
		mainPanel.add(ffScrollPanel ,c);
	}
	
	public JPanel getPanel() {
		return mainPanel;
	}
	
	public void setCurrentSolution(P3 p3) {
		Solution currentSol = p3.getCurrentSolution();
		solutionPanelContainer.removeAll();
		
		SolutionPanel solP;
		
		if (currentSol==null) {
			solP = new SolutionPanel(p3.getSolutionSize());
		} else {
			solP = new SolutionPanel(currentSol, p3);
			fullStringLabel.setText(currentSol.toString());
		}
		solutionPanelContainer.add(solP.getPanel());
		
		ffLabel.setText(p3.getFF().toString());
	}
	
	
}