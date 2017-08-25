/* Model.java
 * Given occurrences, builds 2d entropy array. Uses this to find distance 
 * between clusters. Can use this to build entire cluster tree, and return
 * ordered list of clusters for crossover.
 */

package p3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Model {
	
	// 2d array which stores single bit cluster entropy in the first entry of
	// each column. 2 variable clusters are stored in positions where column
	// index is greater than row index.
	private double[][] entropy;
	private int solutionSize;
	private int populationSize;
	private Random rand;
	private Cluster modelFullTree;
	
	// Constructor. Given counts for each bit pair permutation and population
	// size, creates the entropy array.
	public Model(int[][][] bitPairPermutationCounts, int pSize, long seed) {
		solutionSize = bitPairPermutationCounts.length;
		populationSize = pSize;
		rand = new Random(seed);
		
		// fill entropy array.
		entropy = new double[solutionSize][solutionSize];
		for (int i=0; i<solutionSize; i++) {
			//calculate single bit cluster entropy
			entropy[i][0] = calculateEntropy(bitPairPermutationCounts[i][0]);
			
			//calculate 2 bit cluster entropy
			for (int j=i+1; j<solutionSize; j++) {
				entropy[i][j] = calculateEntropy(bitPairPermutationCounts[i][j]);
			}
		}
		
	}
	
	// Given the counts for each bit pair combination (00,01,10,11), calculate 
	// the entropy.
	private double calculateEntropy(int[] bitComboCounts) {
		double clusterEntropy = 0;

		// Add to entropy for each bit pair permutation.
		for (int k=0; k<4; k++) {
			double pmf = (double)bitComboCounts[k]/(double)populationSize;
			double log2pmf = 0.0;

			// Check pmf doesn't equal 0 to avoid log(0).
			if (pmf!=0.0) {
				log2pmf = Math.log(pmf)/Math.log(2.0);
			}
			clusterEntropy -= pmf*log2pmf;					 
		}
		return clusterEntropy;
	}
	
	public double getEntropy(int clusterBit) {
		return entropy[clusterBit][0];
	}
	
	public double getEntropy(int clusterBit1, int clusterBit2) {
		// Depending on cluster variables given, access entropy array correctly.
		if (clusterBit1<clusterBit2) {
			return entropy[clusterBit1][clusterBit2];
		}
		else {
			return entropy[clusterBit2][clusterBit1];
		}
	}
	
	// Create the cluster tree then return it as a sorted list of int[] clusters 
	// from smallest to largest, ready for p3 crossover.
	public ArrayList<int[]> getClusterModel() {
		ArrayList<Cluster> clusterModel = new ArrayList<Cluster>();
		ArrayList<Cluster> unmerged = new ArrayList<Cluster>();		
		int creationStep = 0;
		
		// Add all single bit clusters to unmerged and model
		for(int i=0; i<solutionSize; i++) {
			int[] singleBitArr = {i};
			Cluster bitCluster = new Cluster(singleBitArr, creationStep);
			unmerged.add(bitCluster);
			clusterModel.add(bitCluster);
		}
		
		// Continue merging clusters until all have been merged.
		while (unmerged.size()>1) {
			creationStep++;
			//Collections.shuffle(unmerged, rand);
			Cluster minDistanceCluster = getSmallestDistance(unmerged, creationStep);
			
			// Add the combined cluster of the minimum distance pair to the model.
			clusterModel.add(minDistanceCluster);
			unmerged.add(minDistanceCluster);

			Cluster firstCluster = minDistanceCluster.getLeftChild();
			Cluster secondCluster = minDistanceCluster.getRightChild();
			
			//remove individual clusters from clusterModel 
			//as they are part of minDistanceCluster
			removeClusterFromList(clusterModel, firstCluster);
			removeClusterFromList(clusterModel, secondCluster);			
						
			// Remove the two individual clusters from unmerged.
			removeClusterFromList(unmerged, firstCluster);
			removeClusterFromList(unmerged, secondCluster);
			
			// Check if cluster distance is 0, if so remove individual clusters
			// from the pair from the model, as they are not useful for crossover.
			double clusterPairDistance = getDistance(firstCluster, secondCluster);
			if(clusterPairDistance==0.0) {
				firstCluster.setDead();
				secondCluster.setDead();
			}
		}
		
		// Only cluster in clusterModel should be full tree
		modelFullTree = clusterModel.get(0);
		
		// Set cluster containing all bits to dead, as it is unnecessary
		modelFullTree.setDead();
		
		ArrayList<int[]> modelArrayList = modelFullTree.asArrayList();
		
		// Shuffle the model and sort by cluster size.
		Collections.shuffle(modelArrayList, rand);
		Collections.sort(modelArrayList, Comparator.comparingInt(l -> l.length));
		
		return modelArrayList;
	}
	
	public Cluster getClusterModelTree() {
		if (modelFullTree==null) {
			getClusterModel();
		}
		return modelFullTree;
	}
	
	// Given an ArrayList of clusters and one to remove, search and remove from the arraylist
	private void removeClusterFromList(ArrayList<Cluster> clusterList, 
																Cluster clusterToRemove) {
		int[] toRemoveArr = clusterToRemove.getClusterValue();
		for (int i=0; i<clusterList.size(); i++) {
			int[] clusterListArr = clusterList.get(i).getClusterValue();
			if(Arrays.equals(clusterListArr, toRemoveArr)) {
				clusterList.remove(i);
			}
		}
	}

	// Uses average pairwise distance to efficiently measure an approximation
	// of distance between clusters.
	public double getDistance(Cluster firstCluster, Cluster secondCluster) {
		int[] cluster1Arr = firstCluster.getClusterValue();
		int[] cluster2Arr = secondCluster.getClusterValue();
		
		double variablePairsCount = 
				(double)cluster1Arr.length*cluster2Arr.length;
		double sumOfPairwiseDistances = 0.0;
		
		// For each bit pair, add the distance to the sumOfPairwiseDistances.
		for (int i=0; i<cluster1Arr.length; i++) {
			for (int j=0; j<cluster2Arr.length; j++) {
				double cluster1Entropy = getEntropy(cluster1Arr[i]);
				double cluster2Entropy = getEntropy(cluster2Arr[j]);
				double combinedClusterEntropy = 
						getEntropy(cluster1Arr[i], cluster2Arr[j]);
				
				// If the entropy of the combined cluster is 0, distance is 2
				// to avoid dividing by 0.
				if (combinedClusterEntropy!=0) {
					sumOfPairwiseDistances += 2-
						(cluster1Entropy+cluster2Entropy)/combinedClusterEntropy;
				}
			}
		}
		
		return sumOfPairwiseDistances/variablePairsCount;
	}

	// Calculate the distance for each pair of clusters to find the 
	// pair with the smallest distance.
	public Cluster getSmallestDistance(ArrayList<Cluster> clusterList, int creationStep) {
		Cluster smallestDistanceCluster = new Cluster(null, creationStep);
		double smallestDistance = Double.POSITIVE_INFINITY;
		
		// For each pair of clusters in the list, get distance and compare with
		// current minimum distance found.
		for(int i=0; i<clusterList.size(); i++) {
			for(int j=i+1; j<clusterList.size(); j++) {
				double clusterDistance = 
						getDistance(clusterList.get(i),clusterList.get(j));
				if (clusterDistance<smallestDistance) {
					smallestDistance = clusterDistance;
					smallestDistanceCluster = 
							new Cluster(clusterList.get(i),clusterList.get(j), creationStep);
				}
				if (smallestDistance==0.0) {
					return smallestDistanceCluster;
				}
			}
		}
		
		return smallestDistanceCluster;
	}
	
	public String toString() {
		Cluster tree = getClusterModelTree();
		return tree.toString();
	}
	
}