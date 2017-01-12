package com.sbb.encoder.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.log4j.Logger;

import com.sbb.encoder.decoder.BaseDecoder;
import com.sbb.encoder.decoder.DecoderInterFace;
import com.sbb.encoder.encoder.BaseEncoder;
import com.sbb.encoder.encoder.EncoderInterFace;
import com.sbb.encoder.exception.EncoderRuntimeException;
import com.sbb.encoder.exception.EncoderValidationException;
import com.sbb.encoder.util.FileUtil;

public class Main {
	Logger logger = Logger.getLogger(this.getClass());

	public static void main(String[] args) {
		new Main().makeProgress(args);
	}

	public void makeProgress(String[] args) {
		try {
			if (args == null || args.length < 2)
				throw new EncoderValidationException("You must enter at least two params");
			if ("E".equals(args[0].toUpperCase()))
				encode(args);
			else if ("D".equals(args[0].toUpperCase()))
				decode(args);
			else 
				throw new EncoderValidationException("you can only use first param like \"D\" for decoding  or \"E\" for encoding ");
			

		} catch (EncoderValidationException e) {
			logger.error("!!! ERROR !!! There is validation problems in params. Please Fix it and try again ");
			logUsage();
		} catch (EncoderRuntimeException e) {
			logger.error("!!! ERROR !!! There are problems in runtime. Please please contact your system administrators ");
			logUsage();
		}  catch (Exception e) {
			logger.error("!!! ERROR !!! There are problems in runtime. Please please contact your system administrators ");
			logUsage();
		}
	}

	private void logUsage() {
		logger.info("------");
		logger.info("Example usage:");
		logger.info("Example usage: java -jar <jarName> E FF <sourceFile> <destFile> for encode source file to Dest file");
		logger.info("Example usage: java -jar <jarName> E FS <sourceFile>  for encode source file to console output");
		logger.info("Example usage: java -jar <jarName> E S <text to encode>  for encode text to console output");
		logger.info("Example usage: java -jar <jarName> D FF <sourceFile> <destFile> for decode source file to Dest file");
		logger.info("Example usage: java -jar <jarName> D FS <text to encode> <destFile>  for decode text to Dest File");
		logger.info("Example usage: java -jar <jarName> D S <text to encode>  for decode text to console output");
		logger.info("------");
	}

	private void encode(String[] args) throws EncoderValidationException, EncoderRuntimeException {
		EncoderInterFace encoder = new BaseEncoder();

		if ("FF".equals(args[1].toUpperCase())) {
			deleteAndCreateFile(args[3]);
			RandomAccessFile sourceFile = getRandomAccessFile(args[2], "r");
			RandomAccessFile destFile = getRandomAccessFile(args[3], "rw");
			encoder.encode(sourceFile, destFile);
		} else if ("FS".equals(args[1].toUpperCase())) {
			RandomAccessFile sourceFile = getRandomAccessFile(args[2], "r");
			logger.info(encoder.encode(sourceFile));
		} else if ("S".equals(args[1].toUpperCase())) {
			logger.info(encoder.encode(args[2]));
		} else {
			throw new EncoderValidationException(
					"you can only use second param for encoding like \"FS\" for from file to text string  or \"FF\" for from file to file ");
		}
		logger.info("------");
		logger.info("Encode successful");
		logger.info("------");
	}

	private void deleteAndCreateFile(String arg) throws EncoderRuntimeException {
		try {
			new FileUtil().deleteAndCreateFile(arg);
		} catch (IOException e) {
			throw new EncoderRuntimeException("There is a problem with file progress");
		}
	}

	private void decode(String[] args) throws EncoderValidationException, EncoderRuntimeException {
		DecoderInterFace decoder = new BaseDecoder();
		if ("FF".equals(args[1].toUpperCase())) {
			deleteAndCreateFile(args[3]);
			RandomAccessFile destFile = getRandomAccessFile(args[3], "rw");
			RandomAccessFile sourceFile = getRandomAccessFile(args[2], "r");
			decoder.decode(sourceFile, destFile);
		} else if ("FS".equals(args[1].toUpperCase())) {
			deleteAndCreateFile(args[3]);
			RandomAccessFile destFile = getRandomAccessFile(args[3], "rw");
			decoder.decode(args[2], destFile);
		} else if ("S".equals(args[1].toUpperCase())) {
			logger.info(new String(decoder.decode(args[2])));
		} else {
			throw new EncoderValidationException(
					"you can only use second param for encoding like \"FS\" for from file to text string  or \"FF\" for from file to file ");
		}
		logger.info("------");
		logger.info("Decode successful");
		logger.info("------");
	}

	private RandomAccessFile getRandomAccessFile(String filePath, String mod) throws EncoderValidationException {
		try {
			return new FileUtil().getRandomAccessfile(filePath, mod);
		} catch (FileNotFoundException e) {
			throw new EncoderValidationException("SourceFile could not be found");
		}
	}
}
