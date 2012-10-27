package model.mail.transformerimpl;

import java.io.IOException;
import java.util.Map;

import model.parser.mime.MimeHeader;

public class LeetTransformer extends Transformer {

	@Override
	public StringBuilder transform(StringBuilder text, Map<String, MimeHeader> partheaders) throws IOException {
		MimeHeader contentType = partheaders.get("Content-Type");
		if (contentType.getValue().startsWith("text/")) {
			String textString = text.toString();
			// FIXME: esto es SUPER ineficiente... usar: text.replace(start, end, str)
			textString = textString.replaceAll("a", "4");
			textString = textString.replaceAll("e", "3");
			textString = textString.replaceAll("i", "1");
			textString = textString.replaceAll("o", "0");
			return new StringBuilder(textString);
		}
		return text;
	}

}