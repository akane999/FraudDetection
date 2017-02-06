package com.millenium.credibanco.weka.emisor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.Logistic;
import weka.classifiers.fuzzy.FuzzyNN;
import weka.classifiers.trees.RandomTree;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.experiment.InstanceQuery;


public class Presente_Nacional {
	
	public static void main(String[] args) throws ClassNotFoundException, Exception {
		InstanceQuery query = new InstanceQuery();
		  query.setUsername("postgres");
		  query.setPassword("postgres");
		  query.setQuery("select regla1::numeric, "
		  			+ "	regla2::numeric, "
		  			+ " regla3::numeric, "
					+ " regla5::numeric, "
					+ " regla8::numeric, "
					+ "	regla9::numeric, "
					+ "	regla10::numeric, "
					+ " regla11::numeric "
					+ " from public.alertas_bancospequenos "
					+ " where tipo_transaccion='Presente_nacional' "
					+ " and tiene_historico='SI'");
		  

		  Instances data = query.retrieveInstances();

	}
	
	public Map<String, String> evaluar(Instances data) throws ClassNotFoundException, Exception{
		ArrayList<String> classVal = new ArrayList<String>();
        classVal.add("NO");
        classVal.add("SI");

       
       
        data.insertAttributeAt(new Attribute("estado",classVal), 8);
		  
		//System.out.println(data);

		         
		Attribute testAttribute = data.attribute(8);
		data.setClass(testAttribute);
				
		String c1 = 	System.getProperty( "modelo.emisor.presente.c1" , "./weka-modelo/emisor/Presente_Nacional/IBK.model" );
		String c2 = 			System.getProperty( "modelo.emisor.presente.c2" , "./weka-modelo/emisor/Presente_Nacional/J48.model" );
		String c3 = 			System.getProperty( "modelo.emisor.presente.c3" , "./weka-modelo/emisor/Presente_Nacional/LOGISTIC.model" );
		String c4 = 		System.getProperty( "modelo.emisor.presente.c4" , "./weka-modelo/emisor/Presente_Nacional/NAIVE_BAYES.model" );
		
		System.out.println( "============================" );
		System.out.println( "  Rutas modelo presente  " );
		System.out.println( "============================" );
		System.out.println( "clasificador 1=" + c1 );
		System.out.println( "clasificador 2=" + c2 );
		System.out.println( "clasificador 3=" + c3 );
		System.out.println( "clasificador 4=" + c4 );
		 		
		AbstractClassifier classifier1 = (AbstractClassifier) SerializationHelper.read( c1 );
		AbstractClassifier classifier2 = (AbstractClassifier) SerializationHelper.read( c2 );
		AbstractClassifier classifier3 = (AbstractClassifier) SerializationHelper.read( c3 );
		AbstractClassifier classifier4 = (AbstractClassifier) SerializationHelper.read( c4 );
			

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


