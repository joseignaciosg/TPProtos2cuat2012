package model.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import model.configuration.Config;

import org.apache.log4j.Logger;


public class ConfigWriter {

	private static Logger logger = Logger.getLogger(ConfigWriter.class);
	
	private String fileName;
	private StringBuilder fileContents;
	
	public ConfigWriter(String fileName) {
		this.fileName = fileName;
		fileContents = new StringBuilder();
	}
	
	public void appendLine(String line) {
		fileContents.append(line + "\r\n");
	}
	
	public void flush() {
		String path = Config.getInstance().getFullPath(fileName);
		logger.trace("Guardo archivo de configuracion para " + fileName);
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			out.write(fileContents.toString());
			out.close();
		} catch (IOException e) {
			logger.error("error flushing file: " + fileName + " - " + e.getMessage());
		}
	}
	
}
