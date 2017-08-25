package p3.tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import p3.Population;
import p3.Pyramid;
import p3.Solution;
import p3.StepTracker;
import p3.fitness.FitnessFn;
import p3.fitness.OneMax;

public class StepTrackerTest {

	private Random rand = new Random();
	
	@Test
	public void creationTest() {
		Solution s1 = new Solution("01111");
		Solution s2 = new Solution("10101");
		Solution s3 = new Solution("11111");

		int solSize = 5;
		FitnessFn ff = new OneMax(solSize);
		Pyramid pyramid = new Pyramid(solSize, ff);
		pyramid.addSolution(s1, 0, rand.nextLong());
		pyramid.addSolution(s2, 0, rand.nextLong());
		pyramid.addSolutionToNewPopulation(s3, rand.nextLong());
		
		StepTracker st = new StepTracker(pyramid);
		
		assertTrue(st.getCurrentGen()==0);
		assertTrue(st.getCurrentGenStep()==0);
		assertTrue(st.getCurrentStep()==0);
		
	}
	
	@Test
	public void nextStepNextGenTest() {
		Solution s1 = new Solution("01111");
		Solution s2 = new Solution("10101");
		Solution s3 = new Solution("11111");

		int solSize = 5;
		FitnessFn ff = new OneMax(solSize);
		Pyramid pyramid = new Pyramid(solSize, ff);
		pyramid.addSolution(s1, 0, rand.nextLong());
		pyramid.addSolutionToNewPopulation(s3, rand.nextLong());
		
		StepTracker st = new StepTracker(pyramid);
		
		st.nextStep();		
		assertTrue(st.getCurrentGen()==0);
		assertTrue(st.getCurrentGenStep()==1);
		assertTrue(st.getCurrentStep()==1);
		
		st.nextStep();
		assertTrue(st.getCurrentGen()==0);
		assertTrue(st.getCurrentGenStep()==2);
		assertTrue(st.getCurrentStep()==2);
		
		pyramid.addSolution(s2, 1, rand.nextLong());
		st.nextGen(pyramid);
		assertTrue(st.getCurrentGen()==1);
		assertTrue(st.getCurrentGenStep()==0);
		assertTrue(st.getCurrentStep()==3);
		
		st.nextStep();
		assertTrue(st.getCurrentGen()==1);
		assertTrue(st.getCurrentGenStep()==1);
		assertTrue(st.getCurrentStep()==4);
		
		st.nextGen(pyramid);
		assertTrue(st.getCurrentGen()==2);
		assertTrue(st.getCurrentGenStep()==0);
		assertTrue(st.getCurrentStep()==5);
		
		st.nextGen(pyramid);
		assertTrue(st.getCurrentGen()==3);
		assertTrue(st.getCurrentGenStep()==0);
		assertTrue(st.getCurrentStep()==6);
		
		
	}
	
	@Test
	public void previousGenTest() {
		Solution s1 = new Solution("01001");
		Solution s3 = new Solution("10111");

		int solSize = 5;
		FitnessFn ff = new OneMax(solSize);
		Pyramid pyramid = new Pyramid(solSize, ff);
		pyramid.addSolution(s1, 0, rand.nextLong());
		pyramid.addSolution(s3, 0, rand.nextLong());
		
		StepTracker st = new StepTracker(pyramid);
		
		st.nextStep();		
		st.nextStep();
		
		st.previousGen();
		assertTrue(st.getCurrentGen()==0);
		assertTrue(st.getCurrentGenStep()==0);
		assertTrue(st.getCurrentStep()==0);
		
		// Test Steptracker handles when asked to go before first gen
		st.previousGen();
		assertTrue(st.getCurrentGen()==0);
		assertTrue(st.getCurrentGenStep()==0);
		assertTrue(st.getCurrentStep()==0);
		
		st.nextStep();
		st.nextStep();		
		st.nextGen(pyramid);
		st.nextGen(pyramid);
		st.nextStep();		
		
		st.previousGen();
		assertTrue(st.getCurrentGen()==2);
		assertTrue(st.getCurrentGenStep()==0);
		assertTrue(st.getCurrentStep()==4);

		st.previousGen();
		assertTrue(st.getCurrentGen()==1);
		assertTrue(st.getCurrentGenStep()==0);
		assertTrue(st.getCurrentStep()==3);
		
	}
	
