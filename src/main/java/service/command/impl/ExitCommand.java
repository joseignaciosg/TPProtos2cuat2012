package service.command.impl;

import service.AbstractSockectService;
import service.command.ServiceCommand;

public class ExitCommand extends ServiceCommand {

	public ExitCommand(AbstractSockectService owner) {
		super(owner);
	}
	
	@Override
	public void execute(String[] params) {
		owner.setEndOfTransmission(true);
		owner.echoLine(0, "Good bye!");
	}

}
