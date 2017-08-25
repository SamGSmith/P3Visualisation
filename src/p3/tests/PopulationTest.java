package p3.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import p3.*;

public class PopulationTest {

	private Random rand = new Random();
	
	@Test
	public void populationSizeAndIsEmptyTest() {
		int solutionSize = 2;
		Population p = new Population(solutionSize);
		assertTrue(p.isEmpty()&&p.size()==0);
	}
	
	@Test
	public void populationSizeIsOneAndIsEmptyIsFalseTest() {
		int solutionSize = 1;
		Population population = new Population(solutionSize);
		Solution solution = new Solution(solutionSize, rand.nextLong());
		population.add(solution, rand.nextLong());
		assertTrue(!population.isEmpty()&&population.size()==1);
	}
	
	@Test
	public void toStringTest() {
		int solutionSize = 7;
		Population population = new Population(solutionSize);
		
		Solution solution1 = new Solution("0000000");
		population.add(solution1, rand.nextLong());
		Solution solution2 = new Solution("0000001");
		population.add(solution2, rand.nextLong());
		Solution solution3 = new Solution("0000010");
		population.add(solution3, rand.nextLong());
		Solution solution4 = new Solution("0000011");
		population.add(solution4, rand.nextLong());
		Solution solution5 = new Solution("0000100");
		population.add(solution5, rand.nextLong());
		Solution solution6 = new Solution("1111111");
		population.add(solution6, rand.nextLong());

		String popString = 
				"0000000, 0000001, 0000010, 0000011, 0000100, 1111111";
		
		assertTrue(population.toString().equals(popString));
	}
	
	@Test
	public void emptyPopToStringTest() {
		Population population = new Population(2);
		assertTrue(population.toString().equals("Empty"));
	}
	
	@Test
	public void combinationCountsTest() {
		int solutionSize = 2;
		Population population = new Population(solutionSize);
		
		Solution solution1 = new Solution("00");
		population.add(solution1, rand.nextLong());
		
		Solution solution2 = new Solution("01");
		population.add(solution2, rand.nextLong());
		
		Solution solution3 = new Solution("01");
		population.add(solution3, rand.nextLong());
		
		Solution solution4 = new Solution("11");
		population.add(solution4, rand.nextLong());
		
		//1d counts: ==0, ==1, 0, 0
		int[] expectedCC00 = {3,1,0,0};
		int[] expectedCC10 = {1,3,0,0};
		//2d counts: ==00, ==10, ==01, ==11
		int[] expectedCC01 = {1,0,2,1};
		//left at default value as not used
		int[] expectedCC11 = {0,0,0,0};
		
		int[][][] comboCounts = population.getCombinationCounts();
		int[] cc00 = comboCounts[0][0];
		int[] cc10 = comboCounts[1][0];
		int[] cc01 = comboCounts[0][1];
		int[] cc11 = comboCounts[1][1];
		
		assertTrue(Arrays.equals(expectedCC00, cc00));
		assertTrue(Arrays.equals(expectedCC10, cc10));
		assertTrue(Arrays.equals(expectedCC01, cc01));
		assertTrue(Arrays.equals(expectedCC11, cc11));
		
		//System.out.println(Arrays.toString(comboCounts[1][1]));
		
	}
	
	@Test
	public void inPopTest() {
		int solutionSize = 3;
		Population population = new Population(solutionSize);
		
		Solution solution1 = new Solution("000");
		population.add(solution1, rand.nextLong());
		
		Solution solution2 = new Solution("011");
		
		assertFalse(population.inPopulation(solution2));

		population.add(solution2, rand.nextLong());
		
		assertTrue(population.inPopulation(solution2));
	}
	
	@Test
	public void getPopSolTest() {
		int solutionSize = 3;
		Population population = new Population(solutionSize);
		ArrayList<Solution> solList = new ArrayList<Solution>();
		
		Solution solution1 = new Solution("010");
		solList.add(solution1);
		population.add(solution1, rand.nextLong());
		
		Solution solution2 = new Solution("101");
		solList.add(solution2);
		population.add(solution2, rand.nextLong());
		
		ArrayList<Solution> popAsList = population.getPopulation();
		for(int i=0; i<solList.size(); i++) {
			Solution solListSol = solList.get(i);
			Solution popListSol = popAsList.get(i);
			
			Solution popSol = population.getSolution(i);
			
			assertTrue(solListSol.equals(popListSol));
			assertTrue(solListSol.equals(popSol));
		}
	}

}