	@Test
	public void previousGenStepTest() {
		Solution s1 = new Solution("00");
		Solution s3 = new Solution("11");

		int solSize = 2;
		FitnessFn ff = new OneMax(solSize);
		Pyramid pyramid = new Pyramid(solSize, ff);
		pyramid.addSolution(s1, 0, rand.nextLong());
		pyramid.addSolution(s3, 0, rand.nextLong());
		
		StepTracker st = new StepTracker(pyramid);
		
		st.nextStep();		
		assertTrue(st.getPreviousGenStep()==0);
		
		st.nextStep();		
		assertTrue(st.getPreviousGenStep()==1);
		
		st.nextGen(pyramid);
		System.out.println(st.getPreviousGenStep());
		assertTrue(st.getPreviousGenStep()==2);
		st.nextStep();		
		assertTrue(st.getPreviousGenStep()==0);
		
	}
	
	@Test
	public void restartTest() {
		Solution s1 = new Solution("01111110");
		Solution s2 = new Solution("10000001");

		int solSize = 8;
		FitnessFn ff = new OneMax(solSize);
		Pyramid pyramid = new Pyramid(solSize, ff);
		pyramid.addSolution(s1, 0, rand.nextLong());
		pyramid.addSolution(s2, 0, rand.nextLong());
		
		StepTracker st = new StepTracker(pyramid);
		
		st.nextStep();		
		st.nextStep();
		st.nextGen(pyramid);
		st.nextStep();
		st.nextStep();
		st.nextStep();
		st.nextGen(pyramid);
		st.nextStep();
		
		st.restart();
		assertTrue(st.getCurrentGen()==0);
		assertTrue(st.getCurrentGenStep()==0);
		assertTrue(st.getCurrentStep()==0);
		
		// Check performs correctly at first step
		st.restart();
		assertTrue(st.getCurrentGen()==0);
		assertTrue(st.getCurrentGenStep()==0);
		assertTrue(st.getCurrentStep()==0);
		
	}
	
	@Test
	public void getSeedTest() {
		Solution s1 = new Solution("01111110");
		Solution s2 = new Solution("10000001");

		int solSize = 8;
		FitnessFn ff = new OneMax(solSize);
		Pyramid pyramid = new Pyramid(solSize, ff);
		pyramid.addSolution(s1, 0, rand.nextLong());
		pyramid.addSolution(s2, 0, rand.nextLong());
		
		StepTracker st = new StepTracker(pyramid);
		
		Long step0Seed = st.getSeed();
		
		st.nextStep();		
		st.nextStep();
		
		Long step2Seed = st.getSeed();
		
		st.nextGen(pyramid);
		Long step3Seed = st.getSeed();
		
		st.nextStep();
		st.nextStep();
		st.nextStep();
		
		st.previousGen();
		assertTrue(st.getSeed()==step3Seed);
		
		st.restart();
		
		assertTrue(st.getSeed()==step0Seed);
		
		st.nextStep();		
		st.nextStep();
		assertTrue(st.getSeed()==step2Seed);
		
		st.nextGen(pyramid);
		assertTrue(st.getSeed()==step3Seed);
		
	}
	
	@Test
	public void getPyramidTest() {
		Solution s1 = new Solution("00000001");
		Solution s2 = new Solution("01111111");

		int solSize = 8;
		FitnessFn ff = new OneMax(solSize);
		Pyramid pyramid = new Pyramid(solSize, ff);
		pyramid.addSolution(s1, 0, rand.nextLong());
		
		StepTracker st = new StepTracker(pyramid);
		
		assertTrue(st.getPyramid().getFittestSolution().equals(s1));
		
		st.nextStep();		
		st.nextStep();
		
		pyramid.addSolution(s2, 0, rand.nextLong());
		st.nextGen(pyramid);
		assertTrue(st.getPyramid().getFittestSolution().equals(s2));
		
		
	}

}
