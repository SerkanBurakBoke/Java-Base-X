package com.sbb.encoder.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
	public void createfile(String destFileName) throws IOException {
		Path file = Paths.get(destFileName);
		Files.write(file, new byte[0]);
	}

	public void deleteFile(String destFileName) {
		File f = new File(destFileName);
		f.delete();
	}

	public RandomAccessFile getRandomAccessfile(String sourceFileName, String mod) throws FileNotFoundException {
		RandomAccessFile sourceFile = new RandomAccessFile(sourceFileName, mod);
		return sourceFile;
	}

	public void close(Channel channel) throws IOException {
		if (channel != null && channel.isOpen())
			channel.close();
	}

	public void close(RandomAccessFile file) throws IOException {
		if (file != null)
			file.close();
	}

	public void writeToFile(byte[] bytes, FileChannel fileChannel) {
		try {
			ByteBuffer buf = ByteBuffer.allocate(bytes.length);
			buf.clear();
			buf.put(bytes);
			buf.flip();
			while (buf.hasRemaining()) {
				fileChannel.write(buf);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeToFile(String str, FileChannel fileChannel) {
		writeToFile(str.getBytes(), fileChannel);
	}

	public FileChannel getChannel(RandomAccessFile sourceFile) {
		return sourceFile.getChannel();
	}

	public void deleteAndCreateFile(String destFileName) throws IOException {
		deleteFile(destFileName);
		createfile(destFileName);
	}

}
