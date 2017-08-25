/* Solution.java
 * class to represent a single solution in the ga
 */

package p3;

import java.util.*;

import p3.fitness.FitnessFn;

public class Solution {
	
	private BitSet genotype;
	private int size;
	private double fitness;
	private int creationStep;
	
	// Constructs a new random solution based on solution size and seed
	public Solution(int size, long seed) {
		this.size = size;
		genotype = new BitSet(size);
		
		Random rand = new Random(seed);
		for (int i = 0; i<size; i++) {
			genotype.set(i, rand.nextBoolean());
		}
	}
	
	// Constructs a solution given a specified bitset
	public Solution(BitSet b, int size) {
		genotype = (BitSet) b.clone();
		this.size = size;
	}
	
	// Constructs a solution from a given string of 1s and 0s
	public Solution(String s) throws IllegalArgumentException {
		int stringLength = s.length();
		BitSet sGenotype = new BitSet(stringLength);
		for (int i=0; i<stringLength; i++) {
			char c = s.charAt(i);
			if (c=='0') {
				sGenotype.set(i, false);
			}
			else if (c=='1') {
				sGenotype.set(i, true);
			} 
			else {
				throw new IllegalArgumentException("String must "
						+ "contain only 0s and 1s (no spaces etc)");
			}
		}
		this.genotype = sGenotype;
		this.size = stringLength;
	}
	
	// Check the genotype of two solutions are the same
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Solution)) {
			return false;			
		}
		
		Solution s2 = (Solution)obj;
		if (s2.size() == size) {
			return genotype.equals(s2.genotype());
		}
		else {			
			return false;			
		}
	}
	
	public int hashCode() {
		return genotype.hashCode();
	}
	
	public int size() {
		return size;
	}
	
	public BitSet genotype() {
		return genotype;
	}

	public void setCreationStep(int creationStep) {
		this.creationStep = creationStep;
	}
	
	public int getCreationStep() {
		return creationStep;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder(size);
		for (int i = 0; i < size; i++) {
			s.append(genotype.get(i) ? '1' : '0');
		}
		return s.toString();
	}
	
	public void set(int index, boolean value) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException();
		genotype.set(index, value);
	}

	public boolean get(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException();
		return genotype.get(index);
	}

	public void flip(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException();
		genotype.flip(index);
	}
	
	public double getFitness(FitnessFn ff) {
		fitness = ff.getFitness(this);
		return fitness;
	}
	
	// Return a deep copy of a solution
	public Solution getCopy() {
		BitSet copyGenotype = new BitSet(size);
		
		for(int i=0; i<size; i++) {
			copyGenotype.set(i, genotype.get(i));
		}
		
		Solution copy = new Solution(copyGenotype, size);
		return copy;
	}
	
	public boolean equalsString(String gString) {
		if(gString.length()!=size) {
			return false;
		} else {
			for (int i = 0; i < size; i++) {
				boolean bothZero = (gString.charAt(i)=='0')&&!genotype.get(i);
				boolean bothOnes = (gString.charAt(i)=='1')&&genotype.get(i);
				if (!(bothZero||bothOnes)) {
					return false;
				}
			}
			return true;
		}
	}

}