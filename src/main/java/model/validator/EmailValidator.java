package model.validator;

import model.User;
import model.mail.Mail;

public interface EmailValidator {
	
	public void validate(User user, Mail email) throws MailValidationException;

}
