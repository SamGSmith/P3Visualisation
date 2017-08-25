package p3.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.junit.Test;
import p3.*;

public class ModelTest {
	
	private Random rand = new Random();
	
	@Test
	public void entropyCalculationsTest() {
		// effectively making bit pair perm counts for pop(0110,1010,0100,1010).
		// Each cluster var is index of bit
		int[][][] bitPairPermutationCounts = new int[4][4][4];

		int[] clusterVar0 = { 2, 2, 0, 0 };
		int[] clusterVar1 = { 2, 2, 0, 0 };
		int[] clusterVar2 = { 1, 3, 0, 0 };
		int[] clusterVar3 = { 4, 0, 0, 0 };

		bitPairPermutationCounts[0][0] = clusterVar0;
		bitPairPermutationCounts[1][0] = clusterVar1;
		bitPairPermutationCounts[2][0] = clusterVar2;
		bitPairPermutationCounts[3][0] = clusterVar3;

		int[] clusterVar01 = { 0, 2, 2, 0 };
		int[] clusterVar02 = { 1, 1, 0, 2 };
		int[] clusterVar03 = { 2, 0, 2, 0 };
		int[] clusterVar12 = { 0, 2, 1, 1 };
		int[] clusterVar13 = { 2, 0, 2, 0 };
		int[] clusterVar23 = { 1, 0, 3, 0 };

		bitPairPermutationCounts[0][1] = clusterVar01;
		bitPairPermutationCounts[0][2] = clusterVar02;
		bitPairPermutationCounts[0][3] = clusterVar03;
		bitPairPermutationCounts[1][2] = clusterVar12;
		bitPairPermutationCounts[1][3] = clusterVar13;
		bitPairPermutationCounts[2][3] = clusterVar23;

		Model model = new Model(bitPairPermutationCounts, 4, rand.nextLong());

		Boolean entropyCorrect = model.getEntropy(0) == 1.0;
		entropyCorrect = entropyCorrect && model.getEntropy(1) == 1.0;
		entropyCorrect = entropyCorrect && model.getEntropy(2) == 0.8112781244591328;
		entropyCorrect = entropyCorrect && model.getEntropy(3) == 0.0;

		entropyCorrect = entropyCorrect && model.getEntropy(0, 1) == 1.0;
		entropyCorrect = entropyCorrect && model.getEntropy(0, 2) == 1.5;
		entropyCorrect = entropyCorrect && model.getEntropy(0, 3) == 1.0;
		entropyCorrect = entropyCorrect && model.getEntropy(1, 2) == 1.5;
		entropyCorrect = entropyCorrect && model.getEntropy(1, 3) == 1.0;
		entropyCorrect = entropyCorrect && model.getEntropy(2, 3) == 0.8112781244591328;

		assertTrue(entropyCorrect);
	}

	@Test
	public void getEntropy2BitsTest() {
		Solution s1 = new Solution("01101");
		Solution s2 = new Solution("11001");
		Solution s3 = new Solution("00001");

		Population p = new Population(5);
		p.add(s1, rand.nextLong());
		p.add(s2, rand.nextLong());
		p.add(s3, rand.nextLong());

		Model m = new Model(p.getCombinationCounts(), p.size(), rand.nextLong());

		Boolean ent04 = m.getEntropy(4, 0) == m.getEntropy(0, 4);
		Boolean ent01 = m.getEntropy(1, 0) == m.getEntropy(0, 1);
		Boolean ent34 = m.getEntropy(3, 4) == m.getEntropy(4, 3);

		assertTrue(ent04 && ent01 && ent34);
	}

	@Test
	public void getDistanceTest() {
		Solution s1 = new Solution("0110");
		Solution s2 = new Solution("1101");
		Solution s3 = new Solution("0000");
		Solution s4 = new Solution("0101");

		Population p = new Population(4);
		p.add(s1, rand.nextLong());
		p.add(s2, rand.nextLong());
		p.add(s3, rand.nextLong());
		p.add(s4, rand.nextLong());

		Model m = new Model(p.getCombinationCounts(), p.size(), rand.nextLong());

		int[] clusterArr0 = { 0 };
		Cluster cluster0 = new Cluster(clusterArr0, 0);
		int[] clusterArr3 = { 3 };
		Cluster cluster3 = new Cluster(clusterArr3, 1);
		int[] clusterArr12 = { 1, 2 };
		Cluster cluster12 = new Cluster(clusterArr12, 2);
		int[] clusterArr123 = { 1, 2, 3 };
		Cluster cluster123 = new Cluster(clusterArr123, 3);
		double d1 = m.getDistance(cluster0, cluster3);
		double d2 = m.getDistance(cluster12, cluster3);
		double d3 = m.getDistance(cluster0, cluster12);
		double d4 = m.getDistance(cluster0, cluster123);
		Boolean d1Correct = d1 == 0.792481250360578;
		Boolean d2Correct = d2 == 0.792481250360578;
		Boolean d3Correct = d3 == 0.9182958340544896;
		Boolean d4Correct = d4 == 0.8763576394898523;

		assertTrue(d1Correct && d2Correct && d3Correct && d4Correct);
	}

