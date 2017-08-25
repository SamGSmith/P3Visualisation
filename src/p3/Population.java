/* Population.java
 * Class for a set of individual solutions known as a population. 
 */

package p3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

public class Population {

	private ArrayList<Solution> population;
	private ArrayList<int[]> clusterModelList;
	private Cluster tree;
	private int solutionSize, populationSize;

	// An array which stores the counts for each binary combination (00,01,10,11) between
	// each combination of two bits. The first column stores single bit counts, (i.e. 
	// bitPairPermutationCounts[i][0] = [[2],[4],[],[]]. Information is only stores where
	// column value is greater than row (to avoid duplicate info), i.e. 
	// bitPairPermutationCounts[0][1] will contain an array but 
	// bitPairPermutationCounts[1][0] will be empty. Used to create cluster model.
	private int[][][] bitPairPermutationCounts;

	public Population(int solutionSize) {
		this.solutionSize = solutionSize;
		bitPairPermutationCounts = new int[solutionSize][solutionSize][4];
		population = new ArrayList<Solution>();
		populationSize = 0;
		clusterModelList = new ArrayList<int[]>();
	}

	// Adds solution to population, and checks each permutation of bit pairs to
	// add to bit pair counts. Also recreates the model.
	public void add(Solution solution, long seed) {

		population.add(solution); 

		BitSet genotype = solution.genotype();
		for (int i = 0; i < solutionSize; i++) {
			int bitValue = genotype.get(i) ? 1 : 0; 

			// Use first column for single variable counts
			bitPairPermutationCounts[i][0][bitValue] += 1;

			for (int j = i + 1; j < solutionSize; j++) {
				//Find pairvalue based on bitvalue and solution bit j value
				int pairValue = bitValue + (genotype.get(j) ? 2 : 0);
				bitPairPermutationCounts[i][j][pairValue] += 1;
			}
		}

		populationSize += 1;

		//Recreate model and associated tree, as new solution has been added
		Model model = new Model(bitPairPermutationCounts, populationSize, seed);
		clusterModelList = model.getClusterModel();
		tree = model.getClusterModelTree();
		//System.out.println("Tree: "+ tree.toString());
	}

	public ArrayList<Solution> getPopulation() {
		return population;
	}

	public Solution getSolution(int index) {
		return population.get(index);
	}

	public ArrayList<int[]> getClusterModel() {
		return clusterModelList;
	}

	public Cluster getTree() {
		return tree;
	}

	public boolean inPopulation(Solution s) {
		return population.contains(s);
	}

	public boolean isEmpty() {
		return population.isEmpty();
	}

	public int size() {
		return populationSize;
	}

	public int[][][] getCombinationCounts() {
		return bitPairPermutationCounts;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		if (population.size() > 0) {
			for (Solution solution : population) {
				s.append(solution.toString() + ", ");
			}
			s.delete(s.length() - 2, s.length());
		} else {
			s.append("Empty");
		}
		return s.toString();
	}

	public Population getDeepCopy() {
		ArrayList<Solution> populationCopy = new ArrayList<Solution>(populationSize);
		for (Solution sol : population) {
			populationCopy.add(sol.getCopy());
		}

		ArrayList<int[]> clusterModelListCopy = new ArrayList<int[]>();
		for (int[] cluster : clusterModelList) {
			clusterModelListCopy.add(Arrays.copyOf(cluster, cluster.length));
		}
		
		int[][][] bitPairPermCountsCopy = new int[solutionSize][solutionSize][4];
		for (int i=0; i<solutionSize; i++) {
			for (int j=0; j<solutionSize; j++) {
				for (int k=0; k<4; k++) {
					bitPairPermCountsCopy[i][j][k] = bitPairPermutationCounts[i][j][k];					
				}
			}
		}
		
		//Able to shallow copy as tree is only set once, and a new one is
		//made each time (rather than changing values).
		Cluster treeCopy = tree;
		
		return new Population(solutionSize, populationSize, populationCopy, 
				clusterModelListCopy, bitPairPermCountsCopy, treeCopy);
	}

	//Constructor used for deep copy, setting all variables
	public Population(int solutionSize, int populationSize, ArrayList<Solution> population,
			ArrayList<int[]> clusterModelList, int[][][] bitPairPermutationCounts, Cluster tree) {
		this.solutionSize = solutionSize;
		this.populationSize = populationSize;
		this.population = population;
		this.clusterModelList = clusterModelList;
		this.bitPairPermutationCounts = bitPairPermutationCounts;
		this.tree = tree;
	}

}