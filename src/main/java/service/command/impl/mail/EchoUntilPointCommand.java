package service.command.impl.mail;

import java.io.BufferedReader;

import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;

public class EchoUntilPointCommand  extends ServiceCommand {

	public EchoUntilPointCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) throws Exception {
		MailSocketService service = (MailSocketService) getOwner();
		service.echoLineToOriginServer(getOriginalLine());
		BufferedReader responseBuffer = service.readFromOriginServer();
		String statusLine = responseBuffer.readLine();
		service.echoLine(statusLine);
		if (!statusLine.startsWith("+OK")) {
			return;
		}
		String line;
		do {
			line = responseBuffer.readLine();
			service.echoLine(line);
		} while (!line.equals("."));
	}

}
