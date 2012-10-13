package service.command.impl;

import java.util.List;

import service.command.ServiceCommand;

public class ListCommand extends ServiceCommand {

	@Override
	public void execute(String[] params) {
		List<String> commands = owner.getStateMachine().getCurrent().getAvailableCommands();
		for (String cmdName : commands) {
			owner.echoLine(cmdName);
		}
		owner.echoLine(".");
		owner.echoLine(0, "Commands listed");
	}

}
