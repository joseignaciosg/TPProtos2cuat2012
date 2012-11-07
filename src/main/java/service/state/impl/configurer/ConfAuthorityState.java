package service.state.impl.configurer;

import service.AbstractSockectService;
import service.command.impl.ExitCommand;
import service.command.impl.configurer.ConfigurerAuthCommand;
import service.state.State;

public class ConfAuthorityState extends State {

	public ConfAuthorityState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("AUTH", ConfigurerAuthCommand.class);
		commandRecognizer.register("EXIT", ExitCommand.class);
	}
	
}
