package com.millenium.credibanco.evaq.util;

import java.util.List;

import com.millenium.credibanco.evaq.model.ParametrosEvaluadorQ;
import com.millenium.credibanco.evaq.model.Tx;

public class ServiceCredibancoUtil {
	
	public static void printParametrosEvaluador(ParametrosEvaluadorQ parametroEvaluador, String tipoAlertamiento ){
		
		String parametros=""
				+ "\n===================================="
				+ "\n     ParametrosEvaluador "+tipoAlertamiento
				+ "\n===================================="
				+ "\nNombre: " + parametroEvaluador.getId()+"-"+parametroEvaluador.getNombre()
				+ "\nTipoOrigen: " + parametroEvaluador.getTipoOrigen()
				+ "\nTipo: " + parametroEvaluador.getTipo()
				+ "\nRevisión: " + parametroEvaluador.getTipoRevision()
				+ "\nNacional: " + (parametroEvaluador.getNacional()?"SI":"NO")
				+ "\nPresencial: " + (parametroEvaluador.getPresencial()?"SI":"NO")
				+ "\nDescripción: " + parametroEvaluador.getDescripcion()
				+ "";
		System.out.println( parametros  );
		
	}

}
