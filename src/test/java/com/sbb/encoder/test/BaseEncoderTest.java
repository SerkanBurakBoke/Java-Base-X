package com.sbb.encoder.test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sbb.encoder.decoder.BaseDecoder;
import com.sbb.encoder.encoder.BaseEncoder;
import com.sbb.encoder.properties.BaseProperties;
import com.sbb.encoder.util.BaseConversionUtil;
import com.sbb.encoder.util.FileUtil;

public class BaseEncoderTest {
	static Logger logger = Logger.getLogger(BaseEncoderTest.class);

	public String getFileName() {
		return "data/ec1.rar";
	}
	private static BaseProperties baseProperties = new BaseProperties();
	
	@Before
	public void setUpBeforeClass() throws Exception {
		testEncoding();
		testDecoding();
	}

	@Test
	public void verify() throws Exception {
		String sourceFileName = getFileName();
		String destFileName = sourceFileName + ".dec";
		Assert.assertTrue(FileUtils.contentEquals(new File(sourceFileName), new File(destFileName)));
	}

	@Test
	public void verify4String() throws Exception {
		String testStr = "test string";
		String decodeStr = new String(new BaseDecoder() .decode(new BaseEncoder().encode(testStr)));
		Assert.assertTrue(testStr.equals(decodeStr));
	}
	
	@Test
	public void verify4ByteArray() throws Exception {
		byte[] bytes = { (byte)0x99, (byte)0x83, (byte)0x52, (byte)0x46, (byte)0x47, (byte)0xAB, (byte)0x6C, (byte)0x79, (byte)0xF1, (byte)0x3C, (byte)0xAC, (byte)0x16 };
		
		BaseEncoder encoder = new BaseEncoder();
		encoder.setBaseProperties(baseProperties);
		BaseDecoder decoder = new BaseDecoder();
		decoder.setBaseProperties(baseProperties);

		printByteArray(bytes);
		String encodedString = encoder.encode(bytes);
		logger.info(encodedString);
		byte[]  newBytes = decoder.decode(encodedString);
		printByteArray(newBytes);
		boolean isOk = true;
		try {
			for(int i = 0; i<bytes.length; i++){
				if(bytes[i] != newBytes[i]){
					isOk = false;
					break;
				}
			}
				
		} catch (Exception e) {
			isOk = false;
		}
		
		
		Assert.assertTrue(isOk);
	}
	
	@Test
	public void verify4ByteZero() throws Exception {
		byte[] bytes = { (byte)0x99, (byte)0x83, (byte)0x52, (byte)0x46, (byte)0x47, (byte)0xAB, (byte)0x6C, (byte)0x79, (byte)0xF1, (byte)0x3C, (byte)0x00, (byte)0x00 };
		
		BaseEncoder encoder = new BaseEncoder();
		encoder.setBaseProperties(baseProperties);
		BaseDecoder decoder = new BaseDecoder();
		decoder.setBaseProperties(baseProperties);

		printByteArray(bytes);
		String encodedString = encoder.encode(bytes);
		logger.info(encodedString);
		byte[]  newBytes = decoder.decode(encodedString);
		printByteArray(newBytes);
		boolean isOk = true;
		try {
			for(int i = 0; i<bytes.length; i++){
				if(bytes[i] != newBytes[i]){
					isOk = false;
					break;
				}
			}
				
		} catch (Exception e) {
			isOk = false;
		}
		
		
		Assert.assertTrue(isOk);
	}
	
	
	
	public void verify4ByteArrays() throws Exception {
		
		byte[] bytes = { (byte)0x99, (byte)0x83, (byte)0x52, (byte)0x46, (byte)0x47, (byte)0xAB, (byte)0x6C, (byte)0x79, (byte)0xF1, (byte)0x3C, (byte)0xAC, (byte)0x16 };
		int size = 10;
		int index = 0;
		for(int i = 5; i<size; i ++){
			bytes = generateNewZeroArray(i);
			index = 0;
			boolean isCon = true;
			while (isCon){
				index = 0;
				
				while((index < i) && !(bytes[index] == -1)){
					testByteArray(bytes);
					bytes[index]++;
				}
				
				
				while((index < i) && (bytes[index] == -1)){		
					if(index != i-1)
						bytes[index] = 0;
					index ++;
				}
				
				if(index == i){
					if((bytes[index - 1 ] == -1)){
						isCon = false;
					}
				}else{
					bytes[index] ++ ;
				}
			}

		}
		
		boolean isOk = testByteArray(bytes);
		
		
		Assert.assertTrue(isOk);
	}

	private byte[] generateNewZeroArray(int size) {
		byte[] example = new byte[size];
		for(int i = 0; i<size; i ++ )
			example[i] = (byte)0x00;
		return example;
	}

	private boolean testByteArray(byte[] bytes) {
		String encodedString = new BaseEncoder().encode(bytes);
		byte[]  newBytes = new BaseDecoder().decode(encodedString);
		boolean isOk = true;
		try {
			for(int i = 0; i<bytes.length; i++){
				if(bytes[i] != newBytes[i]){
					printByteArray(bytes);
					isOk = false;
					break;
				}
			}
				
		} catch (Exception e) {
			printByteArray(bytes);
			isOk = false;
		}
		return isOk;
	}

	public void testEncoding() throws IOException {

		FileUtil fileUtil = new FileUtil();
		String sourceFileName = getFileName();
		String destFileName = sourceFileName + ".enc";

		fileUtil.deleteAndCreateFile(destFileName);
		RandomAccessFile sourceFile = fileUtil.getRandomAccessfile(getFileName(), "r");
		RandomAccessFile destFile = fileUtil.getRandomAccessfile(destFileName, "rw");

		try {
			new BaseEncoder().encode(sourceFile, true, destFile, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testDecoding() throws IOException {
		FileUtil fileUtil = new FileUtil();
		String sourceFileName = getFileName() + ".enc";
		String destFileName = getFileName() + ".dec";
		fileUtil.deleteAndCreateFile(destFileName);

		RandomAccessFile sourceFile = fileUtil.getRandomAccessfile(sourceFileName, "r");
		RandomAccessFile destFile = fileUtil.getRandomAccessfile(destFileName, "rw");

		try {
			new BaseDecoder().decode(sourceFile, true, destFile, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void printByteArray(byte[] bytes){
		new BaseConversionUtil(baseProperties.getBaseCharMap()).printByteArray(bytes);
	}
}
