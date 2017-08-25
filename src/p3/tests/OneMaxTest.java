package p3.tests;

import static org.junit.Assert.*;

import java.util.BitSet;
import java.util.Random;

import org.junit.Test;

import p3.Solution;
import p3.fitness.*;

public class OneMaxTest {

	@Test
	public void optimumTest() {
		FitnessFn oM0 = new OneMax(0);
		FitnessFn oM1 = new OneMax(1);
		FitnessFn oM999 = new OneMax(999);
		assertTrue(oM0.getOptimalFitness()==0 &&
					oM1.getOptimalFitness()==1 &&
					oM999.getOptimalFitness()==999);
	}
	
	@Test
	public void fitnessTest() {
		FitnessFn oM = new OneMax(7);
		BitSet bs = new BitSet(7);
		Solution s0 = new Solution(bs, 7);
		bs.set(2, 5);
		Solution s3 = new Solution(bs, 7);
		bs.set(0, 7);
		Solution s7 = new Solution(bs, 7);
		
		Boolean s0FitTrue = oM.getFitness(s0)==0;
		Boolean s3FitTrue = oM.getFitness(s3)==3;
		Boolean s7FitTrue = oM.getFitness(s7)==7;
		
		assertTrue(s0FitTrue&&s3FitTrue&&s7FitTrue);
	}
	
	@Test
	public void getEvalCountTest() {
		FitnessFn oM = new OneMax(3);
		long randLong = new Random().nextLong();
		Solution s = new Solution(3, randLong);
		assertTrue(oM.getEvalCount()==0);
		
		for(int i=0; i<1000; i++) {
			oM.getFitness(s);
		}
		assertTrue(oM.getEvalCount()==1000);
	}
	
	@Test
	public void minimumTest() {
		FitnessFn oM = new OneMax(909);
		assertTrue(oM.getMinimumFitness()==0);
	}
	
	@Test
	public void toStringTest() {
		FitnessFn oM = new OneMax(11);
		assertTrue(oM.toString().equals("OneMax\nSolution Size: 11"));
	}

}
