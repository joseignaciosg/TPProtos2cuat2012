package service.command.impl;

import service.command.ServiceCommand;

public class ExitCommand extends ServiceCommand {

	@Override
	public void execute(String[] params) {
		owner.setEndOfTransmission(true);
		owner.echoLine(0, "Good bye!");
	}

}
