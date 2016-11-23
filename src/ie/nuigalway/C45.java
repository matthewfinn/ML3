package ie.nuigalway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class C45 {

	String[] attributes;
	List<Instance> train;
	List<Instance> test;
	List<List<Double>> attSplits;
	List<List<Integer>> attSplitCount;
	/**
	 * This algorithm has a few base cases.

		1. All the samples in the list belong to the same class. When this happens, it simply creates a leaf node for the decision tree saying to choose that class.
		2. None of the features provide any information gain. In this case, C4.5 creates a decision node higher up the tree using the expected value of the class.
		3. Instance of previously-unseen class encountered. Again, C4.5 creates a decision node higher up the tree using the expected value.


		Check for the above base cases.
		For each attribute a, find the normalized information gain ratio from splitting on a.
		Let a_best be the attribute with the highest normalized information gain.
		Create a decision node that splits on a_best.
		Recur on the sublists obtained by splitting on a_best, and add those nodes as children of node.
	 */

	public C45(String[] atts, List<Instance> tr, List<Instance> tst){

		attributes = atts;
		train = tr;
		test = tst;
		attSplits = new ArrayList<List<Double>>();
		attSplitCount = new ArrayList<List<Integer>>();
	}


	public void runC45(){

		//gets the data splits for each attribute and count
		int x = train.get(0).getAttributes().length - 1;
		for (int i = 0; i < x; i++){
			System.out.println("Sorted by: "+attributes[i]);
			Instance.setSortAttribute(i);
			Collections.sort(train);

			for(Instance a: train){
				System.out.println(a.toString());
			}
			thresholdSplit(train, i);
			System.out.println(attSplits.get(i).toString());
			System.out.println(attSplitCount.get(i).toString());
		}

		//System.out.println(attSplits.toString());



	}

	public double calculateEntropy(List<Instance> i){
		int set = train.size();


		return 0;
	}

	public double calculateIG(){
		return 0;
	}

	public List<Double> thresholdSplit(List<Instance> tr, int i){

		List<Double> splits = new ArrayList<Double>();
		List<Integer> splitCount = new ArrayList<Integer>();
		int tar = tr.get(0).getAttributes().length-1;
		int count = 0;

		for(int y = 0; y < tr.size()-1; y++){

			String current = tr.get(y).getType();
			String next =  tr.get(y+1).getType();

			count++;
			if(!current.equals(next)){
				//int count = y+1;
				Double split = ((Double) (tr.get(y).getAttributes()[i]) + (Double)(tr.get(y+1).getAttributes()[i]))/2;
				splits.add(split);
				splitCount.add(count);
				count=0;
			}

		}
		attSplits.add(splits);
		attSplitCount.add(splitCount);

		return null;

	}


	public void train(){

	}


	public void test(){}
}
