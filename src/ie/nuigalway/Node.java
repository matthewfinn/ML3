package ie.nuigalway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Node {

	private Double value;
	private String name;
	private HashMap<String, Node> children;
	private Node parent;
	private boolean hasChildren;
	private boolean isLeaf;
	private int[] chOff;
	private List<Instance> data; //instances used in this node
	HashMap<String,Integer> targetCount;

	List<List<Double>> attSV; //List that holds lists of 'split' values for each sorting order of the training instances
	List<List<String>> attST; //List that holds lists of instance type values for each split in each ordering of training instances
	List<List<Integer>> attSC; //List that holds lists of instance type counts before each split for each split in each ordering of training instances
	HashMap<String,Double> IGValues; //Iterate over list to find max value and associated key

	public Node(List<Instance> list){
		this.data = list;

		targetCount = new HashMap<String,Integer>();
		attSV = new ArrayList<List<Double>>();
		attST = new ArrayList<List<String>>();
		attSC = new ArrayList<List<Integer>>();
		IGValues = new HashMap<String,Double>();

	}
	public String getName() {
		return name;
	}

	public Double getValue() {
		return value;
	}

	public HashMap<String, Node> getChildren() {
		return children;
	}


	public Node getParent() {
		return parent;
	}


	public List<Instance> getData() {
		return data;
	}


	public HashMap<String, Integer> getTargetCount() {
		return targetCount;
	}


	public List<List<Double>> getattSV() {
		return attSV;
	}


	public List<List<String>> getattST() {
		return attST;
	}


	public List<List<Integer>> getattSC() {
		return attSC;
	}


	public HashMap<String, Double> getIGValues() {
		return IGValues;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setValue(Double value) {
		this.value = value;
	}


	public void setChildren(HashMap<String, Node> children) {
		this.children = children;
	}


	public void setParent(Node parent) {
		this.parent = parent;
	}


	public void setData(List<Instance> data) {
		this.data = data;
	}


	public void setTargetCount(HashMap<String, Integer> targetCount) {
		this.targetCount = targetCount;
	}


	public void setattSV(List<List<Double>> attSV) {
		this.attSV = attSV;
	}


	public void setattST(List<List<String>> attST) {
		this.attST = attST;
	}


	public void setattSC(List<List<Integer>> attSC) {
		this.attSC = attSC;
	}


	public void setIGValues(HashMap<String, Double> IGValues) {
		this.IGValues = IGValues;
	}

	public boolean hasChildren() {
		return hasChildren;
	}
	public boolean isLeaf() {
		return isLeaf;
	}
	public int[] getChOff() {
		return chOff;
	}
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	public void setChOff(int[] chOff) {
		this.chOff = chOff;
	}
}

