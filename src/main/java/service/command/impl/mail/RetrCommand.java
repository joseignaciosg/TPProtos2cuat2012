package service.command.impl.mail;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;

import model.mail.Mail;
import model.parser.mime.MailMimeParser;

import org.apache.log4j.Logger;

import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;

public class RetrCommand extends ServiceCommand {

	protected static final Logger logger = Logger.getLogger(DeleCommand.class);

	public RetrCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) throws Exception {
		boolean showToClient = true;
		if (params.length == 3) {
			showToClient = Boolean.valueOf(params[3]);
		}
		MailSocketService mailSocketService = (MailSocketService) owner;
		owner.echoLine(getOriginalLine());
		BufferedReader breader = mailSocketService.read();
		DataOutputStream outstream = null;
		String firstLine = breader.readLine();
		File mailContent;
		if (showToClient) {
			outstream = mailSocketService.getOutPutStream();
		}
		mailContent = mailSocketService.getMailRetriever().retrieve("mail", breader, outstream);
		MailMimeParser parser = new MailMimeParser();
		int sizeInBytes = Integer.valueOf(firstLine.split(" ")[1]);
		Mail mail = parser.parse(mailContent, sizeInBytes,
				mailSocketService.getMailTranformer());
		if (!showToClient) {
			getBundle().put("DELE_" + params[0], mail);
		}
	}
}
