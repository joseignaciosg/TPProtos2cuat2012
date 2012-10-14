package service.command.impl.mail;

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
		MailSocketService service = (MailSocketService) getOwner();
		service.echoLineToOriginServer(CollectionUtil.join(getOriginalParams(), " "));
		String response = service.readFromOriginServer();
		service.echoLine(response);
	}

}
