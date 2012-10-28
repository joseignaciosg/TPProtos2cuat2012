package model.parser.mime;

import java.io.FileWriter;
import java.util.Scanner;

import model.mail.Mail;
import model.mail.MailTransformer;

public class ParseParameters {

	Mail mail;
	Scanner sourceScanner;
	FileWriter destinationWriter;
	MailTransformer transformer;

	public ParseParameters(Mail mail, Scanner sourceScanner,
			FileWriter destinaionWriter, MailTransformer trasformer) {
		this.mail = mail;
		this.sourceScanner = sourceScanner;
		this.destinationWriter = destinaionWriter;
		this.transformer = trasformer;
	}
}
