package model.mail.transformerimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.mail.Mail;
import model.mail.MailTransformer;
import model.util.ConfigReader;
import model.util.ConfigSimpleReader;

import org.apache.log4j.Logger;


public class FileMailTransformer implements MailTransformer {

	private static final Logger logger = Logger.getLogger(FileMailTransformer.class);
	
	@Override
	public void transform(Mail mail) throws IOException {
		ConfigReader configReader = new ConfigSimpleReader("transformation");
		String option;
		while ((option = configReader.readLine()) != null) {
			executeTransformation(mail, option);
		}
	}

	private void executeTransformation(Mail mail, String option) throws IOException {
		List<Transformer> transformers = new ArrayList<Transformer>();
		if ("l33t".equals(option)) {
			transformers.add(new LeetTransformer());
		} else if ("rotateimages".equals(option)) {
			transformers.add(new ImageTransformer());
		} else if ("hidesender".equals(option)) {
			// TODO: temrinar hide sender!
		}
		for (Transformer t : transformers) {
			t.apply(mail);
		}

	}

}
