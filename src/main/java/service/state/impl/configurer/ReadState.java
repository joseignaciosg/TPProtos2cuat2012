package service.state.impl.configurer;

import service.AbstractSockectService;
import service.command.impl.AddCommand;
import service.command.impl.ListCommand;
import service.command.impl.LogOutCommand;
import service.command.impl.ShowCommand;
import service.state.State;

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
