package com.sbb.encoder.exception;

import org.apache.log4j.Logger;

public class EncoderRuntimeException extends Exception {

	private static final long serialVersionUID = -8399637201827765991L;
	
	Logger logger = Logger.getLogger(this.getClass());

	public EncoderRuntimeException() {
		super();
	}

	public EncoderRuntimeException(Exception e) {
		super(e);
	}

	public EncoderRuntimeException(String message) {
		super(message);
		logger.info("!!! ERROR !!! " + message);
	}


}
