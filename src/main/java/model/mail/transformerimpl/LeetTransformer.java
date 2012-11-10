package model.mail.transformerimpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import model.mail.MimeHeaderCollection;
import model.parser.mime.MimeHeader;
import model.util.Base64Util;

public class LeetTransformer implements Transformer {

	private static final Logger logger = Logger.getLogger(LeetTransformer.class);
	
	@Override
	public StringBuilder transform(StringBuilder text, MimeHeaderCollection partheaders) throws IOException {
		MimeHeader contentType = partheaders.get("Content-Type");
		if (contentType != null && !contentType.getValue().toLowerCase().startsWith("text/plain")) {
			return text;
		}
		MimeHeader contentTransferEncoding = partheaders.get("Content-Transfer-Encoding");
		if (contentTransferEncoding == null) {
			return new StringBuilder(toLeet(text.toString()));
		} else {
			String encoding = contentTransferEncoding.getValue();
			if ("base64".equalsIgnoreCase(encoding)) {
				File encodedFile = File.createTempFile("encode_", ".tmp");
				FileWriter encodedContentsWriter = new FileWriter(encodedFile);
				try {
					File decodedFile = Base64Util.decodeUsingOS(text.toString());
					Scanner decodedContents = new Scanner(decodedFile);
					while (decodedContents.hasNextLine()) {
						encodedContentsWriter.append(toLeet(decodedContents.nextLine()) + "\r\n");
					}
					decodedContents.close();
					encodedContentsWriter.close();
					File base64EncodedFile = Base64Util.encodeToFile(encodedFile);
					Scanner encodedTextScaner = new Scanner(base64EncodedFile);
					StringBuilder builder = new StringBuilder();
					while (encodedTextScaner.hasNextLine()) {
						builder.append(encodedTextScaner.nextLine());
					}
					encodedTextScaner.close();
					return builder;
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}
			} else if ("quoted-printable".equalsIgnoreCase(encoding)) {
				return new StringBuilder(leetQuoted(text.toString()));
			} else {
				logger.error("Unknown encoding " + encoding + ". No changed done to the text");
				return text;
			}
		}
	}

	private String leetQuoted(String decodeString) {
		char[] chars = decodeString.toCharArray();
		String quotedLeet = "";
		for (int i = 0; i < chars.length; i++) {
			switch (chars[i]) {
				case '=':
					quotedLeet += chars[i] + "" + chars[i + 1] + "" + chars[i + 2];
					i += 2;
					break;
				default:
					quotedLeet += toLeet(String.valueOf(chars[i]));
			}
		}
		return quotedLeet;
	}

	private String toLeet(String text) {
		String leetText = text;
		leetText = leetText.replace("a", "4");
		leetText = leetText.replace("e", "3");
		leetText = leetText.replace("i", "1");
		leetText = leetText.replace("o", "0");
		return leetText;
	}

}