package parser;

import java.io.File;


public class FileMailTransformer implements MailTransformer {

	private File file;
	
	public FileMailTransformer(File file) {
		this.file = file;
	}
	
	@Override
	public void transform() {
		
	}
}
