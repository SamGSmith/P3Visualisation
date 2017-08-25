package p3.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

import org.junit.Test;

import p3.*;
import p3.fitness.ConcatenatedTraps;
import p3.fitness.FitnessFn;
import p3.fitness.HIFF;
import p3.fitness.OneMax;

public class HillClimberTest {

	@Test
	public void basicLocalTest() {
		FitnessFn oM = new OneMax(6);
		
		BitSet bs = new BitSet(6);
		Solution s = new Solution(bs, 6);
		double oldFit = s.getFitness(oM);
		long randLong = new Random().nextLong();
		s = new HillClimber(s, oM, randLong).getLocalOptimum();
		
		//HillClimber.localSearch(s, oM);
		
		double newFit = s.getFitness(oM);
		
		assertTrue(newFit>oldFit);
	}
	
	@Test
	public void cTrapsZeroTest() {
		BitSet bs = new BitSet(16);
		FitnessFn cT = new ConcatenatedTraps(16, 4);
		
		Solution s = new Solution(bs, 16);
		double oldFit = s.getFitness(cT);
		long randLong = new Random().nextLong();
		s = new HillClimber(s, cT, randLong).getLocalOptimum();
		//HillClimber.localSearch(s, cT);
		
		double newFit = s.getFitness(cT);
		
		assertTrue(newFit==oldFit);
	}

	@Test
	public void cTrapsClimbTest() {
		BitSet bs = new BitSet(9);
		bs.set(2, 4);
		bs.set(5, 7);
		FitnessFn cT = new ConcatenatedTraps(9, 3);
		
		Solution s = new Solution(bs, 9);
		double oldFit = s.getFitness(cT);	
		//HillClimber.localSearch(s, cT);
		long randLong = new Random().nextLong();
		s = new HillClimber(s, cT, randLong).getLocalOptimum();
		double newFit = s.getFitness(cT);
		
		assertTrue((newFit>oldFit)&&(newFit<9));
	}

	@Test
	public void hiffTest() {
		FitnessFn hiff = new HIFF(32);
		BitSet bs = new BitSet(32);
		for(int i=0; i<32; i+=2) {
			bs.set(i);
		}
		Solution s = new Solution(bs, 32);
		
		double oldFit = s.getFitness(hiff);
		//System.out.println(s.toString()+", fitness:"+oldFit);
		
		//HillClimber.localSearch(s, hiff);
		long randLong = new Random().nextLong();
		s = new HillClimber(s, hiff, randLong).getLocalOptimum();
		
		double newFit = s.getFitness(hiff);
		//System.out.println(s.toString()+", fitness:"+newFit);
		
		assertTrue(newFit>oldFit);
	}
	
	@Test
	public void fitnessListAtOptimumTest() {
		FitnessFn oM = new OneMax(8);
		Solution s = new Solution("11111111");
		
		long randLong = new Random().nextLong();
		ArrayList<Double> fitList = new HillClimber(s, oM, randLong).getFitnessProgression();
		System.out.println(fitList.toString());
		
		ArrayList<Double> expected = new ArrayList<Double>();
		expected.add(8.0);
		for (int i=0; i<8; i++) {
			expected.add(7.0);
		}
		
		assertTrue(fitList.equals(expected));
	}
	
	@Test
	public void fitnessListMinToMaxTest() {
		FitnessFn oM = new OneMax(8);
		Solution s = new Solution("00000000");
		
		long randLong = new Random().nextLong();
		HillClimber hc = new HillClimber(s, oM, randLong);
		
		assertTrue(hc.getInitialSol().equals(s));
		
		ArrayList<Double> fitList = hc.getFitnessProgression();
		System.out.println(fitList.size());
		
		assertTrue(fitList.get(0) == 0.0);
		assertTrue(fitList.get(fitList.size()-1) == 7.0);
		assertTrue(fitList.size()>8);
		assertTrue(fitList.size()<38);
	}
	
}
