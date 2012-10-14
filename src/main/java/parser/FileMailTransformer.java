package parser;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import util.Config;
import util.ConfigReader;
import util.ConfigSimpleReader;

public class FileMailTransformer implements MailTransformer {

	private static final Logger logger = Logger
			.getLogger(FileMailTransformer.class);
	private static final Config specific = Config.getInstance().getConfig(
			"transform_path.properties");

	private File mail;
	private MailHeader mailHeader;

	public FileMailTransformer(File mail) {
		this.mail = mail;
		this.mailHeader = new MailHeader(mail);
	}

	@Override
	public void transform() throws IOException {
		// read transformations file to know what transformations should be
		// applied
		this.specific.get("l33t");
		ConfigReader configReader = new ConfigSimpleReader("transformation");
		String option;
		final String service;
		while ((option = configReader.readLine()) != null) {
			this.executeTransformation(option);
		}
	}

	private void executeTransformation(final String option) throws IOException {
		String service;
		logger.debug("Printing headers: " + mailHeader.getHeader());
		if ("l33t".equals(option)) {
			service = this.specific.get("l33t");
			// this.l33tTransformation();
		} else if ("rotateimages".equals(option)) {
			service = this.specific.get("rotateimages");
		} else if ("hidesender".equals(option)) {
			service = this.specific.get("hidesender");
		}
	}

}
