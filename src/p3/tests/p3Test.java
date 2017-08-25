package p3.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import p3.*;
import p3.fitness.*;

public class p3Test {

	/*
	 * Add various tests with different fitness functions and sol sizes then
	 * compare number of evaluations to Goldman ones
	 */

	@Test
	public void runP3Test() {
		P3 p3 = new P3();
		p3.setupP3(32, new HIFF(32));
		Solution fittestSol = p3.runP3();
		System.out.println(fittestSol.toString());
		assertTrue((fittestSol.equalsString("11111111111111111111111111111111"))
				 || fittestSol.equalsString("00000000000000000000000000000000"));
		
		p3 = new P3();
		p3.setupP3(20, new ConcatenatedTraps(20, 4));
		fittestSol = p3.runP3();
		System.out.println(fittestSol.toString());
		assertTrue(fittestSol.equalsString("11111111111111111111"));
	}
	
	@Test
	public void basicGetterTests() {
		P3 p3 = new P3();
		FitnessFn ff = new ConcatenatedTraps(24, 6);
		p3.setupP3(24, ff);
		
		assertTrue(p3.getFF()==ff);
		assertTrue(p3.getSolutionSize()==24);
		assertTrue(p3.getCurrentPopIndex()==0);
		assertTrue(p3.getCurrentSolution()==null);
		
		Pyramid initP3Pyramid = p3.getPyramid();
		assertTrue(initP3Pyramid.getSolCount()==0);
		p3.oneStep();
		p3.oneStep();
		initP3Pyramid = p3.getPyramid();
		assertTrue(initP3Pyramid.getSolCount()==1);
		assertTrue(p3.getCurrentSolution().size()==24);
		
		ArrayList<Double> hCProg = p3.getHillClimbProgression();
		assertTrue(hCProg.get(0)<hCProg.get(hCProg.size()-1));		
		
		p3.previousGen();
		assertTrue(p3.getCurrentSolution()==null);
		
		
	}
	
	@Test
	public void basicStepCountTests() {
		P3 p3 = new P3();
		FitnessFn ff = new ConcatenatedTraps(24, 6);
		p3.setupP3(24, ff);
		
		assertTrue(p3.getStepCount()==0);
		assertTrue(p3.getGenCount()==0);
		assertTrue(p3.getSolCount()==0);
		
		p3.oneStep();
		assertTrue(p3.getStepCount()==1);
		
		p3.previousStep();
		assertTrue(p3.getStepCount()==0);

		p3.oneIteration();
		assertTrue(p3.getGenCount()==1);
		assertTrue(p3.getSolCount()==1);
		
		Solution fittestSol = p3.getFittestSolution();
		assertTrue(fittestSol.size()==24);
		
	}
	
	@Test
	public void basicStageNameTests() {
		P3 p3 = new P3();
		FitnessFn ff = new JumpK(20, 4);
		p3.setupP3(20, ff);
		
		//current step null when starting
		//assertTrue(p3.getCurrentStep().equals(""));
		assertTrue(p3.getNextStep().equals("New Random Solution"));

		p3.oneStep();
		assertTrue(p3.getCurrentStep().equals("New Random Solution"));
		assertTrue(p3.getNextStep().equals("Local Search"));
		
		p3.oneStep();
		assertTrue(p3.getCurrentStep().equals("Local Search"));
		assertTrue(p3.getNextStep().equals("Clustering"));
		
		p3.oneStep();
		assertTrue(p3.getCurrentStep().equals("Clustering"));
		assertTrue(p3.getNextStep().equals("Crossover"));
		
		p3.oneStep();
		assertTrue(p3.getCurrentStep().equals("Crossover"));
		assertTrue(p3.getNextStep().equals("New Random Solution"));
		
		p3.oneIteration();
		assertTrue(p3.getCurrentStep().equals("Crossover"));
		assertTrue(p3.getNextStep().equals("New Random Solution"));

		p3.oneStep();
		
		p3.previousGen();
		assertTrue(p3.getCurrentStep().equals("Crossover"));
		assertTrue(p3.getNextStep().equals("New Random Solution"));

		//Not very rigorous, as technically could have one step crossover, though v unlikely
		p3.previousStep();
		assertTrue(p3.getCurrentStep().equals("Crossover"));
		assertTrue(p3.getNextStep().equals("Crossover"));
		
		p3.previousGen();
		assertTrue(p3.getCurrentStep().equals("Crossover"));
		assertTrue(p3.getNextStep().equals("New Random Solution"));
		
		
	}

}
