/* Pyramid.java
 * Class containing the pyramid of solutions, stored as a series of populations.
 */

package p3;

import java.util.*;

import p3.fitness.*;

public class Pyramid {

	private ArrayList<Population> pyramid = new ArrayList<Population>();
	private HashMap<Solution, Double> pyramidMap = new HashMap<Solution, Double>();
	private int solutionSize;
	private FitnessFn fitnessFunction;
	private Solution maxFitnessSolution;
	private double maxFitness = Double.MIN_VALUE;
	private int evalCount;

	public Pyramid(int solutionSize, FitnessFn fitnessFunction) {
		this.solutionSize = solutionSize;
		this.fitnessFunction = fitnessFunction;
		Population population0 = new Population(solutionSize);
		pyramid.add(population0);
		evalCount=0;
	}

	public Solution getFittestSolution() {
		return maxFitnessSolution;
	}

	public double getMaxFitness() {
		return maxFitness;
	}

	// Check whether pyramid contains a specified solution
	public boolean contains(Solution solution) {
		return pyramidMap.containsKey(solution);
	}

	// Add a new solution to the pyramid in a specified population
	public void addSolution(Solution solToAdd, int popIndex, long seed) {
		pyramid.get(popIndex).add(solToAdd, seed);
		addToMap(solToAdd, solToAdd.getFitness(fitnessFunction));
	}

	// Used to add a new population with a solution
	public void addSolutionToNewPopulation(Solution solToAdd, long seed) {
		Population nextPop = new Population(solutionSize);
		nextPop.add(solToAdd, seed);
		pyramid.add(nextPop);
		addToMap(solToAdd, solToAdd.getFitness(fitnessFunction));
	}

	// Add to map to keep track of all solutions, and check if new fittest solution
	private void addToMap(Solution solution, double fitness) {
		// double fitness = solution.getFitness(fitnessFunction);
		pyramidMap.put(solution, fitness);

		// Check if solution being added is the most fit in pyramid
		if (fitness > maxFitness) {
			maxFitnessSolution = solution;
			maxFitness = fitness;
		}
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Pyramid\n");
		for (Population population : pyramid) {
			s.append(population.toString() + "\n");
		}
		return s.toString();
	}

	public Population getPopulation(int popIndex) {
		return pyramid.get(popIndex);
	}

	public ArrayList<Population> getPyramid() {
		return pyramid;
	}

	public int getPopCount() {
		return pyramid.size();
	}

	public int getSolCount() {
		int solCount = 0;
		for (Population pop : pyramid) {
			solCount+=pop.size();
		}
		return solCount;
	}
	
	public FitnessFn getFF() {
		return fitnessFunction;
	}
	
	public void increaseEvalCount(int increaseAmount) {
		evalCount+=increaseAmount;
	}
	
	public int getEvalCount() {
		return evalCount;
	}

	public Pyramid getDeepCopy() {
		ArrayList<Population> pyramidListCopy = new ArrayList<Population>(pyramid.size());
		for (Population pop : pyramid) {
			pyramidListCopy.add(pop.getDeepCopy());
		}
		
		HashMap<Solution, Double> pyramidMapCopy = new HashMap<Solution, Double>();
		for (Map.Entry<Solution, Double> entry : pyramidMap.entrySet()) {
			pyramidMapCopy.put(entry.getKey().getCopy(), entry.getValue());
		}
		
		Solution maxFitnessSolutionCopy = null;
		if (maxFitnessSolution!=null) {
			maxFitnessSolutionCopy = maxFitnessSolution.getCopy();
		}
		
		return new Pyramid(solutionSize, fitnessFunction, pyramidListCopy, pyramidMapCopy, 
				maxFitnessSolutionCopy, maxFitness, evalCount);
	}

	// Constructor used for deep copy
	public Pyramid(int solutionSize, FitnessFn fitnessFunction, ArrayList<Population> pyramid,
			HashMap<Solution, Double> pyramidMap, Solution maxFitnessSolution, double maxFitness, 
			int evalCount) {
		this.solutionSize = solutionSize;
		this.fitnessFunction = fitnessFunction;
		this.pyramid = pyramid;
		this.pyramidMap = pyramidMap;
		this.maxFitnessSolution = maxFitnessSolution;
		this.maxFitness = maxFitness;
		this.evalCount = evalCount;
	}
}