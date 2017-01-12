package com.sbb.encoder.util;

import java.nio.ByteBuffer;

public class BaseConversionUtil {
	private char[] charArrayMap;
	private String baseCharMap;
	ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE);

	public BaseConversionUtil(String baseCharMap) {
		setBaseCharMap(baseCharMap);
		setCharArrayMap(baseCharMap.toCharArray());
	}

	public String convert2Base(long number, int base) {
		long quot = (long) (number / base);
		int remain = (int) (number % base);
		if (quot == 0)
			return String.valueOf(getCharArrayMap()[remain]);
		else
			return getCharArrayMap()[remain] + convert2Base(quot, base);
	}

	public static Long convert2decimal(byte byt, long result, long impact) {
		if (impact == 0)
			return 0L;
		byte b1 = 0x01;
		for (int j = 0; j < 8; j++) {
			result += ((byt & b1) != 0 ? impact : 0);
			impact <<= 1;
			byt >>= 1;
		}
		return result;
	}

	public Long convert2decimal(String baseNum, int base) {
		long result = 0;
		long impact = 1;

		for (int i = 0; i < baseNum.length(); i++) {
			result += (long) (impact * baseCharMap.indexOf(baseNum.charAt(i)));
			impact *= base;
		}
		return result;
	}

	public byte[] convert2Byte(long result, int byteCount) {
		byte[] longBytes = long2Bytes(result);
		byte[] bytes = new byte[byteCount];

		for (int i = 2 + byteCount; i >= 3; i--) {
			bytes[byteCount + 2 - i] = longBytes[i];
		}
		return bytes;
	}

	private byte[] long2Bytes(long result) {
		buffer = ByteBuffer.allocate(Long.SIZE);
		buffer.putLong(result);
		return buffer.array();
	}

	public static Long convert2decimal(byte byt) {
		return convert2decimal(byt, 0, 1);
	}

	public static Long convert2decimal(byte byt, long impact) {
		return convert2decimal(byt, 0, impact);
	}

	public String getBaseCharMap() {
		return baseCharMap;
	}

	public void setBaseCharMap(String baseCharMap) {
		this.baseCharMap = baseCharMap;
	}

	public char[] getCharArrayMap() {
		return charArrayMap;
	}

	public void setCharArrayMap(char[] charArrayMap) {
		this.charArrayMap = charArrayMap;
	}
}
