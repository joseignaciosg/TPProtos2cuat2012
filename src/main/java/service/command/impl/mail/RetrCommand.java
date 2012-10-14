package service.command.impl.mail;

import model.Email;
import service.AbstractSockectService;
import service.command.ServiceCommand;

public class RetrCommand extends ServiceCommand {

	public RetrCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		getBundle().put("DELE_" + params[0], new Email());
	}

}
