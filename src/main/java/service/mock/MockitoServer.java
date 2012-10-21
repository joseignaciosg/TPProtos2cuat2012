package service.mock;

import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


public class MockitoServer implements Server {

	private Server mocked;
	private boolean authLogin;
	
	public MockitoServer() {
		authLogin = false;
		final Pattern deleteNumber = Pattern.compile("DELE (.+?)");
		final Pattern apopUser = Pattern.compile("APOP (.+?) ");
		final Pattern userName = Pattern.compile("USER (.+?) ");
		
		mocked = mock(Server.class);
		when(mocked.exec(null)).thenReturn("+OK POP3 server ready\r\n");
		when(mocked.exec("CAPA")).thenReturn("+OK\r\nCAPA\r\nTOP\r\nUIDL\r\nRESP-CODES\r\nPIPELINING\r\nUSER\r\nSASL PLAIN LOGIN\r\n.\r\n");
		when(mocked.exec(matches("APOP (\\w)+ (\\w)+"))).thenAnswer(
			new Answer<String>() {
				@Override
				public String answer(InvocationOnMock invocation) throws Throwable {
					Object[] args = invocation.getArguments();
					Matcher matcher = apopUser.matcher((String) args[0]);
					matcher.find();
					return "+OK " + matcher.group(1) + "'s maildrop has 2 messages (240 octets)\r\n";
				}
			}
		);
		when(mocked.exec(matches("PASS (.+?)"))).thenReturn("+OK Logged In.\r\n");
		when(mocked.exec("STAT")).thenReturn("+OK 2 240\r\n");
		when(mocked.exec("UIDL")).thenReturn("+OK\r\n1 UID1-" + (int) (Math.random()*1000) + "\r\n2 UID2-" + (int) (Math.random()*1000) + "\r\n.\r\n");
		when(mocked.exec("LIST")).thenReturn("+OK 2 messages (240 octets)\r\n1 120\r\n2 120\r\n.\r\n");
		when(mocked.exec(matches("DELE (\\d)+"))).thenAnswer(
			new Answer<String>() {
				@Override
				public String answer(InvocationOnMock invocation) throws Throwable {
					Object[] args = invocation.getArguments();
					Matcher matcher = deleteNumber.matcher((String) args[0]);
					System.out.println(args[0]);
					matcher.find();
					return "+OK message " + matcher.group(1) + " deleted\r\n";
				}
			}
		);
		when(mocked.exec(matches("RETR (\\d)+"))).thenAnswer(
			new Answer<String>() {
				@Override
				public String answer(InvocationOnMock invocation) throws Throwable {
					int size = 120;
					String mail = "";
					while (size > 0) {
						mail += "A";
						size--;
					}
					return "+OK 120 octets\r\nThis is the message " + mail + "\r\n.\r\n";
				}
			}
		);
		when(mocked.exec(matches("USER (.+?)"))).thenAnswer(
				new Answer<String>() {
					@Override
					public String answer(InvocationOnMock invocation) throws Throwable {
						Object[] args = invocation.getArguments();
						Matcher matcher = userName.matcher((String) args[0] + " ");
						matcher.find();
						return "+OK " + matcher.group(1) + " is a real hoopy frood\r\n";
					}
				}
			);
		when(mocked.exec("QUIT")).thenReturn("+OK dewey POP3 server signing off (maildrop empty)\r\n");
	}

	@Override
	public String exec(String line) {
		if (authLogin) {
			authLogin = false;
			return "+OK Logged in.\r\n";
		}
		if ("AUTH PLAIN".equals(line)) {
			authLogin = true;
			return "+\r\n";
		}
		return mocked.exec(line);
	}

}
