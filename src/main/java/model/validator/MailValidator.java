package model.validator;

import model.User;
import model.mail.Mail;

public interface MailValidator {
	
	public void validate(User user, Mail email) throws MailValidationException;

}
