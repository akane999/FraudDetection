package com.millenium.credibanco.evaq.model;

import java.util.HashMap;
import java.util.Map;

public enum TipoAlertamientoEnum {

	EMISOR("EMISOR"),
	ADQUIRENTE("ADQUIRENTE");

	public static Map<String, TipoAlertamientoEnum> elementosId = new HashMap<String, TipoAlertamientoEnum>();

	static{
		for( TipoAlertamientoEnum t : TipoAlertamientoEnum.values() ){
			elementosId.put( t.getCode() , t  );
		}
	}

	private String code;

	private TipoAlertamientoEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
