package server.state.impl;

import server.AbstractSockectService;
import server.command.impl.AddCommand;
import server.command.impl.ListCommand;
import server.command.impl.LogOutCommand;
import server.command.impl.ShowCommand;
import server.state.State;

public class ReadState extends State {
	
	public ReadState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("ADD", AddCommand.class);
		commandRecognizer.register("PRT", ShowCommand.class);
		commandRecognizer.register("List", ListCommand.class);
		commandRecognizer.register("EXIT", LogOutCommand.class);
	}
	
	@Override
	public void exec(String[] params) {
		commandRecognizer.exec(params);
	}

}
