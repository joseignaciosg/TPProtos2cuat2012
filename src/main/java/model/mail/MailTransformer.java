package model.mail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.configuration.Config;
import model.configuration.SimpleListConfiguration;
import model.mail.transformerimpl.HideSenderTransformer;
import model.mail.transformerimpl.ImageTransformer2;
import model.mail.transformerimpl.LeetTransformer;
import model.mail.transformerimpl.Transformer;
import model.parser.mime.MimeHeader;

import org.apache.log4j.Logger;

public class MailTransformer {

	private static final Logger logger = Logger.getLogger(MailTransformer.class);
	private static final SimpleListConfiguration transConfig = Config.getInstance().getSimpleListConfig("transformation");
	private static final SimpleListConfiguration externalTransConfig = Config.getInstance().getSimpleListConfig("external_transformation");

	public void transformHeader(MimeHeader header) throws IOException {
		List<HeaderTransformer> transformers = new ArrayList<HeaderTransformer>();
		for (String option : transConfig.getValues()) {
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
				e.printStackTrace();
				logger.error("Could not apply transformer: " + transformer.getClass(), e);
			}
		}
		return retPart;
	}
	
	private List<Transformer> getTransformerList() {
		List<Transformer> transformers = new ArrayList<Transformer>();
		Collection<String> options = transConfig.getValues();
		for (String option : options) {
			if ("l33t".equals(option.toLowerCase())) {
				transformers.add(new LeetTransformer());
			} else if ("rotateimages".equals(option.toLowerCase())) {
				transformers.add(new ImageTransformer2());
			}
		}
		return transformers;
	}
	
	public void transformComplete(Mail mail) {
		List<String> xternalTransformers = getExternalTransformerList();
		List<String> commands = new LinkedList<String>();
		try {
			File transformedIn = mail.getContents();
			File transformedOut = File.createTempFile("externalTransformOut", ".txt");
			for (String command : xternalTransformers) {
				commands.clear();
				commands.addAll(Arrays.asList(command.split(" ")));
				commands.add(transformedIn.getAbsolutePath());
				ProcessBuilder pb = new ProcessBuilder(commands);
				pb.redirectOutput(transformedOut);
				Process p = pb.start();
				p.waitFor();
				if (p.exitValue() == 0) {
					mail.setContents(transformedOut);
					// switch in <-> out
					File tmp = transformedIn;
					transformedIn = transformedOut;
					transformedOut = tmp;
				} else {
					logger.warn(command + "did not finish succesfuly. Exit code " + p.exitValue());
				}
			}
			if (transformedIn == mail.getContents()) {
				transformedOut.delete();
			} else {
				transformedIn.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("External trasformations could not be applied!!");
		}
	}

	private List<String> getExternalTransformerList() {
		List<String> transformers = new LinkedList<String>();
		Collection<String> options = externalTransConfig.getValues();
		for (String option : options) {
			transformers.add(option);
		}
		return transformers;
	}
}
