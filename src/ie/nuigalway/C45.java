/**
 * Author - Matthew Finn 2016
 */

package ie.nuigalway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class C45 {

	String[] attributeNames;
	List<Node> nodes = new ArrayList<Node>();
	HashMap<String,Integer> attributeIndexes;
	String spl;
	Double val;

	public C45(String[] atts, List<Instance> tr){

		attributeNames = atts; //attribute names for each instances
		//create a hashmap for attribute index values so each
		//instance attribute can be accessd through attribute name
		attributeIndexes =new HashMap<>();
		for(int i = 0; i < attributeNames.length; i++){

			attributeIndexes.put(attributeNames[i],i);
		}
		//if the dataset for the root node is not null create the root node and run C4.5
		if(tr!=null){

			Node node = new Node(tr);
			runC45(node);
		}
	}

	public void runC45(Node node){

		Node nd = node;
		val = 0.0; //setting the initial value of the variable used to set the value of the node created
		spl = null; //setting the initial value of the variable used to set the name of the node created

		//if checkBaseCases returns true the node can be added to the list of nodes for the algorithm
		if(checkBaseCases(nd)){
			nodes.add(nd);
		}
		//if checkBaseCases returns false then the entropy and information gain for each attribute in the node is calculates
		else if(!checkBaseCases(nd)) {
			//gets the number of attribute (Less the target) for each instance
			int x = nd.getData().get(0).getAttributes().length - 1;

			//loops over each attribute in the training set instances
			for (int i = 0; i < x; i++){

				Instance.setSortAttribute(i);
				Collections.sort(nd.getData());

				countInstances(nd);
				thresholdSplit(nd, i);
				calculateEntropys(nd);
			}
		}

	}

	public boolean checkBaseCases(Node node){

		Node nd = node;
		//All instances belong to the same class
		HashMap<String,Integer> targetCount = countInstances(nd);
		for (Instance inst : node.getData()){
			for (String key : targetCount.keySet()) {
				if(targetCount.get(key)==nd.getData().size()){
					nd.setName(key);
					nd.setValue(null);
					nd.setHasChildren(false);
					System.out.println("Node created with value "+key);
					return true;
				}
			}
		}

		//if training set is empty
		if(nd.getData().isEmpty()){
			System.out.println("No data in array");
			nd.setName("Failure To Classify");
			nd.setValue(null);
			nd.setHasChildren(false);
			return true;
		}

		//new class type encountered.
		for(Instance inst:nd.getData()){
			if(targetCount.get(inst.getType()) == null){
				nd.setValue(null);
				nd.setHasChildren(false);
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 * @param node
	 * @return hashmap of instances types and their value(count) in node
	 */
	public HashMap<String,Integer> countInstances(Node node){
		Node nd = node;

		//Counts the instance of each target value in the dataset
		HashMap<String,Integer> targetCount = new HashMap<String, Integer>();

		String [] instanceCount = new String[nd.getData().size()];
		int c = 0;
		for(Instance in: nd.getData()){

			instanceCount[c]= in.getType();
			c++;
		}
		for(String s: instanceCount){
			if (targetCount.containsKey(s)){
				targetCount.put(s, targetCount.get(s) + 1);
			}else{
				targetCount.put(s,new Integer(1));
			}
		}
		nd.setTargetCount(targetCount);
		return targetCount;
	}

	/**
	 *
	 * @param node
	 * @param i - attribute index to sort
	 * @return list of midpoint split values
	 */
	public void thresholdSplit(Node node, int i){

		Node nd = node;
		int u = i;

		List<Double> splits = new ArrayList<Double>(); //arraylist to hold attribute midpoint split values
		List<Integer> splitCount = new ArrayList<Integer>();//arraylist to hold number of instances before each split
		List<String> splitType = new ArrayList<String>();//arraylist to hold type of instance value split on
		int count = 0; //counter

		for(int y = 0; y < nd.getData().size()-1; y++){

			String current = nd.getData().get(y).getType(); //current instance type
			String next =  nd.getData().get(y+1).getType(); //instance after current type

			count++; //increment counter
			if(!current.equals(next)){ //if current instance type is not equal to next instance type

				//get value at y and y+1, add them and divide by 2 to get midpoint between
				Double split = ((Double) (nd.getData().get(y).getAttributes()[u]) +
						(Double)(nd.getData().get(y+1).getAttributes()[u]))/2;
				splits.add(split); //adds the value of the split
				splitCount.add(count); //adds the number of instances between each split
				splitType.add(current); //adds the instance type of the instance before the split
				count=0; //resets counter
			}

		}
		List<List<Double>> asv = nd.getattSV();
		asv.add(splits);
		nd.setattSV(asv); //assigns split value List to node

		List<List<Integer>> asc = nd.getattSC();
		asc.add(splitCount);
		nd.setattSC(asc); //assigns split count list to node

		List<List<String>> ast = nd.getattST();
		ast.add(splitType);
		nd.setattST(ast); //assigns split instance type to node
	}

	public void calculateEntropys(Node node){

		Node nd = node;

		/*
		 * 1. Get list of lists of split values
		   2. Get list of lists of split types
		   3. Get list of lists of counts before split for each target
		   4. Generate List of counts after split for each target
		      iterate for each numerical attribute*/

		Map<String, Integer> map = nd.getTargetCount();
		int b4 = 0; //int var to hold before split instance count

		List<List<Double>> splitValues = nd.getattSV();
		List<List<String>> attST = nd.getattST();
		List<List<Integer>> attSC = nd.getattSC();
		int x = 0; //counter
		for(List<Double> a: splitValues){ //iterate over lists of split values

			List<String> targets = attST.get(x); //gets list of split targets
			List<Integer> targetCount = attSC.get(x); //gets list of counts before each split
			Map<String,Integer> beforeSplit = new HashMap<String, Integer>();
			Map<String,Integer> afterSplit = new HashMap<String, Integer>();

			for(String tar: map.keySet()){ //puts each target value in the hashmap
				beforeSplit.put(tar, 0);
				afterSplit.put(tar, map.get(tar));
			}

			for(int i = 0 ; i < a.size(); i ++){ //for each split value in array of split values

				String target = targets.get(i);
				int tarCount = targetCount.get(i);
				beforeSplit.put(target, beforeSplit.get(target)+tarCount);
				afterSplit.put(target, map.get(target)-beforeSplit.get(target));

				int r = 0;
				for (int f : beforeSplit.values()) { //gets number of all instances before split
					r += f;
				}
				int g = 0;
				for (int h : afterSplit.values()) { //gets the number of all instances after the split
					g += h;
				}

				double entb4 = 0;
				double m = 0;
				for (int j : beforeSplit.values()) {
					if(j!=0){

						double p = j;
						double t = r;
						//Sum of :
						//target value occurances before split /
						//total num of instances before split *
						//log base 2 (target value occurances before split/num instances before split)
						entb4 += (-p/t * (Math.log(p/t)/Math.log(2)));
						m+=j; //count of all values before split
					}
				}

				double entAf =0;
				double n = 0; //count of total values after split
				for (int j : afterSplit.values()) {
					if(j!=0){

						double q = j;
						double w = g;

						//Sum of :
						//target value occurances after split /
						//total num of instances after split *
						//log base 2 (target value occurances before split/num instances after split)
						entAf += (-q/w * (Math.log(q/w)/Math.log(2)));
						n+=j;
					}
				}
				b4 = (int) m; //cast before split occurace to an int to send to splitList function

				//if either entropy is 0.0 set the node name and value as the target type before split
				if(entb4==0.0 && entAf==0.0){
					nd.setName(attributeNames[x]);
					System.out.println("Node created with value "+nd.getName()+"><="+nd.getValue());
					nd.setValue(a.get(i));
					nd.setHasChildren(true);
					nodes.add(nd);
					splitList(nd.getName(),nd.getValue(),nd, b4);
				}else{

					calculateInformationGains(nd, attributeNames[x], entb4, entAf, m, n, a.get(i));
				}
			}
			x++;
		}
		if(spl!=null || val!=0.0){
			nd.setName(spl);
			nd.setValue(val);
			nd.setHasChildren(true);
			System.out.println("Node created with value "+nd.getName()+"><="+nd.getValue());
			nodes.add(nd);
			splitList(nd.getName(), nd.getValue(), nd, b4);
		}

	}
	/**
	 *
	 * @param node - to get data needed to calculate info gain
	 * @param target - target to store gains for
	 * @param entBe - entropy before split
	 * @param entAf - entropy after split
	 * @param countb4 - count of instances before split
	 * @param countaf - count of instance after split
	 * @param splitValue - value data is split on
	 */
	public void calculateInformationGains(Node node, String target,
			Double entBe, Double entAf, Double countb4, Double countaf, Double splitValue){

		Map<String, Integer> totalCount = node.getTargetCount(); //get target counts for node passed in
		HashMap<String, Double> infoGains; //new hashmap
		Node nd = node;

		if(nd.getIGValues()!=null){ //if the node contains IG values get them
			infoGains = nd.getIGValues();
		}else{ //else initialise new hashmap
			infoGains = new HashMap<String,Double>();
		}
		int s = nd.getData().size(); //size of the training data set
		double ig=0; //double initialised that will be used to store information gain val

		//gets the IG of the entire training data set
		for(int j: totalCount.values()){
			double tar = j;
			double size = s;

			ig += (-tar/size * (Math.log(tar/size)/Math.log(2)));
		}
		//ent(training data set) - entropy of before after split pt.
		double infoGain = ig - (entBe*(countb4/s))-(entAf*(countaf/s)); //gets info gain for split value

		//if the target attribute doesn't exist add it to hashmap and give it value calculated
		if(infoGains.get(target)==null){
			infoGains.put(target, infoGain);
			spl = target;
			val = splitValue;
		}
		//only adds information gain for the target attribute if it's greater than the current value
		if(infoGains.get(target)<infoGain){
			infoGains.put(target, infoGain);
			spl = target;
			val = splitValue;
		}
		nd.setIGValues(infoGains); //sets nodes map of IG values to map generated
	}

	public void splitList(String name, Double v, Node node, int a){

		Node nd = node;
		int x = attributeIndexes.get(name);
		Instance.setSortAttribute(x); //setting the sorting attribute so list can be split on IG value
		Collections.sort(nd.getData());

		//new lists to hold instance objects when list passed in is split
		List<Instance> l1 = new ArrayList<Instance>();
		List<Instance> l2 = new ArrayList<Instance>();

		int i = 0;
		for (Instance in : nd.getData()){

			if(i<a){
				l1.add(in);
			}
			if(i>=a){
				l2.add(in);
			}

			i++;
		}
		Node n1 = new Node(l1);
		Node n2 = new Node(l2);

		if( nd.getChildren().length<2){
			nd.addChild(n1,0);
			runC45(n1);


			nd.addChild(n2,1);
			runC45(n2);
		}
	}
}
