package service.command.impl.stats;

import model.StatusCodes;
import service.AbstractSockectService;
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
