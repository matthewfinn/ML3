package ie.nuigalway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class C45 {

	String[] attributes;
	List<Node> nodes = new ArrayList<Node>();
	HashMap<String,Integer> attribute;
	String spl;
	Double val;

	/* This algorithm has a few base cases.
		Check for the above base cases.
		For each attribute a, find the normalized information gain ratio from splitting on a.
		Let a_best be the attribute with the highest normalized information gain.
		Create a decision node that splits on a_best.
		Recur on the sublists obtained by splitting on a_best, and add those nodes as children of node.
	 */

	public C45(String[] atts, List<Instance> tr){

		attributes = atts;

		//CREATE A HASHMAP OF ATTRIBUTE INDEXES/VALUES
		attribute =new HashMap<>();
		for(int i = 0; i < attributes.length; i++){

			attribute.put(attributes[i],i);
		}

		if(tr!=null){

			Node node = new Node(tr);
			runC45(node);
		}


		System.out.println(nodes.size());

	}

	public void runC45(Node node){

		val = 0.0;
		spl = null;

		if(checkBaseCases(node)){
			nodes.add(node);
		}
		else if(!checkBaseCases(node)) {
			int x = node.getData().get(0).getAttributes().length - 1;
			for (int i = 0; i < x; i++){
				//			System.out.println("Sorted by: "+attributes[i]);

				Instance.setSortAttribute(i);
				Collections.sort(node.getData());

				countInstances(node);
				thresholdSplit(node, i);
				calculateEntropys(node);
			}
		}

	}

	public boolean checkBaseCases(Node node){


		//All instances belong to the same class
		HashMap<String,Integer> count = countInstances(node);

		for (Instance inst : node.getData()){

			for (String key : count.keySet()) {
				if(count.get(key)==node.getData().size()){
					node.setName(key);
					node.setValue(null);
					node.setHasChildren(false);
					System.out.println("Node created with value "+key);
					return true;
				}
			}
		}

		//empty training set
		if(node.getData().isEmpty()){
			System.out.println("No data in array");
			node.setName("Failure To Classify");
			node.setValue(null);
			node.setHasChildren(false);
			return true;
		}


		//new class type encountered.
		for(Instance inst:node.getData()){

			if(count.get(inst.getType()) == null){

				node.setValue(null);
				node.setHasChildren(false);
				return true;
			}
		}

		if(node.getData().size()==1){

			node.setName(node.getData().get(0).getType());
			node.setValue(null);
			node.setHasChildren(false);
			return true;

		}

		return false;
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

	public void calculateEntropys(Node node){

		//get list of lists of split values
		//get list of lists of split types
		//get list of lists of counts before split for each target
		//Generate List of counts after split for each target
		//iterate for each numerical attribute

		Map<String, Integer> map = node.getTargetCount();
		System.out.println("Number of Training Instances: "+node.getData().size());
		int b4 = 0;
		int af = 0;


		//int y = node.getData().size();

		List<List<Double>> splitValues = node.getattSV();
		List<List<String>> attST = node.getattST(); //List that holds lists of instance type values for each split in each ordering of training instances
		List<List<Integer>> attSC = node.getattSC(); //
		int x = 0;
		for(List<Double> a: splitValues){

			System.out.println("\n\nCALCULATING ENTROPY OF SPLITS "+ attributes[x]);
			System.out.println(a.toString());


			List<String> targets = attST.get(x);
			List<Integer> targetCount = attSC.get(x);
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
				System.out.println("Total Count: "+map.toString());
				System.out.println("Before split at : " +a.get(i)+" "+beforeSplit.toString());
				System.out.println("After split at : " +a.get(i)+" "+afterSplit.toString());

				int r = 0;
				for (int f : beforeSplit.values()) {
					r += f;
				}
				int g = 0;
				for (int h : afterSplit.values()) {
					g += h;
				}

				double entb4 = 0;
				double m = 0;
				for (int j : beforeSplit.values()) {
					if(j!=0){

						double p = j;
						double t = r;
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

						entAf += (-q/w * (Math.log(q/w)/Math.log(2)));
						n+=j;
					}
				}
				b4 = (int)m;
				//System.out.println("Number of all instances before split :"+ r);
				//System.out.println("Number of all instances after split :"+ g);
				System.out.println("Before Split Entropy :"+ entb4);
				System.out.println("After Split Entropy :"+ entAf);
				if(entb4==0.0 || entAf==0.0){
					node.setName(attributes[x]);
					node.setValue(a.get(i));
					node.setHasChildren(true);
					nodes.add(node);
					splitList(node.getName(),node.getValue(),node, b4);

				}else{

					calculateInformationGains(node, attributes[x], entb4, entAf, m, n, a.get(i));
				}
			}
			x++;
		}
		node.setName(spl);
		node.setValue(val);
		node.setHasChildren(true);
		nodes.add(node);
		System.out.println("Node Name: "+ node.getName() + ". Node Value: "+node.getValue());
		splitList(node.getName(), node.getValue(), node, b4);

	}

	public void calculateInformationGains(Node node, String target, Double entBe, Double entAf, Double countb4, Double countaf, Double splitValue){

		Map<String, Integer> totalCount = node.getTargetCount();
		HashMap<String, Double> infoGains;
		if(node.getIGValues()!=null){
			infoGains = node.getIGValues();
		}else{
			infoGains = new HashMap<String,Double>();
		}
		int s = node.getData().size(); //size of the training data set

		double ig=0;
		//gets the entropy of the entire training data set
		for(int j: totalCount.values()){
			double tar = j;
			double size = s;

			ig += (-tar/size * (Math.log(tar/size)/Math.log(2)));
		}
		System.out.println("IG for total set: "+ig);

		double infoGain = ig - (entBe*(countb4/s))-(entAf*(countaf/s));


		if(infoGains.get(target)==null){
			infoGains.put(target, infoGain);
			System.out.println("Added IG of "+infoGain+" for "+target+" at value "+splitValue);
			spl = target;
			val = splitValue;
		}
		if(infoGains.get(target)<infoGain){
			infoGains.put(target, infoGain);
			System.out.println("Added IG of "+infoGain+" for "+target+" at value "+splitValue);
			spl = target;
			val = splitValue;
		}
		node.setIGValues(infoGains); //not needed
		System.out.println(node.getIGValues().toString()+"\n\n"); //not needed


	}

	public void splitList(String name, Double v, Node nd, int a){

		int x = attribute.get(name);
		Instance.setSortAttribute(x);
		Collections.sort(nd.getData());

		List<Instance> l1 = new ArrayList<Instance>();
		List<Instance> l2 = new ArrayList<Instance>();

		System.out.println(a);
		//System.out.println(b);
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
		System.out.println(l1.toString());
		System.out.println(l2.toString());

		Node n1 = new Node(l1);
		nd.addChild(n1);
		runC45(n1);

		Node n2 = new Node(l2);
		nd.addChild(n2);
		runC45(n2);
	}
}
