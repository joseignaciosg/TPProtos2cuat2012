package service.command.impl;

import java.util.Collections;
import java.util.List;

import model.StatusCodes;

import service.AbstractSockectService;
import service.command.ServiceCommand;

public class ListCommand extends ServiceCommand {

	public ListCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		owner.echoLine(StatusCodes.OK_COMMANDS_LISTED);
		List<String> commands = owner.getStateMachine().getCurrent().getAvailableCommands();
		Collections.sort(commands);
		for (String cmdName : commands) {
			owner.echoLine(cmdName.toUpperCase());
		}
		owner.echoLine(".");
	}

}
