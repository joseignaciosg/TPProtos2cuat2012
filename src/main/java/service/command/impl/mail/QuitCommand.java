package service.command.impl.mail;

import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;

public class QuitCommand extends ServiceCommand {

	public QuitCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) throws Exception {
		owner.setEndOfTransmission(true);
		MailSocketService mailService = ((MailSocketService) owner);
		if (mailService.hasOriginServerSocket()) {
			// Solo se tiene una coneccion con un origin server si el usuario esta autenticado!
			mailService.echoLineToOriginServer(getOriginalLine());
			mailService.echoLine(mailService.readFromOriginServer().readLine());
		}
	}

}
