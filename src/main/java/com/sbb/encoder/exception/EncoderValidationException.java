package com.sbb.encoder.exception;

import org.apache.log4j.Logger;

public class EncoderValidationException extends Exception {
	private static final long serialVersionUID = 7950803260684219391L;
	Logger logger = Logger.getLogger(this.getClass());

	public EncoderValidationException() {
		super();
	}

	public EncoderValidationException(Exception e) {
		super(e);
	}

	public EncoderValidationException(String message) {
		super(message);
		logger.info("!!! ERROR !!! " + message);
	}

}
