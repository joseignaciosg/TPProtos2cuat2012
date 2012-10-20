package service.state.impl.stats;

import service.AbstractSockectService;
import service.command.impl.ExitCommand;
import service.command.impl.stats.StatsAuthCommand;
import service.state.State;

public class AuthorityState extends State {

	public AuthorityState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("AUTH", StatsAuthCommand.class);
		commandRecognizer.register("EXIT", ExitCommand.class);
	}

	@Override
	public void exec(String[] params) {
		commandRecognizer.exec(params);
	}
	
}
