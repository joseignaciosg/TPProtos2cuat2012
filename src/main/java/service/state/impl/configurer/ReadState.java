package service.state.impl.configurer;

import service.AbstractSockectService;
import service.command.impl.AddCommand;
import service.command.impl.ListCommand;
import service.command.impl.RemoveLineCommand;
import service.command.impl.PrintFileCommand;
import service.command.impl.configurer.LogOutCommand;
import service.state.State;

public class ReadState extends State {
	
	public ReadState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("ADD", AddCommand.class);
		commandRecognizer.register("DEL", RemoveLineCommand.class);
		commandRecognizer.register("PRT", PrintFileCommand.class);
		commandRecognizer.register("LIST", ListCommand.class);
		commandRecognizer.register("EXIT", LogOutCommand.class);
	}
	
	@Override
	public void exec(String[] params) {
		commandRecognizer.exec(params);
	}

}
