import java.io.File;
import java.io.IOException;

import junit.framework.Assert;
import model.Mail;

import org.junit.Before;

import parser.mime.MailMimeParser;
import parser.mime.MimeHeader;
import util.IOUtil;

public class MailHeaderTest {

	private Mail mail;

	@Before
	public void init() throws IOException {
		File file = new File(IOUtil.fullPath("example/mail_7937octets.txt"));
		MailMimeParser mimeParser = new MailMimeParser();
		mail = mimeParser.parse(file, 140);
	}

	// @Test
	public void getHeadersTest() throws IOException {
		
	}

	// @Test
	public void getHeaderTest() throws IOException {
		MimeHeader from = mail.getHeader("From");
		System.out.println(from);
		Assert.assertTrue(from.equals(" John Doe <example@example.com>"));
		MimeHeader notexistent = mail.getHeader("notexistent");
		Assert.assertTrue(notexistent == null);
	}

}
