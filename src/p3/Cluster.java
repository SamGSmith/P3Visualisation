/* Cluster.java
 * Class representing a single cluster, primarily holding the integer array representing
 * the bits of the cluster, but also whether it is alive. Each Cluster can either be a leaf,
 * which has no children, or a node with two child clusters. A cluster can represent a binary 
 * tree.
 */

package p3;

import java.util.ArrayList;

public class Cluster {

	private int[] clusterValue;
	private boolean alive, isLeaf;
	private int creationStep;
	private Cluster leftChild;
	private Cluster rightChild;
	
	//Constructor for leaf
	public Cluster(int[] clusterValue, int creationStep) {
		this.clusterValue = clusterValue;
		this.creationStep = creationStep;
		isLeaf = true;
		alive = true;
	}
	
	//Constructor for parent node
	public Cluster(Cluster leftChild, Cluster rightChild, int creationStep) {
		this.creationStep = creationStep;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		isLeaf = false;
		alive = true;
		sortChildren();
		this.clusterValue = combineChildren();
	}
	
	//Get the integer array with each variable in the cluster
	public int[] getClusterValue() {
		return clusterValue;
	}
	
	public Cluster getLeftChild() {
		return leftChild;
	}
	
	public Cluster getRightChild() {
		return rightChild;
	}
	
	// Combine child clusters to make this cluster
	public int[] combineChildren() {
		int[] combinedCluster = new int[leftChild.size()+rightChild.size()];
		System.arraycopy(leftChild.getClusterValue(), 0, 
				combinedCluster, 0, leftChild.size());
		
		System.arraycopy(rightChild.getClusterValue(), 0, 
				combinedCluster, leftChild.size(), rightChild.size());
		
		return combinedCluster;
	}
	
	// Return the size of the current cluster
	public int size() {
		return clusterValue.length;
	}
	
	public int getCreationStep() {
		return creationStep;
	}

	// Used to determine if cluster should be used in final model.
	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive() {
		alive = true;
	}
	
	public void setDead() {
		alive = false;
	}
	
	public boolean isLeaf() {
		return isLeaf;
	}
	
	// Sort child clusters by first value in array
	public void sortChildren() {
		if(rightChild.getClusterValue()[0]<leftChild.getClusterValue()[0]) {
			Cluster tempCluster = leftChild;
			leftChild = rightChild;
			rightChild = tempCluster;
		}
	}
	
	// Find binary tree height
	public int getHeight() {
		int height = 1;
		if(!isLeaf) {
			int leftHeight = leftChild.getHeight();
			int rightHeight = rightChild.getHeight();
			height += (leftHeight>rightHeight) ? leftHeight : rightHeight;
		}
		return height;
	}
	
	// Flatten binary tree by recursively calling asArrayList to return arraylist of clusters
	public ArrayList<int[]> asArrayList() {
		ArrayList<int[]> arrList = new ArrayList<int[]>();
		
		if (!isLeaf) {
			for (int[] arr : leftChild.asArrayList()) {		
				arrList.add(arr);
			}			
		}
		
		if (alive) {
			arrList.add(clusterValue);			
		}
		
		if (!isLeaf) {
			for (int[] arr : rightChild.asArrayList()) {
				arrList.add(arr);
			}						
		}
		
		return arrList;
	}
	
	public String toString() {
		ArrayList<int[]> clusterList = asArrayList();
		
		StringBuilder s = new StringBuilder();
		
		for (int[] cluster : clusterList) {
			s.append("[");
			for (int i=0; i<cluster.length; i++) {
				s.append(cluster[i]+", ");
			}
			s.delete(s.length()-2, s.length());
			s.append("], ");
		}
		if(clusterList.size()>0) {
			s.delete(s.length()-2, s.length());		
		}
		
		return s.toString();
	}
	
}
