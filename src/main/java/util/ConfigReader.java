package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

public abstract class ConfigReader {

	private static Logger logger = Logger.getLogger(ConfigWriter.class);

	BufferedReader reader;

	public ConfigReader(final String fileName) throws FileNotFoundException {
		final String file = Config.getInstance().get("specific_conf_dir")
				+ fileName;
		final String path = this.getClass().getClassLoader().getResource(file)
				.getPath();
		logger.trace("Reading config file: " + fileName);
		this.reader = new BufferedReader(new FileReader(path));
	}

	public abstract String readLine() throws IOException;

}
