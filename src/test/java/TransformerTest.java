import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import model.mail.Mail;
import model.mail.transformerimpl.LeetTransformer;
import model.util.IOUtil;

import org.junit.Before;


public class TransformerTest {

	private Mail mail;
	private LeetTransformer transformer;

	@Before
	public void init() throws IOException {
		String path = IOUtil.fullPath("test_mail.txt");
		if (path != null) {
			File tmpMailFile = new File(path);
			mail = new Mail(tmpMailFile);			
		}
		transformer = new LeetTransformer();
	}

	private void printFile(File file) throws IOException {
		Scanner s = new Scanner(file);
		while (s.hasNextLine()) {
			System.out.println(s.nextLine());
		}
		s.close();
	}

}
