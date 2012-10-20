package parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import parser.mime.MimeHeaderParser;
import util.ConfigReader;
import util.ConfigSimpleReader;

public class FileMailTransformer implements MailTransformer {

	private static final Logger logger = Logger.getLogger(FileMailTransformer.class);

	private File mail;
	private MimeHeaderParser mailHeader;

	public FileMailTransformer(File mail) {
		this.mail = mail;
		this.mailHeader = new MimeHeaderParser();
		// this.mailHeader.parse(mail);
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
		if ("l33t".equals(option)) {
			transformers.add(new LeetTransformer());
		} else if ("rotateimages".equals(option)) {
			transformers.add(new ImageTransformer());
		} else if ("hidesender".equals(option)) {
			// TODO: temrinar hide sender!
		}
		for (Transformer t : transformers) {
			mail = t.apply(mail);
		}

	}

}
