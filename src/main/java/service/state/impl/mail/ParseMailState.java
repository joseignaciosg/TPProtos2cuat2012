package service.state.impl.mail;

import service.AbstractSockectService;
import service.command.impl.mail.AuthCommand;
import service.command.impl.mail.DefaultCommand;
import service.command.impl.mail.EchoUntilPointCommand;
import service.state.State;

public class ParseMailState extends State {

	public ParseMailState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("AUTH", AuthCommand.class);
		commandRecognizer.register("CAPA", EchoUntilPointCommand.class);
		commandRecognizer.register("LIST", EchoUntilPointCommand.class);
		commandRecognizer.register("UIDL", EchoUntilPointCommand.class);
		// commandRecognizer.register("RETR", RetrCommand.class);
		// commandRecognizer.register("DELE", DeleCommand.class);
		commandRecognizer.registerDefault(DefaultCommand.class);
	}

	@Override
	public void exec(String[] params) {
		commandRecognizer.exec(params);
	}

}
