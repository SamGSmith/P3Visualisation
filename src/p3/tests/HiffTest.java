package p3.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import p3.Solution;
import p3.fitness.*;

public class HiffTest {

	@Test
	public void hiffBadConstructorTests() {
		
		try {
			FitnessFn hiff = new HIFF(10);			
		}catch(IllegalArgumentException e1) {
			System.out.println(e1.getMessage());
			assertTrue(e1.getMessage().length()>1);
		}
		
	}
	
	@Test
	public void optimalMinimumFitTests() {
		FitnessFn hiff = new HIFF(128);
		
		assertTrue(hiff.getMinimumFitness()==128);
		assertTrue(hiff.getOptimalFitness()==(128*8));
		
	}
	
	@Test
	public void evalCountTest() {
		FitnessFn hiff = new HIFF(16);
		
		assertTrue(hiff.getEvalCount()==0);
		
		Solution s1 = new Solution("1111000010000111");
		hiff.getFitness(s1);
		assertTrue(hiff.getEvalCount()==1);
		
		Solution s2 = new Solution("0111000011100111");		
		hiff.getFitness(s2);
		hiff.getFitness(s1);
		assertTrue(hiff.getEvalCount()==3);
		
	}
	
	@Test
	public void getFitnessTest() {
		
		FitnessFn hiff = new HIFF(8);
		
		Solution s0 = new Solution("00000000");
		assertTrue(hiff.getFitness(s0)==32);

		Solution s1 = new Solution("01101010");
		assertTrue(hiff.getFitness(s1)==8);

		Solution s2 = new Solution("11111010");
		assertTrue(hiff.getFitness(s2)==16);

		
		hiff = new HIFF(16);
		
		Solution s3 = new Solution("1100110001010000");
		assertTrue(hiff.getFitness(s3)==32);
		
		Solution s4 = new Solution("1111111100000000");
		assertTrue(hiff.getFitness(s4)==64);
		
		Solution s5 = new Solution("1111111111111111");
		assertTrue(hiff.getFitness(s5)==80);

	}
	
	@Test
	public void toStringTest() {
		FitnessFn hiff = new HIFF(8);
		assertTrue(hiff.toString().equals("HIFF\nSolution Size: 8"));
	}

}
