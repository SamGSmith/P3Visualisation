/* PlaybackPanel.java
 * JPanel which includes buttons for playback, and some additional info about
 * generation and number of solutions.
 */

package p3.views;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeListener;

public class PlaybackPanel {
	
	private JPanel mainPanel;
	private JButton previousGenButton = new JButton("Previous Gen");
	private JButton previousStepButton = new JButton("Previous Step");
	private JButton pausePlayButton = new JButton("Play/Pause");
	private JButton nextStepButton = new JButton("Next Step");
	private JButton nextGenButton = new JButton("Next Gen");
	private JButton rerunButton = new JButton(" Restart Run ");
	private JButton resetButton = new JButton("New Problem");
	private JSlider delaySlider = new JSlider(JSlider.HORIZONTAL, 0, 2000, 200);
	private JLabel stepCountLabel = new JLabel("Fitness Evaluations: 0");
	private JLabel generationLabel = new JLabel("Generation: 0");
	private JLabel popSizeLabel = new JLabel("Solutions in Pyramid: 0");
	
	
	public PlaybackPanel () {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JPanel emptyPanelLeft = new JPanel();
		emptyPanelLeft.setMinimumSize(new Dimension(50, 0));
		c.insets = new Insets(2,2,2,2);
		c.gridheight = 3;
		c.weightx = 0.2;
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 0;
		mainPanel.add(emptyPanelLeft, c);
		
		Dimension minButtonSize = new Dimension(110,10);
		
		c.weightx = 0;
		c.gridx = 1;
		c.gridy = 0;
		previousGenButton.setPreferredSize(minButtonSize);
		mainPanel.add(previousGenButton, c);
		
		c.gridx = 2;
		c.gridy = 0;
		previousStepButton.setPreferredSize(minButtonSize);
		mainPanel.add(previousStepButton, c);
		
		c.gridx = 3;
		c.gridy = 0;
		pausePlayButton.setPreferredSize(minButtonSize);
		mainPanel.add(pausePlayButton, c);
		
		c.gridx = 4;
		c.gridy = 0;
		nextStepButton.setPreferredSize(minButtonSize);
		mainPanel.add(nextStepButton, c);
		
		c.gridx = 5;
		c.gridy = 0;
		nextGenButton.setPreferredSize(minButtonSize);
		mainPanel.add(nextGenButton, c);
		
		c.insets = new Insets(2,20,2,2);
		c.gridx = 6;
		c.gridy = 0;
		c.gridheight = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.3;
		delaySlider.setMajorTickSpacing(400);
		delaySlider.setMinorTickSpacing(40);
		delaySlider.setSnapToTicks(true);
		//delaySlider.setPaintTicks(true);
		delaySlider.setPaintLabels(true);
		mainPanel.add(delaySlider, c);
		
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.gridx = 6;
		c.gridy = 2;
		c.gridheight = 1;
		mainPanel.add(new JLabel("Step Duration (ms)"), c);
		
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 7;
		c.gridy = 0;
		mainPanel.add(stepCountLabel, c);
		
		c.gridx = 7;
		c.gridy = 1;
		mainPanel.add(generationLabel, c);
		
		c.gridx = 7;
		c.gridy = 2;
		mainPanel.add(popSizeLabel, c);
		
		c.insets = new Insets(2,40,2,2);
		//c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.EAST;
		c.weightx = 0.1;
		c.gridx = 8;
		c.gridy = 0;
		mainPanel.add(rerunButton, c);
		
		c.gridy = 1;
		mainPanel.add(resetButton, c);
	}
	
	public JPanel getPanel() {
		return mainPanel;
	}
	
	public void addPreviousGenListener(ActionListener previousGenListener) {
		previousGenButton.addActionListener(previousGenListener);
	}
	
	public void addPreviousStepListener(ActionListener previousStepListener) {
		previousStepButton.addActionListener(previousStepListener);
	}
	
	public void addPausePlayListener(ActionListener pausePlayListener) {
		pausePlayButton.addActionListener(pausePlayListener);
	}
	
	public void addNextStepListener(ActionListener nextStepListener) {
		nextStepButton.addActionListener(nextStepListener);
	}
	
	public void addNextGenListener(ActionListener nextGenListener) {
		nextGenButton.addActionListener(nextGenListener);
	}
	
	public void addSliderListener(ChangeListener sliderListener) {
		delaySlider.addChangeListener(sliderListener);
	}
	
	public void addResetListener(ActionListener resetListener) {
		resetButton.addActionListener(resetListener);
	}
	
	public void addRerunListener(ActionListener rerunListener) {
		rerunButton.addActionListener(rerunListener);
	}
	
	public void setPyramidInfoLabels(int fitnessEvals, int gen, int popSize) {
		stepCountLabel.setText("Fitness Evaluations: "+fitnessEvals);
		generationLabel.setText("Generation: "+gen);
		popSizeLabel.setText("Solutions in Pyramid: "+popSize);
	}
	
}