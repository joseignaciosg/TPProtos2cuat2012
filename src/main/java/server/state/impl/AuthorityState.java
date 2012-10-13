package server.state.impl;

import server.AbstractSockectService;
import server.command.impl.AuthCommand;
import server.command.impl.ExitCommand;
import server.state.State;

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
