package service.command.impl.mail;

import service.AbstractSockectService;
import service.command.ServiceCommand;

public class UnsupportedCommand extends ServiceCommand {

	public UnsupportedCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) throws Exception {
		logger.error("Command: " + getOriginalLine() + " is not supported");
		owner.echoLine("-ERR Unsupported Command.");		
	}

}
