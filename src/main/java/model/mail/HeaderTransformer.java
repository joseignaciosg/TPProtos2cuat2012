package model.mail;

import java.io.IOException;

import model.parser.mime.MimeHeader;

public interface HeaderTransformer {
	
	void transform(MimeHeader header) throws IOException;

}
