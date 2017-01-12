package com.sbb.encoder.encoder;

import java.io.RandomAccessFile;

import com.sbb.encoder.exception.EncoderRuntimeException;
import com.sbb.encoder.properties.BaseProperties;
import com.sbb.encoder.util.BaseConversionUtil;

public abstract class EncoderAbs implements EncoderInterFace {
	private BaseConversionUtil baseConversionUtil;
	private BaseProperties baseProperties;

	@Override
	public abstract String encode(byte[] byteArray);

	@Override
	public abstract String encode(String text);

	@Override
	public abstract void encode(RandomAccessFile sourceFile, boolean isSourceFileBeClosed, RandomAccessFile destFile, boolean isDestFileBeClosed)
			throws EncoderRuntimeException;

	@Override
	public abstract void encode(RandomAccessFile sourceFile, RandomAccessFile destFile) throws EncoderRuntimeException;

	@Override
	public abstract String encode(RandomAccessFile sourceFile, boolean isSourceFileBeClosed) throws EncoderRuntimeException;

	@Override
	public abstract String encode(RandomAccessFile sourceFile) throws EncoderRuntimeException;

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

	public EncoderAbs() {
		setBaseProperties(new BaseProperties());
	}

	public EncoderAbs(BaseProperties baseProperties) {
		setBaseProperties(baseProperties);
	}

}
