import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import parser.HeaderReader;

public class HeaderReaderTest {

	private HeaderReader reader;
	private final String mail = "test_mail.txt";
	String mail_path;

	@Before
	public void init() {
		this.mail_path = this.getClass().getClassLoader()
				.getResource(this.mail).getPath();
		this.reader = new HeaderReader(this.mail_path);
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
