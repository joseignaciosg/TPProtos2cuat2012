package service.command.impl.stats;

import model.StatusCodes;
import model.util.CollectionUtil;
import service.AbstractSockectService;
import service.command.ServiceCommand;

public class HistogramCommand extends ServiceCommand {

	public HistogramCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		if (CollectionUtil.empty(params)) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_ARGUMENTS);
			return;
		}
		
		UserHistogram uh = statsService.getStatsByUser(params[0]);
		if(uh == null){
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_USER);
			return;
		}
		owner.echoLine(StatusCodes.OK_USER_STATS_DISPLAYED);
		owner.echo(uh.getPrettyFormat());
		owner.echoLine(".");
	}

}
