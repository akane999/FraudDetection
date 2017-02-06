package com.millenium.credibanco.evaq.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RespuestaEvaluador {
	
	private Long idAlerta; 
	private List<LlaveValor> resultadosQ  = new ArrayList<LlaveValor>();
	private String nivelRiesgo;
	private Map<String, String> respuestaModelo;
		
	
	public RespuestaEvaluador() {
	
	}



	public Long getIdAlerta() {
		return idAlerta;
	}
	public void setIdAlerta(Long idAlerta) {
		this.idAlerta = idAlerta;
	}



	public List<LlaveValor> getResultadosQ() {
		return resultadosQ;
	}
	public void setResultadosQ(List<LlaveValor> resultadosQ) {
		this.resultadosQ = resultadosQ;
	}



	public String getNivelRiesgo() {
		return nivelRiesgo;
	}
	public void setNivelRiesgo(String nivelRiesgo) {
		this.nivelRiesgo = nivelRiesgo;
	}



	public Map<String, String> getRespuestaModelo() {
		return respuestaModelo;
	}
	public void setRespuestaModelo(Map<String, String> respuestaModelo) {
		this.respuestaModelo = respuestaModelo;
	}
	
	
}
