package p3.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import p3.Solution;
import p3.fitness.FitnessFn;
import p3.fitness.JumpK;

public class JumpKTest {
	
	@Test
	public void optimalMinimumFitTests() {
		FitnessFn jumpK = new JumpK(128, 20);
		
		assertTrue(jumpK.getMinimumFitness()==0);
		assertTrue(jumpK.getOptimalFitness()==148);
		
	}
	
	@Test
	public void evalCountTest() {
		FitnessFn jumpK = new JumpK(16, 1);
		
		assertTrue(jumpK.getEvalCount()==0);
		
		Solution s1 = new Solution("1111000010000111");
		jumpK.getFitness(s1);
		assertTrue(jumpK.getEvalCount()==1);
		
		Solution s2 = new Solution("0111000011100111");		
		jumpK.getFitness(s2);
		jumpK.getFitness(s1);
		assertTrue(jumpK.getEvalCount()==3);
		
	}
	
	@Test
	public void getFitnessTest() {
		
		FitnessFn jumpK = new JumpK(8, 3);
		
		Solution s0 = new Solution("00000000");
		assertTrue(jumpK.getFitness(s0)==3);

		Solution s1 = new Solution("01101010");
		assertTrue(jumpK.getFitness(s1)==7);

		Solution s2 = new Solution("10111110");
		assertTrue(jumpK.getFitness(s2)==2);

		
		jumpK = new JumpK(13, 5);
		
		Solution s3 = new Solution("1111111111111");
		assertTrue(jumpK.getFitness(s3)==18);
		
		Solution s4 = new Solution("1111111101111");
		assertTrue(jumpK.getFitness(s4)==1);
		
		Solution s5 = new Solution("1010101011110");
		assertTrue(jumpK.getFitness(s5)==13);
		
	}
	
	@Test
	public void toStringTest() {
		FitnessFn jumpK = new JumpK(9, 5);
		assertTrue(jumpK.toString().equals("JumpK\nSolution Size: 9\nK value: 5"));
	}

}
