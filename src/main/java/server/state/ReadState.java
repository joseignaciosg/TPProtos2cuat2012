package server.state;

import server.AbstractSockectService;
import server.command.AddCommand;
import server.command.ShowCommand;

public class ReadState extends State {
	
	public ReadState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("ADD", AddCommand.class);
		commandRecognizer.register("PRT", ShowCommand.class);
	}
	
	@Override
	public void exec(String[] params) {
		commandRecognizer.exec(params);
	}

}
