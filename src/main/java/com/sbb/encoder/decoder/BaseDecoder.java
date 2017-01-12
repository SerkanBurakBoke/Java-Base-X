package com.sbb.encoder.decoder;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.sbb.encoder.exception.EncoderRuntimeException;
import com.sbb.encoder.util.FileUtil;

public class BaseDecoder extends DecoderAbs {
	private FileUtil fileUtil = null;

	@Override
	public byte[] decode(String encodedString) {
		return decode(encodedString, true);
	}

	private byte[] decode(String encodedString, boolean isEnd) {

		int extraBytesCount = 0;
		if (isEnd) {
			extraBytesCount = getBaseConversionUtil().convert2decimal(Character.toString(encodedString.charAt(encodedString.length() - 1)),
					getBaseProperties().getBase()).intValue();
			encodedString = encodedString.substring(0, encodedString.length() - 1);
		}
		int smallStrSize = getEncodedStrPartSize(), smallSize;
		int mod = encodedString.length() % smallStrSize;
		int i = mod == 0 ? 0 : 1;
		byte[] encodeBytes = null;
		byte[] fullArray = new byte[(encodedString.length() / smallStrSize + i) * getBaseProperties().getByteCount()];
		Long decodeDecimal;
		String smallStr = null;
		int j = 0;
		for (i = 0; i < encodedString.length(); i += smallStrSize) {
			if (i + smallStrSize <= encodedString.length())
				smallSize = smallStrSize;
			else
				smallSize = encodedString.length() - i;
			smallStr = encodedString.substring(i, i + smallSize);
			decodeDecimal = getBaseConversionUtil().convert2decimal(smallStr, getBaseProperties().getBase());
			encodeBytes = getBaseConversionUtil().convert2Byte(decodeDecimal, getBaseProperties().getByteCount());
			System.arraycopy(encodeBytes, 0, fullArray, j, encodeBytes.length);
			j += getBaseProperties().getByteCount();
		}
		fullArray = cleanByteArray(fullArray, extraBytesCount);
		return fullArray;
	}

	private byte[] cleanByteArray(byte[] array, int extraBytesCount) {
		if (extraBytesCount == 0)
			return array;
		int limit = array.length - extraBytesCount;
		byte[] cleanArray = new byte[limit];
		System.arraycopy(array, 0, cleanArray, 0, limit);
		return cleanArray;
	}

	public int getEncodedStrPartSize() {
		return getBaseProperties().getEncodedStrPartSize();
	}

	public void decode(String encodedString, RandomAccessFile destFile) throws EncoderRuntimeException {
		decode(encodedString, destFile, true);
	}

	public void decode(String encodedString, RandomAccessFile destFile, boolean isDestFileBeClosed) throws EncoderRuntimeException {
		byte bytes[] = decode(encodedString);
		FileChannel outChannel = getFileUtil().getChannel(destFile);
		getFileUtil().writeToFile(bytes, outChannel);
		try {
			getFileUtil().close(outChannel);
			if (isDestFileBeClosed)
				getFileUtil().close(destFile);
		} catch (IOException e) {
			throw new EncoderRuntimeException("Error when making progress with files");
		}

	}

	public void decode(RandomAccessFile sourceFile, boolean isSourceFileBeClosed, RandomAccessFile destFile, boolean isDestFileBeClosed)
			throws EncoderRuntimeException {
		FileChannel inChannel = getFileUtil().getChannel(sourceFile);
		ByteBuffer buffer = generateReadByteBuffer();
		byte[] bytes = generateReadByteArray();
		FileChannel outChannel = getFileUtil().getChannel(destFile);
		try {
			while (inChannel.read(buffer) > 0)
				getFileUtil().writeToFile(makeByteBufferDecodeProcess(buffer, bytes, inChannel.position() == inChannel.size()), outChannel);
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

	public void decode(RandomAccessFile sourceFile, RandomAccessFile destFile) throws EncoderRuntimeException {
		decode(sourceFile, true, destFile, true);
	}

	private byte[] generateReadByteArray() {
		return new byte[getBaseProperties().getReadByteSize4Decode()];
	}

	private ByteBuffer generateReadByteBuffer() {
		return ByteBuffer.allocate(getBaseProperties().getReadByteSize4Decode());
	}

	private byte[] makeByteBufferDecodeProcess(ByteBuffer buffer, byte[] bytes, boolean isEnd) {
		buffer.flip();
		if (buffer.limit() < bytes.length) {
			bytes = new byte[buffer.limit()];
			for (int i = 0; i < buffer.limit(); i++)
				bytes[i] = buffer.get();
		} else
			buffer.get(bytes);
		buffer.clear();
		String str = new String(bytes);
		return decode(str, isEnd);
	}

	public FileUtil getFileUtil() {
		if (fileUtil == null)
			fileUtil = new FileUtil();
		return fileUtil;
	}

}
