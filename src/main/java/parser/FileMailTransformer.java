package parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import util.ConfigReader;
import util.ConfigSimpleReader;

public class FileMailTransformer implements MailTransformer {

    private static final Logger logger = Logger
	    .getLogger(FileMailTransformer.class);

    private File mail;
    private MailHeader mailHeader;

    public FileMailTransformer(File mail) {
	this.mail = mail;
	this.mailHeader = new MailHeader(mail);
    }

    @Override
    public void transform() throws IOException {
	ConfigReader configReader = new ConfigSimpleReader("transformation");
	String option;
	while ((option = configReader.readLine()) != null) {
	    this.executeTransformation(option);
	}
    }

    private void executeTransformation(final String option) throws IOException {
	List<Transformer> transformers = new ArrayList<Transformer>();
	logger.debug("Printing headers: " + mailHeader.getHeader());
	if ("l33t".equals(option)) {
	    transformers.add(new LeetTransformer());
	} else if ("rotateimages".equals(option)) {
	    transformers.add(new ImageTransformer());
	} else if ("hidesender".equals(option)) {
//	    TODO
	}
	
	for(Transformer t: transformers){
	    mail = t.apply(mail);
	}
	
    }

}
