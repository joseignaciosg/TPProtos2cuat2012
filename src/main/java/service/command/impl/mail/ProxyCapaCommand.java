package service.command.impl.mail;

import service.AbstractSockectService;
import service.command.ServiceCommand;

public class ProxyCapaCommand extends ServiceCommand {

	public ProxyCapaCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) throws Exception {
		owner.echoLine("+OK");
		owner.echoLine("AUTH");
		owner.echoLine("USER");
		owner.echoLine("CAPA");
		owner.echoLine("STAT");
		owner.echoLine("UIDL");
		owner.echoLine("RESP-CODES");
		owner.echoLine(".");
	}
}
