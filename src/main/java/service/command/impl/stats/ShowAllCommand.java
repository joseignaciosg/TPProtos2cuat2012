package service.command.impl.stats;

import service.AbstractSockectService;
import service.StatusCodes;
import service.command.ServiceCommand;

public class ShowAllCommand extends ServiceCommand {

	public ShowAllCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		owner.echoLine(StatusCodes.OK_ALL_USER_STATS_DISPLAYED);
		owner.echo(statsService.getAllUserStats());
		owner.echoLine(".");
	}
}