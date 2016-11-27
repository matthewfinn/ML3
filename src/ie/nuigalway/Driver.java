package ie.nuigalway;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class Driver extends JFrame implements ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 3607480549773219415L;
	//All Components needed to build GUI
	public static File file;
	JLabel dataset;
	JTextArea filename;
	JLabel trainingData;
	JTextArea splitPercentage;
	JButton chooseFile;
	JButton run;
	JLabel output;
	JTextArea results;
	Container c;
	GroupLayout lay;
	JFrame fr;
	JPanel jpan;
	Border border;
	String percent;

	List<Instance> trainingInstances;
	List<Instance> testInstances;
	public static void main(String[] args){

		Driver ml3 = new Driver("ML3");
		ml3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public Driver(String app){

		fr = new JFrame();
		fr.setTitle("C4.5 Classifier");
		jpan = new JPanel();
		lay = new GroupLayout(jpan);
		jpan.setLayout(lay);
		jpan.setBackground(new Color(152,152,152));

		border = BorderFactory.createLineBorder(Color.BLACK);
		dataset = new JLabel("Dataset: ");
		filename = new JTextArea(2,30);
		filename.setEditable(false);
		filename.setBorder(border);
		chooseFile = new JButton("Choose File");
		chooseFile.addActionListener(this);
		trainingData = new JLabel("Training Data %: ");
		splitPercentage = new JTextArea();
		splitPercentage.setEditable(true);
		//splitPercentage.setText("0");
		splitPercentage.setBorder(border);
		run = new JButton("Run");
		run.addActionListener(this);
		output = new JLabel("Results:");
		results = new JTextArea(10,30);
		results.setEditable(false);
		results.setBorder(border);

		lay.setAutoCreateGaps(true);
		lay.setAutoCreateContainerGaps(true);

		lay.setHorizontalGroup(lay.createSequentialGroup()
				.addGroup(lay.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(dataset).addComponent(trainingData).addComponent(output)
						)
				.addGroup(lay.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(filename).addComponent(splitPercentage).addComponent(results)
						)
				.addGroup(lay.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(chooseFile).addComponent(run)
						)
				);

		lay.setVerticalGroup(lay.createSequentialGroup()
				.addGroup(lay.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(dataset).addComponent(filename).addComponent(chooseFile))
				.addGroup(lay.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(trainingData).addComponent(splitPercentage).addComponent(run))
				.addGroup(lay.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(output).addComponent(results)));

		fr.add(jpan);
		fr.pack();
		fr.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == chooseFile){
			final JFileChooser fc = new JFileChooser();
			//In response to a button click:
			Component aComponent = null;
			int returnVal = fc.showOpenDialog(aComponent);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				//This is where a real application would open the file.
				filename.setText(file.getName());
			}
		}

		if(e.getSource() == run){

			if (file == null){

				filename.append("ERROR! No dataset chosen. \nPlease choose dataset before running algorithm.");
			}
			else{

				DataPreparationTool dpr = new DataPreparationTool();

				try {
					if(!splitPercentage.getText().equals("")){
						percent = splitPercentage.getText();
					}
					else{
						percent = null;
					}
					dpr.getData(file.toString(), percent);

					C45 alg = new C45(dpr.attributeNames,dpr.getTrainingInstances());
					//	alg.runC45();

				} catch (Exception e1) {

					e1.printStackTrace();
				}

				//trainingInstances = dpr.getTrainingInstances();
				//testInstances = dpr.getTestInstances();

				//Run algorithm 10 times. Print results each time
				//results.append("Hello World \n");
				//results.append("Hello World \n");

				//compute average accuracy

			}

		}
	}
}



