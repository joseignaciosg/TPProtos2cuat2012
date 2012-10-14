package service.command.impl.mail;

import java.io.IOException;

import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;
import util.CollectionUtil;

public class DefaultCommand extends ServiceCommand {

	public DefaultCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		System.out.println(CollectionUtil.join(originalParams, " "));
		MailSocketService service = (MailSocketService) getOwner();
		service.echoLineToOriginServer(CollectionUtil.join(getOriginalParams(), " "));
		String response;
		try {
			response = service.readFromOriginServer().readLine();
			service.echoLine(response);
		} catch (IOException e) {
			throw new IllegalStateException("Could not read line - " + e.getMessage());
		}
	}

}