	@Test
	public void getSmallestsDistanceTest() {
		Solution s1 = new Solution("11010");
		Solution s2 = new Solution("11011");
		Solution s3 = new Solution("00111");
		Solution s4 = new Solution("11001");

		Population p = new Population(5);
		p.add(s1, rand.nextLong());
		p.add(s2, rand.nextLong());
		p.add(s3, rand.nextLong());
		p.add(s4, rand.nextLong());

		Model m = new Model(p.getCombinationCounts(), p.size(), rand.nextLong());

		int[] clusterArr01 = { 0, 1 };
		Cluster cluster01 = new Cluster(clusterArr01, 0);
		int[] clusterArr2 = { 2 };
		Cluster cluster2 = new Cluster(clusterArr2, 1);
		int[] clusterArr3 = { 3 };
		Cluster cluster3 = new Cluster(clusterArr3, 2);
		int[] clusterArr34 = { 3, 4 };
		Cluster cluster34 = new Cluster(clusterArr34, 3);
		ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
		clusterList.add(cluster01);
		clusterList.add(cluster2);
		clusterList.add(cluster3);
		clusterList.add(cluster34);

		Cluster cPair = new Cluster(cluster01, cluster2, 4);
		System.out.println(cPair.toString());

		Cluster smallestDistance = m.getSmallestDistance(clusterList, 4);
		System.out.println(smallestDistance.toString());

		assertTrue(cPair.toString().equals(smallestDistance.toString()));
	}

	@Test
	public void getClusterModelTest() {
		Solution s1 = new Solution("000000");
		Solution s2 = new Solution("110000");
		Solution s3 = new Solution("001100");
		Solution s4 = new Solution("100011");
		Population p = new Population(6);
		p.add(s1, rand.nextLong());
		assertTrue(p.getClusterModel().size() == 0);

		p.add(s2, rand.nextLong());
		ArrayList<int[]> cModel2 = p.getClusterModel();
		int[] cluster01 = { 0, 1 };
		int[] cluster2345 = { 2, 3, 4, 5 };
		Boolean contains01 = arrayListDeepContains(cModel2, cluster01);
		Boolean contains2345 = 
				arrayListDeepContains(cModel2, cluster2345);
		assertTrue(contains01 && contains2345);

		p.add(s3, rand.nextLong());
		ArrayList<int[]> cModel3 = p.getClusterModel();
		int[] cluster23 = { 2, 3 };
		int[] cluster45 = { 4, 5 };
		int[] cluster0123 = { 0, 1, 2, 3 };
		contains01 = arrayListDeepContains(cModel3, cluster01);
		Boolean contains23 = arrayListDeepContains(cModel3, cluster23);
		Boolean contains45 = arrayListDeepContains(cModel3, cluster45);
		Boolean contains0123 = arrayListDeepContains(cModel3, cluster0123);
		assertTrue(contains01 && contains23 && contains45 &&
																contains0123);

		p.add(s4, rand.nextLong());
		ArrayList<int[]> cModel4 = p.getClusterModel();
		int[] cluster0 = { 0 };
		int[] cluster1 = { 1 };
		int[] cluster023 = { 0, 2, 3 };
		int[] cluster045 = { 0, 4, 5 };
		//int[] cluster0145 = { 0, 1, 4, 5 };
		
		Boolean contains0 = arrayListDeepContains(cModel4, cluster0);
		Boolean contains1 = arrayListDeepContains(cModel4, cluster1);
		contains23 = arrayListDeepContains(cModel4, cluster23);
		contains45 = arrayListDeepContains(cModel4, cluster45);
		contains0123 = arrayListDeepContains(cModel4, cluster0123);
		
		contains01 = arrayListDeepContains(cModel4, cluster01);
		Boolean contains023 = arrayListDeepContains(cModel4, cluster023);
		Boolean contains045 = arrayListDeepContains(cModel4, cluster045);
		//Boolean contains0145 = arrayListDeepContains(cModel4, cluster0145);
		
		// If unmerged is shuffled, other clusters are possible
		assertTrue(contains0 && contains1 && contains23 && contains45 &&
					contains0123 && (contains045||contains023||contains01));
		

	}
	
	@Test
	public void treeAndToStringTest() {
		Solution s1 = new Solution("111000");
		Solution s2 = new Solution("101000");
		Solution s3 = new Solution("000111");
		Solution s4 = new Solution("100011");
		Population p = new Population(6);
		p.add(s1, rand.nextLong());
		p.add(s2, rand.nextLong());
		p.add(s3, rand.nextLong());
		p.add(s4, rand.nextLong());
		
		Model m = new Model(p.getCombinationCounts(), p.size(), rand.nextLong());
		Cluster tree = p.getTree();
		
		assertTrue(tree.toString().equals(m.toString()));
		
		
	}
	

	private boolean arrayListDeepContains(ArrayList<int[]> arrayList, 
													int[] clusterToCheck) {
		for (int[] cluster : arrayList) {
			if (Arrays.equals(cluster, clusterToCheck)) {
				return true;
			}
		}
		return false;
	}
	
}
