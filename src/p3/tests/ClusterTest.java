package p3.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import p3.Cluster;

public class ClusterTest {

	private int[] clusterVal = {5,4,10,0};
	private int creationStep = 0;
	private Cluster leaf = new Cluster(clusterVal, creationStep);
	
	@Test
	public void leafgetClusterTest() {
		assertTrue(Arrays.equals(leaf.getClusterValue(), clusterVal));		
	}
		
	@Test
	public void leafCreationStepTest() {
		assertTrue(leaf.getCreationStep()==creationStep);		
	}
		
	@Test
	public void leafSizeTest() {
		assertTrue(leaf.size()==clusterVal.length);		
	}
		
	@Test
	public void leafIsAliveSetDeadTests() {
		assertTrue(leaf.isAlive());
		leaf.setDead();
		assertTrue(!leaf.isAlive());
		leaf.setAlive();
		assertTrue(leaf.isAlive());		
	}
		
	@Test
	public void leafIsLeafTest() {
		assertTrue(leaf.isLeaf());
	}

	@Test
	public void leafGetHeightTest() {
		assertTrue(leaf.getHeight()==1);		
	}
	
	@Test
	public void leafArrListTest() {
		ArrayList<int[]> leafArrList = leaf.asArrayList();
		ArrayList<int[]> clusterArrList = new ArrayList<int[]>();
		clusterArrList.add(clusterVal);
		assertTrue(leafArrList.equals(clusterArrList));		
	}
	
	@Test
	public void leafToStringTest() {
		String clusterString = Arrays.toString(clusterVal);
		//System.out.println(clusterString);
		//System.out.println(leaf.toString());
		
		assertTrue(leaf.toString().equals(clusterString));
	}
	
	private int[] rightClusterVal = {124,1};
	private int rightCreationStep = 1;
	private Cluster rightChild = new Cluster(rightClusterVal, rightCreationStep);
	
	//Have right child of main node be a parent node 
	private int[] leftLeftClusterVal = {0,2,3,55};
	private int leftLeftCreationStep = 3;
	private Cluster leftLeftLeaf = new Cluster(leftLeftClusterVal, leftLeftCreationStep);
	private int[] leftRightClusterVal = {9};
	private int leftRightCreationStep = 4;
	private Cluster leftRightLeaf = new Cluster(leftRightClusterVal, leftRightCreationStep);
	private int leftCreationStep = 7;
	private Cluster leftChild = new Cluster(leftLeftLeaf, leftRightLeaf, leftCreationStep);

	private int parentCreationStep = 100;
	private Cluster parent = new Cluster(leftChild, rightChild, parentCreationStep);
	
	@Test
	public void getClusterValParentTest() {
		int[] expectedClusterVal = {0,2,3,55,9,124,1};
		//System.out.println(Arrays.toString(parent.getClusterValue()));
		assertTrue(Arrays.equals(parent.getClusterValue(), expectedClusterVal));
	}
	
	@Test
	public void parentCreationStepTest() {
		assertTrue(parent.getCreationStep()==parentCreationStep);		
	}
	
	@Test
	public void parentSizeTest() {
		assertTrue(parent.size()==7);		
	}
	
	@Test
	public void parentIsAliveSetDeadTests() {
		assertTrue(parent.isAlive());
		parent.setDead();
		assertTrue(!parent.isAlive());
		parent.setAlive();
		assertTrue(parent.isAlive());	
		rightChild.setDead();
		assertTrue(!rightChild.isAlive());	
		assertTrue(parent.isAlive());	
		
		//Set back to alive 
		parent.setAlive();
		rightChild.setAlive();
		
	}
	
	@Test
	public void parentIsLeafTest() {
		assertTrue(!parent.isLeaf());
		assertTrue(rightChild.isLeaf());
		assertTrue(leftLeftLeaf.isLeaf());
		assertTrue(!leftChild.isLeaf());
	}
	
	@Test
	public void parentGetHeightTest() {
		assertTrue(parent.getHeight()==3);		
		assertTrue(rightChild.getHeight()==1);		
		assertTrue(leftChild.getHeight()==2);		
	}
	
	@Test
	public void parentArrListTest() {
		ArrayList<int[]> parentArrList = parent.asArrayList();
		ArrayList<int[]> expectedArrList = new ArrayList<int[]>();
		expectedArrList.add(leftLeftClusterVal);		
		expectedArrList.add(leftChild.getClusterValue());
		expectedArrList.add(leftRightClusterVal);		
		expectedArrList.add(parent.getClusterValue());
		expectedArrList.add(rightClusterVal);
		
		//System.out.println(expectedArrList.toString());
		
		assertTrue(parentArrList.size()==expectedArrList.size());		
		
		for (int i=0; i<parentArrList.size(); i++) {
			//System.out.println(Arrays.toString(expectedArrList.get(i)));
			//System.out.println(Arrays.toString(parentArrList.get(i)));
			assertTrue(Arrays.equals(parentArrList.get(i), expectedArrList.get(i)));					
		}
		
		parent.setDead();
		rightChild.setDead();
		leftChild.setDead();
		parentArrList = parent.asArrayList();
		expectedArrList = new ArrayList<int[]>();
		expectedArrList.add(leftLeftClusterVal);		
		expectedArrList.add(leftRightClusterVal);		
		for (int i=0; i<expectedArrList.size(); i++) {
			//System.out.println(Arrays.toString(expectedArrList.get(i)));
			//System.out.println(Arrays.toString(parentArrList.get(i)));
			assertTrue(Arrays.equals(parentArrList.get(i), expectedArrList.get(i)));					
		}
		
	}
	
	
	@Test
	public void parentToStringTest() {
		String expected = "[0, 2, 3, 55], [0, 2, 3, 55, 9], [9], [0, 2, 3, 55, 9, 124, 1], [124, 1]";
		
		assertTrue(parent.toString().equals(expected));
	}
	
	@Test
	public void getChildrenTest() {
		int[] parentLeftChildCluster = parent.getLeftChild().getClusterValue();
		int[] leftChildCluster = leftChild.getClusterValue();
		assertTrue(Arrays.equals(leftChildCluster, parentLeftChildCluster));
		
		int[] parentRightChildCluster = parent.getRightChild().getClusterValue();
		assertTrue(Arrays.equals(rightClusterVal, parentRightChildCluster));
		
	}

	@Test
	public void sortChildrenTest() {
		//sortChildren will mean clusterval same no matter which 
		//order children in constructor
		
		Cluster parent2 = new Cluster(rightChild, leftChild, parentCreationStep);
		
		assertTrue(Arrays.equals(parent.getClusterValue(), parent2.getClusterValue()));
		
	}
	
}
