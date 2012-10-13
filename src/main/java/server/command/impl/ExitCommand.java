package server.command.impl;

import server.command.ServiceCommand;

public class ExitCommand extends ServiceCommand {

	@Override
	public void execute(String[] params) {
		owner.setEndOfTransmission(true);
		owner.echoLine(0, "Good bye!");
	}

}
