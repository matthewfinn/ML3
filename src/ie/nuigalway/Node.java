/**
 * Author - Matthew Finn 2016
 */

package ie.nuigalway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Node {
	private Double value;
	private String name;
	private Node[] children;
	private boolean hasChildren;
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
		children = new Node[2];
	}
	public String getName() {
		return name;
	}
	public Double getValue() {
		return value;
	}
	public Node[] getChildren() {
		return children;
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
	public void addChild(Node n, int i) {
		this.children[i] = n;
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
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	@Override
	public String toString(){
		if(this.hasChildren){
			String x = null;
			for(int i = 0; i< this.getChildren().length; i++){
				if(this.getChildren()[i]!=null){
					x+= this.getChildren()[i].toString()+"  |";
				}
			}
			return "Name: "+this.name +"| Value:"+ this.value + " - Children Nodes["+ x+"] ";
		}
		if(this.value==null){
			return this.name;
		}
		return this.name + this.value;
	}

}

