package ie.nuigalway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
public class C45 {

	String[] attributes;
	List<Node> nodes;
	//	List<Instance> train;
	//	List<Instance> test;
	//	HashMap<String,Integer> targetCount; //hashmap that holds the number of each instance 'type'
	//
	//	List<List<Double>> attSV; //List that holds lists of 'split' values for each sorting order of the training instances
	//	List<List<String>> attST; //List that holds lists of instance type values for each split in each ordering of training instances
	//	List<List<Integer>> attSC; //List that holds lists of instance type counts before each split for each split in each ordering of training instances
	//	List<HashMap<String,Double>> entropyValues;
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
		if(tr!=null){
			Node node = new Node(tr);

			runC45(node);
		}

	}


	public void runC45(Node node){


		//gets the data splits for each attribute and count
		int x = node.getData().get(0).getAttributes().length - 1;

		for (int i = 0; i < x; i++){
			System.out.println("Sorted by: "+attributes[i]);
			Instance.setSortAttribute(i);
			Collections.sort(node.getData());

			thresholdSplit(node, i);

			for(Instance a: node.getData()){
				System.out.println(a.toString());
			}
			System.out.println("Instance types calculated: "+ node.getattST().get(i).toString());
			System.out.println("Instance type count calculated: "+ node.getattSC().get(i).toString());
			System.out.println("Midpoints calculated: "+ node.getattSV().get(i).toString());
		}
		System.out.println("Number of each type of owl: "+countInstances(node));
	}

	public HashMap<String,Integer> countInstances(Node node){
		//Counts the instance of each target value in the dataset

		HashMap<String,Integer> targetCount = new HashMap<String, Integer>();

		String [] counter = new String[node.getData().size()];
		int c = 0;
		for(Instance in: node.getData()){

			counter[c]= in.getType();
			c++;
		}
		for(String s: counter){
			if (targetCount.containsKey(s)){
				targetCount.put(s, targetCount.get(s) + 1);
			}else{
				targetCount.put(s,new Integer(1));
			}
		}

		node.setTargetCount(targetCount);
		return targetCount;

	}


	public List<Double> thresholdSplit(Node node, int i){

		List<Double> splits = new ArrayList<Double>();
		List<Integer> splitCount = new ArrayList<Integer>();
		List<String> splitType = new ArrayList<String>();
		int tar = node.getData().get(0).getAttributes().length-1;
		int count = 0;

		for(int y = 0; y < node.getData().size()-1; y++){

			String current = node.getData().get(y).getType();
			String next =  node.getData().get(y+1).getType();

			count++;
			if(!current.equals(next)){
				//int count = y+1;
				Double split = ((Double) (node.getData().get(y).getAttributes()[i]) + (Double)(node.getData().get(y+1).getAttributes()[i]))/2;
				splits.add(split);
				splitCount.add(count);
				splitType.add(current);
				count=0;
			}

		}
		List<List<Double>> asv = node.getattSV();
		asv.add(splits);
		node.setattSV(asv);

		List<List<Integer>> asc = node.getattSC();
		asc.add(splitCount);
		node.setattSC(asc);

		List<List<String>> ast = node.getattST();
		ast.add(splitType);
		node.setattST(ast);

		return null;

	}

	public double calculateEntropys(Node node){

		for(List<Double> a : node.getattSV() ){

			for (int l=0; l< a.size(); l++){

				a.get(l);
			}

		}


		// y/numAllInstanceB4split * log base 2 (y/


		return 0;
	}



	public double calculateIG(){
		return 0;
	}

	public void train(){

	}


	public void test(){}
}
