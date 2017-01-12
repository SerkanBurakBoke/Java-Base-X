package com.sbb.encoder.encoder;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.sbb.encoder.exception.EncoderRuntimeException;
import com.sbb.encoder.properties.BaseProperties;
import com.sbb.encoder.util.BaseConversionUtil;
import com.sbb.encoder.util.FileUtil;

public class BaseEncoder extends EncoderAbs {
	private FileUtil fileUtil = null;

	public BaseEncoder() {
		setBaseProperties(new BaseProperties());
	}

	@Override
	public String encode(String text) {
		return encode(text.getBytes());
	}

	@Override
	public String encode(byte[] byteArray) {
		return encode(byteArray, true);
	}

	private String encode(byte[] byteArray, boolean isEnd) {
		int modBytes = byteArray.length % getBaseProperties().getByteCount();
		int whileSize = byteArray.length - modBytes;
		StringBuilder builder = new StringBuilder().append(generateMainEncodeString(byteArray, whileSize));
		if (whileSize != byteArray.length) {
			builder.append(generateLastEncodedString(byteArray, whileSize));
		}
		if (isEnd){
			if(modBytes == 0)
				builder.append(getBaseConversionUtil().convert2Base(0, getBaseProperties().getBase()));
			else
				builder.append(getBaseConversionUtil().convert2Base(Long.valueOf(getBaseProperties().getByteCount() - modBytes), getBaseProperties().getBase()));
		}
			

		return builder.toString();
	}

	@Override
	public void setBaseProperties(BaseProperties baseProperties) {
		super.setBaseProperties(baseProperties);
	}

	private String generateLastEncodedString(byte[] byteArray, int whileSize) {
		StringBuilder baseSb = new StringBuilder();
		byte byt;
		int i;
		Long result;
		Long impact;
		result = 0L;
		impact = 1L;
		for (i = whileSize; i < whileSize + getBaseProperties().getByteCount(); i++) {
			if (i >= byteArray.length)
				byt = 0x00;
			else
				byt = byteArray[i];
			result += BaseConversionUtil.convert2decimal(byt, impact);
			impact <<= 8;
		}
		String part = getBaseConversionUtil().convert2Base(result, getBaseProperties().getBase());
		baseSb.append(part);
		return baseSb.toString();
	}

	private String generateMainEncodeString(byte[] byteArray, int whileSize) {
		StringBuilder baseSb = new StringBuilder();
		int encodedStrPartSize = getBaseProperties().getEncodedStrPartSize();
		byte byt = 0;
		int i = 0;
		Long result = 0L;
		Long impact = 1L;
		while (i < whileSize) {
			byt = byteArray[i];
			result += BaseConversionUtil.convert2decimal(byt, impact);
			impact <<= 8;
			i++;
			if (i % getBaseProperties().getByteCount() == 0) {
				String part = getBaseConversionUtil().convert2Base(result, getBaseProperties().getBase());
				while (part.length() < encodedStrPartSize)
					part += getBaseConversionUtil().getCharArrayMap()[0];
				baseSb.append(part);
				result = 0L;
				impact = 1L;
			}
		}
		return baseSb.toString();
	}

	@SuppressWarnings("unused")
	private String generateMainEncodedString(byte[] byteArray) {

		int whileSize = byteArray.length + 5 - byteArray.length % 5;

		StringBuilder baseSb = new StringBuilder();

		byte byt = 0;
		int i = 0;
		Long result = 0L;
		Long impact = 1L;
		while (i < whileSize) {
			if (i < byteArray.length)
				byt = byteArray[i];
			else
				byt = 0x00;
			result += BaseConversionUtil.convert2decimal(byt, impact);
			impact <<= 8;
			i++;
			if (i % getBaseProperties().getByteCount() == 0) {
				baseSb.append(getBaseConversionUtil().convert2Base(result, getBaseProperties().getBase()));
				baseSb.append(getBaseConversionUtil().getCharArrayMap()[0]);
				result = 0L;
				impact = 1L;
			}
		}
		return baseSb.toString();
	}

	public String encode(RandomAccessFile sourceFile) throws EncoderRuntimeException {
		return encode(sourceFile, true);
	}

	public String encode(RandomAccessFile sourceFile, boolean isSourceFileBeClosed) throws EncoderRuntimeException {
		FileChannel inChannel = getFileUtil().getChannel(sourceFile);
		ByteBuffer buffer = generateReadByteBuffer();
		byte[] bytes = generateReadByteArray();
		StringBuilder sb = new StringBuilder();
		try {
			while (inChannel.read(buffer) > 0)
				sb.append(makeByteBufferEncodeProcess(buffer, bytes, true));
			getFileUtil().close(inChannel);
			if (isSourceFileBeClosed)
				getFileUtil().close(sourceFile);
		} catch (IOException e) {
			throw new EncoderRuntimeException("Error when making progress with files");
		}

		return sb.toString();
	}

	public void encode(RandomAccessFile sourceFile, boolean isSourceFileBeClosed, RandomAccessFile destFile, boolean isDestFileBeClosed)
			throws EncoderRuntimeException {
		FileChannel inChannel = getFileUtil().getChannel(sourceFile);
		ByteBuffer buffer = generateReadByteBuffer();
		byte[] bytes = generateReadByteArray();
		FileChannel outChannel = getFileUtil().getChannel(destFile);
		try {
			while (inChannel.read(buffer) > 0) {
				String str = makeByteBufferEncodeProcess(buffer, bytes, inChannel.position() == inChannel.size());
				getFileUtil().writeToFile(str, outChannel);
			}
			getFileUtil().close(inChannel);
			getFileUtil().close(outChannel);
			if (isSourceFileBeClosed)
				getFileUtil().close(sourceFile);
			if (isDestFileBeClosed)
				getFileUtil().close(destFile);
		} catch (IOException e) {
			throw new EncoderRuntimeException("Error when making progress with files");
		}

	}

	public void encode(RandomAccessFile sourceFile, RandomAccessFile destFile) throws EncoderRuntimeException {
		encode(sourceFile, true, destFile, true);
	}

	private byte[] generateReadByteArray() {
		return new byte[getBaseProperties().getReadByteSize4Encode()];
	}

	private ByteBuffer generateReadByteBuffer() {
		return ByteBuffer.allocate(getBaseProperties().getReadByteSize4Encode());
	}

	private String makeByteBufferEncodeProcess(ByteBuffer buffer, byte[] bytes, boolean isEnd) {
		buffer.flip();
		if (buffer.limit() < bytes.length) {
			bytes = new byte[buffer.limit()];
			for (int i = 0; i < buffer.limit(); i++)
				bytes[i] = buffer.get();
		} else
			buffer.get(bytes);
		buffer.clear();
		return encode(bytes, isEnd);
	}

	public FileUtil getFileUtil() {
		if (fileUtil == null)
			fileUtil = new FileUtil();
		return fileUtil;
	}

}
