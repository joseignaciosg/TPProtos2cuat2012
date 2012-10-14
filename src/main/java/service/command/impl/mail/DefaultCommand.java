package service.command.impl.mail;

import java.io.IOException;

import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;

public class DefaultCommand extends ServiceCommand {

	public DefaultCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		MailSocketService service = (MailSocketService) getOwner();
		try {
			service.echoLineToOriginServer(getOriginalLine());
			service.echoLine(service.readFromOriginServer().readLine());
		} catch (IOException e) {
			throw new IllegalStateException("Could not read line - " + e.getMessage());
		}
	}

}
