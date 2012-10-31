package service.command.impl.mail;

import service.AbstractSockectService;
import service.command.ServiceCommand;

public class UnknownCommand extends ServiceCommand {
	
	public UnknownCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) throws Exception {
		owner.echoLine("-ERR Unknown command.");
	}

}
