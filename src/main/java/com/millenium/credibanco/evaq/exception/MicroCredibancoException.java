package com.millenium.credibanco.evaq.exception;

import java.io.Serializable;

public class MicroCredibancoException extends Exception implements Serializable {
	
	public MicroCredibancoException() {
	}


	public MicroCredibancoException(String message, Throwable cause) {
		super(message, cause);
	}

	public MicroCredibancoException(String message) {
		super(message);
	}

	public MicroCredibancoException(Throwable cause) {
		super(cause);
	}
	
	

}
