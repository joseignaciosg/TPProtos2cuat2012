package model.validator;

import model.User;
import model.mail.Mail;

public interface MailValidator {
	
	void validate(User user, Mail email) throws MailValidationException;

}
