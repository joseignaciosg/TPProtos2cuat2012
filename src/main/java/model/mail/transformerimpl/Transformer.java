package model.mail.transformerimpl;

import java.io.IOException;

import model.mail.MimeHeaderCollection;

public interface Transformer {

	public StringBuilder transform(StringBuilder part, MimeHeaderCollection partheaders) throws IOException;

}
