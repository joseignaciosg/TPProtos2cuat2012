package service.state.impl.mail;

import service.AbstractSockectService;
import service.command.impl.mail.DeleCommand;
import service.command.impl.mail.EchoUntilPointCommand;
import service.command.impl.mail.QuitCommand;
import service.command.impl.mail.ReadAndEchoLineCommand;
import service.command.impl.mail.RetrCommand;
import service.command.impl.mail.UIDLCommand;
import service.command.impl.mail.UnknownCommand;
import service.command.impl.mail.UnsupportedCommand;
import service.state.State;

public class TransactionMailState extends State {

	public TransactionMailState(AbstractSockectService owner) {
		super(owner);
		// Mandatory
		commandRecognizer.register("DELE", DeleCommand.class);
		commandRecognizer.register("LIST", EchoUntilPointCommand.class);
		commandRecognizer.register("NOOP", ReadAndEchoLineCommand.class);
		commandRecognizer.register("QUIT", QuitCommand.class);
		commandRecognizer.register("RETR", RetrCommand.class);
		commandRecognizer.register("RSET", ReadAndEchoLineCommand.class);
		commandRecognizer.register("STAT", ReadAndEchoLineCommand.class);
		// Optional commands
		commandRecognizer.register("TOP", EchoUntilPointCommand.class);
		commandRecognizer.register("UIDL", UIDLCommand.class);
		// Unavailable during this phase
		commandRecognizer.register("USER", UnknownCommand.class);
		commandRecognizer.register("PASS", UnknownCommand.class);
		commandRecognizer.register("APOP", UnknownCommand.class);
		// Non rfc stadard
		commandRecognizer.register("CAPA", EchoUntilPointCommand.class);
		// Default
		commandRecognizer.registerDefault(UnsupportedCommand.class);
	}
	
	@Override
	public void exec(String[] params) {
		commandRecognizer.exec(params);
	}

}
