/**
 * Author - Matthew Finn 2016
 */

package ie.nuigalway;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

public class DataPreparationTool {

	private static final String DEFAULT_SEPARATOR = ",";
	List<String[]> trainingAttributes;
	List<String[]> testAttributes;
	BufferedReader br;
	String line;
	String file;
	double tra;
	String[] attributeNames;
	List<String[]> testData;
	List<String[]> trainingData;
	List<Instance> trainingInstances;
	List<Instance> testInstances;
	public void getData(String f, String training) throws Exception{
		testData = new ArrayList<>();
		trainingData = new ArrayList<>();
		file = f;
		br = new BufferedReader(new FileReader(file));
		attributeNames = br.readLine().split(DEFAULT_SEPARATOR);

		while ((line = br.readLine()) != null) { //splits on each line, csv is line separated
			//splits each attribute by comma separator and places in String array
			String[] objectHolder = line.split(DEFAULT_SEPARATOR);

			//adds the objectholder
			testData.add(objectHolder);
		}
		if (training!=null){
			tra = Double.parseDouble(training); //checks if input for training data % is null
			if(tra!=0){
				getTrainingData(tra);//calls a data split if entry is not = 0
			}
		}
	}
	public List<String[]> getTrainingData(double tr){

		double trainingSize = (double)testData.size()/100 * tr;
		for(int i = 0; i < trainingSize; i++){
			Collections.shuffle(testData);
			trainingData.add(testData.remove(0));
		}
		return trainingData;
	}

	public List<Instance> getTrainingInstances(){
		trainingInstances = new ArrayList<>();
		int x = trainingData.get(0).length;
		for(String[] inst : trainingData){
			Object instance[] = new Object[x];
			for(int i = 0; i<x;i++){
				//parses input to double if possible, else it's a string
				if(isNumber(inst[i])){
					instance[i] = Double.parseDouble(inst[i]);
				}else{
					instance[i] = inst[i];
				}
			}
			trainingInstances.add(new Instance(instance));
		}
		return trainingInstances;
	}

	public List<Instance> getTestInstances(){
		testInstances = new ArrayList<>();
		int x = testData.get(0).length;

		for (String[] inst: testData){
			Object instance[] = new Object[x];
			for(int i = 0; i<x;i++){
				//parses input to double if possible, else it's assumed to be a string
				if(isNumber(inst[i])){
					instance[i] = Double.parseDouble(inst[i]);
				}else{
					instance[i] = inst[i];
				}
			}
			testInstances.add(new Instance(instance));
		}
		return testInstances;
	}

	public List<String[]> getTrainingAttributes(List<String[]> tr){
		return trainingAttributes;
	}

	public List<String[]> getTestAttributes(){
		return testAttributes;
	}

	@SuppressWarnings("deprecation")
	public boolean isNumber(String s){
		return NumberUtils.isNumber(s);
	}

}


