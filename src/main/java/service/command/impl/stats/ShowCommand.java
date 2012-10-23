package service.command.impl.stats;

import service.AbstractSockectService;
import service.StatusCodes;
import service.command.ServiceCommand;

public class ShowCommand extends ServiceCommand {

	public ShowCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		owner.echoLine(StatusCodes.OK_SERVER_STATS_DISPLAYED);
		owner.echo(statsService.getPrettyFormat());
		owner.echoLine(".");
	}

}
