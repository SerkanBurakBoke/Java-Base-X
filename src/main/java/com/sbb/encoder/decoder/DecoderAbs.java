package com.sbb.encoder.decoder;

import java.io.RandomAccessFile;

import com.sbb.encoder.exception.EncoderRuntimeException;
import com.sbb.encoder.properties.BaseProperties;
import com.sbb.encoder.util.BaseConversionUtil;

public abstract class DecoderAbs implements DecoderInterFace {
	private BaseConversionUtil baseConversionUtil;
	private BaseProperties baseProperties;

	@Override
	public abstract byte[] decode(String encodedString);

	@Override
	public abstract void decode(String encodedString, RandomAccessFile destFile) throws EncoderRuntimeException;

	@Override
	public abstract void decode(String encodedString, RandomAccessFile destFile, boolean isDestFileBeClosed) throws EncoderRuntimeException;

	@Override
	public abstract void decode(RandomAccessFile sourceFile, boolean isSourceFileBeClosed, RandomAccessFile destFile, boolean isDestFileBeClosed)
			throws EncoderRuntimeException;

	@Override
	public abstract void decode(RandomAccessFile sourceFile, RandomAccessFile destFile) throws EncoderRuntimeException;

	protected BaseConversionUtil getBaseConversionUtil() {
		if (baseConversionUtil == null)
			baseConversionUtil = new BaseConversionUtil(getBaseProperties().getBaseCharMap());
		return baseConversionUtil;
	}

	protected void setBaseConversionUtil(BaseConversionUtil baseConversionUtil) {
		this.baseConversionUtil = baseConversionUtil;
	}

	public void setBaseProperties(BaseProperties baseProperties) {
		this.baseProperties = baseProperties;
	}

	public BaseProperties getBaseProperties() {
		return baseProperties;
	}

	public DecoderAbs() {
		setBaseProperties(new BaseProperties());
	}

	public DecoderAbs(BaseProperties baseProperties) {
		setBaseProperties(baseProperties);
	}

}
