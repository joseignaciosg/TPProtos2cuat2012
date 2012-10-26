package service.command.impl.mail;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import model.mail.Mail;
import model.parser.mime.MailMimeParser;

import org.apache.log4j.Logger;

import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;
import service.state.impl.mail.ParseMailState;

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
		mailSocketService.echoLineToOriginServer(getOriginalLine()); //sends to Origin Server RETR ..
		BufferedReader breader = mailSocketService.readFromOriginServer();
		String firstLine = breader.readLine(); //+OK n octets
		File mailContent;
		//Puts mail content from origin server into a file
		mailContent = mailSocketService.getMailRetriever().retrieve("mail", breader);
		MailMimeParser parser = new MailMimeParser();
		int sizeInBytes = Integer.valueOf(firstLine.split(" ")[1]);
		Mail mail = parser.parse(mailContent, sizeInBytes,
				mailSocketService.getMailTranformer());
		if (showToClient) {
		    echoMailToClient(mail, mailSocketService);
		}
		if (!showToClient) {
			getBundle().put("DELE_" + params[0], mail);
		}
	}

	private void echoMailToClient(Mail mail,MailSocketService mailSocketService) throws IOException {
	    DataOutputStream os  = mailSocketService.getToClientOutPutStream();
	    Scanner s = new Scanner(mail.getTransformedContents());
	    while(s.hasNextLine()){
		os.writeBytes(s.nextLine() );
		logger.debug(s.nextLine() );
	    }
//	    os.close();
	    s.close();
	}
}
