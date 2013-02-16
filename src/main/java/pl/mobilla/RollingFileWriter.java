package pl.mobilla;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;


public class RollingFileWriter implements Closeable {
	private FileWriter fileWriter;
	private File file;
	private final long maximumLength;

	public RollingFileWriter(String fileName, long maximumLength) throws IOException {
		this.maximumLength = maximumLength;
		file = new File(fileName);
		if(file.length() > maximumLength)
			file = findNewFile(fileName);
		fileWriter = new FileWriter(fileName);
	}

	private File findNewFile(String fileName) {
		Iterable<String> split = Splitter.on(".").split(fileName);
		String extension = Iterables.getLast(split);
		return null;
	}

	public void write(String string) {
		
	}
	
	@Override
	public void close() throws IOException {
		fileWriter.close();
	}

}
