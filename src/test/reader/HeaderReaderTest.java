import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import parser.HeaderReader;
import util.IOUtil;

import src.main.java.parser.HeaderReader;

public class HeaderReaderTest {

	private HeaderReader reader;
	private String mail = "test_mail.txt";
	private String mail_path;

	@Before
	public void init() {
		mail_path = IOUtil.getResource(mail).getPath();
		reader = new HeaderReader(mail_path);
	}

	@Test
	public void getHeadersTest() {
		Assert.assertTrue(reader.getHeaders() != null);
	}
}
