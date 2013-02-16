package pl.mobilla;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


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
		
		return null;
	}

	public void write(String string) {
		
	}
	
	@Override
	public void close() throws IOException {
		fileWriter.close();
	}

}
