import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import parser.HeaderReader;
import util.IOUtil;

public class HeaderReaderTest {

	private HeaderReader reader;
	private final String mail = "test_mail.txt";
	String mail_path;

	@Before
	public void init() {
		mail_path = IOUtil.getResource(mail).getPath();
		reader = new HeaderReader(this.mail_path);
	}

	@Test
	public void getHeadersTest() {
		try {
			Assert.assertTrue(this.reader.getHeaders() != null);
			System.out.println(this.reader.getHeaders());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
