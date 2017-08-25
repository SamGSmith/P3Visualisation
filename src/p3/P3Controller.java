/* P3Controller.java
 * Class for interfacing between P3 itself and the visualisation GUI
 */

package p3;

import p3.fitness.*;
import p3.views.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class P3Controller {
	private P3 p3;
	private FitnessFn ff;
	private Double maxFitness;
	private MenuPanel menuPanel;
	private PlaybackPanel playbackPanel;
	private VisualisationPanel visPanel;
	private PyramidPanel pyramidPanel;
	private HillClimbPanel hillClimbPanel;
	private P3GUI gui;
	private int timerDelay = 150;
	private boolean paused = true;
	private Timer timer;

	public P3Controller(P3GUI guiArg, P3 p3Arg) {
		this.gui = guiArg;
		menuPanel = gui.getMenuPanel();
		playbackPanel = gui.getPlaybackPanel();
		visPanel = gui.getVisPanel();
		pyramidPanel = gui.getPyramidPanel();
		hillClimbPanel = gui.getHillClimbPanel();
		this.p3 = p3Arg;

		menuPanel.addStartP3Listener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (menuPanel.validSetup()) {
					gui.switchToVis();
					int sS = menuPanel.getSS();
					ff = menuPanel.getFF();
					p3.setupP3(sS, ff);
					maxFitness = ff.getOptimalFitness();
					hillClimbPanel.showBlank(sS,ff);
					paused = true;
					// p3.oneStep();
					// p3.previousStep();
					visPanel.setStage("Local Search");
					visPanel.setCurrentSol(p3);
					pyramidPanel.visPyramid(p3);
				} else {
					menuPanel.showSetupError();
				}
			}
		});

		// Change delay time between steps during continuous playback 
		// based on slider position
		playbackPanel.addSliderListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider delaySlider = (JSlider) e.getSource();
				timerDelay = delaySlider.getValue();
				if (timer != null) {
					timer.setDelay(timerDelay);
				}
			}
		});

		playbackPanel.addPausePlayListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (paused) {
					visPanel.setStage(p3.getNextStep());
					p3.oneStep();
					pyramidPanel.visPyramid(p3);
					visPanel.setCurrentSol(p3);
					// System.out.println(maxFitness.toString());

					timer = new Timer(timerDelay, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (p3.getPyramidMaxFitness() < maxFitness) {
								p3.oneStep();
							} else {
								((Timer) e.getSource()).stop();
								paused = true;
								String nextStep = p3.getNextStep();
								
								if (nextStep == "Local Search") {
									p3.oneStep();
								}
							}
							updateGUI();
						}
					});
					timer.start();
					paused = false;
				} else {
					timer.stop();
					paused = true;
				}
			}
		});

		playbackPanel.addNextGenListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p3.oneIteration();
				updateGUI();
			}
		});

		playbackPanel.addNextStepListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p3.oneStep();
				updateGUI();
			}
		});

		playbackPanel.addPreviousGenListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (p3.getStepCount() > 0) {
					p3.previousGen();
					updateGUI();
				}
			}
		});

		playbackPanel.addPreviousStepListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (p3.getStepCount() > 0) {
					p3.previousStep();
					updateGUI();
				}
			}
		});

		playbackPanel.addResetListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.reset();
				pyramidPanel.reset();
				if (timer != null) {
					timer.stop();
				}
				paused = true;
			}
		});
		
		playbackPanel.addRerunListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p3.rerun();

				if (timer != null) {
					timer.stop();
				}
				paused = true;
				//updateGUI();
				hillClimbPanel.showBlank(p3.getSolutionSize(),ff);
				visPanel.setStage("Local Search");
				visPanel.setCurrentSol(p3);
				pyramidPanel.visPyramid(p3);
	
			}
		});
		
		gui.getCrossoverPanel().addSkipListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p3.stepToCrossoverEnd();
				updateGUI();
			}
		});

	}
	
	// Update the pyramid and sidepanel gui
	private void updateGUI() {
		visPanel.setStage(p3.getCurrentStep());
		pyramidPanel.visPyramid(p3);
		visPanel.setCurrentSol(p3);
		
		switch (p3.getCurrentStep()) {
		case "New Random Solution":
			hillClimbPanel.showInitialRandom(p3.getCurrentSolution(), p3);
			break;
		case "Local Search":
			hillClimbPanel.showImprovement(p3.getCurrentSolution(), 
					p3.getHillClimbProgression(), p3);
			break;
		case "Clustering":
			int popIndex = p3.getCurrentPopIndex();
			Population currentPop = p3.getPyramid().getPopulation(popIndex);
			//System.out.println("Pop: "+ currentPop.toString());
			
			gui.getClusterPanel().displayClusterModel(currentPop.getTree(), 
					currentPop.getClusterModel(), popIndex);
			break;
		case "Crossover":
			CrossoverTracker crossoverTracker = p3.getCrossoverTracker();
			CrossoverPanel crossoverPanel = gui.getCrossoverPanel();
			crossoverPanel.displayCrossover(crossoverTracker, ff);
			
			// Need to call this so that random solution for later step isn't shown
			// when stepping backwards. (Due to currentStep being set to crossover
			// when reversing)
			hillClimbPanel.showBlank(p3.getSolutionSize(),ff);
			
			if(p3.getNextStep()=="Crossover") {
				crossoverPanel.skipButtonEnabled(true);
			} else {
				crossoverPanel.skipButtonEnabled(false);				
			}
			
			if(p3.getStepCount()==0) {
				visPanel.setStage("Local Search");
				visPanel.setCurrentSol(p3);
			}
			break;
		default:
			System.out.println("Step recieved:"+p3.getCurrentStep());
		}

		playbackPanel.setPyramidInfoLabels(p3.getEvalCount(), p3.getGenCount(), p3.getSolCount());
	}

}