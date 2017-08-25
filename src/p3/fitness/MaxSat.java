/* MaxSat.java
 * A version of the satisfiability problem, where the solution is the conjunction of
 * several 3 variable disjunct clauses. The fitness is based on how many of these
 * clauses evaluate to true. 
 */

package p3.fitness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import p3.Solution;

public class MaxSat extends FitnessFn {
	private String setupString;
	private int numberOfClauses;
	private int fitnessEvalCount = 0;
	private ArrayList<int[]> clauseList = new ArrayList<int[]>();
	private ArrayList<boolean[]> signsList = new ArrayList<boolean[]>();
	
	// Possible clause values, all have at least one true, so that solution of all 1s
	// is an optimal solution
	//private boolean[][] signMatchOpts = { { true, false, false }, 
	//		{ false, true, false }, { false, false, true }, { false, true, true }, 
	//		{ true, false, true }, { true, true, false }, 	{ true, true, true } };
	
	// Version of signMatchOpts based on Goldman and Punch implementation where
	// 1/6th chance all match target, 1/6th chance two match target and 4/6th
	// chance 1 matches. Used to prevent bias towards certain signs.
	private boolean[][] signMatchOpts = { { true, false, false }, 
			{ false, true, false }, { false, false, true }, { false, true, false }, 
			{ true, false, true }, 	{ true, true, true } };
	private Random rand;

	public MaxSat(int solutionSize, double clauseToVarRatio, Solution targetSol, long seed) {
		this.numberOfClauses = Math.round((float) (clauseToVarRatio * solutionSize));
		
		//Set setupString for use with MaxSat.toString();
		setupString = "Solution Size: "+solutionSize+"\nClause to Variable Ratio: "+clauseToVarRatio+
				". Number of Clauses: "+numberOfClauses+"\nTarget Solution: "+targetSol.toString()+". Seed: "+seed+"\n";

		rand = new Random(seed);

		for (int i = 0; i < numberOfClauses; i++) {
			boolean[] signClauseOpts = signMatchOpts[rand.nextInt(signMatchOpts.length)];
			
			int[] clauseVars = new int[3];
			boolean[] clauseSigns = new boolean[3];
			
			ArrayList<Integer> uniqueRandVars = shuffledOptions(solutionSize);
			
			for (int j = 0; j < 3; j++) {
				int randVar = uniqueRandVars.get(j);
				clauseVars[j] = randVar;
				clauseSigns[j] = (signClauseOpts[j]) ? targetSol.get(randVar) : !targetSol.get(randVar); 
			}
			
			clauseList.add(clauseVars);
			signsList.add(clauseSigns);
		}

	}

	// Get fitness based on number of satisfied clauses
	@Override
	public double getFitness(Solution s) {
		double fitness = 0;

		for (int i = 0; i < numberOfClauses; i++) {
			int[] clause = clauseList.get(i);
			boolean[] clauseSigns = signsList.get(i);
			boolean var0 = (clauseSigns[0]) ? s.get(clause[0]) : !s.get(clause[0]);
			boolean var1 = (clauseSigns[1]) ? s.get(clause[1]) : !s.get(clause[1]);
			boolean var2 = (clauseSigns[2]) ? s.get(clause[2]) : !s.get(clause[2]);
			// Check if any of the variables in the clause have been satisfied
			if (var0 || var1 || var2) {
				fitness++;
			}
		}

		fitnessEvalCount++;
		return fitness;
	}

	@Override
	public double getOptimalFitness() {
		return numberOfClauses;
	}

	@Override
	public double getMinimumFitness() {
		return 0;
	}

	@Override
	public int getEvalCount() {
		return fitnessEvalCount;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("MAX-SAT\n");
		s.append(setupString);
		s.append("Clauses: ");
		s.append(getClauseString());
		return s.toString();
	}
	
	public String getClauseString() {
		StringBuilder s = new StringBuilder();
		for (int i=0; i<numberOfClauses; i++) {
			s.append("(");
			for (int j=0; j<3; j++) {
				String notSymbol = (signsList.get(i)[j]) ? "" : "\u00AC";
				s.append(notSymbol + clauseList.get(i)[j] + " \u2228 ");
			}
			s.delete(s.length()-3, s.length());
			s.append(") \u2227 ");
		}
		s.delete(s.length()-3, s.length());
		return s.toString();
	}
	
	private ArrayList<Integer> shuffledOptions(int size) {
		ArrayList<Integer> options = new ArrayList<Integer>(size);
		for (int i = 0; i < size; i++) {
			options.add(i);
		}

		Collections.shuffle(options, rand);
		return options;
	}

}
