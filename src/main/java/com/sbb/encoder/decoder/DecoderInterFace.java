package com.sbb.encoder.decoder;

import java.io.RandomAccessFile;

import com.sbb.encoder.exception.EncoderRuntimeException;

public interface DecoderInterFace {
	public byte[] decode(String encodedString);

	public void decode(String encodedString, RandomAccessFile destFile) throws EncoderRuntimeException;

	public void decode(String encodedString, RandomAccessFile destFile, boolean isDestFileBeClosed) throws EncoderRuntimeException;

	public void decode(RandomAccessFile sourceFile, boolean isSourceFileBeClosed, RandomAccessFile destFile, boolean isDestFileBeClosed)
			throws EncoderRuntimeException;

	public void decode(RandomAccessFile sourceFile, RandomAccessFile destFile) throws EncoderRuntimeException;
}
