package model.mail;

import java.io.IOException;

public interface MailTransformer {

	void transform(Mail mail) throws IOException;

}
