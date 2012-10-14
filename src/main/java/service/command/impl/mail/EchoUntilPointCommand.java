package service.command.impl.mail;

import java.io.BufferedReader;
import java.io.IOException;

import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;

public class EchoUntilPointCommand  extends ServiceCommand {

	public EchoUntilPointCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		MailSocketService service = (MailSocketService) getOwner();
		service.echoLineToOriginServer(getOriginalLine());
		BufferedReader responseBuffer = service.readFromOriginServer();
		String statusLine = readLine(responseBuffer);
		service.echoLine(statusLine);
		if (!statusLine.startsWith("+OK")) {
			service.echoLine(statusLine);
			System.out.println("Error! - " + statusLine);
			return;
		}
		String line;
		do {
			line = readLine(responseBuffer);
			service.echoLine(line);
		} while (!line.equals("."));
	}

	public String readLine(BufferedReader responseBuffer) {
		try {
			return responseBuffer.readLine();
		} catch (IOException e) {
			throw new IllegalStateException("Error reading line - " + e.getMessage());
		}
	}
}
