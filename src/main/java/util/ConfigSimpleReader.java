package util;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigSimpleReader extends ConfigReader {

	public ConfigSimpleReader(final String fileName)
			throws FileNotFoundException {
		super(fileName);
	}

	@Override
	public String readLine() throws IOException {
		return this.reader.readLine();
	}

}
