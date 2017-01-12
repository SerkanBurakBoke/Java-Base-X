package com.sbb.encoder.encoder;

import java.io.RandomAccessFile;

import com.sbb.encoder.exception.EncoderRuntimeException;

public interface EncoderInterFace {
	public String encode(byte[] byteArray);

	public String encode(String text);

	public void encode(RandomAccessFile sourceFile, boolean isSourceFileBeClosed, RandomAccessFile destFile, boolean isDestFileBeClosed)
			throws EncoderRuntimeException;

	public void encode(RandomAccessFile sourceFile, RandomAccessFile destFile) throws EncoderRuntimeException;

	public String encode(RandomAccessFile sourceFile) throws EncoderRuntimeException;
	
	public String encode(RandomAccessFile sourceFile, boolean isSourceFileBeClosed) throws EncoderRuntimeException;
}
