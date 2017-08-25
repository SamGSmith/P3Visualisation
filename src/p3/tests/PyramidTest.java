package p3.tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import p3.*;
import p3.fitness.*;

public class PyramidTest {

	@Test
	public void creationTest() {
		int solSize = 32;
		FitnessFn ff = new OneMax(solSize);
		Pyramid pyramid = new Pyramid(solSize, ff);
		assertTrue(pyramid.toString().equals("Pyramid\nEmpty\n"));
	}
	
	@Test
	public void toStringTest() {
		int solSize = 4;
		FitnessFn ff = new HIFF(solSize);
		Pyramid pyramid = new Pyramid(solSize, ff);
		Random rand = new Random();
		Solution s1 = new Solution("1100");
		pyramid.addSolution(s1, 0, rand.nextLong());
		Solution s2 = new Solution("1001");
		pyramid.addSolutionToNewPopulation(s2, rand.nextLong());
		
		String expectedOut = "Pyramid\n1100\n1001\n";
		System.out.println(pyramid.toString());
		assertTrue(expectedOut.equals(pyramid.toString()));
	}
	
	@Test
	public void countTests() {
		int solSize = 8;
		FitnessFn ff = new HIFF(solSize);
		Pyramid pyramid = new Pyramid(solSize, ff);
		Random rand = new Random();
		
		assertTrue(pyramid.getPopCount()==1);
		assertTrue(pyramid.getSolCount()==0);
		
		Solution s1 = new Solution("11010100");
		pyramid.addSolution(s1, 0, rand.nextLong());
		assertTrue(pyramid.getPopCount()==1);
		assertTrue(pyramid.getSolCount()==1);
		
		Solution s2 = new Solution("10111111");
		pyramid.addSolutionToNewPopulation(s2, rand.nextLong());
		assertTrue(pyramid.getPopCount()==2);
		assertTrue(pyramid.getSolCount()==2);
		
		assertTrue(pyramid.getFittestSolution().equals(s2));
	}
	
	@Test
	public void containsTest() {
		int solSize = 6;
		FitnessFn ff = new ConcatenatedTraps(solSize, 2);
		Pyramid pyramid = new Pyramid(solSize, ff);
		Random rand = new Random();
		
		Solution s1 = new Solution("110101");
		assertFalse(pyramid.contains(s1));
		pyramid.addSolution(s1, 0, rand.nextLong());
		assertTrue(pyramid.contains(s1));
		
		Solution s2 = new Solution("001111");
		pyramid.addSolutionToNewPopulation(s2, rand.nextLong());
		assertTrue(pyramid.contains(s2));
		assertTrue(pyramid.contains(s1));
		
	}
	
	@Test
	public void deepCopyTest() {
		int solSize = 6;
		FitnessFn ff = new ConcatenatedTraps(solSize, 2);
		Pyramid pyramid = new Pyramid(solSize, ff);
		Random rand = new Random();
		
		Solution s1 = new Solution("111000");
		pyramid.addSolution(s1, 0, rand.nextLong());
		
		Solution s2 = new Solution("000000");
		pyramid.addSolution(s2, 0, rand.nextLong());
		
		Solution s3 = new Solution("111111");
		pyramid.addSolutionToNewPopulation(s3, rand.nextLong());

		Solution s4 = new Solution("000111");
		pyramid.addSolution(s4, 1, rand.nextLong());
		
		Pyramid pCopy = pyramid.getDeepCopy();
		
		assertTrue(pCopy.getFittestSolution().size()==solSize);
		assertTrue(pCopy.getMaxFitness()==s3.getFitness(ff));
		assertTrue(pCopy.getPopCount()==2);
		assertTrue(pCopy.getSolCount()==4);
		assertTrue(pCopy.contains(s4));
		assertTrue(pCopy.contains(s2));
		
		
	}
	

}
