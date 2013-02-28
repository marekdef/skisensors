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
	private int currentPostfix = 0;
	private String fileBase;
	private String fileExtension;

	public RollingFileWriter(String fileName, long maximumLength) throws IOException {
		this.maximumLength = maximumLength;
		
		this.fileExtension = getExtension(fileName);
		this.fileBase = getBase(fileName, this.fileExtension);
		
		file = new File(fileName);
		findNextAvailablePostfix();

		fileWriter = new FileWriter(getFileName());
	}

	private String getBase(String fileName, String fileExtension) {	
		return fileName.substring(0, fileName.length() - fileExtension.length() - 1);
	}

	private String getExtension(String fileName) {
		Iterable<String> split = Splitter.on(".").split(fileName);
		String extension = Iterables.getLast(split);
		return extension;
	}

	private void findNextAvailablePostfix() throws IOException {
		while(file.exists() && file.length() >= this.maximumLength) {
			currentPostfix++;
			close();
			file = new File(getFileName());
		}
		fileWriter = new FileWriter(file);
	}

	private String getFileName() {
		if(currentPostfix == 0)
			return String.format("%s.%s", this.fileBase, this.fileExtension);
		return String.format("%s.%d.%s", this.fileBase, this.currentPostfix, this.fileExtension);
	}

	public void write(String string) throws IOException {
		fileWriter.write(string);
		findNextAvailablePostfix();
	}
	
	@Override
	public void close() throws IOException {
		fileWriter.close();
		fileWriter = null;
	}

}
