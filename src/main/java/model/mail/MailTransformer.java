package model.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.configuration.Config;
import model.configuration.SimpleListConfiguration;
import model.mail.transformerimpl.LeetTransformer;
import model.mail.transformerimpl.Transformer;
import model.parser.mime.MimeHeader;


public class MailTransformer {

	private static final SimpleListConfiguration config = Config.getInstance().getSimpleListConfig("transformation");

	public MailTransformer() {
	}

	public void transformHeaders(List<MimeHeader> mailHeaders) {

	}

	public StringBuilder transform(StringBuilder part,
			List<MimeHeader> partHeaders) throws IOException {
		StringBuilder retPart = part;
		List<Transformer> transformers = getTransformerList();
		for (Transformer transformer : transformers) {
			retPart = transformer.transform(part, partHeaders);
		}
		return retPart;
	}

	private List<Transformer> getTransformerList() {
		List<Transformer> transformers = new ArrayList<Transformer>();
		Collection<String> options = config.getValues();
		for (String option : options) {
			if ("l33t".equals(option)) {
				transformers.add(new LeetTransformer());
			} else if ("rotateimages".equals(option)) {
				// transformers.add(new ImageTransformer());
			} else if ("hidesender".equals(option)) {
				// TODO: temrinar hide sender!
			}
		}
		return transformers;
	}

}
