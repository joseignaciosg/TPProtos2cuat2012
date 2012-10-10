<<<<<<< Updated upstream
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import parser.HeaderReader;
import util.IOUtil;
=======
import src.main.java.parser.HeaderReader;
>>>>>>> Stashed changes

public class HeaderReaderTest {

    private HeaderReader reader;
    private final String mail = "test_mail.txt";
    String mail_path;

<<<<<<< Updated upstream
	@Before
	public void init() {
		mail_path = IOUtil.getResource(mail).getPath();
		reader = new HeaderReader(this.mail_path);
	}
=======
    @Before
    public void init() {
	this.mail_path = this.getClass().getClassLoader()
		.getResource(this.mail).getPath();
	this.reader = new HeaderReader(this.mail_path);
    }
>>>>>>> Stashed changes

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
