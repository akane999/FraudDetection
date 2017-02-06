package com.millenium.credibanco.weka.adquiriente;



import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.fuzzy.FuzzyRoughNN;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.experiment.InstanceQuery;

 
public class Adq_no_presente {
    
    public Map<String, String> evaluar(Instances data) throws ClassNotFoundException, Exception{
    	FastVector prueba = new FastVector(2);
        prueba.addElement("SI");
        prueba.addElement("NO");
                  
        Attribute attribute1 = new Attribute("reporte_fraude", prueba); 
        Attribute attribute2 = new Attribute("text",(FastVector) null);
          
        data.insertAttributeAt(attribute1, 6);
        
       // System.out.println(data);
    
               
        Attribute testAttribute = data.attribute(6);
		data.setClass(testAttribute);
		
		String rutaC1 = 		System.getProperty( "modelo.adquiriente.no_presente.c1" , "./weka-modelo/adquiriente/No_Presente/FUZZY_ROUGH_NN.model" );
		String rutaC2 = 		System.getProperty( "modelo.adquiriente.no_presente.c2" , "./weka-modelo/adquiriente/No_Presente/IBK.model" );
		String rutaC3 =			System.getProperty( "modelo.adquiriente.no_presente.c3" , "./weka-modelo/adquiriente/No_Presente/JRIP.model" );
		String rutaC4 = 		System.getProperty( "modelo.adquiriente.no_presente.c4" , "./weka-modelo/adquiriente/No_Presente/RANDOM_FOREST.model" );
		
		System.out.println( "============================" );
		System.out.println( "  Rutas modelo Adquirente no presente " );
		System.out.println( "============================" );
		System.out.println( "clasificador 1=" + rutaC1 );
		System.out.println( "clasificador 2=" + rutaC2 );
		System.out.println( "clasificador 3=" + rutaC3 );
		System.out.println( "clasificador 4=" + rutaC4 );
       		
		AbstractClassifier classifier1 = (AbstractClassifier) SerializationHelper.read( rutaC1 );
		AbstractClassifier classifier2 = (AbstractClassifier) SerializationHelper.read( rutaC2 );
		AbstractClassifier classifier3 = (AbstractClassifier) SerializationHelper.read( rutaC3 );
		AbstractClassifier classifier4 = (AbstractClassifier) SerializationHelper.read( rutaC4 );
		
	
		Map<String,String> result = new HashMap<String, String>();
		for(int i=0;i<data.size();i++){
			double valC1 = classifier1.classifyInstance(data.instance(i)); 
			double valC2 = classifier2.classifyInstance(data.instance(i));
			double valC3 = classifier3.classifyInstance(data.instance(i));
			double valC4 = classifier4.classifyInstance(data.instance(i));
			
			String predictedC1=data.classAttribute().value((int) valC1);
			String predictedC2=data.classAttribute().value((int) valC2);
			String predictedC3=data.classAttribute().value((int) valC3);
			String predictedC4=data.classAttribute().value((int) valC4);
			System.out.println("Class " + classifier1.getClass().getSimpleName() + ": " + predictedC1 );
			System.out.println("Class " + classifier2.getClass().getSimpleName() + ": " + predictedC2 );
			System.out.println("Class " + classifier3.getClass().getSimpleName() + ": " + predictedC3 );
			System.out.println("Class " + classifier4.getClass().getSimpleName() + ": " +  predictedC4 );
			
			int cont = 0;
			
			if(data.classAttribute().value((int) valC1).toString().equals("SI")){
				cont++;
			}
			
			if(data.classAttribute().value((int) valC2).toString().equals("SI")){
				cont++;
			}
			
			if(data.classAttribute().value((int) valC3).toString().equals("SI")){
				cont++;
			}
			
			if(data.classAttribute().value((int) valC4).toString().equals("SI")){
				cont++;
			}
		
			String riesgoVotacion="";
		    if(cont == 4){
		    	riesgoVotacion="Alto";
		    } else if(cont == 3){
		    	riesgoVotacion="Medio";
		    } else if(cont == 2){
		    	riesgoVotacion="Medio";
		    } else if(cont == 1){
		    	riesgoVotacion="Bajo";
		    } else if(cont == 0){
		    	riesgoVotacion="Bajo";
		    }
			
		    System.out.println("RiesgoporVotación: "+ riesgoVotacion );
		    
		    result.put("c1", classifier1.getClass().getSimpleName() + ": " + predictedC1	);
			result.put("c2", classifier2.getClass().getSimpleName() + ": " + predictedC2	);
			result.put("c3", classifier3.getClass().getSimpleName() + ": " + predictedC3	);
			result.put("c4", classifier4.getClass().getSimpleName() + ": " + predictedC4	);
			result.put("RiesgoVotacion", riesgoVotacion );
		}
		
		return result;
    }

}

	
		
   