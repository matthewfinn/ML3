package ie.nuigalway;

import java.util.Arrays;

public class Instance implements Comparable<Instance>{

	private Object[] attributes;
	private static int sortingAtt;
	private String type;

	public Instance(Object[] atts){
		attributes = atts;
		sortingAtt = 0;
		type = (String) attributes[attributes.length-1];
	}

	public Object[] getAttributes(){
		return attributes;
	}

	public void setAttributes(Object[] atts){
		this.attributes = atts;
	}

	public String getType(){

		return type;
	}
	public static void setSortAttribute(int x){

		Instance.sortingAtt = x;
	}

	public static int getSortAttribute(){

		return Instance.sortingAtt;
	}

	@Override
	public String toString(){
		return Arrays.toString(attributes);
	}

	@Override
	public int compareTo(Instance o) {

		double x = (double) this.getAttributes()[sortingAtt];
		double y = (double) o.getAttributes()[sortingAtt];

		if(x<y)
			return -1;
		else if(y<x)
			return 1;


		return 0;
	}
}
