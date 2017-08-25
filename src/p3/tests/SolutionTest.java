package p3.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import p3.*;
import p3.fitness.FitnessFn;
import p3.fitness.OneMax;

import java.util.*;

public class SolutionTest {

	@Test
	public void equalsTrueTest() {
		BitSet bs = new BitSet(4);
		Solution s1 = new Solution(bs, 4);
		Solution s2 = new Solution(bs, 4);
		assertTrue(s1.equals(s2));
	}
	
	@Test
	public void stringTest() {
		BitSet bs = new BitSet(4);
		bs.set(1, 3);
		Solution s = new Solution(bs, 4);
		assertTrue(s.toString().equals("0110"));
	}
	
	@Test
	public void setFrom0To1Test() {
		BitSet bs = new BitSet(8);
		Solution s = new Solution(bs, 8);
		s.set(1, true);
		assertTrue(s.toString().equals("01000000"));
	}
	
	@Test
	public void setFrom1To0Test() {
		BitSet bs = new BitSet(8);
		bs.set(0, 7);
		Solution s = new Solution(bs, 8);
		s.set(7, false);
		assertTrue(s.toString().equals("11111110"));
	}
	
	@Test
	public void getTest() {
		BitSet bs = new BitSet(3);
		bs.set(1);
		Solution s = new Solution(bs, 3);
		assertTrue(s.get(1)&&!s.get(2));
	}
	
	@Test
	public void flipTest() {
		BitSet bs = new BitSet(3);
		Solution s = new Solution(bs, 3);
		s.set(0, true);
		s.flip(0);
		s.flip(2);
		assertTrue(s.toString().equals("001"));
	}
	
	@Test
	public void fitnessTest(){
		FitnessFn oM = new OneMax(7);
		BitSet bs = new BitSet(7);
		Solution s0 = new Solution(bs, 7);
		assertTrue(0==s0.getFitness(oM));
		
		bs.set(3, 6);
		Solution s3 = new Solution(bs, 7);
		assertTrue(3==s3.getFitness(oM));
	}
	
	@Test
	public void stringConstructorTest(){
		String solutionString = "0110000101";
		Solution solution = new Solution(solutionString);
		assertTrue(solution.toString().equals(solutionString));
	}
	
	@Test
	public void randomConstructorTest() {
		int solSize = 1000;
		Random rand = new Random();
		Solution solution1 = new Solution(solSize, rand.nextLong());
		Solution solution2 = new Solution(solSize, rand.nextLong());
		assertTrue(solution1.size()==solution2.size());
		assertFalse(solution1.equals(solution2));
	}
	
	@Test
	public void equalsFalseTest() {
		Solution sol1 = new Solution("010110");
		
		Solution nullSol = null;
		assertFalse(sol1.equals(nullSol));
		
		String notASol = "This is not a solution.";
		assertFalse(sol1.equals(notASol));
		
		Solution solShorter = new Solution("01011");
		assertFalse(sol1.equals(solShorter));
		
		Solution diffGenSol = new Solution("010111");
		assertFalse(sol1.equals(diffGenSol));
	}
	
	@Test
	public void getCopyTest() {
		long randLong = new Random().nextLong();
		Solution s1 = new Solution(1000, randLong);
		Solution s2 = s1.getCopy();
		assertTrue(s1.equals(s2));
		s1.flip(0);
		assertFalse(s1.equals(s2));
	}
	
	@Test
	public void equalsStringTest() {
		Solution s1 = new Solution("0000");
		assertTrue(s1.equalsString("0000"));
		assertFalse(s1.equalsString("000"));
		assertFalse(s1.equalsString("00000"));
		assertFalse(s1.equalsString("0010"));
		
		Solution s2 = new Solution("11001101");
		assertTrue(s2.equalsString("11001101"));
		assertFalse(s2.equalsString("11001100"));
		assertFalse(s2.equalsString(""));
		assertFalse(s2.equalsString("1100110111"));
		
	}
	
	@Test
	public void stringConstructorError() {
		boolean exceptionCaught = false;
		try {
			new Solution("10101010not1or0oops");
		} catch(IllegalArgumentException e1) {
			System.out.println(e1.getMessage());
			assertTrue(e1.getMessage().length()>0);
			exceptionCaught = true;
		}
		assertTrue(exceptionCaught);
	}
	
	@Test
	public void setBitError() {
		boolean exceptionCaught = false;
		try {
			Solution sol = new Solution("11110000");
			sol.set(8, false);
		} catch(IndexOutOfBoundsException e1) {
			exceptionCaught = true;
		}
		assertTrue(exceptionCaught);
		
		exceptionCaught = false;
		try {
			Solution sol = new Solution("11110000");
			sol.set(-1, true);
		} catch(IndexOutOfBoundsException e1) {
			exceptionCaught = true;
		}
		assertTrue(exceptionCaught);
	}
	
	@Test
	public void getBitError() {
		boolean exceptionCaught = false;
		try {
			Solution sol = new Solution("1111");
			sol.get(5);
		} catch(IndexOutOfBoundsException e1) {
			exceptionCaught = true;
		}
		assertTrue(exceptionCaught);
		
		exceptionCaught = false;
		try {
			Solution sol = new Solution("1100");
			sol.get(-1);
		} catch(IndexOutOfBoundsException e1) {
			exceptionCaught = true;
		}
		assertTrue(exceptionCaught);
	}
	
	@Test
	public void flipBitError() {
		boolean exceptionCaught = false;
		try {
			Solution sol = new Solution("1111");
			sol.flip(99);
		} catch(IndexOutOfBoundsException e1) {
			exceptionCaught = true;
		}
		assertTrue(exceptionCaught);
		
		exceptionCaught = false;
		try {
			Solution sol = new Solution("000001100");
			sol.flip(-1);
		} catch(IndexOutOfBoundsException e1) {
			exceptionCaught = true;
		}
		assertTrue(exceptionCaught);
	}
	
	@Test
	public void creationStepTest() {
		Solution sol = new Solution("1010101010");
		
		sol.setCreationStep(0);
		
		sol.flip(4);
		sol.set(6, false);
		
		assertTrue(sol.getCreationStep()==0);
		
		sol.setCreationStep(99);
		assertTrue(sol.getCreationStep()==99);
		
		
	}

}
