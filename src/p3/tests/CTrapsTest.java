package p3.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import p3.Solution;
import p3.fitness.*;

public class CTrapsTest {

	@Test
	public void cTrapsBadConstructorTests() {
		
		try {
			FitnessFn cTraps = new ConcatenatedTraps(10, 3);			
		}catch(IllegalArgumentException e1) {
			System.out.println(e1.getMessage());
			assertTrue(e1.getMessage().length()>1);
		}
		
	}
	
	@Test
	public void optimalMinimumFitTests() {
		FitnessFn cTraps = new ConcatenatedTraps(20, 5);
		
		assertTrue(cTraps.getMinimumFitness()==0);
		assertTrue(cTraps.getOptimalFitness()==20);
		
	}

	@Test
	public void evalCountTest() {
		FitnessFn cTraps = new ConcatenatedTraps(12, 3);
		
		assertTrue(cTraps.getEvalCount()==0);
		
		Solution s1 = new Solution("111100001111");
		cTraps.getFitness(s1);
		assertTrue(cTraps.getEvalCount()==1);
		
		Solution s2 = new Solution("011100001111");		
		cTraps.getFitness(s2);
		cTraps.getFitness(s1);
		assertTrue(cTraps.getEvalCount()==3);
		
	}
	
	@Test
	public void getFitnessTest() {
		
		FitnessFn cTraps = new ConcatenatedTraps(16, 4);
		
		Solution s0 = new Solution("0000000000000000");
		assertTrue(cTraps.getFitness(s0)==12);

		Solution s1 = new Solution("1111111111111111");
		assertTrue(cTraps.getFitness(s1)==16);

		Solution s2 = new Solution("1111000011111010");
		assertTrue(cTraps.getFitness(s2)==12);

		
		cTraps = new ConcatenatedTraps(18, 2);
		
		Solution s3 = new Solution("000000000000000000");
		assertTrue(cTraps.getFitness(s3)==16);
		
		Solution s4 = new Solution("111111111000000000");
		assertTrue(cTraps.getFitness(s4)==17);
		
		Solution s5 = new Solution("101010101010101010");
		assertTrue(cTraps.getFitness(s5)==7);
	
	}
	
	@Test
	public void toStringTest() {
		FitnessFn cTraps = new ConcatenatedTraps(20, 5);
		assertTrue(cTraps.toString().equals("Concatenated Traps\nSolution Size: 20\nNumber of Traps: 5"));
	}

}
