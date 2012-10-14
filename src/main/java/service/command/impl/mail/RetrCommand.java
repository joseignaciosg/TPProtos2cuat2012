package service.command.impl.mail;

import java.io.File;

import model.Email;
import service.AbstractSockectService;
import service.command.ServiceCommand;
import util.IOUtil;

public class RetrCommand extends ServiceCommand {

	public RetrCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		getBundle().put("DELE_" + params[0], new Email(new File(IOUtil.fullPath("test_mail.txt"))) );
	}
}
