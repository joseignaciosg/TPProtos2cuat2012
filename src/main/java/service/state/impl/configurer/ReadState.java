package service.state.impl.configurer;

import service.AbstractSockectService;
import service.command.impl.PutCommand;
import service.command.impl.ListCommand;
import service.command.impl.DeleteLineCommand;
import service.command.impl.GetFileCommand;
import service.command.impl.configurer.ConfLogOutCommand;
import service.state.State;

public class ReadState extends State {
	
	public ReadState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("PUT", PutCommand.class);
		commandRecognizer.register("DEL", DeleteLineCommand.class);
		commandRecognizer.register("GET", GetFileCommand.class);
		commandRecognizer.register("LIST", ListCommand.class);
		commandRecognizer.register("EXIT", ConfLogOutCommand.class);
	}
	
	@Override
	public void exec(String[] params) {
		commandRecognizer.exec(params);
	}

}
