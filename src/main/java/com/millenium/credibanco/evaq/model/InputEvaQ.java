package com.millenium.credibanco.evaq.model;

import java.util.List;

public class InputEvaQ {

	private List<ParametrosEvaluadorQ> listEvaluadorQ;
	private Tx tx;
	private HistoricoTx historicoTx;
	private List<Tx> listTx;

	public InputEvaQ() {

	}

	public List<ParametrosEvaluadorQ> getListEvaluadorQ() {
		return listEvaluadorQ;
	}

	public void setListEvaluadorQ(List<ParametrosEvaluadorQ> listEvaluadorQ) {
		this.listEvaluadorQ = listEvaluadorQ;
	}
	
	public void setListTx(List<Tx> listTx) {
		this.listTx = listTx;
	}
	
	public List<Tx> getListTx() {
		return listTx;
	}

	public Tx getTx() {
		return tx;
	}

	public void setTx(Tx tx) {
		this.tx = tx;
	}

	public HistoricoTx getHistoricoTx() {
		return historicoTx;
	}
	

	public void setHistoricoTx(HistoricoTx historicoTx) {
		this.historicoTx = historicoTx;
	}

}
