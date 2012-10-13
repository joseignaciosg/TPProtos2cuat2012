package server.state;

import server.AbstractSockectService;
import server.command.AuthCommand;

public class AuthorityState extends State {

	public AuthorityState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("AUTH", AuthCommand.class);
	}

	@Override
	public void exec(String[] params) {
		commandRecognizer.exec(params);
	}
	
}
