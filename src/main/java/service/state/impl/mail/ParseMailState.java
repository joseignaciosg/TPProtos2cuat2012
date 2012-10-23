package service.state.impl.mail;

import service.AbstractSockectService;
import service.command.impl.mail.DefaultCommand;
import service.command.impl.mail.DeleCommand;
import service.command.impl.mail.EchoUntilPointCommand;
import service.command.impl.mail.QuitCommand;
import service.command.impl.mail.RetrCommand;
import service.state.State;

public class ParseMailState extends State {

	public ParseMailState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("CAPA", EchoUntilPointCommand.class);
		commandRecognizer.register("LIST", EchoUntilPointCommand.class);
		commandRecognizer.register("UIDL", EchoUntilPointCommand.class);
		commandRecognizer.register("RETR", RetrCommand.class);
		commandRecognizer.register("DELE", DeleCommand.class);
		commandRecognizer.register("QUIT", QuitCommand.class);
		commandRecognizer.registerDefault(DefaultCommand.class);
	}

	@Override
	public void exec(String[] params) {
		commandRecognizer.exec(params);
	}

}
