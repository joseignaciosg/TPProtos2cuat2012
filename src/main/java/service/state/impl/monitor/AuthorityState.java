package service.state.impl.monitor;

import service.AbstractSockectService;
import service.command.impl.AuthCommand;
import service.command.impl.ExitCommand;
import service.state.State;

public class AuthorityState extends State {

	public AuthorityState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("AUTH", AuthCommand.class);
		commandRecognizer.register("EXIT", ExitCommand.class);
	}

	@Override
	public void exec(String[] params) {
		commandRecognizer.exec(params);
	}
	
}
