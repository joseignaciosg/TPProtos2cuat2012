package service.state.impl.monitor;

import service.AbstractSockectService;
import service.command.impl.LogOutCommand;
import service.state.State;

public class ReadState extends State {
	
	public ReadState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("EXIT", LogOutCommand.class);
	}
	
	@Override
	public void exec(String[] params) {
		commandRecognizer.exec(params);
	}

}
