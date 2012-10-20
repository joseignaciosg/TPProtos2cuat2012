package model.parser;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface MailTransformer {

	void transform() throws FileNotFoundException, IOException;

}
