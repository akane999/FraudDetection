package com.millenium.credibanco.weka.emisor;

import java.io.File;

import weka.classifiers.fuzzy.FuzzyRoughNN;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;

/**
 * The Predict class uses the trained Classifier and the test data to create a
 * prediction CSV file. As noted inthe README.md, we modified the original
 * test.csv file to contain the 'survived' column. We do not actually use the
 * values of this column, weka simply requires the train and test data to match.
 * 
 * @author jbirchfield
 * 
 */
public class Test {

	public static void main(String[] args) throws Exception {

		/*
		 * First we load the test data from our ARFF file
		 */
		ArffLoader testLoader = new ArffLoader();
		testLoader.setSource(new File("D:/testInter2.arff"));
		//testLoader.setRetrieval(Loader.BATCH);
		Instances testDataSet = testLoader.getDataSet();

		/*
		 * Now we tell the data set which attribute we want to classify, in our
		 * case, we want to classify the first column: survived
		 */
		Attribute testAttribute = testDataSet.attribute(9);
		testDataSet.setClass(testAttribute);
		//testDataSet.deleteStringAttributes();

		/*
		 * Now we read in the serialized model from disk
		 */
		FuzzyRoughNN classifier = (FuzzyRoughNN) SerializationHelper
				.read("D:/Millenium/Campannas/Credibanco/2016/Modelos/Modelos/Presente_Internacional/FuzzyRoughNN.model");
		
		 
	
		
		for(int i=0;i<testDataSet.size();i++){
			double FuzzyRoughNN = classifier.classifyInstance(testDataSet.instance(i)); 
		
			System.out.println("Class predicted fuzzy: " + testDataSet.classAttribute().value((int) FuzzyRoughNN));
		
		
		
		

	}
	}
}
