package com.sbb.encoder.properties;

import org.apache.log4j.Logger;

import com.sbb.encoder.exception.EncoderValidationException;

public class BaseProperties {
	private int byteCount = 5;
	private int base = 36;
	private String baseCharMap = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private int fileReadByteBuffer = 1024;
	private int fileReadMapcharCountBuffer = 1024;
	Logger logger = Logger.getLogger(this.getClass());

	public int getFileReadByteBuffer() {
		return fileReadByteBuffer;
	}

	public void setFileReadByteBuffer(int fileReadByteBuffer) {
		this.fileReadByteBuffer = fileReadByteBuffer;
	}

	public int getFileReadMapcharCountBuffer() {
		return fileReadMapcharCountBuffer;
	}

	public void setFileReadMapcharCountBuffer(int fileReadMapcharCountBuffer) {
		this.fileReadMapcharCountBuffer = fileReadMapcharCountBuffer;
	}

	public int getByteCount() {
		return byteCount;
	}

	public void setByteCount(int byteCount) throws EncoderValidationException {
		if(byteCount<1 || byteCount>7)
			throw new EncoderValidationException("byteCount must be between 1 and 7");
		this.byteCount = byteCount;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public String getBaseCharMap() {
		return baseCharMap;
	}

	public void setBaseCharMap(String baseCharMap) {
		this.baseCharMap = baseCharMap;
	}

	public int getEncodedStrPartSize() {
		long l = 1l;
		l <<= 8 * getByteCount();
		int i = 0;
		long baseImpact = 1;
		while (baseImpact < l) {
			baseImpact *= getBase();
			i++;
		}
		return i;
	}
	
	public int getReadByteSize4Encode(){
		return getByteCount() * getFileReadByteBuffer();
	}
	
	public int getReadByteSize4Decode(){
		return getEncodedStrPartSize() * getFileReadByteBuffer() * Character.SIZE;
	}

}
