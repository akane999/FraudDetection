package com.millenium.credibanco.weka.emisor;




import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.AbstractClassifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.experiment.InstanceQuery;


public class No_presente {

    public static void main(String[] args) throws ClassNotFoundException,
            SQLException, Exception {

        InstanceQuery query = new InstanceQuery();
        query.setUsername("postgres");
        query.setPassword("postgres");
        query.setQuery("select regla1::numeric,"
        		+ "	regla2::numeric, "
        		+ "	regla3::numeric, "
				+ " regla5::numeric, "
				+ " regla7::numeric, "
				+ " regla8::numeric, "
				+ "	regla10::numeric, "
				+ "	regla11::numeric, "
				+ " regla12::numeric, "
				+ "	regla13::numeric  "
				+ " from public.alertas_bancospequenos "
				+ " where tipo_transaccion='No presente' "
				+ " and tiene_historico='SI'");


        Instances data = query.retrieveInstances();

    }

    public boolean atLeastTwo(boolean a, boolean b, boolean c) {
        return a ? (b || c) : (b && c);
    }

    public Map<String, String> evaluar(Instances data) throws ClassNotFoundException, Exception{
    	ArrayList<String> classVal = new ArrayList<String>();
        classVal.add("NO");
        classVal.add("SI");

       
       
        data.insertAttributeAt(new Attribute("estado",classVal), 10);

       // System.out.println(data);


        Attribute testAttribute = data.attribute(10);
		data.setClass(testAttribute);

		String rutaC1	= System.getProperty( "modelo.emisor.nopresente.c1" , "./weka-modelo/emisor/No_Presente/DECISION_TABLE.model" );
		String rutaC2	= System.getProperty( "modelo.emisor.nopresente.c2" , "./weka-modelo/emisor/No_Presente/MULTILAYER_PERCEPTRON.model" );
		String rutaC3	= System.getProperty( "modelo.emisor.nopresente.c3" , "./weka-modelo/emisor/No_Presente/RANDOM_FOREST.model" );
		//String rutaC4	= System.getProperty( "modelo.emisor.nopresente.c4" , "./weka-modelo/emisor/No_Presente/NAIVE_BAYES.model" );
//		String rutaLogistic = System.getProperty( "modelo.emisor.nopresente.logistic" , "./weka-modelo/emisor/No_Presente/Logistic.model" );
//		String rutaFilteredClassifier = System.getProperty( "modelo.emisor.nopresente.classifier" , "./weka-modelo/emisor/No_Presente/Meta_FilteredClassifier.model" );
//		String rutaPerceptron = System.getProperty( "modelo.emisor.nopresente.perceptron" , "./weka-modelo/emisor/No_Presente/Multilayer_Perceptron.model" );

		System.out.println( "============================" );
		System.out.println( "  Rutas modelo No presente  " );
		System.out.println( "============================" );
		System.out.println( "clasificador 1=" + rutaC1 );
		System.out.println( "clasificador 2=" + rutaC2 );
		System.out.println( "clasificador 3=" + rutaC3 );
		//System.out.println( "clasificador 4=" + rutaC4 );

		AbstractClassifier classifier1 = (AbstractClassifier) SerializationHelper.read( rutaC1 );
		AbstractClassifier classifier2 = (AbstractClassifier) SerializationHelper.read( rutaC2 );
		AbstractClassifier classifier3 = (AbstractClassifier) SerializationHelper.read( rutaC3 );
		//AbstractClassifier classifier4 = (AbstractClassifier) SerializationHelper.read( rutaC4 );


		Map<String,String> result = new HashMap<String, String>();
		for(int i=0;i<data.size();i++){
			double valC1 = classifier1.classifyInstance(data.instance(i)); 
			double valC2 = classifier2.classifyInstance(data.instance(i));
			double valC3 = classifier3.classifyInstance(data.instance(i));
			
			
			//double valC4 = classifier4.classifyInstance(data.instance(i));
			System.out.println("prediccion: "+valC1+" "+valC2+" "+valC3);
			System.out.println("name "+data.classAttribute().name());
			String predictedC1=data.classAttribute().value((int) valC1);
			String predictedC2=data.classAttribute().value((int) valC2);
			String predictedC3=data.classAttribute().value((int) valC3);
			System.out.println("prediccion: "+predictedC1+" "+predictedC2+" "+predictedC3);
			//String predictedC4=data.classAttribute().value((int) valC4);
			System.out.println("Class " + classifier1.getClass().getSimpleName() + ": " + predictedC1 );
			System.out.println("Class " + classifier2.getClass().getSimpleName() + ": " + predictedC2 );
			System.out.println("Class " + classifier3.getClass().getSimpleName() + ": " + predictedC3 );
			//System.out.println("Class " + classifier4.getClass().getSimpleName() + ": " +  predictedC4 );


			String riesgoVotacion="";
			if(data.classAttribute().value((int) valC1).toString().equals("SI") && 
					(data.classAttribute().value((int) valC2).toString().equals("SI"))
					&& (data.classAttribute().value((int) valC3).toString().equals("SI")))
//					&& (data.classAttribute().value((int) valC4).toString().equals("SI")))	
			{
				riesgoVotacion="Alto";
			} else if  
			(atLeastTwo(data.classAttribute().value((int) valC1).toString().equals("SI") ,
					(data.classAttribute().value((int) valC2).toString().equals("SI"))
					, (data.classAttribute().value((int) valC3).toString().equals("SI"))))
					
					
			{ 
				riesgoVotacion="Medio";			
				}
			else if (data.classAttribute().value((int) valC1).toString().equals("SI") ^ data.classAttribute().value((int) valC2).toString().equals("SI") ^ data.classAttribute().value((int) valC3).toString().equals("SI")) { 
				
				riesgoVotacion="Bajo";			
				}
			else {
				riesgoVotacion="Bajo";	
			}
			
			System.out.println("RiesgoporVotación: "+ riesgoVotacion );
		    
			result.put("c1", classifier1.getClass().getSimpleName() + ": " + predictedC1	);
			result.put("c2", classifier2.getClass().getSimpleName() + ": " + predictedC2	);
			result.put("c3", classifier3.getClass().getSimpleName() + ": " + predictedC3	);
			//result.put("c4", classifier4.getClass().getSimpleName() + ": " + predictedC4	);
			result.put("RiesgoVotacion", riesgoVotacion );
		}
		return result;
    }

}



