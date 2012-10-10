package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import util.Config;
import util.ConfigReader;
import util.ConfigSimpleReader;

public class FileMailTransformer implements MailTransformer {

    Config specific = Config.getInstance().getConfig(
	    "transform_path.properties");
    private File mail;
    private static Logger logger = Logger.getLogger(FileMailTransformer.class);
    private static Config configurerConfig = Config.getInstance().getConfig(
	    "configurer_conf");
    HeaderReader headerReader;
    
    public FileMailTransformer(final File mail) {
	this.mail = mail;
	this.headerReader = new HeaderReader(mail);
    }

    @Override
    public void transform() throws IOException {
	// read transformations file to know what transformations should be
	// applyed

	this.specific.get("l33t");

	final ConfigReader configReader = new ConfigSimpleReader(
		"transformation");
	String option;
	final String service;
	while ((option = configReader.readLine()) != null) {
	    this.executeTransformation(option);
	}

    }

    private void executeTransformation(final String option) throws IOException {
	String service;
	logger.debug("Printing headers: " + this.headerReader.getHeaders());

	if ("l33t".equals(option)) {
	    service = this.specific.get("l33t");
	    this.l33tTransformation();
	} else if ("rotateimages".equals(option)) {
	    service = this.specific.get("rotateimages");
	} else if ("hidesender".equals(option)) {
	    service = this.specific.get("hidesender");
	}
    }

    private void l33tTransformation() throws IOException {
	// for reading the mail
	logger.info("Transforming to l33t");
	final BufferedReader reader = new BufferedReader(new FileReader(
		this.mail));

	reader.close();

    }
}
