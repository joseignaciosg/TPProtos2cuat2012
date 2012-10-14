package service.command.impl.mail;

import java.io.IOException;

import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;

public class QuitCommand extends ServiceCommand {

	public QuitCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		owner.setEndOfTransmission(true);
		MailSocketService mailService = ((MailSocketService) owner);
		mailService.echoLineToOriginServer(getOriginalLine());
		String response;
		try {
			response = mailService.readFromOriginServer().readLine();
			mailService.echoLine(response);
		} catch (IOException e) {
			System.out.println("No se pudo terminar la coenccion con el servidor.");
		}
	}

}
