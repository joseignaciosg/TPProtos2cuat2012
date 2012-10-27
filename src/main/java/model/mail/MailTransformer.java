package model.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import model.configuration.Config;
import model.configuration.SimpleListConfiguration;
import model.mail.transformerimpl.HideSenderTransformer;
import model.mail.transformerimpl.ImageTransformer;
import model.mail.transformerimpl.LeetTransformer;
import model.mail.transformerimpl.Transformer;
import model.parser.mime.MimeHeader;

public class MailTransformer {

	private static final SimpleListConfiguration config = Config.getInstance().getSimpleListConfig("transformation");

	public void transformHeaders(Map<String, MimeHeader> mailHeaders) throws IOException {
		List<HeaderTransformer> headerTransformers = getHeaderTransformerList(mailHeaders);
		for (HeaderTransformer headerTransformer : headerTransformers) {
		    headerTransformer.transform();
		}
	}

	public StringBuilder transform(StringBuilder part, Map<String, MimeHeader> partHeaders) throws IOException {
		StringBuilder retPart = part;
		List<Transformer> transformers = getTransformerList();
		for (Transformer transformer : transformers) {
		    retPart = transformer.transform(part, partHeaders);
		}
		return retPart;
	}
	
	private List<HeaderTransformer> getHeaderTransformerList(Map<String, MimeHeader> mailHeaders){
		List<HeaderTransformer> transformers = new ArrayList<HeaderTransformer>();
		Collection<String> options = config.getValues();
		for (String option : options) {
			if ("hidesender".equals(option)) {
				transformers.add(new HideSenderTransformer(mailHeaders));
			}
		}
		return transformers;
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
