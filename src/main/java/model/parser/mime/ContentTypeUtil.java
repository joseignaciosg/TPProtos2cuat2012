package model.parser.mime;

import java.io.IOException;
import java.util.Properties;

import model.util.IOUtil;

import org.apache.log4j.Logger;

public class ContentTypeUtil {

	private static Logger logger = Logger.getLogger(ContentTypeUtil.class);
	private static Properties mimeExtension;
	
	static {
		initializeMimeExtension();
	}

	private static void initializeMimeExtension() {
		try {
			Properties mimeFileProperties = new Properties();
			mimeFileProperties.load(IOUtil.getStream("mime/contenttype"));
			mimeExtension = mimeFileProperties;
		} catch (IOException e) {
			logger.error("File Content-Type Mime-translator not found.");
		}
	}
	
	public static String getContentType(String fileExtension) {
		String mimeType = mimeExtension.getProperty(fileExtension.toLowerCase().trim());
		if (mimeType == null) {
			logger.warn(fileExtension + " is not defined for mime content type file!");
		}
		return mimeType;
	}
	
}
