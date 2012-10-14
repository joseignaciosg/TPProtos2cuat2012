import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;

import parser.MailHeader;
import util.IOUtil;

public class MailHeaderTest {

	private File tmpMailFile;
	private MailHeader reader;

	@Before
	public void init() throws IOException {
		String path = IOUtil.fullPath("test_mail.txt");
		if (path != null) {
			tmpMailFile = new File(path);
		}
		reader = new MailHeader(tmpMailFile);
	}

	// @Test
	public void getHeadersTest() throws IOException {
		System.out.println(reader.getHeader());
	}

	// @Test
	public void getHeaderTest() throws IOException {
		String from = reader.getHeader("From");
		System.out.println(from);
		Assert.assertTrue(from.equals(" John Doe <example@example.com>"));

		String notexistent = reader.getHeader("notexistent");
		Assert.assertTrue(notexistent == null);
	}

}
