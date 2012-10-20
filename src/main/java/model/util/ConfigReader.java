package model.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

public abstract class ConfigReader {

	private static Logger logger = Logger.getLogger(ConfigWriter.class);

	BufferedReader reader;

	public ConfigReader(final String fileName) throws FileNotFoundException {
		String file = Config.getInstance().get("specific_conf_dir")
				+ fileName;
		String path = IOUtil.getResource(file).getPath();
		reader = new BufferedReader(new FileReader(path));
		logger.trace("Reading config file: " + fileName);
	}

	public abstract String readLine() throws IOException;

}
