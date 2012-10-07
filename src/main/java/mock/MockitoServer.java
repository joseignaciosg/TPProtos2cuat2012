package mock;

import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import server.Server;

public class MockitoServer implements Server {

	private Server mocked;
	
	public MockitoServer() {
		final Pattern deleteNumber = Pattern.compile("DELE (.+?)");
		final Pattern apopUser = Pattern.compile("APOP (.+?) ");
		
		mocked = mock(Server.class);
		when(mocked.exec(null)).thenReturn("+OK POP3 server ready\n");
		when(mocked.exec(matches("APOP (\\w)+ (\\w)+"))).thenAnswer(
			new Answer<String>() {
				@Override
				public String answer(InvocationOnMock invocation) throws Throwable {
					Object[] args = invocation.getArguments();
					Matcher matcher = apopUser.matcher((String) args[0]);
					matcher.find();
					return "+OK " + matcher.group(1) + "'s maildrop has 2 messages (240 octets)\n";
				}
			}
		);
		when(mocked.exec("STAT")).thenReturn("+OK 2 240\n");
		when(mocked.exec("LIST")).thenReturn("+OK 2 messages (240 octets)\n1 120\n2 120\n.\n");
		when(mocked.exec(matches("DELE (\\d)+"))).thenAnswer(
			new Answer<String>() {
				@Override
				public String answer(InvocationOnMock invocation) throws Throwable {
					Object[] args = invocation.getArguments();
					Matcher matcher = deleteNumber.matcher((String) args[0]);
					System.out.println(args[0]);
					matcher.find();
					return "+OK message " + matcher.group(1) + " deleted\n";
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
					return "+OK 120 octets\nThis is the message " + mail + "\n.\n";
				}
			}
		);
		when(mocked.exec("QUIT")).thenReturn("+OK dewey POP3 server signing off (maildrop empty)\n");
	}

	@Override
	public String exec(String line) {
		return mocked.exec(line);
	}

}
