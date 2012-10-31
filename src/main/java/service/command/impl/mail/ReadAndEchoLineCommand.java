package service.command.impl.mail;

import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;

public class ReadAndEchoLineCommand extends ServiceCommand {

	public ReadAndEchoLineCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) throws Exception {
		MailSocketService mailService = (MailSocketService) getOwner();
		mailService.echoLineToOriginServer(getOriginalLine());
		mailService.echoLine(mailService.readFromOriginServer().readLine());
	}

}
