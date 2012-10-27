package model.mail.transformerimpl;

import java.io.IOException;
import java.util.Map;

import model.parser.mime.MimeHeader;

public interface Transformer {

	public StringBuilder transform(StringBuilder part,
			Map<String, MimeHeader> partheaders) throws IOException;

}
