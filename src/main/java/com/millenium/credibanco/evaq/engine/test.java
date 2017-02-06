package com.millenium.credibanco.evaq.engine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.millenium.credibanco.evaq.model.LlaveValor;
import com.millenium.credibanco.weka.emisor.No_presente;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class test {

	public static void main(String... arg){

		List<LlaveValor> listRespuestaEvaluador = new ArrayList<LlaveValor>();
		listRespuestaEvaluador.add( new LlaveValor("R1", "100", "" ) );
		listRespuestaEvaluador.add( new LlaveValor("R2", "20", "" ) );
		listRespuestaEvaluador.add( new LlaveValor("R3", "30", "" ) );
		listRespuestaEvaluador.add( new LlaveValor("R4", "40", "" ) );
		listRespuestaEvaluador.add( new LlaveValor("R5", "50", "" ) );
		listRespuestaEvaluador.add( new LlaveValor("R6", "60", "" ) );
		listRespuestaEvaluador.add( new LlaveValor("R7", "70", "" ) );
		listRespuestaEvaluador.add( new LlaveValor("R8", "80", "" ) );
		listRespuestaEvaluador.add( new LlaveValor("R9", "80", "" ) );
		listRespuestaEvaluador.add( new LlaveValor("R10", "10", "" ) );

		double[] valores = listRespuestaEvaluador.stream().mapToDouble( d -> Double.valueOf( d.getValor() ) ).toArray();

		Instances data = new Instances(
				"resultRs"
				, new ArrayList<Attribute>( Arrays.asList(
						new Attribute("regla1")
						,new Attribute("regla2")
						,new Attribute("regla3")
						,new Attribute("regla4")
						,new Attribute("regla5")
						,new Attribute("regla6")
						,new Attribute("regla7")
						,new Attribute("regla8")
						,new Attribute("regla9")
						,new Attribute("regla10")
						 ) )
				, 0 );
		data.add( new DenseInstance(1.0,  valores ) );

		try {
//			new Presente_Nacional().evaluar(data); // 8 reglas
//			new PresenteIntNal().evaluar(data); // 9 reglas
			new No_presente().evaluar(data); // 10 reglas
//			new Adq_no_presente().evaluar(data); // 6 reglas
//			new Adq_presente().evaluar(data); // 6 reglas
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}