package model.mail.transformerimpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import model.mail.MimeHeaderCollection;
import model.parser.mime.MimeHeader;
import model.util.Base64Util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;

public class LeetTransformer implements Transformer {

	@Override
	public StringBuilder transform(StringBuilder text, MimeHeaderCollection partheaders) throws IOException {
		MimeHeader contentType = partheaders.get("Content-Type");
		if (contentType == null || !contentType.getValue().toLowerCase().startsWith("text/plain")) {
			return text;
		}
		MimeHeader contentTransferEncoding = partheaders.get("Content-Transfer-Encoding");
		if (contentTransferEncoding == null) {
			return new StringBuilder(toLeet(text.toString()));
		} else {
			if ("base64".equals(contentTransferEncoding.getValue().toLowerCase())) {
				File encodedFile = File.createTempFile("encode_", ".tmp");
				FileWriter encodedContentsWriter = new FileWriter(encodedFile);
				try {
					File decodedFile = Base64Util.decodeUsingOS(text
							.toString());
					Scanner decodedContents = new Scanner(decodedFile);
					while (decodedContents.hasNextLine()) {
						encodedContentsWriter.append(toLeet(decodedContents
								.nextLine() + "\r\n"));
					}
					decodedContents.close();
					encodedContentsWriter.close();
					File encodedText = Base64Util.encodeToFile(encodedFile);
					Scanner encodedTextScaner = new Scanner(encodedText);
					StringBuilder builder = new StringBuilder();
					while (encodedTextScaner.hasNextLine()) {
						String line = encodedTextScaner.nextLine();
						builder.append(line);
					}
					encodedTextScaner.close();
					return builder;
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}
			} else { // quoted-printable
				String decodeString = decode(text.toString());
				String transformed = toLeet(decodeString);
				return new StringBuilder(transformed);
			}
		}
	}

	private String toLeet(String text) {
		String textString = text;
		textString = textString.replace("a", "4");
		textString = textString.replace("e", "3");
		textString = textString.replace("i", "1");
		textString = textString.replace("o", "0");
		return textString;
	}

	private String decode(String quotedPrintable) {
//		QuotedPrintableCodec codec = new QuotedPrintableCodec("ISO-8859-1");
		QuotedPrintableCodec codec = new QuotedPrintableCodec("ISO-8859-*");
		try {
			return codec.decode(quotedPrintable);
		} catch (DecoderException e) {
			throw new IllegalStateException("Could not decode: "
					+ quotedPrintable);
		}
	}

}