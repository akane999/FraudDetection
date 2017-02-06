package com.millenium.credibanco.evaq.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Tx {
	private Long id;
	private String codigocondicionpos;
	private LocalDateTime fechaTx;
	private LocalDateTime fechaAfiliacion;
	private String mcc;
	private String modoentrada;
	private double montotx;
	private String nombrecomercio;
	private String ubicacionTrx;
	private String presente;
	private String nacional;
	private String terminal;
	private String alerto;
	private String estadoTrx;

	private String fiid;
	private String reponseCode;
	private TipoAlertamientoEnum tipoAlertamiento;

	private String idComercio;
	private String nombreAplicacion;
	private String codigoAutorizacion;
	private String portafolio;

	// private String pais;
	// private String idTarjeta;
	// private String fraude;
	// private String idBanco;
	// private String codigoAutorizacion;

	// id, codigocondicionpos, fechatx, mcc, modoentrada, montotx,
	// nombrecomercio, ubicacionTrx, pais, idTarjeta, idComercio
	public Tx(Long id, String codigocondicionpos, LocalDateTime fechatx, String mcc, String modoentrada,  
			double montotx, String nombreComercio, String ubicacionTrx, String pais, String idTarjeta, 
			String idComercio,String portafolio,String nombreaplicacion,String terminal,String presente,
			String nacional,String alerto,String estadotrx) {
		super();
		this.id = id;
		this.codigocondicionpos = codigocondicionpos;
		this.fechaTx = fechatx;

		this.mcc = mcc;
		this.modoentrada = modoentrada;
		this.montotx = montotx;
		this.nombrecomercio = nombreComercio;
		this.ubicacionTrx = ubicacionTrx;
		this.portafolio=portafolio;
		this.nombreAplicacion=nombreaplicacion;
		this.terminal=terminal;
//		this.pais = pais;
	//	this.idTarjeta=idTarjeta;
		this.idComercio=idComercio;
		this.presente=presente;
		this.nacional=nacional;
		this.alerto=alerto;
		this.estadoTrx=estadotrx;
	}



	

	public LocalDateTime getDaytx() {
		return fechaTx.truncatedTo(ChronoUnit.DAYS);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigocondicionpos() {
		return codigocondicionpos;
	}

	public void setCodigocondicionpos(String codigocondicionpos) {
		this.codigocondicionpos = codigocondicionpos;
	}

	public LocalDateTime getFechaTx() {
		return fechaTx;
	}

	public void setFechaTx(LocalDateTime fechatx) {
		this.fechaTx = fechatx;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getModoentrada() {
		return modoentrada;
	}

	public void setModoentrada(String modoentrada) {
		this.modoentrada = modoentrada;
	}

	public double getMontotx() {
		return montotx;
	}

	public void setMontotx(double montotx) {
		this.montotx = montotx;
	}

	public String getNombrecomercio() {
		return nombrecomercio;
	}

	public void setNombrecomercio(String nombrecomercio) {
		this.nombrecomercio = nombrecomercio;
	}

	public String getUbicacionTrx() {
		return ubicacionTrx;
	}

	public void setUbicacionTrx(String ubicacionTrx) {
		this.ubicacionTrx = ubicacionTrx;
	}

	// public String getPais() {
	// return pais;
	// }
	//
	//
	// public void setPais(String pais) {
	// this.pais = pais;
	// }
	//
	//
	// public String getIdTarjeta() {
	// return idTarjeta;
	// }
	//
	//
	// public void setIdTarjeta(String idTarjeta) {
	// this.idTarjeta = idTarjeta;
	// }

	public String getIdComercio() {
		return idComercio;
	}

	public void setIdComercio(String idComercio) {
		this.idComercio = idComercio;
	}

	public String getPresente() {
		return presente;
	}

	public void setPresente(String presente) {
		this.presente = presente;
	}

	public String getNacional() {
		return nacional;
	}

	public void setNacional(String nacional) {
		this.nacional = nacional;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	// public String getFraude() {
	// if(fraude==null)fraude="";
	// return fraude;
	// }
	//
	//
	// public void setFraude(String fraude) {
	// this.fraude = fraude;
	// }
	//
	//
	// public String getIdBanco() {
	// return idBanco;
	// }
	// public void setIdBanco(String idBanco) {
	// this.idBanco = idBanco;
	// }

	public String getAlerto() {
		return alerto;
	}

	public void setAlerto(String alerto) {
		this.alerto = alerto;
	}

	public TipoAlertamientoEnum getTipoAlertamiento() {
		return tipoAlertamiento;
	}

	public void setTipoAlertamiento(TipoAlertamientoEnum tipoAlertamiento) {
		this.tipoAlertamiento = tipoAlertamiento;
	}

	public LocalDateTime getFechaAfiliacion() {
		return fechaAfiliacion;
	}

	public void setFechaAfiliacion(LocalDateTime fechaAfiliacion) {
		this.fechaAfiliacion = fechaAfiliacion;
	}

	public String getFiid() {
		return fiid;
	}

	public void setFiid(String fiid) {
		this.fiid = fiid;
	}

	public String getReponseCode() {
		if (reponseCode == null)
			reponseCode = "";
		return reponseCode;
	}

	// "000" "001" "002"....
	public void setReponseCode(String reponseCode) {
		this.reponseCode = reponseCode;
	}

	public String getEstadoTrx() {
		return estadoTrx;
	}

	public void setEstadoTrx(String estadoTrx) {
		this.estadoTrx = estadoTrx;
	}

	public String getNombreAplicacion() {
		return nombreAplicacion;
	}

	public void setNombreAplicacion(String nombreAplicacion) {
		this.nombreAplicacion = nombreAplicacion;
	}

	public String getCodigoAutorizacion() {
		if (codigoAutorizacion == null)
			codigoAutorizacion = "";
		return codigoAutorizacion;
	}

	// "0000000" "00123456" ....
	public void setCodigoAutorizacion(String codigoAutorizacion) {
		this.codigoAutorizacion = codigoAutorizacion;
	}

	public String getPortafolio() {
		return portafolio;
	}

	public void setPortafolio(String portafolio) {
		this.portafolio = portafolio;
	}

	@Override
	public String toString() {
		return "Tx [id=" + id + ", codigocondicionpos=" + codigocondicionpos + ", fechaTx=" + fechaTx + ", fechaAfiliacion=" + fechaAfiliacion + ", mcc=" + mcc + ", modoentrada=" + modoentrada
				+ ", montotx=" + montotx + ", nombrecomercio=" + nombrecomercio + ", ubicacionTrx=" + ubicacionTrx + ", presente=" + presente + ", nacional=" + nacional + ", terminal=" + terminal
				+ ", alerto=" + alerto + ", estadoTrx=" + estadoTrx + ", fiid=" + fiid + ", reponseCode=" + reponseCode + ", tipoAlertamiento=" + tipoAlertamiento + ", idComercio=" + idComercio
				+ ", nombreAplicacion=" + nombreAplicacion + ", codigoAutorizacion=" + codigoAutorizacion + ", portafolio=" + portafolio + "]";
	}

}
