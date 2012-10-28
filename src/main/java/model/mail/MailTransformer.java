package model.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import model.configuration.Config;
import model.configuration.SimpleListConfiguration;
import model.mail.transformerimpl.HideSenderTransformer;
import model.mail.transformerimpl.ImageTransformer;
import model.mail.transformerimpl.LeetTransformer;
import model.mail.transformerimpl.Transformer;
import model.parser.mime.MimeHeader;

public class MailTransformer {

	private static final Logger logger = Logger.getLogger(MailTransformer.class);
	private static final SimpleListConfiguration config = Config.getInstance().getSimpleListConfig("transformation");

	public void transformHeader(MimeHeader header) throws IOException {
		List<HeaderTransformer> transformers = new ArrayList<HeaderTransformer>();
		for (String option : config.getValues()) {
			if ("hidesender".equals(option)) {
				transformers.add(new HideSenderTransformer());
			}
		}
		for (HeaderTransformer headerTransformer : transformers) {
		    headerTransformer.transform(header);
		}
	}

	public StringBuilder transformPart(Map<String, MimeHeader> partHeaders, StringBuilder part) throws IOException {
		StringBuilder retPart = part;
		List<Transformer> transformers = getTransformerList();
		for (Transformer transformer : transformers) {
			try {
				retPart = transformer.transform(retPart, partHeaders);
			} catch (Exception e) {
				logger.error("Could not apply transformer: " + transformer.getClass(), e);
			}
		}
		return retPart;
	}

	private List<Transformer> getTransformerList() {
		List<Transformer> transformers = new ArrayList<Transformer>();
		Collection<String> options = config.getValues();
		for (String option : options) {
			if ("l33t".equals(option.toLowerCase())) {
				transformers.add(new LeetTransformer());
			} else if ("rotateimages".equals(option.toLowerCase())) {
				transformers.add(new ImageTransformer());
			}
		}
		return transformers;

	}
}
