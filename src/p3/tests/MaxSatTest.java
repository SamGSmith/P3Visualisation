package p3.tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import p3.Solution;
import p3.fitness.*;

public class MaxSatTest {

	@Test
	public void maxFitnessTest() {
		int sSize = 8;
		double cToVRatio = 4.27;
		Solution sol = new Solution("11111111");
		long seed = new Random().nextLong();
		FitnessFn maxSat = new MaxSat(sSize, cToVRatio, sol, seed);
		//System.out.println(maxSat.toString());
		//System.out.println(maxSat.toString().length());
		assertTrue(maxSat.getOptimalFitness()==34);
	}
	
	@Test
	public void minimumFitTests() {
		int sSize = 8;
		double cToVRatio = 3;
		Solution sol = new Solution("00000000");
		long seed = new Random().nextLong();
		FitnessFn maxSat = new MaxSat(sSize, cToVRatio, sol, seed);
		
		assertTrue(maxSat.getMinimumFitness()==0);
		
	}
	
	@Test
	public void evalCountTest() {
		int sSize = 11;
		double cToVRatio = 5.4;
		Solution sol = new Solution("00011111111");
		long seed = new Random().nextLong();
		FitnessFn maxSat = new MaxSat(sSize, cToVRatio, sol, seed);
		
		assertTrue(maxSat.getEvalCount()==0);
		
		Solution s1 = new Solution("11110000100");
		maxSat.getFitness(s1);
		assertTrue(maxSat.getEvalCount()==1);
		
		Solution s2 = new Solution("00011100111");		
		maxSat.getFitness(s2);
		maxSat.getFitness(s1);
		assertTrue(maxSat.getEvalCount()==3);
		
	}
	
	@Test
	public void getFitnessTest() {
		int sSize = 15;
		double cToVRatio = 3;
		Solution sol = new Solution("111110000011111");
		long seed = new Random().nextLong();
		FitnessFn maxSat = new MaxSat(sSize, cToVRatio, sol, seed);
		Solution s0 = new Solution("111110000011111");
		assertTrue(maxSat.getFitness(s0)==45);

		
		sSize = 12;
		cToVRatio = 4.32;
		sol = new Solution("000000000000");
		seed = new Random().nextLong();
		maxSat = new MaxSat(sSize, cToVRatio, sol, seed);
		s0 = new Solution("000000000000");
		assertTrue(maxSat.getFitness(s0)==52);
		
		
	}
	
	@Test
	public void toStringTest() {
		int sSize = 5;
		double cToVRatio = 3.3;
		Solution sol = new Solution("10101");
		long seed = new Random().nextLong();
		FitnessFn maxSat = new MaxSat(sSize, cToVRatio, sol, seed);
		
		
		System.out.println(maxSat.toString());
		int numberOfClauses = Math.round((float)(sSize*cToVRatio));
		//Formula for character count: number of characters in "Clauses: \n"
		// + shortest form of clause*numberOfClauses + connecting " \u2227 ". 
		int stringMinLength = 11*numberOfClauses+3*(numberOfClauses-1);
		//Same but with max length of clause
		int stringMaxLength = 14*numberOfClauses+3*(numberOfClauses-1);
		
		System.out.println(maxSat.toString().length());
		assertTrue(((MaxSat) maxSat).getClauseString().length()>=stringMinLength);
		assertTrue(((MaxSat) maxSat).getClauseString().length()<=stringMaxLength);
		
	}

}
